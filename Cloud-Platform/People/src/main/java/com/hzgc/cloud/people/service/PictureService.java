package com.hzgc.cloud.people.service;

import com.hzgc.cloud.people.dao.PictureMapper;
import com.hzgc.cloud.people.model.PictureWithBLOBs;
import com.hzgc.cloud.people.param.PictureDTO;
import com.hzgc.cloud.people.param.PictureVO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.peoman.SyncPeopleManager;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.FaceAttribute;
import com.hzgc.jniface.FaceUtil;
import com.hzgc.jniface.PictureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class PictureService {
    @Autowired
    @SuppressWarnings("unused")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    @SuppressWarnings("unused")
    private InnerService innerService;

    @Autowired
    @SuppressWarnings("unused")
    private PictureMapper pictureMapper;

    @Value("${people.kafka.topic}")
    @NotNull
    @SuppressWarnings("unused")
    private String topic;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sendKafka(String key, Object data) {
        kafkaTemplate.send(topic, key, JacksonUtil.toJson(data));
    }
    /**
     * 添加人口库照片信息
     *
     * @param dto 添加照片信息
     * @return 成功状态 1：修改成功, 0：修改失败
     */
    public int insertPicture(PictureDTO dto) {
        PictureWithBLOBs picture = new PictureWithBLOBs();
        picture.setPeopleid(dto.getPeopleId());
        byte[] bytes = FaceUtil.base64Str2BitFeature(dto.getPicture());
        if (dto.getType() == 0) {
            picture.setIdcardpic(bytes);
        }
        if (dto.getType() == 1) {
            picture.setCapturepic(bytes);
        }
        PictureData pictureData = innerService.faceFeautreCheck(dto.getPicture());
        if (pictureData == null) {
            log.error("Face feature extract is null");
            return 0;
        }
        FaceAttribute faceAttribute = pictureData.getFeature();
        if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
            log.error("Face feature extract failed");
            return 0;
        }
        picture.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
        picture.setBitfeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
        int status = pictureMapper.insertSelective(picture);
        if (status != 1) {
            log.info("Insert picture to t_picture failed");
            return 0;
        }
        SyncPeopleManager manager = new SyncPeopleManager();
        manager.setType("2");
        manager.setPersonid(dto.getPeopleId());
        this.sendKafka("ADD", manager);
        return 1;
    }

    /**
     * 修改人口库照片信息
     *
     * @param dto 修改照片信息
     * @return 成功状态 1：修改成功, 0：修改失败
     */
    public int updatePicture(PictureDTO dto) {
        PictureWithBLOBs picture = new PictureWithBLOBs();
        picture.setId(dto.getPictureId());
        picture.setPeopleid(dto.getPeopleId());
        byte[] bytes = FaceUtil.base64Str2BitFeature(dto.getPicture());
        if (dto.getType() == 0) {
            picture.setIdcardpic(bytes);
        }
        if (dto.getType() == 1) {
            picture.setCapturepic(bytes);
        }
        PictureData pictureData = innerService.faceFeautreCheck(dto.getPicture());
        if (pictureData == null) {
            log.error("Face feature extract is null");
            return 0;
        }
        FaceAttribute faceAttribute = pictureData.getFeature();
        if (faceAttribute == null || faceAttribute.getFeature() == null || faceAttribute.getBitFeature() == null) {
            log.error("Face feature extract failed");
            return 0;
        }
        picture.setFeature(FaceUtil.floatFeature2Base64Str(faceAttribute.getFeature()));
        picture.setBitfeature(FaceUtil.bitFeautre2Base64Str(faceAttribute.getBitFeature()));
        int status = pictureMapper.updateByPrimaryKeyWithBLOBs(picture);
        if (status != 1) {
            log.info("Update picture to t_picture failed");
            return 0;
        }
        SyncPeopleManager manager = new SyncPeopleManager();
        manager.setType("3");
        manager.setPersonid(dto.getPeopleId());
        this.sendKafka("UPDATE", manager);
        return 1;
    }

    /**
     * 删除人口库照片信息
     *
     * @param dto 删除照片信息
     * @return 成功状态 1：修改成功, 0：修改失败
     */
    public int deletePicture(PictureDTO dto) {
        int status = pictureMapper.deleteByPrimaryKey(dto.getPictureId());
        if (status != 1) {
            log.info("Delete picture to t_picture failed, picture id:" + dto.getPictureId());
            return 0;
        }
        SyncPeopleManager manager = new SyncPeopleManager();
        manager.setType("4");
        manager.setPersonid(dto.getPeopleId());
        this.sendKafka("DELETE", manager);
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
}
