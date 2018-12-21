package com.hzgc.cloud.people.service;

import com.hzgc.cloud.people.dao.ImeiMapper;
import com.hzgc.cloud.people.model.Imei;
import com.hzgc.cloud.people.param.ImeiVO;
import com.hzgc.cloud.people.param.MentalPatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MentalPatientService {
    @Autowired
    @SuppressWarnings("unused")
    private ImeiMapper imeiMapper;

    public int insertMentalPatient(MentalPatientDTO mentalPatientDTO) {
        Imei imei = mentalPatientDTO.mentalPatientDTOShift_insert(mentalPatientDTO);
        return imeiMapper.insertSelective(imei);
    }

    public int updateMentalPatient(MentalPatientDTO mentalPatientDTO) {
        Imei imei = mentalPatientDTO.mentalPatientDTOShift_insert(mentalPatientDTO);
        return imeiMapper.updateByPrimaryKeySelective(imei);
    }

    public ImeiVO selectMentalPatientByPeopleId(String peopleId) {
        ImeiVO vo = new ImeiVO();
        Imei imei = imeiMapper.selectByPeopleId(peopleId);
        vo.setId(imei.getId());
        vo.setPeopleId(imei.getPeopleid());
        vo.setImei(imei.getImei());
        vo.setGuardianName(imei.getGuardianname());
        vo.setGuardianPhone(imei.getGuardianphone());
        vo.setCadresName(imei.getCadresname());
        vo.setCadresPhone(imei.getCadresphone());
        vo.setPoliceName(imei.getPolicename());
        vo.setPolicePhone(imei.getPolicephone());
        return vo;
    }

    /**
     * 根据精神病手环ID查询peopleId(检测精神病手环ID是否绑定人口信息)
     *
     * @param imeiId 精神病手环ID
     * @return peopleId
     */
    public String selectPeopleIdByImeiId(String imeiId) {
        return imeiMapper.selectPeopleIdByImei(imeiId);
    }
}
