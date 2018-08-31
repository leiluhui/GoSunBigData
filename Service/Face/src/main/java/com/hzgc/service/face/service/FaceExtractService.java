package com.hzgc.service.face.service;

import com.hzgc.jni.*;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jniface.CarAttribute;
import com.hzgc.seemmo.bean.ImageResult;
import com.hzgc.seemmo.bean.carbean.Vehicle;
import com.hzgc.seemmo.service.ImageToData;
import com.hzgc.service.face.util.FtpDownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FaceExtractService {

    @Autowired
    @SuppressWarnings("unused")
    private FaceExtractService faceExtractService;

    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    public FaceExtractService() {
        try {
            log.info("Start NativeFunction init....");
            //NativeFunction.init();
            log.info("Init NativeFunction successful!");
        } catch (Exception e) {
            log.error("Init NativeFunction failure!");
            e.printStackTrace();
        }
    }

    /**
     * 特征提取
     *
     * @param imageBytes 图片的字节数组
     * @return float[] 特征值:长度为512的float[]数组
     */
    public PictureData featureExtractByImage(byte[] imageBytes) {
        PictureData pictureData = new PictureData();
        pictureData.setImageID(UuidUtil.getUuid());
        pictureData.setImageData(imageBytes);
        FaceAttribute faceAttribute = FaceFunction.featureExtract(imageBytes);
        if (faceAttribute != null) {
            log.info("Face extract successful, image contains feature");
            pictureData.setFeature(faceAttribute);
            return pictureData;
        } else {
            log.info("Face extract failed, image not contains feature");
            return null;
        }
    }

    //ftp获取特征值
    public PictureData getFeatureExtractByFtp(String pictureUrl){
        log.info("PictureUrl is :" + pictureUrl);
        PictureData pictureData;
        //FTP匿名账号Anonymous和密码
        byte[] bytes = FtpDownloadUtils.downloadftpFile2Bytes(pictureUrl,"anonymous",null);
        if (null != bytes){
            log.info("Face extract successful, pictureUrl contains feature");
            pictureData = faceExtractService.featureExtractByImage(bytes);
            return pictureData;
        }else {
            log.info("Face extract failed, pictureUrl not contains feature");
            return null;
        }
    }



    /**
     * 特征提取
     *
     * @param imageBin 图片数组
     * @return CarPictureData
     */
    public CarPictureData carExtractByImage(byte[] imageBin) {
        String a = "110.110.110.110";
        String b = "http://172.18.18.139:8000/?cmd=recogPic";
        String tag = "0";
        String url = environment.getProperty(a);
        String imagePath = environment.getProperty(b);


        ImageResult imageResult = ImageToData.getImageResult(b, imageBin, tag);
        if (imageResult == null){
            log.error("imageResult is null !");
            return null;
        }
        CarPictureData carPictureData = new CarPictureData();
        carPictureData.setImageID(UuidUtil.getUuid());
        carPictureData.setImageData(imageBin);

        List<CarAttribute> attributeList = new ArrayList<>();
        List<Vehicle> vehicleList = imageResult.getVehicleList();
        for (Vehicle vehicle : vehicleList) {
            CarAttribute carAttribute = new CarAttribute();
            carAttribute.setVehicle_object_type(vehicle.getVehicle_object_type());
            carAttribute.setBelt_maindriver(vehicle.getBelt_maindriver());
            carAttribute.setBelt_codriver(vehicle.getBelt_codriver());
            carAttribute.setBrand_name(vehicle.getBrand_name());
            carAttribute.setCall_code(vehicle.getCall_code());
            carAttribute.setVehicle_color(vehicle.getVehicle_color());
            carAttribute.setCrash_code(vehicle.getCrash_code());
            carAttribute.setDanger_code(vehicle.getDanger_code());
            carAttribute.setMarker_code(vehicle.getMarker_code());
            carAttribute.setPlate_schelter_code(vehicle.getPlate_schelter_code());
            carAttribute.setPlate_flag_code(vehicle.getPlate_flag_code());
            carAttribute.setPlate_licence(vehicle.getPlate_licence());
            carAttribute.setPlate_destain_code(vehicle.getPlate_destain_code());
            carAttribute.setPlate_color_code(vehicle.getPlate_color_code());
            carAttribute.setPlate_type_code(vehicle.getPlate_type_code());
            carAttribute.setRack_code(vehicle.getRack_code());
            carAttribute.setSparetire_code(vehicle.getSparetire_code());
            carAttribute.setMistake_code(vehicle.getMistake_code());
            carAttribute.setSunroof_code(vehicle.getSunroof_code());
            carAttribute.setVehicle_type(vehicle.getVehicle_type());
            attributeList.add(carAttribute);
        }
        carPictureData.setAttributeList(attributeList);
        return carPictureData;
    }

}
