package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureVO implements Serializable {
    private List<Long> pictureIds;
    private List<byte[]> idcardPics;
    private List<byte[]> capturePics;
}
