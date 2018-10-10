package com.hzgc.service.people.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnMessage implements Serializable {
    private Integer status;
    private String message;
}
