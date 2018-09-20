package com.hzgc.service.people.fields;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Sex implements Serializable {

    private static final Map<Integer, String> model = new LinkedHashMap<>();

    static {
        model.put(0, "未知");
        model.put(1, "男");
        model.put(2, "女");
    }

    public static String getSex(int i) {
        return model.get(i);
    }
}
