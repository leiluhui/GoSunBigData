package com.hzgc.compare.util;

import com.github.ltsopensource.core.domain.Job;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class JobUtil {
    private static Gson gson;
    static {
        gson = new Gson();
    }
    public static Job jsonToJob(String json) {
        return gson.fromJson(json, Job.class);
    }


    public static String jobToJson(Object obj){
        return gson.toJson(obj);
    }

    public static HashMap jsonToMap(String json){
        return gson.fromJson(json, HashMap.class);
    }

    public static String mapToJson(Map<String,String> map){
        return gson.toJson(map);
    }

}
