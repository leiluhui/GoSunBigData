package com.hzgc.seemmo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzgc.seemmo.bean.ImageBean;

public class JsonUtil {

    //imageInfo对象转字符串
    public static String objectToJsonString(String imagePath) {
        String imageStr = BASE64Util.getImageStr(imagePath);
        ImageBean imageBean = new ImageBean();
        imageBean.setJpeg(imageStr);
//        ImageInfo imageInfo = new ImageInfo();
//        imageInfo.setImageType(0);
//        imageInfo.setImageId(1122);
//        imageInfo.setImageData(imageStr);
        return JSON.toJSONString(imageBean);
    }

    public static String objectToJsonString(byte[] bytes) {
        String imageStr = BASE64Util.getImageStr(bytes);
        ImageBean imageBean = new ImageBean();
        imageBean.setJpeg(imageStr);
        return JSON.toJSONString(imageBean);
    }

    //字符串装jsonobject
    public static JSONObject stringToJsonObject(String result) {
        return JSON.parseObject(result);
    }
}
