package com.hzgc.compare.util;

import com.github.ltsopensource.core.domain.Job;
import com.google.gson.Gson;

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

}
