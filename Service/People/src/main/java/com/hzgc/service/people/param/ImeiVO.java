package com.hzgc.service.people.param;

import com.hzgc.service.people.model.People;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "返回信息装类")
@Data
public class ImeiVO implements Serializable {
    @ApiModelProperty(value = "手环ID")
    private Long id;
    @ApiModelProperty(value = "人员全局ID")
    private String peopleId;
    @ApiModelProperty(value = "手环码")
    private String imei;
    @ApiModelProperty(value = "监护人名称")
    private String guardianName;
    @ApiModelProperty(value = "监护人联系方式")
    private String guardianPhone;
    @ApiModelProperty(value = "负责干部名称")
    private String cadresName;
    @ApiModelProperty(value = "负责干部联系方式")
    private String cadresPhone;
    @ApiModelProperty(value = "负责干警名称")
    private String policeName;
    @ApiModelProperty(value = "负责干警联系方式")
    private String policePhone;

    public ImeiVO imeiToImeiVO(People people) {
        ImeiVO imeiVO = new ImeiVO();
        imeiVO.setId(people.getImei().getId());
        imeiVO.setPeopleId(people.getImei().getPeopleid());
        imeiVO.setImei(people.getImei().getImei());
        imeiVO.setGuardianName(people.getImei().getGuardianname());
        imeiVO.setGuardianPhone(people.getImei().getGuardianphone());
        imeiVO.setCadresName(people.getImei().getCadresname());
        imeiVO.setCadresPhone(people.getImei().getCadresphone());
        imeiVO.setPoliceName(people.getImei().getPolicename());
        imeiVO.setPolicePhone(people.getImei().getPolicephone());
        return imeiVO;
    }
}