package com.hzgc.common.service.faceattribute.enumclass;


import com.hzgc.common.service.faceattribute.bean.Logistic;

import java.io.Serializable;

/**
 * 性别：0->女；1->男；
 */
public enum Gender implements Serializable {

    Male(1), Female(0);

    private int value;

    /**
     * 与其他属性的拼接运算，默认是OR运算
     */
    private Logistic logistic = Logistic.OR;

    private Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Logistic getLogistic() {
        return logistic;
    }

    public void setLogistic(Logistic logistic) {
        this.logistic = logistic;
    }

    public static Gender get(int gendervalue) {
        for (Gender gender : Gender.values()) {
            if (gendervalue == gender.getValue()) {
                return gender;
            }
        }
        return null;
    }

    /**
     * 获取属性描述
     *
     * @param gender 属性对象
     * @return 属性描述信息
     */
    public static String getDesc(Gender gender) {
        if (gender == Gender.Male) {
            return "男";
        } else if (gender == Gender.Female) {
            return "女";
        }
        return null;
    }
}
