package com.hzgc.service.face.controller;

import com.hzgc.common.carattribute.bean.CarAttribute;
import com.hzgc.common.carattribute.service.CarAttributeService;
import com.hzgc.common.faceattribute.bean.Attribute;
import com.hzgc.common.faceattribute.service.AttributeService;
import com.hzgc.common.personattribute.bean.PersonAttribute;
import com.hzgc.common.personattribute.service.PersonAttributeService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.service.rest.BigDataPath;
import com.hzgc.jni.CarPictureData;
import com.hzgc.jni.PersonPictureData;

import com.hzgc.jni.PictureData;
import com.hzgc.service.face.service.CarExtractService;
import com.hzgc.service.face.service.FaceExtractService;
import com.hzgc.service.face.service.PersonExtractService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "人脸特征属性服务")
@Slf4j
public class FaceController {

    @Autowired
    @SuppressWarnings("unused")
    private FaceExtractService faceExtractService;

    @Autowired
    @SuppressWarnings("unused")
    private AttributeService attributeService;

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

    //特征值获取
    @ApiOperation(value = "图片的特征值提取", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_BIN, method = RequestMethod.POST)
    public ResponseResult<PictureData> featureExtract(@ApiParam(name = "image", value = "图片") MultipartFile image) {
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


    //行人属性获取
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

    /**
     * 人/车属性查询
     *
     * @return List<Attribute>
     */
    @ApiOperation(value = "属性特征查询", response = ResponseResult.class)
    @RequestMapping(value = BigDataPath.FACE_ATTRIBUTE, method = RequestMethod.GET)
    @SuppressWarnings("unused")
    public ResponseResult<List<Attribute>> getAttribute() {
        List<Attribute> attributeList;
        attributeList = attributeService.getAttribute();
        if (null != attributeList) {
            log.info("AttributeList acquires is successed");
            return ResponseResult.init(attributeList);
        } else {
            log.error("AttributeList acquires is null");
            return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
        }
    }

    /**
     * 获取行人属性
     *
     * @return List<Attribute>
     */
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

    //ftp提取特征值
    @ApiOperation(value = "根据url提取图片特征值", response = ResponseResult.class)
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

    //图片数组提取特征值
    @ApiIgnore
    @RequestMapping(value = BigDataPath.FEATURE_EXTRACT_BYTES, method = RequestMethod.POST)
    public PictureData getFeatureExtract(@RequestBody byte[] bytes) {
        if (null != bytes) {
            return faceExtractService.featureExtractByImage(bytes);
        }
        log.info("Bytes param is null");
        return null;
    }


    //获取车辆属性
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

    /**
     * 车属性查询
     *
     * @return List<Attribute>
     */
    @ApiOperation(value = "车属性查询", response = ResponseResult.class)
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
