package com.hzgc.service.people.service;

import com.hzgc.service.people.fields.Edulevel;
import com.hzgc.service.people.fields.Flag;
import com.hzgc.service.people.fields.Politic;
import com.hzgc.service.people.fields.Provinces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Map<Integer, String> getEdulevelPick() {
        return Edulevel.getEdulevel();
    }

    public Map<Integer, String> getFlagPick() {
        return Flag.getFlag();
    }

    public Map<Integer, String> getPoliticPick() {
        return Politic.getPolitic();
    }

    public Map<Integer, String> getProvincesPick() {
        return Provinces.getProvince();
    }

    public Map<Integer, String> getCityPick(int index) {
        return Provinces.getCity(index);
    }
}
