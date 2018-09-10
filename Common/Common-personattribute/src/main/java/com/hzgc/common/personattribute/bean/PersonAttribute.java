package com.hzgc.common.personattribute.bean;

import java.io.Serializable;
import java.util.List;

public class PersonAttribute implements Serializable {
    private String desc;
    private String identify;
    private PersonLogistic personLogistic;
    private List<PersonAttributeValue> values;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public PersonLogistic getPersonLogistic() {
        return personLogistic;
    }

    public void setPersonLogistic(PersonLogistic personLogistic) {
        this.personLogistic = personLogistic;
    }

    public List<PersonAttributeValue> getValues() {
        return values;
    }

    public void setValues(List<PersonAttributeValue> values) {
        this.values = values;
    }
}
