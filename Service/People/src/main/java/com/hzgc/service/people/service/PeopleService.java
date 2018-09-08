package com.hzgc.service.people.service;

import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureFormat;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.people.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Slf4j
public class PeopleService {
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private FlagMapper flagMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private ImsiMapper imsiMapper;

    @Autowired
    private PeopleMapper peopleMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    @Autowired
    private PictureMapper pictureMapper;

    public Long insertPeople(People people) {
        peopleMapper.insertSelective(people);
        return people.getId();
    }

    public Integer insertPeople_flag(Long flagId, List<Integer> flags) {
        for (Integer i : flags) {
            Flag flag = new Flag();
            flag.setFlagid(flagId);
            flag.setFlag(i);
            int status = flagMapper.insertSelective(flag);
            if (status != 1) {
                log.info("Insert t_flag failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_idcardpic(Long peopleid, Long idcardpicid, List<byte[]> idCardPics) {
        for (byte[] b : idCardPics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleid);
            picture.setIdcardpicid(idcardpicid);
            picture.setIdcardpic(b);
            FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(b, PictureFormat.JPG);
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.info("Face feature extract failed, insert idCard pic to t_picture failed");
                return 0;
            }
            picture.setFeature(FaceFunction.floatArray2string(faceAttribute.getFeature()));
            try {
                picture.setBitfeature(new String(faceAttribute.getBitFeature(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int status = pictureMapper.insertSelective(picture);
            if (status != 1) {
                log.info("Insert idCard pic to t_picture failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_capturepic(Long peopleid, Long capturepicid, List<byte[]> capturePics) {
        for (byte[] b : capturePics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleid);
            picture.setCapturepicid(capturepicid);
            picture.setCapturepic(b);
            FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(b, PictureFormat.JPG);
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.info("Face feature extract failed, insert idCard pic to t_picture failed");
                return 0;
            }
            picture.setFeature(FaceFunction.floatArray2string(faceAttribute.getFeature()));
            try {
                picture.setBitfeature(new String(faceAttribute.getBitFeature(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int status = pictureMapper.insertSelective(picture);
            if (status != 1) {
                log.info("Insert capture pic to t_picture failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_imsi(Long imsiid, List<String> imsis) {
        for (String s : imsis) {
            Imsi imsi = new Imsi();
            imsi.setImsiid(imsiid);
            imsi.setImsi(s);
            int status = imsiMapper.insertSelective(imsi);
            if (status != 1) {
                log.info("Insert imsi to t_imsi failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_phone(Long phoneid, List<String> phones) {
        for (String s : phones) {
            Phone phone = new Phone();
            phone.setPhoneid(phoneid);
            phone.setPhone(s);
            int status = phoneMapper.insertSelective(phone);
            if (status != 1) {
                log.info("Insert phone to t_phone failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_house(Long houseid, List<String> houses) {
        for (String s : houses) {
            House house = new House();
            house.setHouseid(houseid);
            house.setHouse(s);
            int status = houseMapper.insertSelective(house);
            if (status != 1) {
                log.info("Insert house to t_house failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer insertPeople_car(Long carid, List<String> cars) {
        for (String s : cars) {
            Car car = new Car();
            car.setCarid(carid);
            car.setCar(s);
            int status = carMapper.insertSelective(car);
            if (status != 1) {
                log.info("Insert car to t_car failed");
                return 0;
            }
        }
        return 1;
    }

}
