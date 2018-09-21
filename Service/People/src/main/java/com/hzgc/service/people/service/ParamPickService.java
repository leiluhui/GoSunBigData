package com.hzgc.service.people.service;

import com.hzgc.service.people.fields.Edulevel;
import com.hzgc.service.people.fields.Flag;
import com.hzgc.service.people.fields.Politic;
import com.hzgc.service.people.fields.Provinces;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParamPickService {
    @Autowired
    private Edulevel edulevelPick;
    @Autowired
    private Flag flagPick;
    @Autowired
    private Politic politicPick;
    @Autowired
    private Provinces provincesPick;

    public List<Param> getEdulevelPick() {
        Map<Integer, String> map = Edulevel.getEdulevel();
        List<Param> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(new Param(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public List<Param> getFlagPick() {
        Map<Integer, String> map = Flag.getFlag();
        List<Param> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(new Param(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public List<Param> getPoliticPick() {
        Map<Integer, String> map = Politic.getPolitic();
        List<Param> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(new Param(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public List<Param> getProvincesPick() {
        Map<Integer, String> map = Provinces.getProvince();
        List<Param> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(new Param(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public List<Param> getCityPick(int index) {
        Map<Integer, String> map = Provinces.getCity(index);
        List<Param> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            list.add(new Param(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    @ApiModel(value = "返回选项封装")
    @Data
    public class Param implements Serializable{
        @ApiModelProperty(value = "下标")
        private int index;
        @ApiModelProperty(value = "选项")
        private String param;

        public Param(int index, String param) {
            this.index = index;
            this.param = param;
        }
    }
}
