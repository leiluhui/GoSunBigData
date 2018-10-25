package com.hzgc.jniface;

import java.util.List;

public class BigPersonPictureData extends PersonPictureData {

    //大图检测，行人特征对象
    private List<PersonPictureData> smallImages;

    //行人的个数
    private Integer total;

    //图片类型
    private String imageType;

    public List <PersonPictureData> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List <PersonPictureData> smallImages) {
        this.smallImages = smallImages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
