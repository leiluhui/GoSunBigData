package com.hzgc.jniface;

import java.io.Serializable;

public class PersonPictureData implements Serializable {

    private byte[] imageData;

    private String imageID;

    private PersonAttributes personAttributes;

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

    public PersonAttributes getPersonAttributes() {
        return personAttributes;
    }

    public void setPersonAttributes(PersonAttributes personAttributes) {
        this.personAttributes = personAttributes;
    }
}
