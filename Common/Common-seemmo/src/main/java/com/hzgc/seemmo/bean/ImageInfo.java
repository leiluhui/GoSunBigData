package com.hzgc.seemmo.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class ImageInfo implements Serializable {

    @JSONField(name = "ImageType")
    private Integer ImageType;
    @JSONField(name = "ImageId")
    private Integer ImageId;
    @JSONField(name = "ImageData")
    private String ImageData;

    public Integer getImageType() {
        return ImageType;
    }

    public void setImageType(Integer imageType) {
        ImageType = imageType;
    }

    public Integer getImageId() {
        return ImageId;
    }

    public void setImageId(Integer imageId) {
        ImageId = imageId;
    }

    public String getImageData() {
        return ImageData;
    }

    public void setImageData(String imageData) {
        ImageData = imageData;
    }
}
