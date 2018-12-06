package com.hzgc.cloud.dyncar.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SingleSearchResult implements Serializable {
    private String searchId;
    private List<CapturedPicture> pictures;
    private int total;
    private List<GroupByIpc> devicePictures;
    private Integer deviceTotal;

}
