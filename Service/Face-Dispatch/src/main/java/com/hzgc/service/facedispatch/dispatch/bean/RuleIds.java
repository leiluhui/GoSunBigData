package com.hzgc.service.facedispatch.dispatch.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RuleIds<T> implements Serializable{

    private static final long serialVersionUID = 114352653081166330L;
    private List<T> ruleIds;
}
