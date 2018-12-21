package com.hzgc.cloud.community.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzgc.cloud.community.dao.RecognizeRecordMapper;
import com.hzgc.cloud.community.param.*;
import com.hzgc.cloud.people.dao.PictureMapper;
import com.hzgc.cloud.people.model.Car;
import com.hzgc.cloud.people.model.Flag;
import com.hzgc.cloud.people.model.Imsi;
import com.hzgc.cloud.people.model.Phone;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.basic.ImsiUtil;
import com.hzgc.common.util.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PeopleRecognizeService {
    @Autowired
    @SuppressWarnings("unused")
    private RecognizeRecordMapper recognizeRecordMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PictureMapper pictureMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private InnerService innerService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ImportantRecognizeVO importantPeopleRecognize(ImportantRecognizeDTO param) {
        ImportantRecognizeVO vo = new ImportantRecognizeVO();
        List<Long> communityIds = platformService.getCommunityIdsById(param.getRegionId());
        if (communityIds == null || communityIds.size() == 0) {
            log.info("Search community ids by region id is null, so return null");
            return null;
        }
        log.info("Search community ids by region id is:" + JacksonUtil.toJson(communityIds));
        ImportantRecognizeSearchParam search = new ImportantRecognizeSearchParam();
        search.setSearchType(param.getSearchType());
        search.setCommunityIds(communityIds);
        try {
            Timestamp startTime = new Timestamp(sdf.parse(param.getStartTime()).getTime());
            Timestamp endTime = new Timestamp(sdf.parse(param.getEndTime()).getTime());
            search.setStartTime(startTime);
            search.setEndTime(endTime);
        } catch (ParseException e) {
            log.error("Date parse error, because param time error, so return null");
            return null;
        }
        log.info("Search community important people recognize param:" + JacksonUtil.toJson(search));
        Page page = PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<ImportantPeopleRecognize> recognizes = recognizeRecordMapper.getImportantRecognizeRecord(search);
        PageInfo info = new PageInfo(page.getResult());
        if (recognizes == null || recognizes.size() == 0) {
            log.info("Search community important people recognize is null, so return null");
            return null;
        }
        List<ImportantPeopleRecognizeVO> voList = new ArrayList<>();
        for (ImportantPeopleRecognize recognize : recognizes) {
            ImportantPeopleRecognizeVO importantPeopleRecognize = new ImportantPeopleRecognizeVO();
            importantPeopleRecognize.setId(recognize.getPeopleId());
            importantPeopleRecognize.setType(recognize.getType());
            importantPeopleRecognize.setName(recognize.getName());
            importantPeopleRecognize.setIdCard(recognize.getIdCard());
            importantPeopleRecognize.setLastTime(recognize.getLastTime() != null ? sdf.format(recognize.getLastTime()) : "");
            //当搜索类型不是0的时候为识别类型,识别类型数据库pictureid为空,所以需要手动查询一张图片出来
            if (param.getSearchType() == 0) {
                importantPeopleRecognize.setPeoplePictureId(recognize.getPictureId());
                importantPeopleRecognize.setPictureId(recognize.getPictureId());
            } else {
                Long picId = pictureMapper.getPictureIdByPeopleId(recognize.getPeopleId());
                importantPeopleRecognize.setPeoplePictureId(picId);
                importantPeopleRecognize.setPictureId(picId);
            }
            if (recognize.getType() == 1 || recognize.getType() == 3) {
                importantPeopleRecognize.setDeviceId(recognize.getDeviceId());
                importantPeopleRecognize.setDeviceName(platformService.getCameraDeviceName(recognize.getDeviceId()));
            } else if (recognize.getType() == 2) {
                importantPeopleRecognize.setDeviceId(platformService.getImsiDeviceId(recognize.getDeviceId()));
                importantPeopleRecognize.setDeviceName(platformService.getImsiDeviceName(recognize.getDeviceId()));
            }
            importantPeopleRecognize.setCaptureTime(sdf.format(recognize.getCaptureTime()));
            List<Car> cars = recognize.getCar();
            List<String> carList = new ArrayList<>();
            for (Car car : cars) {
                carList.add(car.getCar());
            }
            importantPeopleRecognize.setCar(carList);
            List<Imsi> imsis = recognize.getImac();
            List<String> imsiList = new ArrayList<>();
            for (Imsi imsi : imsis) {
                imsiList.add(ImsiUtil.toMac(imsi.getImsi()));
            }
            importantPeopleRecognize.setImac(imsiList);
            List<Flag> flags = recognize.getFlag();
            List<com.hzgc.cloud.people.param.Flag> flagList = new ArrayList<>();
            for (Flag flag : flags) {
                com.hzgc.cloud.people.param.Flag ff = new com.hzgc.cloud.people.param.Flag();
                ff.setId(flag.getFlagid());
                ff.setFlag(flag.getFlag());
                flagList.add(ff);
            }
            importantPeopleRecognize.setFlag(flagList);
            if (recognize.getType() == 1 || recognize.getType() == 3) {
                importantPeopleRecognize.setBurl(innerService.httpHostNameToIp(recognize.getBurl()).getHttp_ip());
                importantPeopleRecognize.setSurl(innerService.httpHostNameToIp(recognize.getSurl()).getHttp_ip());
            }
            importantPeopleRecognize.setSimilarity(recognize.getSimilarity());
            importantPeopleRecognize.setImsi(ImsiUtil.toMac(recognize.getImsi()));
            importantPeopleRecognize.setMac(recognize.getMac());
            importantPeopleRecognize.setPlate(recognize.getPlate());
            voList.add(importantPeopleRecognize);
        }
        switch (param.getSearchType()) {
            case 0:
                vo.setImportantPeopleCaptureList(voList);
                break;
            case 1:
                vo.setImportantPeopleRecognizeList(voList);
                break;
            case 2:
                vo.setImportantPeopleRecordList(voList);
                break;
            default:
                log.error("param search type error");
                return null;
        }
        vo.setTotalNum((int) info.getTotal());
        return vo;
    }

    public List<ImportantPeopleRecognizeHistoryVO> importantPeopleRecognizeHistory(Long regionId) {
        List<ImportantPeopleRecognizeHistoryVO> voList = new ArrayList<>();
        List<Long> communityIds = platformService.getCommunityIdsById(regionId);
        if (communityIds == null || communityIds.size() == 0) {
            log.info("Search community ids by region id is null, so return null");
            return null;
        }
        log.info("Search community ids by region id is:" + JacksonUtil.toJson(communityIds));
        List<ImportantPeopleRecognizeHistory> histories = recognizeRecordMapper.getImportantPeopleRecognizeHistory(communityIds);
        if (histories == null || histories.size() == 0) {
            log.info("Search community important people recognize history is null, so return null");
            return null;
        }
        for (ImportantPeopleRecognizeHistory history : histories){
            ImportantPeopleRecognizeHistoryVO vo = new ImportantPeopleRecognizeHistoryVO();
            vo.setPeopleId(history.getPeopleId());
            vo.setName(history.getName());
            vo.setIdcard(history.getIdcard());
            vo.setAge(history.getAge());
            vo.setSex(history.getSex());
            vo.setBirthplace(history.getBirthplace());
            vo.setAddress(history.getAddress());
            List<Phone> phones = history.getPhones();
            if (phones != null && phones.size() > 0){
                List<String> phoneList = new ArrayList<>();
                for (Phone phone : phones) {
                    phoneList.add(phone.getPhone());
                }
                vo.setPhone(phoneList);
            }
            List<Car> cars = history.getCars();
            if (cars != null && cars.size() > 0){
                List<String> carList = new ArrayList<>();
                for (Car car : cars) {
                    carList.add(car.getCar());
                }
                vo.setCar(carList);
            }
            List<Imsi> imsis = history.getImsis();
            if (imsis != null && imsis.size() > 0){
                List<String> imsiList = new ArrayList<>();
                for (Imsi imsi : imsis) {
                    imsiList.add(ImsiUtil.toMac(imsi.getImsi()));
                }
                vo.setMac(imsiList);
            }
            vo.setFlag(history.getType());
            //当type等于1的时候：为人脸识别类型；不等于1的时候，数据库pictureid为空,所以需要手动查询一张图片出来
            if (history.getType() == 1) {
                vo.setPictureId(history.getPictureId());
            } else {
                Long picId = pictureMapper.getPictureIdByPeopleId(history.getPeopleId());
                vo.setPictureId(picId);
            }
            vo.setImsi(ImsiUtil.toMac(history.getImsi()));
            vo.setPlate(history.getPlate());
            vo.setBurl(innerService.httpHostNameToIp(history.getBurl()).getHttp_ip());
            vo.setSurl(innerService.httpHostNameToIp(history.getSurl()).getHttp_ip());
            vo.setTime(sdf.format(history.getCaptureTime()));
            vo.setCommunityId(history.getCommunity());
            vo.setCommunity(platformService.getCommunityName(history.getCommunity()));
            vo.setDeviceId(history.getDeviceId());
            if (history.getType() == 1 || history.getType() == 3){
                vo.setDeviceId(history.getDeviceId());
                vo.setDeviceName(platformService.getCameraDeviceName(history.getDeviceId()));
            }
            if (history.getType() == 2){
                vo.setDeviceId(platformService.getImsiDeviceId(history.getDeviceId()));
                vo.setDeviceName(platformService.getImsiDeviceName(history.getDeviceId()));
            }
            voList.add(vo);
        }
        return voList;
    }
}
