package com.hzgc.service.facedispatch.starepo.model;

import java.io.Serializable;

public class ObjectTypeDTO implements Serializable {
    private String id;

    private String name;

    private String reason;

    private String creator;

    public ObjectType objectTypeDTOShift(ObjectTypeDTO dto){
        ObjectType type = new ObjectType();
        if (dto == null){
            return type;
        }
        type.setId(dto.id);
        type.setName(dto.name);
        type.setReason(dto.reason);
        type.setCreator(dto.creator);
        return type;
    }

    @Override
    public String toString() {
        return "ObjectTypeDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getCreator() {
        return creator;
    }
}
