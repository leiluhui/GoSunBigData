package com.hzgc.cloud.people.fields;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Politic implements Serializable {
    private static final Map<Integer, String> model = new LinkedHashMap<>();

    static {
        model.put(0, "群众");
        model.put(1, "共青团员");
        model.put(2, "中共预备党员");
        model.put(3, "中共党员");
        model.put(4, "民革党员");
        model.put(5, "民盟盟员");
        model.put(6, "民建会员");
        model.put(7, "民进会员");
        model.put(8, "农工党党员");
        model.put(9, "致公党党员");
        model.put(10, "九三学社社员");
        model.put(11, "台盟盟员");
        model.put(12, "无党派人士");
    }

    public static Map<Integer, String> getPolitic() {
        return model;
    }
}
