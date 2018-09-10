package com.hzgc.jniface;

public class SmallImage {
    //图片二进制数据流
    private byte[] pictureStream;
    //人特整整及属性
    private FaceAttribute faceAttribute;
    //行人特征及属性
    private PersonAttribute personAttribute;
    //车辆特征及属性
    private CarAttribute carAttribute;
    //图片类型
    private String imageType;

    public SmallImage() {
    }

    public byte[] getPictureStream() {
        return pictureStream;
    }

    public void setPictureStream(byte[] pictureStream) {
        this.pictureStream = pictureStream;
    }

    public FaceAttribute getFaceAttribute() {
        return faceAttribute;
    }

    public void setFaceAttribute(FaceAttribute faceAttribute) {
        this.faceAttribute = faceAttribute;
    }

    public PersonAttribute getPersonAttribute() {
        return personAttribute;
    }

    public void setPersonAttribute(PersonAttribute personAttribute) {
        this.personAttribute = personAttribute;
    }

    public CarAttribute getCarAttribute() {
        return carAttribute;
    }

    public void setCarAttribute(CarAttribute carAttribute) {
        this.carAttribute = carAttribute;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
