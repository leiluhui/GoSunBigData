package com.hzgc.service.facedispatch.starepo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilterField implements Serializable {
    private List<String> idList;                  // 对象ID列表
    private List<String> typeIdList;              // 对象类型ID列表
    private String name;                          // 对象名字
    private String idCard;                        // 身份证
    private Integer sex;                          // 性别            [0 = 未知（默认选项），1 = 男，2 = 女]
    private String creator;                       // 创建人
    private String createPhone;                   // 布控人联系方式
}
