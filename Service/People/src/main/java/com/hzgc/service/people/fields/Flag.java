package com.hzgc.service.people.fields;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Flag implements Serializable {
    private static Map<Integer, String> model = new LinkedHashMap<>();

    static {
        model.put(0, "矫正人员");
        model.put(1, "刑释解戒");
        model.put(2, "精神病人");
        model.put(3, "吸毒人员");
        model.put(4, "境外人员");
        model.put(5, "艾滋病人");
        model.put(6, "重点青少年");
        model.put(7, "留守人员");
        model.put(8, "重点人员");
        model.put(9, "关爱人员");
    }

    public static Map<Integer, String> getFlag() {
        return model;
    }

    public static String getFlag(Integer index) {
        return model.get(index);
    }
}
