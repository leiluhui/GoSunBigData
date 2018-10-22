package com.hzgc.service.dispatch.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceUtil;
import com.hzgc.service.dispatch.dao.DispatchMapper;
import com.hzgc.service.dispatch.dao.DispatchRecognizeMapper;
import com.hzgc.service.dispatch.model.Dispatch;
import com.hzgc.service.dispatch.model.DispatchRecognize;
import com.hzgc.service.dispatch.param.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DispatchService {
    @Autowired
    @SuppressWarnings("unused")
    private DispatchMapper dispatchMapper;

    @Autowired
    @SuppressWarnings("unused")
    private InnerService innerService;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    //布控告警历史查询
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
                if (null != dispatch) {
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

    /**
     * 查询布控信息（模糊查询）
     *
     * @param searchDispatchDTO 查询字段封装
     * @return DispatchVO 查询返回参数封装
     */
    public SearchDispatchVO searchDispatch(SearchDispatchDTO searchDispatchDTO) {
        SearchDispatchVO vo = new SearchDispatchVO();
        Page page = PageHelper.offsetPage(searchDispatchDTO.getStart(), searchDispatchDTO.getLimit(), true);
        List <Dispatch> dispatchList = dispatchMapper.searchDispatch(searchDispatchDTO);
        PageInfo info = new PageInfo(page.getResult());
        int total = (int) info.getTotal();
        vo.setTotal(total);
        List <DispatchVO> list = new ArrayList <>();
        for (Dispatch dispatch : dispatchList) {
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
            dispatchVO.setCreateTime(sdf.format(dispatch.getCreateTime()));
            dispatchVO.setUpdateTime(sdf.format(dispatch.getCreateTime()));
            list.add(dispatchVO);
        }
        vo.setPeopleVOList(list);
        return vo;
    }

    /**
     * 根据人员ID查询布控人脸照片
     *
     * @param id 人员ID
     * @return byte[] 照片
     */
    public byte[] getFace(String id) {
        Dispatch dispatch = dispatchMapper.selectFaceById(id);
        if (dispatch != null) {
            return dispatch.getFace();
        }
        return null;
    }

    /**
     * 修改布控人员状态
     *
     * @param id     人员ID
     * @param status 状态
     * @return 0：失败，1：成功
     */
    public Integer dispatchStatus(String id, int status) {
        Dispatch dispatch = new Dispatch();
        dispatch.setId(id);
        dispatch.setStatus(status);
        int i = dispatchMapper.updateStatusById(dispatch);
        if (i == 1) {
            if (status == 0) {
                Dispatch dispatchData = dispatchMapper.selectByPrimaryKey(id);
                KafkaMessage kafkaMessage = new KafkaMessage();
                kafkaMessage.setId(id);
                kafkaMessage.setRegionId(dispatchData.getRegion());
                kafkaMessage.setBitFeature(dispatchData.getBitFeature());
                kafkaMessage.setCar(dispatchData.getCar());
                kafkaMessage.setMac(dispatchData.getMac());
                this.sendKafka(ADD, kafkaMessage);
            }
            if (status == 1) {
                this.sendKafka(DELETE, id);
            }
            return 1;
        }
        return 0;
    }

    public Integer insertDeploy(DispatchDTO dto) {
        Dispatch dispatch = new Dispatch();
        dispatch.setId(UuidUtil.getUuid());
        dispatch.setRegion(dto.getRegionId());
        dispatch.setName(dto.getName());
        dispatch.setIdcard(dto.getIdCard());
        dispatch.setCar(dto.getCar());
        dispatch.setMac(dto.getMac());
        dispatch.setNotes(dto.getNotes());
        if (dto.getFace() != null) {
            byte[] bytes = FaceUtil.base64Str2BitFeature(dto.getFace());
            FaceAttribute faceAttribute =
                    innerService.faceFeautreExtract(dto.getFace()) != null ? innerService.faceFeautreExtract(dto.getFace()).getFeature() : null;
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.error("Face feature extract failed, insert t_dispatch failed");
                throw new RuntimeException("Face feature extract failed, insert  t_dispatch failed");
            }
            dispatch.setFace(bytes);
            dispatch.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
            dispatch.setBitFeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
        }
        Integer status = dispatchMapper.insertSelective(dispatch);
        if (status != 1) {
            log.error("Insert info failed");
            return 0;
        }
        KafkaMessage message = new KafkaMessage();
        message.setId(dispatch.getId());
        message.setRegionId(dispatch.getRegion());
        message.setBitFeature(dispatch.getBitFeature());
        message.setCar(dispatch.getCar());
        message.setMac(dispatch.getMac());
        this.sendKafka(ADD, message);
        log.info("Insert info successfully");
        return status;
    }

    public Integer deleteDeploy(String id) {
        Integer status = dispatchMapper.deleteByPrimaryKey(id);
        if (status != 1) {
            log.info("Delete info failed ");
            return 0;
        }
        this.sendKafka(DELETE, id);
        log.info("Delete info successfully ");
        return status;
    }


    public Integer updateDeploy(DispatchDTO dto) {
        Dispatch dispatch = new Dispatch();
        dispatch.setId(dto.getId());
        dispatch.setRegion(dto.getRegionId());
        dispatch.setName(dto.getName());
        dispatch.setIdcard(dto.getIdCard());
        dispatch.setCar(dto.getCar());
        dispatch.setMac(dto.getMac());
        dispatch.setNotes(dto.getNotes());
        if (dto.getFace() != null) {
            byte[] bytes = FaceUtil.base64Str2BitFeature(dto.getFace());
            FaceAttribute faceAttribute = innerService.faceFeautreExtract(dto.getFace()).getFeature();
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.error("Face feature extract failed, update t_dispatch failed");
                throw new RuntimeException("Face feature extract failed, update t_dispatch failed");
            }
            dispatch.setFace(bytes);
            dispatch.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
            dispatch.setBitFeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
        }
        Integer status = dispatchMapper.updateByPrimaryKeySelective(dispatch);
        if (status != 1) {
            log.error("update info failed");
            return 0;
        }
        KafkaMessage message = new KafkaMessage();
        message.setId(dispatch.getId());
        message.setRegionId(dispatch.getRegion());
        message.setBitFeature(dispatch.getBitFeature());
        message.setCar(dispatch.getCar());
        message.setMac(dispatch.getMac());
        this.sendKafka(UPDATE, message);
        log.info("update info successfully");
        return status;
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

    //判断文件格式
    private Workbook getWorkbook(InputStream inputStream, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT, "Excel文件格式不正确");
        }
        return workbook;
    }

    private static Object parseValue(String s, Class c) {
        Object obj = null;
        String className = c.getName();
        //excel中的数字解析之后可能末尾会有.0，需要去除
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);

        if (className.equals("java.lang.Integer")) { //Integer
            obj = new Integer(s);
        } else if (className.equals("int")) { //int
            obj = (int) Integer.parseInt(s);
        } else if (className.equals("java.lang.String")) { //String
            obj = s;
        } else if (className.equals("java.lang.Double")) { //Double
            obj = new Double(s);
        } else if (className.equals("double")) { //double
            obj = (double) new Double(s);
        } else if (className.equals("java.lang.Float")) { //Float
            obj = new Float(s);
        } else if (className.equals("float")) { //float
            obj = (float) new Float(s);
        } else if (className.equals("java.util.Date")) { //Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                obj = sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (className.equals("long")) { //long
            obj = Long.parseLong(s);
        } else if (className.equals("java.util.Long")) { //Long
            obj = new Long(s);
        }
        return obj;
    }
}
