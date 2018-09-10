package com.hzgc.service.collect.controller;

import com.hzgc.common.carattribute.bean.CarAttribute;
import com.hzgc.common.carattribute.service.CarAttributeService;
import com.hzgc.common.faceattribute.bean.Attribute;
import com.hzgc.common.faceattribute.service.AttributeService;
import com.hzgc.common.personattribute.bean.PersonAttribute;
import com.hzgc.common.personattribute.service.PersonAttributeService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.jniface.CarPictureData;
import com.hzgc.jniface.PersonPictureData;
import com.hzgc.jniface.PictureData;
import com.hzgc.service.collect.service.CarExtractService;
import com.hzgc.service.collect.service.FaceExtractService;
import com.hzgc.service.collect.service.PersonExtractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "特征属性服务")
@Slf4j
public class ExtractController {

    @Autowired
    @SuppressWarnings("unused")
    private FaceExtractService faceExtractService;

    @Autowired
    @SuppressWarnings("unused")
    private AttributeService faceAttributeService;

    @Autowired
    @SuppressWarnings("unused")
    private PersonExtractService personExtractService;

    @Autowired
    @SuppressWarnings("unused")
    private PersonAttributeService personAttributeService;

    @Autowired
    @SuppressWarnings("unused")
    private CarExtractService carExtractService;

    @Autowired
    @SuppressWarnings("unused")
    private CarAttributeService carAttributeService;

    @ApiOperation(value = "人脸特征值提取", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_BIN, method = RequestMethod.POST)
    public ResponseResult<PictureData> faceFeatureExtract(@ApiParam(name = "image", value = "图片") MultipartFile image) {
        byte[] imageBin = null;
        if (image == null) {
            log.error("Start extract feature by binary, image is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        try {
            imageBin = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PictureData pictureData = faceExtractService.featureExtractByImage(imageBin);
        return ResponseResult.init(pictureData);
    }

    @ApiOperation(value = "根据ftpUrl提取人脸特征值", response = ResponseResult.class)
    @ApiImplicitParam(name = "pictureUrl", value = "图片路径", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_FTP, method = RequestMethod.GET)
    public ResponseResult<PictureData> getFeatureExtract(String pictureUrl) {
        if (null == pictureUrl) {
            log.error("Start extract feature by ftp, pictureUrl is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        PictureData pictureData = faceExtractService.getFeatureExtractByFtp(pictureUrl);
        return ResponseResult.init(pictureData);
    }

    //根据图片提取人脸特征值
    @ApiIgnore
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_BYTES, method = RequestMethod.POST)
    public PictureData getFeatureExtract(@RequestBody byte[] bytes) {
        if (null != bytes) {
            return faceExtractService.featureExtractByImage(bytes);
        }
        log.info("Bytes param is null");
        return null;
    }

    @ApiOperation(value = "人脸属性查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.FACE_ATTRIBUTE, method = RequestMethod.GET)
    public ResponseResult<List<Attribute>> getAttribute() {
        List<Attribute> attributeList;
        attributeList = faceAttributeService.getAttribute();
        if (null != attributeList) {
            log.info("AttributeList acquires is success");
            return ResponseResult.init(attributeList);
        } else {
            log.error("AttributeList acquires is null");
            return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
        }
    }

    @ApiOperation(value = "行人的特征值提取", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PERSON_FEATURE_EXTRACT_BIN, method = RequestMethod.POST)
    public ResponseResult<PersonPictureData> personFeatureExtract(@ApiParam(name = "image", value = "图片") MultipartFile image) {
        byte[] imageBin = null;
        if (null == image) {
            log.error("Start extract person feature by binary, image is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        try {
            imageBin = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PersonPictureData personPictureData = personExtractService.featureExtractByImage(imageBin);
        return ResponseResult.init(personPictureData);
    }

    @ApiOperation(value = "行人属性查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.PERSON_ATTRIBUTE, method = RequestMethod.GET)
    public ResponseResult<List<PersonAttribute>> getPersonAttribute() {
        List<PersonAttribute> attributeList = personAttributeService.getPersonAttribute();
        if (null != attributeList) {
            log.info("AttributeList acquire is succeed");
            return ResponseResult.init(attributeList);
        } else {
            log.error("AttributeList is null");
            return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
        }
    }

    @ApiOperation(value = "获取车辆属性", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.CAR_EXTRACT, method = RequestMethod.POST)
    public ResponseResult<CarPictureData> carExtract(@ApiParam(name = "image", value = "图片") MultipartFile image) {
        byte[] imageBin = null;
        if (image == null) {
            log.error("Start car extract by binary, image is null");
            return ResponseResult.error(RestErrorCode.ILLEGAL_ARGUMENT);
        }
        try {
            imageBin = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CarPictureData carPictureData = carExtractService.carExtractByImage(imageBin);
        return ResponseResult.init(carPictureData);
    }

    @ApiOperation(value = "车辆属性查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.CAR_ATTRIBUTE, method = RequestMethod.GET)
    public ResponseResult<List<CarAttribute>> getCarAttribute() {
        List<CarAttribute> attributeList = carAttributeService.getCarAttribute();
        if (null != attributeList) {
            log.info("AttributeList acquire is succeed");
            return ResponseResult.init(attributeList);
        } else {
            log.error("AttributeList is null");
            return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
        }
    }
}
