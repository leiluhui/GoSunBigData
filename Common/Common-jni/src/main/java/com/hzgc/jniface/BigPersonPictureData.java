package com.hzgc.jniface;

import java.util.List;

public class BigPersonPictureData extends PersonPictureData {

    //大图检测，行人特征对象
    private List<PersonPictureData> smallImage;

    //行人的个数
    private Integer personTotal;

    //图片类型
    private String imageType;

    public List <PersonPictureData> getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(List <PersonPictureData> smallImage) {
        this.smallImage = smallImage;
    }


    public void setPersonTotal(Integer personTotal) {
        this.personTotal = personTotal;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
