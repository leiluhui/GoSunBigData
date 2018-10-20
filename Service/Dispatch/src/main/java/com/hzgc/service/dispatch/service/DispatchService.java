package com.hzgc.service.dispatch.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.dispatch.dao.DispatchMapper;
import com.hzgc.service.dispatch.dao.DispatchRecognizeMapper;
import com.hzgc.service.dispatch.model.Dispatch;
import com.hzgc.service.dispatch.model.DispatchRecognize;
import com.hzgc.service.dispatch.param.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DispatchService {
    @Autowired
    @SuppressWarnings("unused")
    private DispatchMapper dispatchMapper;

    @Autowired
    @SuppressWarnings("unused")
    private DispatchRecognizeMapper dispatchRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    //Spring-kafka-template
    private KafkaTemplate <String, String> kafkaTemplate;

    @Value("${dispatch.kafka.topic}")
    @NotNull
    @SuppressWarnings("unused")
    private String kafkaTopic;

    private static final String ADD = "ADD";

    private static final String DELETE = "DELETE";

    private static final String UPDATE = "UPDATE";

    private static final String IMPORT = "IMPORT";

    private void sendKafka(String key, Object data) {
        try {
            ListenableFuture <SendResult <String, String>> resultFuture =
                    kafkaTemplate.send(kafkaTopic, key, JacksonUtil.toJson(data));
            RecordMetadata metaData = resultFuture.get().getRecordMetadata();
            ProducerRecord <String, String> producerRecord = resultFuture.get().getProducerRecord();
            if (metaData != null) {
                log.info("Send Kafka successfully! message:[topic:{}, key:{}, data:{}]",
                        metaData.topic(), key, JacksonUtil.toJson(data));
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public ResponseResult <WarnHistoryVO> searchDeployRecognize(DispatchRecognizeDTO dispatchRecognizeDTO) {
        List <DispatchRecognize> dispatchRecognizeList = dispatchRecognizeMapper.selectSelective(dispatchRecognizeDTO);
        ArrayList <DispatchRecognizeVO> dispatchRecognizeVOS = new ArrayList <>();
        if (null != dispatchRecognizeList && dispatchRecognizeList.size() > 0) {
            for (DispatchRecognize dispatchRecognize : dispatchRecognizeList) {
                DispatchDTO dispatchDTO = new DispatchDTO();
                String dispatchId = dispatchRecognize.getDispatchId();
                dispatchDTO.setId(dispatchId);
                dispatchDTO.setRegionId(dispatchRecognizeDTO.getRegionId());
                Dispatch dispatch = dispatchMapper.selectSelective(dispatchDTO);
                if (null != dispatch && null != dispatchRecognize) {
                    DispatchRecognizeVO dispatchRecognizeVO = getDispatchRecognizeVO(dispatch, dispatchRecognize);
                    dispatchRecognizeVOS.add(dispatchRecognizeVO);
                }
            }
        }
        WarnHistoryVO warnHistoryVO = new WarnHistoryVO();
        warnHistoryVO.setTotal(dispatchRecognizeVOS.size());
        warnHistoryVO.setDispatchRecognizeVOS(getDispatchRecognizeVOByCutPage(dispatchRecognizeDTO, dispatchRecognizeVOS));
        log.info(JacksonUtil.toJson(warnHistoryVO));
        return ResponseResult.init(warnHistoryVO);
    }

    //DispatchVO 数据封装
    private DispatchVO getDispatchVO(Dispatch dispatch) {
        DispatchVO dispatchVO = new DispatchVO();
        dispatchVO.setId(dispatch.getId());
        dispatchVO.setRegionId(dispatch.getRegion());
        dispatchVO.setName(dispatch.getName());
        dispatchVO.setIdCard(dispatch.getIdcard());
        dispatchVO.setThreshold(dispatch.getThreshold());
        dispatchVO.setCar(dispatch.getCar());
        dispatchVO.setMac(dispatch.getMac());
        dispatchVO.setNotes(dispatch.getNotes());
        dispatchVO.setStatus(dispatch.getStatus());
//        dispatchVO.setCreateTime(dispatch.getCreateTime());
//        dispatchVO.setUpdateTime(dispatch.getUpdateTime());
        return dispatchVO;
    }

    //DispatchRecognizeVO 数据封装
    private DispatchRecognizeVO getDispatchRecognizeVO(Dispatch dispatch, DispatchRecognize dispatchRecognize) {
        DispatchRecognizeVO dispatchRecognizeVO = new DispatchRecognizeVO();
        dispatchRecognizeVO.setId(dispatchRecognize.getId());
        dispatchRecognizeVO.setDispatchId(dispatchRecognize.getDispatchId());
        dispatchRecognizeVO.setDeviceId(dispatchRecognize.getDeviceId());
        dispatchRecognizeVO.setBurl(dispatchRecognize.getBurl());
        dispatchRecognizeVO.setSurl(dispatchRecognize.getSurl());
        dispatchRecognizeVO.setSimilarity(dispatchRecognize.getSimilarity());
        dispatchRecognizeVO.setName(dispatch.getName());
        dispatchRecognizeVO.setIdCard(dispatch.getIdcard());
        dispatchRecognizeVO.setCar(dispatch.getCar());
        dispatchRecognizeVO.setMac(dispatch.getMac());
        dispatchRecognizeVO.setNotes(dispatch.getNotes());
        return dispatchRecognizeVO;
    }

    //分页
    private List <DispatchRecognizeVO> getDispatchRecognizeVOByCutPage(DispatchRecognizeDTO dispatchRecognizeDTO,
                                                                       List <DispatchRecognizeVO> dispatchRecognizeVOS) {
        int start = dispatchRecognizeDTO.getStart();
        int limit = dispatchRecognizeDTO.getLimit();
        if (null != dispatchRecognizeVOS && dispatchRecognizeVOS.size() > 0) {
            if (dispatchRecognizeVOS.size() > start && dispatchRecognizeVOS.size() < (start + limit)) {
                return dispatchRecognizeVOS.subList(start, dispatchRecognizeVOS.size());
            }
            if (dispatchRecognizeVOS.size() > start && dispatchRecognizeVOS.size() >= (start + limit)) {
                return dispatchRecognizeVOS.subList(start, limit);
            }
            return null;
        }
        return null;
    }
}
