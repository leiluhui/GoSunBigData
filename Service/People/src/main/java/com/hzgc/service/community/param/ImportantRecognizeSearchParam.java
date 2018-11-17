package com.hzgc.service.community.param;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ImportantRecognizeSearchParam implements Serializable {
    private List<Long> communityIds;
    private Timestamp startTime;
    private Timestamp endTime;
    private int searchType;

}
