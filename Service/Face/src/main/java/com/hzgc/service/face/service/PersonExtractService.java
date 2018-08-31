package com.hzgc.service.face.service;

import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.jni.*;
import com.hzgc.seemmo.bean.ImageResult;
import com.hzgc.seemmo.bean.personbean.Person;
import com.hzgc.seemmo.service.ImageToData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PersonExtractService {

    @Autowired
    private Environment environment;


    public PersonPictureData featureExtractByImage(byte[] imageBin)  {
        PersonPictureData personPictureData = new PersonPictureData();
        personPictureData.setImageID(UuidUtil.getUuid());
        personPictureData.setImageData(imageBin);
        ImageResult imageResult = ImageToData.getImageResult(environment.getProperty("person.url"),imageBin,environment.getProperty("person.tag"));
        List<Person> list = imageResult.getPersonList();
        log.info(""+list.size());

        List<PersonAttribute> personAttributes = new ArrayList<>();
        for (Person person: list){
            PersonAttribute personAttribute =new PersonAttribute();
            personAttribute.setAge(person.getAge_code());
            personAttribute.setBaby(person.getBaby_code());
            personAttribute.setBag(person.getBag_code());
            personAttribute.setBottomColor(person.getBottomcolor_code());
            personAttribute.setBottomType(person.getBottomtype_code());
            personAttribute.setCarType(person.getCar_type());
            personAttribute.setHat(person.getHat_code());
            personAttribute.setKnapSack(person.getKnapsack_code());
            personAttribute.setMessengerBag(person.getMessengerbag_code());
            personAttribute.setOrientation(person.getOrientation_code());
            personAttribute.setSex(person.getSex_code());
            personAttribute.setShoulderBag(person.getShoulderbag_code());
            personAttribute.setUmbrella(person.getUmbrella_code());
            personAttribute.setUpperColor(person.getUppercolor_code());
            personAttribute.setUpperType(person.getUppertype_code());
            personAttribute.setHair(person.getHair_code());

            personAttributes.add(personAttribute);
        }
        personPictureData.setPersonAttribute(personAttributes);
        return personPictureData;
    }

    public static void main(String[] args) {
        ImageResult imageResult = ImageToData.getImageResult("http://172.18.18.139:8000/?cmd=recogPic", "C:\\Users\\ZBL\\Desktop\\100.jpg", "1");
        System.out.println(imageResult.getPersonList().size());
    }
}
