package com.hzgc.service.white.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceUtil;
import com.hzgc.service.dispatch.param.KafkaMessage;
import com.hzgc.service.white.dao.WhiteInfoMapper;
import com.hzgc.service.white.dao.WhiteMapper;
import com.hzgc.service.white.model.White;
import com.hzgc.service.white.model.WhiteInfo;
import com.hzgc.service.white.param.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class WhiteService {
    @Autowired
    private WhiteInfoMapper whiteInfoMapper;

    @Autowired
    private WhiteMapper whiteMapper;

    @Autowired
    private InnerService innerService;
    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "";

    private static final String ADD = "ADD";

    private static final String DELETE = "DELETE";

    private static final String UPDATE = "UPDATE";

    private static final String IMPORT = "IMPORT";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sendKafka(String key, Object data) {
        try {
            ListenableFuture<SendResult<String, String>> resultFuture =
                    kafkaTemplate.send(TOPIC, key, JacksonUtil.toJson(data));
            RecordMetadata metaData = resultFuture.get().getRecordMetadata();
            ProducerRecord<String, String> producerRecord = resultFuture.get().getProducerRecord();
            if (metaData != null) {
                log.info("Send Kafka successfully! message:[topic:{}, key:{}, data:{}]",
                        metaData.topic(), key, JacksonUtil.toJson(data));
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public Integer insertDispatch_white(DispatchWhiteDTO dto) {
        White white = new White();
        white.setId(UuidUtil.getUuid());
        white.setName(dto.getDesignation());
        String deviceCode = "";
        if (dto.getIpc_list() != null) {
            for (DeviceIps ipc : dto.getIpc_list()) {
                deviceCode = deviceCode + ipc.getDeviceCode() + ",";
            }
            white.setDevices(deviceCode);
            white.setOrganization(dto.getOrganization());
            Integer Status = whiteMapper.insertSelective(white);
            if (Status != 1) {
                log.info("Insert info,but insert info to t_dispatch_white failed");
                return 0;
            }
        }
        if (dto.getName_list() != null) {
            for (WhiteName name : dto.getName_list()) {
                WhiteInfo whiteInfo = new WhiteInfo();
                whiteInfo.setWhiteId(white.getId());
                whiteInfo.setName(name.getName());
                if (name.getPicture() != null) {
                    byte[] bytes = FaceUtil.base64Str2BitFeature(name.getPicture());
                    FaceAttribute faceAttribute = innerService.faceFeautreExtract(name.getPicture()).getFeature();
                    if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                        log.error("Face feature extract failed, insert t_dispatch_white failed");
                        throw new RuntimeException("Face feature extract failed, insert t_dispatch_white failed");
                    }
                    whiteInfo.setPicture(bytes);
                    whiteInfo.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
                    whiteInfo.setBitFeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
                }
                Integer insertstatus = whiteInfoMapper.insertSelective(whiteInfo);
                if (insertstatus != 1) {
                    log.info("Insert info,but insert peoplename to t_dispatchname failed");
                    return 0;
                }
                KafkaMessage message = new KafkaMessage();
                message.setId(white.getId());
                this.sendKafka(ADD,message);
                log.info("Insert info successfully");
            }
        }
        return null;
    }

    public Integer deleteDispatch_white(String id) {
        Integer status = whiteMapper.deleteByPrimaryKey(id);
        if (status != 1){
            log.info("Delete info failed");
            return 0;
        }
        this.sendKafka(DELETE,id);
        log.info("Delete info successfully");
        return status;
    }

    public Integer update_Dispatch_white(DispatchWhiteDTO dto) {
        Integer status = whiteMapper.deleteByPrimaryKey(dto.getId());
        if (status !=1){
            log.info("Update t_dispatch_definion failed :" + dto.getId());
            return 0;
        }
        String uuid = UuidUtil.getUuid();
        if (dto.getDesignation() !=null || dto.getIpc_list() !=null){
            //String uuid = UuidUtil.getUuid();
            String designation = dto.getDesignation();
            if (dto.getIpc_list() != null){
                for (DeviceIps ipc : dto.getIpc_list()) {
                    White white = new White();
                    white.setId(uuid);
                    white.setName(designation);
                    white.setDevices(ipc.getDeviceCode());
                    status = whiteMapper.insertSelective(white);
                    if (status != 1) {
                        log.info("Insert info to t_dispatch_white failed");
                        return 0;
                    }
                }
            }
        }
        if (dto.getName_list()!= null) {
            for (WhiteName name : dto.getName_list()) {
                WhiteInfo whiteInfo = new WhiteInfo();
                whiteInfo.setWhiteId(uuid);
                whiteInfo.setName(name.getName());
                if (name.getPicture() != null) {
                    byte[] bytes = FaceUtil.base64Str2BitFeature(name.getPicture());
                    FaceAttribute faceAttribute = innerService.faceFeautreExtract(name.getPicture()).getFeature();
                    if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                        log.error("Face feature extract failed, insert t_dispatch_white failed");
                        throw new RuntimeException("Face feature extract failed, insert t_dispatch_white failed");
                    }
                    whiteInfo.setPicture(bytes);
                    whiteInfo.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
                    whiteInfo.setBitFeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
                }
                status = whiteInfoMapper.insertSelective(whiteInfo);
                if (status != 1) {
                    log.info("Insert info,but insert peoplename to t_dispatch_whiteinfo failed");
                }
                KafkaMessage message = new KafkaMessage();
                message.setId(whiteInfo.getWhiteId());
                this.sendKafka(UPDATE, message);
                log.info("update info successfully");
            }
            return status;
        }
        return null;
    }

    public Integer dispatch_whiteStatus(String id, int status) {
        White white = new White();
        white.setId(id);
        white.setStatus(status);
        int i = whiteMapper.updateStatusById(white);
        if ( i != 1){
            log.info("Update status failed ");
            return 0;
        }
        if (i == 1) {
            if (status == 0) {
                KafkaMessage kafkaMessage = new KafkaMessage();
                kafkaMessage.setId(id);
                this.sendKafka(ADD, kafkaMessage);
            }
            if (status == 1) {
                this.sendKafka(DELETE, id);
            }
            return 1;
        }
        return 1;
    }


    public SearchDispatchWhiteVO searchDispatch_white(SearchDispatchWhiteDTO dto) {
        SearchDispatchWhiteVO vo = new SearchDispatchWhiteVO();
        Page page = PageHelper.offsetPage(dto.getStart(), dto.getLimit(), true);
        List<White> whites = whiteMapper.searchInfo(dto);
        PageInfo info = new PageInfo(page.getResult());
        int total = (int) info.getTotal();
        vo.setTotal(total);
        List<DispatchWhiteVO> dispatchWhiteVOS = new ArrayList<DispatchWhiteVO>();
        for( White white: whites){
            String ipcs = white.getDevices();
            String[] split = ipcs.split(",");
            List<DeviceIps> deviceIps = new ArrayList<>();
            for(int i = 0; i < split.length; i++ ){
                DeviceIps deviceIp = new DeviceIps();
                deviceIp.setDeviceName(split[i]);
                deviceIps.add(deviceIp);
            }
            List<WhiteInfo> whiteInfos = whiteInfoMapper.selectByDefid(white);
            List<WhiteName> whiteNames = new ArrayList<WhiteName>();
            for (WhiteInfo whiteInfo: whiteInfos){
                WhiteName whiteName =
                        new WhiteName(whiteInfo.getId().toString(), toString().valueOf(whiteInfo.getPicture()), whiteInfo.getName());
                whiteNames.add(whiteName);
            }
            DispatchWhiteVO dispatchWhiteVO = new DispatchWhiteVO();
            dispatchWhiteVO.setId(white.getId());
            dispatchWhiteVO.setDesignation(white.getName());
            dispatchWhiteVO.setIpc_list(deviceIps);
            dispatchWhiteVO.setStatus(white.getStatus());
            dispatchWhiteVO.setName_list(whiteNames);
            dispatchWhiteVOS.add(dispatchWhiteVO);
        }
        vo.setDispatch_list(dispatchWhiteVOS);
        return vo;
    }
}
