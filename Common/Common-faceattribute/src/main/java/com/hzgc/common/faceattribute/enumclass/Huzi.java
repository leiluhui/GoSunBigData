package com.hzgc.common.faceattribute.enumclass;


import com.hzgc.common.faceattribute.bean.Logistic;

import java.io.Serializable;

/**
 * 胡子类型：0->没有胡子；1->有胡子；
 */
public enum Huzi implements Serializable {

    Nobeard(0),Hasbeard(1);

    private int value;

    /**
     * 与其他属性的拼接运算，默认是OR运算
     */
    private Logistic logistic = Logistic.OR;

    private Huzi(int value) {
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

    public static Huzi get(int huzivalue) {
        for (Huzi huzi : Huzi.values()) {
            if (huzivalue == huzi.getValue()) {
                return huzi;
            }
        }
        return Huzi.Nobeard;
    }

    /**
     * 获取属性描述
     *
     * @param huzi 属性对象
     * @return 属性描述信息
     */
    public static String getDesc(Huzi huzi) {
        if (huzi == Huzi.Hasbeard) {
            return "有胡子";
        } else if (huzi == Huzi.Nobeard) {
            return "没有胡子";
        }
        return null;
    }
}
