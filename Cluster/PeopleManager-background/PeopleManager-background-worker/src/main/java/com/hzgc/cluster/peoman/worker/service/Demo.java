package com.hzgc.cluster.peoman.worker.service;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceAttribute;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.UUID;

/**
 * Created by Administrator on 2018-9-26.
 */
public class Demo {
    public static void main(String[] args) {

       /* private String id;                  //对象唯一ID
        private String ipcId;               // 设备ID
        private String timeStamp;           // 时间（格式：2017-01-01 00：00：00）
        private FaceAttribute attribute;    // 人脸属性对象
        private String sFtpUrl;                // 小图ftp路径（带hostname的ftpurl）
        private String bFtpUrl;                // 大图ftp路径（带hostname的ftpurl）
        private String sAbsolutePath;        // 小图存储绝对路径(带ftp根目录)
        private String bAbsolutePath;       //大图存储绝对路径(带ftp根目录)
        private String sRelativePath;       //小图存储绝对路径(不带ftp根目录)
        private String bRelativePath;       //大图存储绝对路径(不带ftp根目录)
        private String hostname;            // 图片保存主机:hostname*/
        FaceObject faceObject = new FaceObject();
        FaceAttribute faceAttribute = new FaceAttribute();
        faceAttribute.setBitFeature(Bytes.toBytes("TlfaJrPxn9EMvKU3auGxDb/rqk6rNtyDkXM52TjR+oc="));
        faceObject.setId(UUID.randomUUID().toString());
        faceObject.setIpcId("123");
        faceObject.setTimeStamp("2018-09-26 10:48:57");
        faceObject.setAttribute(faceAttribute);
        faceObject.setsFtpUrl("ftp://s100/surl/test");
        faceObject.setbFtpUrl("ftp://s100/burl/test");
        faceObject.setsAbsolutePath("ftp://172.18.18.100:/surl/test");
        faceObject.setbAbsolutePath("ftp://172.18.18.100:/burl/test");
        faceObject.setsRelativePath("/surl/test");
        faceObject.setbRelativePath("/burl/test");
        faceObject.setHostname("s100");

        String json = JacksonUtil.toJson(faceObject);

        System.out.println(json);
    }
}
