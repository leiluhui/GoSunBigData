package com.hzgc.service.people.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.PictureFormat;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.people.model.*;
import com.hzgc.service.people.param.FilterField;
import com.hzgc.service.people.param.PeopleVO;
import com.hzgc.service.people.param.PictureVO;
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

    /**
     * 添加、修改 t_people 表
     *
     * @param people people对象
     * @param str    添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people(People people, String str) {
        if (INSERT.equals(str)) {
            return peopleMapper.insertSelective(people);
        }
        if (UPDATE.equals(str)) {
            return peopleMapper.updateByPrimaryKeySelective(people);
        }
        return 0;
    }

    /**
     * 添加、修改 t_flag 表
     *
     * @param peopleId 人员全局ID
     * @param flags    人员标签
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_flag(String peopleId, List<Integer> flags, String str) {
        for (Integer i : flags) {
            Flag flag = new Flag();
            flag.setPeopleid(peopleId);
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

    /**
     * 添加、修改 t_picture 表
     *
     * @param peopleId 人员全局ID
     * @param picType  照片类型
     * @param pics     照片数据
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_picture(String peopleId, String picType, List<byte[]> pics, String str) {
        for (byte[] b : pics) {
            PictureWithBLOBs picture = new PictureWithBLOBs();
            picture.setPeopleid(peopleId);
            if (IDCARD_PIC.equals(picType)) {
                picture.setIdcardpic(b);
            }
            if (CAPTURE_PIC.equals(picType)) {
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

    /**
     * 添加、修改 t_imsi 表
     *
     * @param peopleId 人员全局ID
     * @param imsis    imsi信息
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_imsi(String peopleId, List<String> imsis, String str) {
        for (String s : imsis) {
            Imsi imsi = new Imsi();
            imsi.setPeopleid(peopleId);
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

    /**
     * 添加、修改 t_phone 表
     *
     * @param peopleId 人员全局ID
     * @param phones   phone信息
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_phone(String peopleId, List<String> phones, String str) {
        for (String s : phones) {
            Phone phone = new Phone();
            phone.setPeopleid(peopleId);
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

    /**
     * 添加、修改 t_house 表
     *
     * @param peopleId 人员全局ID
     * @param houses   house信息
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_house(String peopleId, List<String> houses, String str) {
        for (String s : houses) {
            House house = new House();
            house.setPeopleid(peopleId);
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

    /**
     * 添加、修改 t_car 表
     *
     * @param peopleId 人员全局ID
     * @param cars     car信息
     * @param str      添加、修改标识
     * @return 1：插入成功, 0：插入失败
     */
    public Integer people_car(String peopleId, List<String> cars, String str) {
        for (String s : cars) {
            Car car = new Car();
            car.setPeopleid(peopleId);
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

    /**
     * 根据照片ID查询照片
     *
     * @param pictureId 照片ID
     * @return byte[] 照片
     */
    public byte[] searchPictureByPicId(Long pictureId) {
        PictureWithBLOBs picture = pictureMapper.selectByPrimaryKey(pictureId);
        if (picture != null){
            byte[] idcardPic = picture.getIdcardpic();
            if (idcardPic != null && idcardPic.length > 0){
                return idcardPic;
            }
            return picture.getCapturepic();
        }
        return null;
    }

    public PictureVO searchPictureByPeopleId(String peopleId) {
        PictureVO pictureVO = new PictureVO();
        List<PictureWithBLOBs> pictures = pictureMapper.selectPictureByPeopleId(peopleId);
        if(pictures != null && pictures.size() > 0){
            List<byte[]> idcardPics = new ArrayList<>();
            List<byte[]> capturePics = new ArrayList<>();
            for (PictureWithBLOBs picture : pictures){
                if (picture != null){
                    byte[] idcardPic = picture.getIdcardpic();
                    if (idcardPic != null && idcardPic.length > 0){
                        idcardPics.add(idcardPic);
                    }else {
                        capturePics.add(picture.getCapturepic());
                    }
                }
            }
            pictureVO.setIdcardPics(idcardPics);
            pictureVO.setCapturePics(capturePics);
        }
        return pictureVO;
    }

    /**
     * 根据id查询人员
     *
     * @param peopleId 人员全局ID
     * @return peopleVO
     */
    public PeopleVO selectByPeopleId(String peopleId) {
        People people = peopleMapper.selectByPrimaryKey(peopleId);
        PeopleVO peopleVO = PeopleVO.peopleShift(people);
        peopleVO.setPictureIds(people.getPicture());
        return peopleVO;
    }

    /**
     * 查询人员对象
     *
     * @param field 查询过滤字段封装
     * @param start 起始行数
     * @param limit 分页行数
     * @return peopleVO 查询返回参数封装
     */
    public List<PeopleVO> searchPeople(FilterField field, int start, int limit) {
        List<PeopleVO> list = new ArrayList<>();
        PageHelper.offsetPage(start, limit);
        List<People> peoples = peopleMapper.searchPeople(field);
        if (peoples != null && peoples.size() > 0) {
            for (People people : peoples) {
                PeopleVO peopleVO = PeopleVO.peopleShift(people);
                List<Long> picIds = new ArrayList<>();
                picIds.add(people.getPicture().get(0));
                peopleVO.setPictureIds(picIds);
                list.add(peopleVO);
            }
        }
        return list;
    }
}
