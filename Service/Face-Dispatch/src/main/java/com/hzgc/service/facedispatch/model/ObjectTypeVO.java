package com.hzgc.service.facedispatch.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ObjectTypeVO implements Serializable {
    private String id;

    private String name;

    private String reason;

    private String creator;

    private Date createtime;

    public static ObjectTypeVO objectTypeShift(ObjectType type){
        ObjectTypeVO vo = new ObjectTypeVO();
        if (type == null){
            return vo;
        }
        vo.setId(type.getId());
        vo.setName(type.getName());
        vo.setReason(type.getReason());
        vo.setCreator(type.getCreator());
        vo.setCreatetime(type.getCreatetime());
        return vo;
    }

    @Override
    public String toString() {
        return "ObjectTypeVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", creator='" + creator + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
