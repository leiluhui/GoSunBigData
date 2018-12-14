package com.hzgc.datasyncer.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = DocumentInfo.CAR_INDEX_NAME, type = DocumentInfo.CAR_TYPE)
public class EsCarObject {
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
    private String feature;

    @Field(type = FieldType.Keyword)
    private String bitfeature;

    @Field(type = FieldType.Keyword)
    private String vehicle_object_type;

    @Field(type = FieldType.Keyword)
    private String belt_maindriver;

    @Field(type = FieldType.Keyword)
    private String belt_codriver;

    @Field(type = FieldType.Keyword)
    private String brand_name;

    @Field(type = FieldType.Keyword)
    private String call_code;

    @Field(type = FieldType.Keyword)
    private String vehicle_color;

    @Field(type = FieldType.Keyword)
    private String crash_code;

    @Field(type = FieldType.Keyword)
    private String danger_code;

    @Field(type = FieldType.Keyword)
    private String marker_code;

    @Field(type = FieldType.Keyword)
    private String plate_schelter_code;

    @Field(type = FieldType.Keyword)
    private String plate_flag_code;

    @Field(type = FieldType.Keyword)
    private String plate_licence;

    @Field(type = FieldType.Keyword)
    private String plate_destain_code;

    @Field(type = FieldType.Keyword)
    private String plate_color_code;

    @Field(type = FieldType.Keyword)
    private String plate_type_code;

    @Field(type = FieldType.Keyword)
    private String rack_code;

    @Field(type = FieldType.Keyword)
    private String sunroof_code;

    @Field(type = FieldType.Keyword)
    private String vehicle_type;

}
