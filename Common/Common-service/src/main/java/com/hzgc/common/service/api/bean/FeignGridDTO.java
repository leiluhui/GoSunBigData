package com.hzgc.common.service.api.bean;

import lombok.Data;

/**
 * 内部查询网格信息数据结构
 *
 * @author zhangdl
 * @date 2018.12.12
 */
@Data
public class FeignGridDTO {
    private Long gridId;
    private String gridName;
    private String areaPath;
    private Long staffId;
    private String staffName;
    private String gridPath;
}
