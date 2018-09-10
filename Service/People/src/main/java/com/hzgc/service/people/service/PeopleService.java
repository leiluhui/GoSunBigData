package com.hzgc.service.people.service;

import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureFormat;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.people.model.*;
import com.hzgc.service.people.param.FilterField;
import com.hzgc.service.people.param.PeopleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    public final static String INSERT = "insert";
    public final static String UPDATE = "update";
    public final static String IDCARD_PIC = "idcardpic";
    public final static String CAPTURE_PIC = "capturepic";

    public Long people(People people, String str) {
        if (INSERT.equals(str)) {
            peopleMapper.insertSelective(people);
        }
        if (UPDATE.equals(str)) {
            peopleMapper.updateByPrimaryKeySelective(people);
        }
        return people.getId();
    }

    public Integer people_flag(Long flagId, List<Integer> flags, String str) {
        for (Integer i : flags) {
            Flag flag = new Flag();
            flag.setFlagid(flagId);
            flag.setFlag(i);
            int status = 0;
            if (INSERT.equals(str)) {
                status = flagMapper.insertSelective(flag);
                if (status != 1) {
                    log.info("Insert t_flag failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                status = flagMapper.updateByPrimaryKeySelective(flag);
                if (status != 1) {
                    log.info("Update t_flag failed");
                    return 0;
                }
            }
        }
        return 1;
    }

    public Integer people_picture(Long peopleId, String picType, Long picid, List<byte[]> pics, String str) {
        for (byte[] b : pics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleId);
            if (IDCARD_PIC.equals(picType)) {
                picture.setIdcardpicid(picid);
                picture.setIdcardpic(b);
            }
            if (CAPTURE_PIC.equals(picType)) {
                picture.setCapturepicid(picid);
                picture.setCapturepic(b);
            }
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
            if (INSERT.equals(str)) {
                int status = pictureMapper.insertSelective(picture);
                if (status != 1) {
                    log.info("Insert idCard pic to t_picture failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                int status = pictureMapper.updateByPrimaryKeySelective(picture);
                if (status != 1) {
                    log.info("Update idCard pic to t_picture failed");
                    return 0;
                }
            }
        }
        return 1;
    }

    public Integer people_imsi(Long imsiid, List<String> imsis, String str) {
        for (String s : imsis) {
            Imsi imsi = new Imsi();
            imsi.setImsiid(imsiid);
            imsi.setImsi(s);
            if (INSERT.equals(str)) {
                int status = imsiMapper.insertSelective(imsi);
                if (status != 1) {
                    log.info("Insert imsi to t_imsi failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                int status = imsiMapper.updateByPrimaryKeySelective(imsi);
                if (status != 1) {
                    log.info("Update imsi to t_imsi failed");
                    return 0;
                }
            }

        }
        return 1;
    }

    public Integer people_phone(Long phoneid, List<String> phones, String str) {
        for (String s : phones) {
            Phone phone = new Phone();
            phone.setPhoneid(phoneid);
            phone.setPhone(s);
            if (INSERT.equals(str)) {
                int status = phoneMapper.insertSelective(phone);
                if (status != 1) {
                    log.info("Insert phone to t_phone failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                int status = phoneMapper.updateByPrimaryKeySelective(phone);
                if (status != 1) {
                    log.info("Update phone to t_phone failed");
                    return 0;
                }
            }
        }
        return 1;
    }

    public Integer people_house(Long houseid, List<String> houses, String str) {
        for (String s : houses) {
            House house = new House();
            house.setHouseid(houseid);
            house.setHouse(s);
            if (INSERT.equals(str)) {
                int status = houseMapper.insertSelective(house);
                if (status != 1) {
                    log.info("Insert house to t_house failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                int status = houseMapper.updateByPrimaryKeySelective(house);
                if (status != 1) {
                    log.info("Update house to t_house failed");
                    return 0;
                }
            }

        }
        return 1;
    }

    public Integer people_car(Long carid, List<String> cars, String str) {
        for (String s : cars) {
            Car car = new Car();
            car.setCarid(carid);
            car.setCar(s);
            if (INSERT.equals(str)) {
                int status = carMapper.insertSelective(car);
                if (status != 1) {
                    log.info("Insert car to t_car failed");
                    return 0;
                }
            }
            if (UPDATE.equals(str)) {
                int status = carMapper.updateByPrimaryKeySelective(car);
                if (status != 1) {
                    log.info("Update car to t_car failed");
                    return 0;
                }
            }
        }
        return 1;
    }

    public List<PeopleVO> searchPeople(FilterField field) {
        List<PeopleVO> list = new ArrayList<>();
        List<People> peoples = null;
        if (field.getName() != null || field.getIdcard() != null) {
            peoples = peopleMapper.searchPeople(field);
        }
        if (field.getImsi() != null) {
            List<Imsi> imsis = imsiMapper.selectImsiIdsByImsi(field.getImsi());
            List<Long> imsiIds = new ArrayList<>();
            for (Imsi imsi : imsis) {
                imsiIds.add(imsi.getImsiid());
            }
            field.setImsiIds(imsiIds);
            peoples = peopleMapper.searchPeople(field);
        }
        if (field.getPhone() != null) {
            List<Phone> phones = phoneMapper.selectPhoneIdsByPhone(field.getPhone());
            List<Long> phoneIds = new ArrayList<>();
            for (Phone phone : phones) {
                phoneIds.add(phone.getPhoneid());
            }
            field.setImsiIds(phoneIds);
            peoples = peopleMapper.searchPeople(field);
        }
        if (peoples == null || peoples.size() == 0) {
            return null;
        }
        for (People people : peoples) {
            PeopleVO peopleVO = peopleShift(people);
            list.add(peopleVO);
        }
        return list;
    }
}
