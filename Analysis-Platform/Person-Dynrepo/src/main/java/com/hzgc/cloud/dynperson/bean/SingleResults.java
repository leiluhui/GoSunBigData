package com.hzgc.cloud.dynperson.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingleResults implements Serializable {

        private String searchId;

        private int total;

        private Integer deviceTotal;

        private List<DevicePictures> devicePicturesList;

        private List<Pictures> pictures;
}
