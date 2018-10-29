package com.hzgc.jniface;

import java.io.Serializable;

public class PersonPictureData implements Serializable {

    private byte[] imageData;

    private String imageID;

    private PersonAttributes feature;

    private int[] image_coordinate;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public PersonAttributes getFeature() {
        return feature;
    }

    public void setFeature(PersonAttributes feature) {
        this.feature = feature;
    }

    public int[] getImage_coordinate() {
        return image_coordinate;
    }

    public void setImage_coordinate(int[] image_coordinate) {
        this.image_coordinate = image_coordinate;
    }
}
