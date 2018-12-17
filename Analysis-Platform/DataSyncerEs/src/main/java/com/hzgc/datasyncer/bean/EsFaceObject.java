package com.hzgc.datasyncer.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;




@Data
@Document(indexName = DocumentInfo.FACE_INDEX_NAME, type = DocumentInfo.FACE_TYPE)
public class EsFaceObject {
    @Id
    private String id;  //唯一ID

    @Field(type = FieldType.Keyword)
    private String ipcid;   //设备序列号

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;   //抓拍时间

    @Field(type = FieldType.Keyword)
    private String hostname;    //FTP服务器主机名

    @Field(type = FieldType.Keyword)
    private String sabsolutepath;   //小图路径

    @Field(type = FieldType.Keyword)
    private String babsolutepath;   //大图路径

    @Field(type = FieldType.Keyword)
    private String provinceid;  //省或直辖市ID

    @Field(type = FieldType.Keyword)
    private String cityid;  //市或直辖市下属区ID

    @Field(type = FieldType.Keyword)
    private String regionid;    //区域ID或者直辖市下属街道ID

    @Field(type = FieldType.Keyword)
    private String communityid; //社区ID

    @Field(type = FieldType.Keyword)
    private int eyeglasses;

    @Field(type = FieldType.Keyword)
    private int age;

    @Field(type = FieldType.Keyword)
    private int mask;

    @Field(type = FieldType.Keyword)
    private int gender;

    @Field(type = FieldType.Keyword)
    private int huzi;

    @Field(type = FieldType.Keyword)
    private int sharpness;

    @Field(type = FieldType.Keyword)
    private String feature;

    @Field(type = FieldType.Keyword)
    private String bitFeature;
}
