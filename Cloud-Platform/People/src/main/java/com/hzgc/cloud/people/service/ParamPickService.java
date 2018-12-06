package com.hzgc.cloud.people.service;

import com.hzgc.cloud.people.fields.Edulevel;
import com.hzgc.cloud.people.fields.Flag;
import com.hzgc.cloud.people.fields.Politic;
import com.hzgc.cloud.people.fields.Provinces;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ParamPickService {

    public List<Param> getEdulevelPick() {
        Map<Integer, String> map = Edulevel.getEdulevel();
        List<Param> list = new ArrayList<>();
        if (map != null && map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                list.add(new Param(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public List<Param> getFlagPick() {
        Map<Integer, String> map = Flag.getFlag();
        List<Param> list = new ArrayList<>();
        if (map != null && map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                list.add(new Param(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public List<Param> getPoliticPick() {
        Map<Integer, String> map = Politic.getPolitic();
        List<Param> list = new ArrayList<>();
        if (map != null && map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                list.add(new Param(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public List<Param> getProvincesPick() {
        Map<Integer, String> map = Provinces.getProvince();
        List<Param> list = new ArrayList<>();
        if (map != null && map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                list.add(new Param(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public List<Param> getCityPick(int index) {
        Map<Integer, String> map = Provinces.getCity(index);
        List<Param> list = new ArrayList<>();
        if (map != null && map.size() > 0){
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                list.add(new Param(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    @ApiModel(value = "返回选项封装")
    @Data
    public class Param implements Serializable {
        @ApiModelProperty(value = "下标")
        private int index;
        @ApiModelProperty(value = "选项")
        private String param;

        Param(int index, String param) {
            this.index = index;
            this.param = param;
        }
    }
}
