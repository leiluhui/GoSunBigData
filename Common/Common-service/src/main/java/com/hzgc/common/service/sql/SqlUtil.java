package com.hzgc.common.service.sql;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SqlUtil {

    public static Map<String, String> getSql(Class<?> clz) {
        try {
            Map<String, String> sql = new HashMap<>();
            Field[] fields = clz.getFields();
            for (Field field : fields) {
                sql.put(field.getName(), (String) field.get(clz));
            }
            return sql;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
