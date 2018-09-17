package com.hzgc.service.facedispatch.starepo.model;

import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.PictureData;

import java.io.Serializable;

public class ObjectInfoDTO implements Serializable {
    private String id;

    private String name;

    private String typeid;

    private String idcard;

    private Integer sex;

    private String reason;

    private String creator;

    private String createphone;

    private PictureData picture;

    public ObjectInfo objectInfoDTOShift(ObjectInfoDTO dto) {
        ObjectInfo info = new ObjectInfo();
        if (dto == null) {
            return info;
        }
        info.setId(dto.id);
        info.setName(dto.name);
        info.setTypeid(dto.typeid);
        info.setIdcard(dto.idcard);
        info.setSex(dto.sex);
        info.setReason(dto.reason);
        info.setCreator(dto.creator);
        info.setCreatephone(dto.createphone);
        if (dto.picture != null) {
            info.setPicture(dto.picture.getImageData());
            if (dto.picture.getFeature() != null) {
                float[] feature = dto.picture.getFeature().getFeature();
                info.setFeature(JacksonUtil.toJson(feature));
            }
        }
        return info;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTypeid() {
        return typeid;
    }

    public String getIdcard() {
        return idcard;
    }

    public Integer getSex() {
        return sex;
    }

    public String getReason() {
        return reason;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatephone() {
        return createphone;
    }

    public PictureData getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "ObjectInfoDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeid='" + typeid + '\'' +
                ", idcard='" + idcard + '\'' +
                ", sex=" + sex +
                ", reason='" + reason + '\'' +
                ", creator='" + creator + '\'' +
                ", createphone='" + createphone + '\'' +
                ", picture=" + picture +
                '}';
    }
}
