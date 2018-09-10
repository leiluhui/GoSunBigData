package com.hzgc.seemmo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzgc.seemmo.bean.ImageInfo;

public class JsonUtil {

    //imageInfo对象转字符串
    public static String objectToJsonString(String imagePath) {
        String imageStr = BASE64Util.getImageStr(imagePath);
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImageType(0);
        imageInfo.setImageId(1122);
        imageInfo.setImageData(imageStr);
        return JSON.toJSONString(imageInfo);
    }

    public static String objectToJsonString(byte[] bytes) {
        String imageStr = BASE64Util.getImageStr(bytes);
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImageType(0);
        imageInfo.setImageId(1122);
        imageInfo.setImageData(imageStr);
        return JSON.toJSONString(imageInfo);
    }

    //字符串装jsonobject
    public static JSONObject stringToJsonObject(String result) {
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject;
    }
}
