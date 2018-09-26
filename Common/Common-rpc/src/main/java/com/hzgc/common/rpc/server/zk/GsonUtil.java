package com.hzgc.common.rpc.server.zk;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class GsonUtil {
    private static Gson gson;
    static {
        gson = new Gson();
    }

    public static HashMap jsonToMap(String json){
        return gson.fromJson(json, HashMap.class);
    }

    public static String mapToJson(Map<String,String> map){
        return gson.toJson(map);
    }

    public static void main(String args[]){
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "11111");
        map.put("bbb", "22222");
        String json = mapToJson(map);
        Map res = jsonToMap(json);
        System.out.println(res.get("aaa"));
        System.out.println(res.get("bbb"));
    }
}
