package com.hzgc.common.carattribute.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸特征属性对象
 */
public class CarAttribute implements Serializable {

    /**
     * 属性字段名称，平台传过来的是枚举类的类名称，数据库查询时需要转小写
     */
    private String identify;

    /**
     * 属性中文描述
     */
    private String desc;

    /**
     * 逻辑关系,AND,OR
     */
    private CarLogistic logistic;

    /**
     * 属性值
     */
    private List<CarAttributeValue> values;

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CarLogistic getLogistic() {
        return logistic;
    }

    public void setLogistic(CarLogistic logistic) {
        this.logistic = logistic;
    }

    public List<CarAttributeValue> getValues() {
        return values;
    }

    public void setValues(List<CarAttributeValue> values) {
        this.values = values;
    }
}
