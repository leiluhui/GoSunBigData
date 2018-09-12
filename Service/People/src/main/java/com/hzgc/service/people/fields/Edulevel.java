package com.hzgc.service.people.fields;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Edulevel implements Serializable {

    private static Map<Integer, String> model = new LinkedHashMap<>();

    static {
        model.put(0, "小学");
        model.put(1, "初中");
        model.put(2, "高中");
        model.put(3, "职高");
        model.put(4, "中专");
        model.put(5, "大专");
        model.put(6, "本科");
        model.put(7, "硕士");
        model.put(8, "博士");
        model.put(9, "博士后");
    }

    public static Map<Integer, String> getEdulevel() {
        return model;
    }
}
