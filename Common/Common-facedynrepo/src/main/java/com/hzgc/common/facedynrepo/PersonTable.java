package com.hzgc.common.facedynrepo;

import java.io.Serializable;

/**
 * 动态库表属性
 */
public class PersonTable implements Serializable {
    //es索引
    public static final String DYNAMIC_INDEX = "person";
    //es类型
    public static final String PERSON_INDEX_TYPE = "recognize";
    //图片的小图ftp地址 xxx/xxx/xxx/
    public static final String SURL = "surl";
    //图片的大图ftp地址 xxx/xxx/xxx/
    public static final String BURL = "burl";
    //设备id
    public static final String IPCID = "ipcid";
    //时间区间 数据格式 小时+分钟 例如:11:30用1130表示
    public static final String TIMESLOT = "timeslot";
    //时间戳 数据格式 xxxx-xx-xx xx:xx:xx(年-月-日 时:分:秒)
    public static final String TIMESTAMP = "timestamp";
    //日期 分区字段 数据格式 xxxx-xx-xx(年-月-日)
    public static final String DATE = "date";

    //行人属性：年龄
    public static final String AGE = "age_code";
    //行人属性：怀抱婴儿
    public static final String BABY = "baby_code";
    //行人属性：拎东西
    public static final String BAG = "bag_code";
    //行人属性：下衣颜色
    public static final String BOTTOMCOLOR = "bottomcolor_code";
    //行人属性：下衣类型
    public static final String BOTTOMTYPE = "bottomtype_code";
    //行人属性：帽子类型
    public static final String HAT = "hat_code";
    //行人属性：头发
    public static final String HAIR = "hair_code";
    //行人属性：背包类型
    public static final String KNAPSACK = "knapsack_code";
    //行人属性：是否斜挎包
    public static final String MESSENGERBAG = "messengerbag_code";
    //行人属性：行人方向
    public static final String ORIENTATION = "orientation_code";
    //行人属性：性别
    public static final String SEX = "sex_code";
    //行人属性：肩上的包
    public static final String SHOULDERBAG = "shoulderbag_code";
    //行人属性：雨伞
    public static final String UMBRELLA = "umbrella_code";
    //行人属性：上衣颜色
    public static final String UPPERCOLOR = "uppercolor_code";
    //行人属性：上衣类型
    public static final String UPPERTYPE = "uppertype_code";
    //行人属性：车辆类型
    public static final String CTYPE = "car_type";

    //public static final String STARTTIME = "startTime";

    public static final String HOSTNAME = "hostname";

    public static final String IP = "ip";

    public static final String RELATIVEPATH = "relativepath";

    public static final String RELATIVEPATH_BIG = "relativepath_big";

}
