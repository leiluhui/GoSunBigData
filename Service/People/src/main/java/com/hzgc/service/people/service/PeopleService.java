package com.hzgc.service.people.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import com.hzgc.jniface.PictureFormat;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.people.fields.Flag;
import com.hzgc.service.people.model.*;
import com.hzgc.service.people.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PeopleService {
    @Autowired
    @SuppressWarnings("unused")
    private PeopleMapper peopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private CarMapper carMapper;

    @Autowired
    @SuppressWarnings("unused")
    private FlagMapper flagMapper;

    @Autowired
    @SuppressWarnings("unused")
    private HouseMapper houseMapper;

    @Autowired
    @SuppressWarnings("unused")
    private ImsiMapper imsiMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PhoneMapper phoneMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PictureMapper pictureMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleService peopleService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final static String IDCARD_PIC = "idcardpic";

    public final static String CAPTURE_PIC = "capturepic";

    public ReturnMessage insertPeople(PeopleDTO peopleDTO) {
        People people = peopleDTO.peopleDTOShift_insert(peopleDTO);
        log.info("Start Insert people info, param is:" + JacksonUtil.toJson(people));
        Integer status = peopleService.people_insert(people);
        if (status == null || status != 1) {
            log.info("Insert people to t_people info failed");
            ReturnMessage message = new ReturnMessage();
            message.setStatus(0);
            message.setMessage("添加人口信息失败");
            return message;
        }
        log.info("Insert t_people info successfully");
        if (peopleDTO.getFlagId() != null || peopleDTO.getIdCardPic() != null || peopleDTO.getCapturePic() != null ||
                peopleDTO.getImsi() != null || peopleDTO.getPhone() != null || peopleDTO.getHouse() != null ||
                peopleDTO.getCar() != null) {
            if (peopleDTO.getFlagId() != null && peopleDTO.getFlagId().size() > 0) {
                Integer insertStatus = peopleService.people_flag_insert(people.getId(), peopleDTO.getFlagId());
                if (insertStatus == 1) {
                    log.info("Insert flag to t_flag successfully");
                } else {
                    log.info("Insert flag to t_flag failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口标签表失败");
                    return message;
                }
            }
            if (peopleDTO.getIdCardPic() != null && peopleDTO.getIdCardPic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_insert(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic());
                if (insertStatus == 1) {
                    log.info("Insert idCard pic to t_picture successfully");
                } else {
                    log.info("Insert idCard pic to t_picture failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口证件照片表失败");
                    return message;
                }
            }
            if (peopleDTO.getCapturePic() != null && peopleDTO.getCapturePic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_insert(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic());
                if (insertStatus == 1) {
                    log.info("Insert capture pic to t_picture successfully");
                } else {
                    log.info("Insert capture pic to t_picture failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口实采照片表失败");
                    return message;
                }
            }
            if (peopleDTO.getImsi() != null && peopleDTO.getImsi().size() > 0) {
                Integer insertStatus = peopleService.people_imsi_insert(people.getId(), peopleDTO.getImsi());
                if (insertStatus == 1) {
                    log.info("Insert imsi to t_imsi successfully");
                } else {
                    log.info("Insert imsi to t_imsi failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口imsi表失败");
                    return message;
                }
            }
            if (peopleDTO.getPhone() != null && peopleDTO.getPhone().size() > 0) {
                Integer insertStatus = peopleService.people_phone_insert(people.getId(), peopleDTO.getPhone());
                if (insertStatus == 1) {
                    log.info("Insert phone to t_phone successfully");
                } else {
                    log.info("Insert phone to t_phone failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口联系方式表失败");
                    return message;
                }
            }
            if (peopleDTO.getHouse() != null && peopleDTO.getHouse().size() > 0) {
                Integer insertStatus = peopleService.people_house_insert(people.getId(), peopleDTO.getHouse());
                if (insertStatus == 1) {
                    log.info("Insert house to t_house successfully");
                } else {
                    log.info("Insert house to t_house failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口房产信息表失败");
                    return message;
                }
            }
            if (peopleDTO.getCar() != null && peopleDTO.getCar().size() > 0) {
                Integer insertStatus = peopleService.people_car_insert(people.getId(), peopleDTO.getCar());
                if (insertStatus == 1) {
                    log.info("Insert car to t_car successfully");
                } else {
                    log.info("Insert car to t_car failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("添加人口车辆信息表失败");
                    return message;
                }
            }
        }
        log.info("Insert people info successfully");
        ReturnMessage message = new ReturnMessage();
        message.setStatus(1);
        message.setMessage("添加成功");
        return message;
    }

    public ReturnMessage updatePeople(PeopleDTO peopleDTO) {
        People people = peopleDTO.peopleDTOShift_update(peopleDTO);
        log.info("Start update object info, param is:" + JacksonUtil.toJson(people));
        Integer status = peopleService.people_update(people);
        if (status == null || status != 1) {
            log.info("Update t_people info failed");
            ReturnMessage message = new ReturnMessage();
            message.setStatus(0);
            message.setMessage("修改人口失败");
            return message;
        }
        log.info("Update t_people info successfully");
        if (peopleDTO.getFlagId() != null || peopleDTO.getIdCardPic() != null || peopleDTO.getCapturePic() != null ||
                peopleDTO.getImsi() != null || peopleDTO.getPhone() != null || peopleDTO.getHouse() != null ||
                peopleDTO.getCar() != null) {
            if (peopleDTO.getFlagId() != null && peopleDTO.getFlagId().size() > 0) {
                Integer insertStatus = peopleService.people_flag_update(people.getId(), peopleDTO.getFlagId());
                if (insertStatus == 1) {
                    log.info("Update flag to t_flag successfully");
                } else {
                    log.info("Update flag to t_flag failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口标签表失败");
                    return message;
                }
            }
            if (peopleDTO.getIdCardPic() != null && peopleDTO.getIdCardPic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_update(people.getId(), PeopleService.IDCARD_PIC,
                        peopleDTO.getIdCardPic());
                if (insertStatus == 1) {
                    log.info("Update idCard pic to t_picture successfully");
                } else {
                    log.info("Update idCard pic to t_picture failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口证件照片表失败");
                    return message;
                }
            }
            if (peopleDTO.getCapturePic() != null && peopleDTO.getCapturePic().size() > 0) {
                Integer insertStatus = peopleService.people_picture_update(people.getId(), PeopleService.CAPTURE_PIC,
                        peopleDTO.getCapturePic());
                if (insertStatus == 1) {
                    log.info("Update capture pic to t_picture successfully");
                } else {
                    log.info("Update capture pic to t_picture failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口实采照片表失败");
                    return message;
                }
            }
            if (peopleDTO.getImsi() != null && peopleDTO.getImsi().size() > 0) {
                Integer insertStatus = peopleService.people_imsi_update(people.getId(), peopleDTO.getImsi());
                if (insertStatus == 1) {
                    log.info("Update imsi to t_imsi successfully");
                } else {
                    log.info("Update imsi to t_imsi failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口imsi表失败");
                    return message;
                }
            }
            if (peopleDTO.getPhone() != null && peopleDTO.getPhone().size() > 0) {
                Integer insertStatus = peopleService.people_phone_update(people.getId(), peopleDTO.getPhone());
                if (insertStatus == 1) {
                    log.info("Update phone to t_phone successfully");
                } else {
                    log.info("Update phone to t_phone failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口联系方式表失败");
                    return message;
                }
            }
            if (peopleDTO.getHouse() != null && peopleDTO.getHouse().size() > 0) {
                Integer insertStatus = peopleService.people_house_update(people.getId(), peopleDTO.getHouse());
                if (insertStatus == 1) {
                    log.info("Update house to t_house successfully");
                } else {
                    log.info("Update house to t_house failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口房产信息表失败");
                    return message;
                }
            }
            if (peopleDTO.getCar() != null) {
                Integer insertStatus = peopleService.people_car_update(people.getId(), peopleDTO.getCar());
                if (insertStatus == 1) {
                    log.info("Update car to t_car successfully");
                } else {
                    log.info("Update car to t_car failed");
                    ReturnMessage message = new ReturnMessage();
                    message.setStatus(0);
                    message.setMessage("修改人口车辆信息表失败");
                    return message;
                }
            }
        }
        log.info("Update people info successfully");
        ReturnMessage message = new ReturnMessage();
        message.setStatus(1);
        message.setMessage("修改成功");
        return message;
    }

    public Integer people_insert(People people) {
        return peopleMapper.insertSelective(people);
    }

    public Integer people_update(People people) {
        return peopleMapper.updateByPrimaryKeySelective(people);
    }

    public Integer people_flag_insert(String peopleId, List<Integer> flags) {
        for (Integer integer : flags) {
            com.hzgc.service.people.model.Flag flag = new com.hzgc.service.people.model.Flag();
            flag.setPeopleid(peopleId);
            flag.setFlagid(integer);
            flag.setFlag(Flag.getFlag(integer));
            int status = flagMapper.insertSelective(flag);
            if (status != 1) {
                log.info("Insert people, but insert t_flag failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_flag_update(String peopleId, List<Integer> flags) {
        List<Long> idList = flagMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = flagMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_flag failed, id: " + id);
                return 0;
            }
        }
        for (Integer integer : flags) {
            com.hzgc.service.people.model.Flag flag = new com.hzgc.service.people.model.Flag();
            flag.setPeopleid(peopleId);
            flag.setFlagid(integer);
            flag.setFlag(Flag.getFlag(integer));
            int insertStatus = flagMapper.insertSelective(flag);
            if (insertStatus != 1) {
                log.info("Update people, but insert flag to t_flag failed, flag:" + integer);
                return 0;
            }
        }
        return 1;
    }

    public Integer people_picture_insert(String peopleId, String picType, List<String> pics) {
        for (String photo : pics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleId);
            byte[] bytes = FaceUtil.base64Str2BitFeature(photo);
            if (IDCARD_PIC.equals(picType)) {
                picture.setIdcardpic(bytes);
            }
            if (CAPTURE_PIC.equals(picType)) {
                picture.setCapturepic(bytes);
            }
            FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(bytes, PictureFormat.JPG);
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.info("Face feature extract failed, insert picture to t_picture failed");
                return 0;
            }
            picture.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
            picture.setBitfeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
            int insertStatus = pictureMapper.insertSelective(picture);
            if (insertStatus != 1) {
                log.info("Insert people, but insert picture to t_picture failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_picture_update(String peopleId, String picType, List<String> pics) {
        List<Long> idList = pictureMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = pictureMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_picture failed, id: " + id);
                return 0;
            }
        }
        for (String photo : pics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleId);
            byte[] bytes = FaceUtil.base64Str2BitFeature(photo);
            if (IDCARD_PIC.equals(picType)) {
                picture.setIdcardpic(bytes);
            }
            if (CAPTURE_PIC.equals(picType)) {
                picture.setCapturepic(bytes);
            }
            FaceAttribute faceAttribute = FaceFunction.faceFeatureExtract(bytes, PictureFormat.JPG);
            if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
                log.info("Face feature extract failed, insert picture to t_picture failed");
                return 0;
            }
            picture.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
            picture.setBitfeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
            int insertStatus = pictureMapper.insertSelective(picture);
            if (insertStatus != 1) {
                log.info("Update people, but insert picture to t_picture failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_imsi_insert(String peopleId, List<String> imsis) {
        for (String s : imsis) {
            Imsi imsi = new Imsi();
            imsi.setPeopleid(peopleId);
            imsi.setImsi(s);
            int status = imsiMapper.insertSelective(imsi);
            if (status != 1) {
                log.info("Insert people, but insert imsi to t_imsi failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_imsi_update(String peopleId, List<String> imsis) {
        List<Long> idList = imsiMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = imsiMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_imsi failed, id: " + id);
                return 0;
            }
        }
        for (String s : imsis) {
            Imsi imsi = new Imsi();
            imsi.setPeopleid(peopleId);
            imsi.setImsi(s);
            int insertStatus = imsiMapper.insertSelective(imsi);
            if (insertStatus != 1) {
                log.info("Update people, but insert imsi to t_imsi failed, imsi: " + s);
                return 0;
            }
        }
        return 1;
    }

    public Integer people_phone_insert(String peopleId, List<String> phones) {
        for (String s : phones) {
            Phone phone = new Phone();
            phone.setPeopleid(peopleId);
            phone.setPhone(s);
            int status = phoneMapper.insertSelective(phone);
            if (status != 1) {
                log.info("Insert people, but insert phone to t_phone failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_phone_update(String peopleId, List<String> phones) {
        List<Long> idList = phoneMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = phoneMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_phone failed, id: " + id);
                return 0;
            }
        }
        for (String s : phones) {
            Phone phone = new Phone();
            phone.setPeopleid(peopleId);
            phone.setPhone(s);
            int insertStatus = phoneMapper.insertSelective(phone);
            if (insertStatus != 1) {
                log.info("Update people, but insert phone to t_phone failed, phone: " + s);
                return 0;
            }
        }
        return 1;
    }

    public Integer people_house_insert(String peopleId, List<String> houses) {
        for (String s : houses) {
            House house = new House();
            house.setPeopleid(peopleId);
            house.setHouse(s);
            int status = houseMapper.insertSelective(house);
            if (status != 1) {
                log.info("Insert people, but insert house to t_house failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_house_update(String peopleId, List<String> houses) {
        List<Long> idList = houseMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = houseMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_house failed, id: " + id);
                return 0;
            }
        }
        for (String s : houses) {
            House house = new House();
            house.setPeopleid(peopleId);
            house.setHouse(s);
            int insertStatus = houseMapper.insertSelective(house);
            if (insertStatus != 1) {
                log.info("Update people, but insert house to t_house failed, house: " + s);
                return 0;
            }
        }
        return 1;
    }

    public Integer people_car_insert(String peopleId, List<String> cars) {
        for (String s : cars) {
            Car car = new Car();
            car.setPeopleid(peopleId);
            car.setCar(s);
            int status = carMapper.insertSelective(car);
            if (status != 1) {
                log.info("Insert people, but insert car to t_car failed");
                return 0;
            }
        }
        return 1;
    }

    public Integer people_car_update(String peopleId, List<String> cars) {
        List<Long> idList = carMapper.selectIdByPeopleId(peopleId);
        for (Long id : idList) {
            int status = carMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                log.info("Update people, but delete t_car failed, id: " + id);
                return 0;
            }
        }
        for (String s : cars) {
            Car car = new Car();
            car.setPeopleid(peopleId);
            car.setCar(s);
            int insertStatus = carMapper.insertSelective(car);
            if (insertStatus != 1) {
                log.info("Update people, but insert car to t_car failed, car: " + s);
                return 0;
            }
        }
        return 1;
    }

    /**
     * 根据照片ID查询照片
     *
     * @param pictureId 照片ID
     * @return byte[] 照片
     */
    public byte[] searchPictureByPicId(Long pictureId) {
        PictureWithBLOBs picture = pictureMapper.selectPictureById(pictureId);
        if (picture != null) {
            if (picture.getIdcardpic() != null) {
                return picture.getIdcardpic();
            } else {
                return picture.getCapturepic();
            }
        }
        return null;
    }

    public PictureVO searchPictureByPeopleId(String peopleId) {
        PictureVO pictureVO = new PictureVO();
        List<PictureWithBLOBs> pictures = pictureMapper.selectPictureByPeopleId(peopleId);
        if (pictures != null && pictures.size() > 0) {
            List<Long> pictureIds = new ArrayList<>();
            List<Long> idcardPictureIds = new ArrayList<>();
            List<Long> capturePictureIds = new ArrayList<>();
            for (PictureWithBLOBs picture : pictures) {
                if (picture != null) {
                    pictureIds.add(picture.getId());
                    byte[] idcardPic = picture.getIdcardpic();
                    if (idcardPic != null && idcardPic.length > 0) {
                        idcardPictureIds.add(picture.getId());
                    } else {
                        capturePictureIds.add(picture.getId());
                    }
                }
            }
            pictureVO.setPictureIds(pictureIds);
            pictureVO.setIdcardPics(idcardPictureIds);
            pictureVO.setCapturePics(capturePictureIds);
        }
        return pictureVO;
    }

    /**
     * 根据ID查询人口信息
     *
     * @param peopleId 人员全局ID
     * @return peopleVO
     */
    public PeopleVO selectByPeopleId(String peopleId) {
        People people = peopleMapper.selectByPrimaryKey(peopleId);
        PeopleVO peopleVO = new PeopleVO();
        if (people != null) {
            peopleVO.setId(people.getId());
            peopleVO.setName(people.getName());
            peopleVO.setIdCard(people.getIdcard());
            peopleVO.setRegion(platformService.getMergerName(people.getRegion()));
            peopleVO.setHousehold(people.getHousehold());
            peopleVO.setAddress(people.getAddress());
            peopleVO.setSex(people.getSex());
            peopleVO.setAge(people.getAge());
            peopleVO.setBirthday(people.getBirthday());
            peopleVO.setPolitic(people.getPolitic());
            peopleVO.setEduLevel(people.getEdulevel());
            peopleVO.setJob(people.getJob());
            peopleVO.setBirthplace(people.getBirthplace());
            peopleVO.setCommunity(platformService.getMergerName(people.getCommunity()));
            if (people.getLasttime() != null) {
                peopleVO.setLastTime(sdf.format(people.getLasttime()));
            }
            if (people.getCreatetime() != null) {
                peopleVO.setCreateTime(sdf.format(people.getCreatetime()));
            }
            if (people.getUpdatetime() != null) {
                peopleVO.setUpdateTime(sdf.format(people.getUpdatetime()));
            }
            List<com.hzgc.service.people.model.Flag> flags = people.getFlag();
            List<Integer> flagIdList = new ArrayList<>();
            for (com.hzgc.service.people.model.Flag flag : flags) {
                flagIdList.add(flag.getFlagid());
            }
            peopleVO.setFlag(flagIdList);
            List<Imsi> imsis = people.getImsi();
            List<String> imsiList = new ArrayList<>();
            for (Imsi imsi : imsis) {
                imsiList.add(imsi.getImsi());
            }
            peopleVO.setImsi(imsiList);
            List<Phone> phones = people.getPhone();
            List<String> phoneList = new ArrayList<>();
            for (Phone phone : phones) {
                phoneList.add(phone.getPhone());
            }
            peopleVO.setPhone(phoneList);
            List<House> houses = people.getHouse();
            List<String> houseList = new ArrayList<>();
            for (House house : houses) {
                houseList.add(house.getHouse());
            }
            peopleVO.setHouse(houseList);
            List<Car> cars = people.getCar();
            List<String> carList = new ArrayList<>();
            for (Car car : cars) {
                carList.add(car.getCar());
            }
            peopleVO.setCar(carList);
            List<PictureWithBLOBs> pictures = people.getPicture();
            if (pictures != null && pictures.size() > 0) {
                peopleVO.setPictureId(pictures.get(0).getId());
                List<Long> idcardPictureIds = new ArrayList<>();
                List<Long> capturePictureIds = new ArrayList<>();
                for (PictureWithBLOBs picture : pictures) {
                    byte[] idcardPic = picture.getIdcardpic();
                    if (idcardPic != null && idcardPic.length > 0) {
                        idcardPictureIds.add(picture.getId());
                    } else {
                        capturePictureIds.add(picture.getId());
                    }
                }
                peopleVO.setIdcardPictureIds(idcardPictureIds);
                peopleVO.setCapturePictureIds(capturePictureIds);
            }
        }
        return peopleVO;
    }

    /**
     * 查询人员对象
     *
     * @param field 查询过滤字段封装
     * @return SearchPeopleVO 查询返回参数封装
     */
    public SearchPeopleVO searchPeople(FilterField field) {
        SearchPeopleVO vo = new SearchPeopleVO();
        List<PeopleVO> list = new ArrayList<>();
        Page page = PageHelper.offsetPage(field.getStart(), field.getLimit(), true);
        List<People> peoples = peopleMapper.searchPeople(field);
        PageInfo info = new PageInfo(page.getResult());
        int total = (int) info.getTotal();
        vo.setTotal(total);
        if (peoples != null && peoples.size() > 0) {
            for (People people : peoples) {
                PeopleVO peopleVO = new PeopleVO();
                if (people != null) {
                    peopleVO.setId(people.getId());
                    peopleVO.setName(people.getName());
                    peopleVO.setIdCard(people.getIdcard());
                    peopleVO.setRegion(platformService.getMergerName(people.getRegion()));
                    peopleVO.setHousehold(people.getHousehold());
                    peopleVO.setAddress(people.getAddress());
                    peopleVO.setSex(people.getSex());
                    peopleVO.setAge(people.getAge());
                    peopleVO.setBirthday(people.getBirthday());
                    peopleVO.setPolitic(people.getPolitic());
                    peopleVO.setEduLevel(people.getEdulevel());
                    peopleVO.setJob(people.getJob());
                    peopleVO.setBirthplace(people.getBirthplace());
                    //peopleVO.setCommunity(String.valueOf(people.getCommunity()));
                    if (people.getLasttime() != null) {
                        peopleVO.setLastTime(sdf.format(people.getLasttime()));
                    }
                    if (people.getCreatetime() != null) {
                        peopleVO.setCreateTime(sdf.format(people.getCreatetime()));
                    }
                    if (people.getUpdatetime() != null) {
                        peopleVO.setUpdateTime(sdf.format(people.getUpdatetime()));
                    }
                    List<com.hzgc.service.people.model.Flag> flags = people.getFlag();
                    List<Integer> flagIdList = new ArrayList<>();
                    for (com.hzgc.service.people.model.Flag flag : flags) {
                        flagIdList.add(flag.getFlagid());
                    }
                    peopleVO.setFlag(flagIdList);
                    if (people.getPicture() != null && people.getPicture().size() > 0) {
                        PictureWithBLOBs picture = people.getPicture().get(0);
                        peopleVO.setPictureId(picture.getId());
                    }
                    list.add(peopleVO);
                }
            }
        }
        vo.setPeopleVOList(list);
        return vo;
    }

    public List<Long> searchCommunityIdsByRegionId(Long regionId) {
        return peopleMapper.searchCommunityIdsByRegionId(regionId);
    }
}
