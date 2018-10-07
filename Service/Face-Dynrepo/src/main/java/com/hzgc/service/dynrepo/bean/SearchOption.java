package com.hzgc.service.dynrepo.bean;

import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.PictureData;
import com.hzgc.seemmo.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索选项
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchOption extends CaptureOption implements Serializable {


    //待查询图片对象列表
    private List<PictureData> images;

    //是否将传入若干图片当做同一个人,不设置默认为false,即不是同一个人
    private boolean singlePerson;

    //阈值
    private float similarity;
}
