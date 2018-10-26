package com.hzgc.collect.service.extract;

import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.jniface.BigPersonPictureData;
import com.hzgc.jniface.PersonAttributes;
import com.hzgc.jniface.PersonPictureData;
import com.hzgc.seemmo.bean.ImageResult;
import com.hzgc.seemmo.bean.personbean.Person;
import com.hzgc.seemmo.service.ImageToData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonExtractService {
    @Value("${seemmo.url}")
    private String seemmoUrl;

    /**
     * 特征提取
     *
     * @param imageBytes 图片数组（大图）
     * @return PersonPictureData
     */
    public BigPersonPictureData featureExtractByImage(byte[] imageBytes) {
        ImageResult imageResult = ImageToData.getImageResult(seemmoUrl, imageBytes, "1");
        if (null == imageResult) {
            log.info("imageResult is null");
            return null;
        }
        BigPersonPictureData bigPersonPictureData = new BigPersonPictureData();
        List <Person> list = imageResult.getPersonList();
        ArrayList <PersonPictureData> smallImages = new ArrayList <>();
        if (list != null && list.size() > 0) {
            for (Person person : list) {
                if (person != null) {
                    PersonPictureData personPictureData = new PersonPictureData();
                    personPictureData.setImageID(UuidUtil.getUuid());
                    personPictureData.setImageData(person.getCar_data());
                    PersonAttributes personAttribute = new PersonAttributes();
                    personAttribute.setAge(person.getAge_code());
                    personAttribute.setBaby(person.getBaby_code());
                    personAttribute.setBag(person.getBag_code());
                    personAttribute.setBottomcolor(person.getBottomcolor_code());
                    personAttribute.setBottomtype(person.getBottomtype_code());
                    personAttribute.setCartype(person.getCar_type());
                    personAttribute.setHat(person.getHat_code());
                    personAttribute.setKnapsack(person.getKnapsack_code());
                    personAttribute.setMessengerbag(person.getMessengerbag_code());
                    personAttribute.setOrientation(person.getOrientation_code());
                    personAttribute.setSex(person.getSex_code());
                    personAttribute.setShoulderbag(person.getShoulderbag_code());
                    personAttribute.setUmbrella(person.getUmbrella_code());
                    personAttribute.setUppercolor(person.getUppercolor_code());
                    personAttribute.setUppertype(person.getUppertype_code());
                    personAttribute.setHair(person.getHair_code());
                    personAttribute.setPerson_coordinate(person.getPerson_image());
                    personPictureData.setFeature(personAttribute);
                    int[] person_image = person.getPerson_image();
                    person_image[2] = person_image[0] + person_image[2];
                    person_image[3] = person_image[1] + person_image[3];
                    personPictureData.setImage_coordinate(person.getPerson_image());
                    smallImages.add(personPictureData);
                }
            }
        }
        bigPersonPictureData.setImageType("person");
        bigPersonPictureData.setImageID(UuidUtil.getUuid());
        bigPersonPictureData.setSmallImages(smallImages);
        bigPersonPictureData.setImageData(imageBytes);
        bigPersonPictureData.setTotal(smallImages.size());
        return bigPersonPictureData;
    }
}
