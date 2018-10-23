package com.hzgc.jniface;

import java.io.Serializable;
import java.util.List;

public class PersonPictureData implements Serializable {

    private byte[] imageData;

    private String imageID;

    private List<PersonAttributes> personAttributes;

    private int personTotal;

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

    public List<PersonAttributes> getPersonAttributes() {
        return personAttributes;
    }

    public void setPersonAttributes(List<PersonAttributes> personAttributes) {
        this.personAttributes = personAttributes;
    }

    public int getPersonTotal() {
        return personTotal;
    }

    public void setPersonTotal(int personTotal) {
        this.personTotal = personTotal;
    }
}
