package com.hzgc.service.facedispatch.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;

@Data
@JsonIgnoreProperties
public class ObjectInfoVO implements Serializable {
    private String id;

    private String name;

    private String typeid;

    private String typename;

    private String idcard;

    private Integer sex;

    private String reason;

    private String creator;

    private String createphone;

    private String createtime;

    private String updatetime;

    public static ObjectInfoVO ObjectInfoShift(ObjectInfo info, String typename){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectInfoVO vo = new ObjectInfoVO();
        if (info == null) {
            return vo;
        }
        vo.setId(info.getId());
        vo.setName(info.getName());
        vo.setTypeid(info.getTypeid());
        vo.setTypename(typename);
        vo.setIdcard(info.getIdcard());
        vo.setSex(info.getSex());
        vo.setReason(info.getReason());
        vo.setCreator(info.getCreator());
        vo.setCreatephone(info.getCreatephone());
        vo.setCreatetime(sdf.format(info.getCreatetime()));
        vo.setUpdatetime(sdf.format(info.getUpdatetime()));
        return vo;
    }

    @Override
    public String toString() {
        return "ObjectInfoVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeid='" + typeid + '\'' +
                ", typename='" + typename + '\'' +
                ", idcard='" + idcard + '\'' +
                ", sex=" + sex +
                ", reason='" + reason + '\'' +
                ", creator='" + creator + '\'' +
                ", createphone='" + createphone + '\'' +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                '}';
    }
}
