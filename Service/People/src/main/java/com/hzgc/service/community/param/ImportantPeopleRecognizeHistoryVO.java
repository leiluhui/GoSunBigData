package com.hzgc.service.community.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "重点人员告警展示(大数据可视化首页左下角)出参")
@Data
public class ImportantPeopleRecognizeHistoryVO implements Serializable {
    // 人口库信息
    @ApiModelProperty(value = "重点人员ID")
    private String peopleId;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证")
    private String idcard;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "籍贯")
    private String birthplace;
    @ApiModelProperty(value = "所在地")
    private String address;
    @ApiModelProperty(value = "电话列表")
    private List<String> phone;
    @ApiModelProperty(value = "车辆列表")
    private List<String> car;
    @ApiModelProperty(value = "MAC(由IMSI码转换而来)列表")
    private List<String> mac;
    // 抓拍识别记录信息
    @ApiModelProperty(value = "类型")
    private Integer flag;
    @ApiModelProperty(value = "被识别照片ID")
    private Long pictureId;
    @ApiModelProperty(value = "被识别IMSI码")
    private String imsi;
    @ApiModelProperty(value = "被识别车牌")
    private String plate;
    @ApiModelProperty(value = "抓拍小图")
    private String surl;
    @ApiModelProperty(value = "抓拍大图")
    private String burl;
    @ApiModelProperty(value = "抓拍时间")
    private String time;
    @ApiModelProperty(value = "抓拍设备所在小区ID")
    private Long communityId;
    @ApiModelProperty(value = "抓拍设备所在小区名称")
    private String community;
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
}
