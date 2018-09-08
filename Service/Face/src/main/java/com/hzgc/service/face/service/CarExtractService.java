package com.hzgc.service.face.service;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jni.CarPictureData;
import com.hzgc.seemmo.bean.ImageResult;
import com.hzgc.seemmo.bean.carbean.Vehicle;
import com.hzgc.seemmo.service.ImageToData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CarExtractService {

    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    /**
     * 特征提取
     *
     * @param imageBytes 图片数组（大图）
     * @return CarPictureData
     */
    public CarPictureData carExtractByImage(byte[] imageBytes) {
        ImageResult imageResult = ImageToData.getImageResult(environment.getProperty("seemmo.url"), imageBytes, environment.getProperty("car.tag"));
        if (imageResult == null){
            log.error("imageResult is null !");
            return null;
        }
        CarPictureData carPictureData = new CarPictureData();
        carPictureData.setImageID(UuidUtil.getUuid());
        carPictureData.setImageData(imageBytes);
        List<CarAttribute> attributeList = new ArrayList<>();
        List<Vehicle> vehicleList = imageResult.getVehicleList();
        if (vehicleList != null && vehicleList.size() > 0){
            for (Vehicle vehicle : vehicleList) {
                if (vehicle != null){
                    CarAttribute carAttribute = new CarAttribute();
                    carAttribute.setVehicle_object_type(vehicle.getVehicle_object_type());
                    carAttribute.setBelt_maindriver(vehicle.getBelt_maindriver());
                    carAttribute.setBelt_codriver(vehicle.getBelt_codriver());
                    carAttribute.setBrand_name(vehicle.getBrand_name());
                    carAttribute.setCall_code(vehicle.getCall_code());
                    carAttribute.setVehicle_color(vehicle.getVehicle_color());
                    carAttribute.setCrash_code(vehicle.getCrash_code());
                    carAttribute.setDanger_code(vehicle.getDanger_code());
                    carAttribute.setMarker_code(vehicle.getMarker_code());
                    carAttribute.setPlate_schelter_code(vehicle.getPlate_schelter_code());
                    carAttribute.setPlate_flag_code(vehicle.getPlate_flag_code());
                    carAttribute.setPlate_licence(vehicle.getPlate_licence());
                    carAttribute.setPlate_destain_code(vehicle.getPlate_destain_code());
                    carAttribute.setPlate_color_code(vehicle.getPlate_color_code());
                    carAttribute.setPlate_type_code(vehicle.getPlate_type_code());
                    carAttribute.setRack_code(vehicle.getRack_code());
                    carAttribute.setSparetire_code(vehicle.getSparetire_code());
                    carAttribute.setMistake_code(vehicle.getMistake_code());
                    carAttribute.setSunroof_code(vehicle.getSunroof_code());
                    carAttribute.setVehicle_type(vehicle.getVehicle_type());
                    attributeList.add(carAttribute);
                }
            }
        }
        carPictureData.setAttributeList(attributeList);
        return carPictureData;
    }
}
