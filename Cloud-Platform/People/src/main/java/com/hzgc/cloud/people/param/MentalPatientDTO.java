package com.hzgc.cloud.people.param;

import com.hzgc.cloud.people.model.Imei;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "前端入参封装类")
@Data
public class MentalPatientDTO implements Serializable {
    @ApiModelProperty(value = "手环ID")
    private Long id;
    @ApiModelProperty(value = "人员全局ID")
    @NotNull
    private String peopleId;
    @ApiModelProperty(value = "手环码")
    @NotNull
    private String imei;
    @ApiModelProperty(value = "监护人名称")
    @NotNull
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

    public Imei mentalPatientDTOShift_insert(MentalPatientDTO mentalPatientDTO) {
        Imei imei = new Imei();
        if(mentalPatientDTO.getId() != null){
            imei.setId(mentalPatientDTO.id);
        }
        imei.setImei(mentalPatientDTO.imei);
        imei.setPeopleid(mentalPatientDTO.peopleId);
        imei.setGuardianname(mentalPatientDTO.guardianName);
        imei.setGuardianphone(mentalPatientDTO.guardianPhone);
        imei.setCadresname(mentalPatientDTO.cadresName);
        imei.setCadresphone(mentalPatientDTO.cadresPhone);
        imei.setPolicename(mentalPatientDTO.policeName);
        imei.setPolicephone(mentalPatientDTO.policePhone);
        return imei;
    }
}
