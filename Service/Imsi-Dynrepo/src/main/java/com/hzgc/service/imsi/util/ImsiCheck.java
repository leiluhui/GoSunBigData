package com.hzgc.service.imsi.util;

import java.util.HashMap;
import java.util.Map;

public class ImsiCheck {

    private static Map<String,Long> imsiMap = new HashMap<>();

    public static boolean checkImsi(String imsi,Long savetime) {
        if (null != imsi && imsiMap.containsKey(imsi)) {
            Long originTime = imsiMap.get(imsi);
            if ((savetime - originTime) > 3600000) {
                imsiMap.put(imsi,savetime);
                return true;
            }else {
                //更新时间
                imsiMap.put(imsi,savetime);
                return false;
            }
        }else {
            imsiMap.put(imsi,savetime);
            return true;
        }
    }
}
