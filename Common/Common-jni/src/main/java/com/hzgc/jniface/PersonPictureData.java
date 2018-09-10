package com.hzgc.jniface;

import java.io.Serializable;
import java.util.List;

public class PersonPictureData implements Serializable {

    private byte[] imageData;

    private String imageID;

    private List<PersonAttribute> personAttribute;

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

    public List<PersonAttribute> getPersonAttribute() {
        return personAttribute;
    }

    public void setPersonAttribute(List<PersonAttribute> personAttribute) {
        this.personAttribute = personAttribute;
    }
}
