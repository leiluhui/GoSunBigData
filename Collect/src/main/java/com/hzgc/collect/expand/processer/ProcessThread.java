package com.hzgc.collect.expand.processer;

import com.hzgc.collect.expand.parser.FtpPathParse;
import com.hzgc.collect.expand.receiver.Event;
import com.hzgc.collect.expand.util.CollectProperties;
import com.hzgc.common.collect.bean.CarObject;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.collect.bean.PersonObject;
import com.hzgc.common.collect.facesub.FtpSubscribeClient;
import com.hzgc.common.util.basic.FileUtil;
import com.hzgc.common.util.basic.ImageUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureFormat;
import com.hzgc.jniface.SmallImage;
import com.hzgc.seemmo.bean.ImageResult;
import com.hzgc.seemmo.bean.carbean.Vehicle;
import com.hzgc.seemmo.bean.personbean.Person;
import com.hzgc.seemmo.service.ImageToData;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {
    private Logger LOG = Logger.getLogger(ProcessThread.class);
    private BlockingQueue<Event> queue;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static String FACE = "face";
    private final static String PERSON = "person";
    private final static String CAR = "car";

    public ProcessThread(BlockingQueue<Event> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Event event;
        try {
            while ((event = queue.take()) != null) {
                byte[] bytes = FileUtil.fileToByteArray(event.getFileAbsolutePath());
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                if (image.getWidth() * image.getHeight() < 1920 * 1080) {
                    LOG.error("Camera error, This is a small picture, fileName: " + event.getFileAbsolutePath());
                    continue;
                }
                List<String> ftpTypes = Arrays.asList(CollectProperties.getFtpType().split(","));
                if (ftpTypes.contains("face")) {
                    ArrayList<SmallImage> smallImageList = FaceFunction.bigPictureCheck(bytes, PictureFormat.JPG);
                    if (smallImageList != null && smallImageList.size() > 0) {
                        int index = 1;
                        for (SmallImage smallImage : smallImageList) {
                            if (smallImage.getPictureStream() == null || smallImage.getPictureStream().length == 0) {
                                LOG.info("Face small image are not extracted, index: " + index + " fileName: " + event.getFileAbsolutePath());
                                continue;
                            }
                            if (smallImage.getFaceAttribute() == null) {
                                LOG.info("Face attribute are not extracted, index: " + index + " fileName: " + event.getFileAbsolutePath());
                                continue;
                            }
                            if (smallImage.getFaceAttribute().getFeature() == null
                                    || smallImage.getFaceAttribute().getFeature().length == 0) {
                                LOG.info("Face feature are not extracted, index: " + index + " fileName: " + event.getFileAbsolutePath());
                                continue;
                            }
                            String smallImagePath = FtpPathParse.path_b2s(event.getFileAbsolutePath(), FACE, index);
                            boolean boo = ImageUtil.save(smallImagePath, smallImage.getPictureStream());
                            if (boo) {
                                this.sendKafka(event, smallImage, FACE, index);
                                this.sendRocketMQ(event, FACE, index, CollectProperties.getRocketmqFaceTopic());
                            }
                            index++;
                        }
                    }
                }
                List<Person> personList = null;
                List<Vehicle> vehicleList = null;
                if (ftpTypes.contains("person") || ftpTypes.contains("car")) {
                    ImageResult result = ImageToData.getImageResult(CollectProperties.getSeemmoUrl(), bytes, "66");
                    if (result != null) {
                        personList = result.getPersonList();
                        vehicleList = result.getVehicleList();
                    }
                }
                if (ftpTypes.contains("person") && personList != null && personList.size() > 0) {
                    int index = 1;
                    for (Person person : personList) {
                        if (person.getCar_data() == null || person.getCar_data().length == 0) {
                            LOG.info("Person small image are not extracted, fileName: " + event.getFileAbsolutePath());
                            continue;
                        }
                        String smallImagePath = FtpPathParse.path_b2s(event.getFileAbsolutePath(), PERSON, index);
                        boolean boo = ImageUtil.save(smallImagePath, person.getCar_data());
                        if (boo) {
                            this.sendKafka(event, person, PERSON, index);
                            this.sendRocketMQ(event, PERSON, index, CollectProperties.getRocketmqPersonTopic());
                        }
                        index++;
                    }
                }
                if (ftpTypes.contains("car") && vehicleList != null && vehicleList.size() > 0) {
                    int index = 1;
                    for (Vehicle vehicle : vehicleList) {
                        if (vehicle.getVehicle_data() == null || vehicle.getVehicle_data().length == 0) {
                            LOG.info("Vehicle small image are not extracted, fileName: " + event.getFileAbsolutePath());
                            continue;
                        }
                        String smallImagePath = FtpPathParse.path_b2s(event.getFileAbsolutePath(), CAR, index);
                        boolean boo = ImageUtil.save(smallImagePath, vehicle.getVehicle_data());
                        if (boo) {
                            this.sendKafka(event, vehicle, CAR, index);
                            this.sendRocketMQ(event, CAR, index, CollectProperties.getRocketmqCarTopic());
                        }
                        index++;
                    }
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendKafka(Event event, SmallImage smallImage, String ftpType, int index) {
        String ftpUrl_hostname_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_hostname(), ftpType, index);
        String ftpUrl_ip_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_ip(), ftpType, index);
        String ftpAbsolutePath_s = FtpPathParse.path_b2s(event.getFtpAbsolutePath(), ftpType, index);
        FaceObject faceObject = FaceObject.builder()
                .setIpcId(event.getIpcId())
                .setTimeStamp(event.getTimeStamp())
                .setDate(event.getDate())
                .setTimeSlot(event.getTimeSlot())
                .setAttribute(smallImage.getFaceAttribute())
                .setSurl(ftpUrl_hostname_s)
                .setBurl(event.getFtpUrl_hostname())
                .setRelativePath(ftpAbsolutePath_s)
                .setRelativePath_big(event.getFtpAbsolutePath())
                .setIp(event.getIp())
                .setHostname(event.getHostname());
        KafkaProducer.getInstance().sendKafkaMessage(
                CollectProperties.getKafkaFaceObjectTopic(),
                ftpUrl_hostname_s,
                JacksonUtil.toJson(faceObject),
                new KafkaCallBack(ftpUrl_ip_s, sdf.format(System.currentTimeMillis())));
    }

    private void sendKafka(Event event, Person person, String ftpType, int index) {
        person.setCar_data(null);
        String ftpUrl_hostname_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_hostname(), ftpType, index);
        String ftpUrl_ip_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_ip(), ftpType, index);
        String relativePath_s = FtpPathParse.path_b2s(event.getFtpAbsolutePath(), ftpType, index);
        PersonObject personObject = PersonObject.builder()
                .setIpcId(event.getIpcId())
                .setTimeStamp(event.getTimeStamp())
                .setDate(event.getDate())
                .setTimeSlot(event.getTimeSlot())
                .setAttribute(person)
                .setSurl(ftpUrl_hostname_s)
                .setBurl(event.getFtpUrl_hostname())
                .setRelativePath(relativePath_s)
                .setRelativePath_big(event.getFtpAbsolutePath())
                .setIp(event.getIp())
                .setHostname(event.getHostname());
        KafkaProducer.getInstance().sendKafkaMessage(
                CollectProperties.getKafkaPersonObjectTopic(),
                ftpUrl_hostname_s,
                JacksonUtil.toJson(personObject),
                new KafkaCallBack(ftpUrl_ip_s, sdf.format(System.currentTimeMillis())));
    }

    private void sendKafka(Event event, Vehicle vehicle, String ftpType, int index) {
        vehicle.setVehicle_data(null);
        String ftpUrl_hostname_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_hostname(), ftpType, index);
        String ftpUrl_ip_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_ip(), ftpType, index);
        String relativePath_s = FtpPathParse.path_b2s(event.getFtpAbsolutePath(), ftpType, index);
        CarObject carObject = CarObject.builder()
                .setIpcId(event.getIpcId())
                .setTimeStamp(event.getTimeStamp())
                .setDate(event.getDate())
                .setTimeSlot(event.getTimeSlot())
                .setAttribute(vehicle)
                .setSurl(ftpUrl_hostname_s)
                .setBurl(event.getFtpUrl_hostname())
                .setRelativePath(relativePath_s)
                .setRelativePath_big(event.getFtpAbsolutePath())
                .setIp(event.getIp())
                .setHostname(event.getHostname());
        KafkaProducer.getInstance().sendKafkaMessage(
                CollectProperties.getKafkaCarObjectTopic(),
                ftpUrl_hostname_s,
                JacksonUtil.toJson(carObject),
                new KafkaCallBack(ftpUrl_ip_s, sdf.format(System.currentTimeMillis())));
    }

    private void sendRocketMQ(Event event, String ftpType, int index, String topic) {
        if (CollectProperties.isFtpSubscribeSwitch()) {
            // ftpSubscribeMap: key is ipcId, value is sessionIds
            Map<String, List<String>> ftpSubscribeMap = FtpSubscribeClient.getSessionMap();
            if (!ftpSubscribeMap.isEmpty()) {
                if (ftpSubscribeMap.containsKey(event.getIpcId())) {
                    List<String> sessionIds = ftpSubscribeMap.get(event.getIpcId());
                    SendMqMessage mqMessage = new SendMqMessage();
                    mqMessage.setSessionIds(sessionIds);
                    String ftpUrl_ip_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_ip(), ftpType, index);
                    mqMessage.setFtpUrl(ftpUrl_ip_s);
                    RocketMQProducer.getInstance().send(
                            topic,
                            event.getIpcId(),
                            event.getTimeStamp(),
                            JacksonUtil.toJson(mqMessage).getBytes());
                }
            }
        } else {
            String ftpUrl_ip_s = FtpPathParse.ftpUrl_b2s(event.getFtpUrl_ip(), ftpType, index);
            RocketMQProducer.getInstance().send(
                    topic,
                    event.getIpcId(),
                    event.getTimeStamp(),
                    ftpUrl_ip_s.getBytes());
        }
    }
}
