package com.hzgc.service.community.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.community.dao.*;
import com.hzgc.service.community.model.*;
import com.hzgc.service.community.param.*;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.people.model.People;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CommunityService {
    @Autowired
    @SuppressWarnings("unused")
    private PeopleMapper peopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private FusionImsiMapper fusionImsiMapper;

    @Autowired
    @SuppressWarnings("unused")
    private NewPeopleMapper newPeopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private OutPeopleMapper outPeopleMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PeopleRecognizeMapper peopleRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private DeviceRecognizeMapper deviceRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private Count24HourMapper count24HourMapper;

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

    public PeopleCountVO countCommunityPeople(Long communityId) {
        PeopleCountVO vo = new PeopleCountVO();
        vo.setCommunityPeoples(peopleMapper.countCommunityPeople(communityId));
        vo.setImportantPeoples(peopleMapper.countImportantPeople(communityId));
        vo.setCarePeoples(peopleMapper.countCarePeople(communityId));
        vo.setNewPeoples(newPeopleMapper.countNewPeople(communityId));
        vo.setOutPeoples(outPeopleMapper.countOutPeople(communityId));
        return vo;
    }

    public List<PeopleVO> searchCommunityPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCommunityPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityImportantPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchImportantPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityCarePeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchCarePeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityNewPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchNewPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    public List<PeopleVO> searchCommunityOutPeople(PeopleDTO param) {
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<People> peopleList = peopleMapper.searchOutPeople(param.getCommunityId());
        return this.shift(peopleList);
    }

    private List<PeopleVO> shift(List<People> peopleList) {
        List<PeopleVO> voList = new ArrayList<>();
        if (peopleList != null && peopleList.size() > 0) {
            for (People people : peopleList) {
                PeopleVO vo = new PeopleVO();
                vo.setId(people.getId());
                vo.setIdCard(people.getIdcard());
                vo.setName(people.getName());
                if (people.getLasttime() != null) {
                    vo.setLastTime(sdf.format(people.getLasttime()));
                }
                vo.setPictureId(people.getPictureId());
                voList.add(vo);
            }
        }
        return voList;
    }

    public List<NewAndOutPeopleCounVO> countCommunityNewAndOutPeople(NewAndOutPeopleCountDTO param) {
        List<Long> communityIdList;
        int offset = param.getStart();
        int count = param.getLimit();
        int size = param.getCommunityIdList().size();
        if (offset > -1 && size > (offset + count - 1)) {
            //结束行小于总数，取起始行开始后续count条数据
            communityIdList = param.getCommunityIdList().subList(offset, offset + count);
        } else {
            //结束行大于总数，则返回起始行开始的后续所有数据
            communityIdList = param.getCommunityIdList().subList(offset, size);
        }
        param.setCommunityIdList(communityIdList);
        log.info("Start count community new and out people, community id list:" + JacksonUtil.toJson(param.getCommunityIdList()));
        List<CountCommunityPeople> totalNewCount = newPeopleMapper.countTotalNewPeople(param);
        List<CountCommunityPeople> totalOutCount = outPeopleMapper.countTotalOutPeople(param);
        List<CountCommunityPeople> confirmNewCount = newPeopleMapper.countConfirmNewPeople(param);
        List<CountCommunityPeople> confirmOutCount = outPeopleMapper.countConfirmOutPeople(param);
        List<NewAndOutPeopleCounVO> voList = new ArrayList<>();
        for (Long communityId : communityIdList) {
            NewAndOutPeopleCounVO vo = new NewAndOutPeopleCounVO();
            vo.setCommunityId(communityId);
            vo.setCommunityName(platformService.getCommunityName(communityId));
            vo.setMonth(param.getMonth());
            for (CountCommunityPeople suggestNew : totalNewCount) {
                if (suggestNew.getCommunity().equals(communityId)) {
                    vo.setSuggestNewCount(suggestNew.getCount());
                    break;
                }
            }
            for (CountCommunityPeople suggestOut : totalOutCount) {
                if (suggestOut.getCommunity().equals(communityId)) {
                    vo.setSuggestOutCount(suggestOut.getCount());
                    break;
                }
            }
            for (CountCommunityPeople confirmNew : confirmNewCount) {
                if (confirmNew.getCommunity().equals(communityId)) {
                    vo.setConfirmNewCount(confirmNew.getCount());
                    break;
                }
            }
            for (CountCommunityPeople confirmOut : confirmOutCount) {
                if (confirmOut.getCommunity().equals(communityId)) {
                    vo.setConfirmOutCount(confirmOut.getCount());
                    break;
                }
            }
            voList.add(vo);
        }
        return voList;
    }

    public NewAndOutPeopleSearchVO searchCommunityNewAndOutPeople(NewAndOutPeopleSearchDTO param) {
        List<NewAndOutPeopleSearch> newAndOutPeopleSearchList = new ArrayList<>();
        // 小区迁入人口查询（疑似与确认）
        if (param.getType() == 0) {
            List<NewPeople> list = newPeopleMapper.searchCommunityNewPeople(param);
            if (list != null && list.size() > 0) {
                for (NewPeople people : list) {
                    NewAndOutPeopleSearch vo = new NewAndOutPeopleSearch();
                    vo.setPeopleId(people.getPeopleid());
                    vo.setCommunityId(param.getCommunityId());
                    vo.setMonth(param.getMonth());
                    vo.setType(param.getType());
                    // 未确认迁入
                    if (people.getIsconfirm() == 1) {
                        vo.setIsconfirm(2);
                        if (people.getFlag() == 1) {
                            vo.setFlag(0);
                            // 未确认迁入人口:预实名
                            vo.setPicture(getPictureIdByPeopleId(people.getPeopleid()));
                        }
                        if (people.getFlag() == 2) {
                            vo.setFlag(1);
                            // 未确认迁入人口:新增
                            vo.setSul(innerService.httpHostNameToIp(getSurlByPeopleId(people.getPeopleid())).getHttp_ip());
                        }
                    }
                    // 已确认迁入
                    if (people.getIsconfirm() == 2) {
                        vo.setIsconfirm(0);
                        if (people.getFlag() == 1) {
                            vo.setFlag(0);
                        }
                        if (people.getFlag() == 2) {
                            vo.setFlag(1);
                        }
                        vo.setPicture(getPictureIdByPeopleId(people.getPeopleid()));
                    }
                    // 已确认未迁入
                    if (people.getIsconfirm() == 3) {
                        vo.setIsconfirm(1);
                        if (people.getFlag() == 1) {
                            vo.setFlag(0);
                            // 已确认未迁入人口:预实名
                            vo.setPicture(getPictureIdByPeopleId(people.getPeopleid()));
                        }
                        if (people.getFlag() == 2) {
                            vo.setFlag(1);
                            // 已确认未迁入人口:新增
                            vo.setSul(getSurlByPeopleId(people.getPeopleid()));
                        }
                    }
                    newAndOutPeopleSearchList.add(vo);
                }
            }
        }
        // 小区迁出人口查询（疑似与确认）
        if (param.getType() == 1) {
            List<OutPeople> list = outPeopleMapper.searchCommunityOutPeople(param);
            if (list != null && list.size() > 0) {
                for (OutPeople people : list) {
                    NewAndOutPeopleSearch vo = new NewAndOutPeopleSearch();
                    vo.setPeopleId(people.getPeopleid());
                    vo.setCommunityId(param.getCommunityId());
                    vo.setMonth(param.getMonth());
                    vo.setPicture(getPictureIdByPeopleId(people.getPeopleid()));
                    vo.setType(param.getType());
                    if (people.getIsconfirm() == 1) {
                        vo.setIsconfirm(5);
                    }
                    if (people.getIsconfirm() == 2) {
                        vo.setIsconfirm(3);
                    }
                    if (people.getIsconfirm() == 3) {
                        vo.setIsconfirm(4);
                    }
                    newAndOutPeopleSearchList.add(vo);
                }
            }
        }
        NewAndOutPeopleSearchVO vo = new NewAndOutPeopleSearchVO();
        int totalNum = newAndOutPeopleSearchList.size();
        vo.setTotalNum(totalNum);
        // 分页配置
        int offset = param.getStart();
        int count = param.getLimit();
        int size = newAndOutPeopleSearchList.size();
        if (offset > -1 && size > (offset + count - 1)) {
            //结束行小于总数，取起始行开始后续count条数据
            newAndOutPeopleSearchList = newAndOutPeopleSearchList.subList(offset, offset + count);
        } else {
            //结束行大于总数，则返回起始行开始的后续所有数据
            newAndOutPeopleSearchList = newAndOutPeopleSearchList.subList(offset, size);
        }
        vo.setVoList(newAndOutPeopleSearchList);
        return vo;
    }

    private Long getPictureIdByPeopleId(String peopleId) {
        return pictureMapper.getPictureIdByPeopleId(peopleId);
    }

    private String getSurlByPeopleId(String peopleId) {
        return peopleRecognizeMapper.getSurlByPeopleId(peopleId);
    }

    public CommunityPeopleInfoVO searchCommunityPeopleInfo(String peopleId) {
        CommunityPeopleInfoVO vo = new CommunityPeopleInfoVO();
        People people = peopleMapper.searchCommunityPeopleInfo(peopleId);
        if (people != null) {
            vo.setId(people.getId());
            vo.setName(people.getName());
            vo.setIdCard(people.getIdcard());
            vo.setSex(people.getSex());
            vo.setBirthday(people.getBirthday());
            vo.setBirthplace(people.getBirthplace());
            vo.setAddress(people.getAddress());
            vo.setPictureId(people.getPictureId());
        }
        return vo;
    }

    public CommunityPeopleInfoVO searchPeopleByIdCard(String idCard) {
        CommunityPeopleInfoVO vo = new CommunityPeopleInfoVO();
        People people = peopleMapper.searchPeopleByIdCard(idCard);
        if (people != null) {
            vo.setId(people.getId());
            vo.setName(people.getName());
            vo.setSex(people.getSex());
            vo.setIdCard(people.getIdcard());
            vo.setBirthday(people.getBirthday());
            vo.setBirthplace(people.getBirthplace());
            vo.setAddress(people.getAddress());
            vo.setPictureId(people.getPictureId());
        }
        return vo;
    }

    public CaptureDetailsVO searchCommunityNewPeopleCaptureDetails(CaptureDetailsDTO param) {
        CaptureDetailsVO vo = new CaptureDetailsVO();
        // 小区迁入人口抓拍详情:设备抓拍统计
        List<CaptureDeviceCount> deviceCountList = new ArrayList<>();
        List<DeviceRecognize> deviceRecognizes = deviceRecognizeMapper.countCommunityNewPeopleCapture(param);
        if (deviceRecognizes != null && deviceRecognizes.size() > 0) {
            for (DeviceRecognize deviceRecognize : deviceRecognizes) {
                CaptureDeviceCount captureDeviceCount = new CaptureDeviceCount();
                String deviceName = platformService.getCameraDeviceName(deviceRecognize.getDeviceid());
                if (StringUtils.isBlank(deviceName)) {
                    deviceName = platformService.getImsiDeviceName(deviceRecognize.getDeviceid());
                }
                captureDeviceCount.setDeviceName(deviceName != null ? deviceName : deviceRecognize.getDeviceid());
                captureDeviceCount.setCount(deviceRecognize.getCount());
                deviceCountList.add(captureDeviceCount);
            }
            vo.setDeviceCountList(deviceCountList);
        }
        // 小区迁入人口抓拍详情:24小时统计
        List<CaptureHourCount> hourCountList = new ArrayList<>();
        List<Count24Hour> count24Hours = count24HourMapper.countCommunityNewPeopleCapture(param);
        System.out.println(JacksonUtil.toJson(count24Hours));
        List<String> hourList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        long longTime = System.currentTimeMillis();
        for (int i = 0; i < 24; i++) {
            longTime = longTime - 3600000;
            String time = dateFormat.format(new Date(longTime));
            hourList.add(time);
        }
        for (String hour : hourList) {
            CaptureHourCount count = new CaptureHourCount();
            count.setHour(hour.substring(8, 10));
            if (count24Hours != null && count24Hours.size() > 0) {
                for (Count24Hour count24Hour : count24Hours) {
                    if (hour.equals(count24Hour.getHour())) {
                        count.setCount(count24Hour.getCount());
                    }
                }
                hourCountList.add(count);
            }
        }
        vo.setHourCountList(hourCountList);
        // 小区迁入人口抓拍详情:人员抓拍列表
        Page page = PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<PeopleRecognize> peopleRecognizes = peopleRecognizeMapper.searchCommunityNewPeopleCaptureData(param.getPeopleId());
        PageInfo pageInfo = new PageInfo(page.getResult());
        int total = (int) pageInfo.getTotal();
        CapturePeopleCount capturePeopleCount = new CapturePeopleCount();
        capturePeopleCount.setTotal(total);
        List<CapturePictureInfo> infoList = new ArrayList<>();
        for (PeopleRecognize peopleRecognize : peopleRecognizes) {
            CapturePictureInfo info = new CapturePictureInfo();
            info.setDeviceId(peopleRecognize.getDeviceid());
            info.setDeviceName(platformService.getCameraDeviceName(peopleRecognize.getDeviceid()));
            info.setPicture(innerService.httpHostNameToIp(peopleRecognize.getSurl()).getHttp_ip());
            Date date = peopleRecognize.getCapturetime();
            if (date != null) {
                info.setCaptureTime(sdf.format(date));
            }
            infoList.add(info);
        }
        capturePeopleCount.setPictureInfos(infoList);
        vo.setPeopleCount(capturePeopleCount);
        return vo;
    }

    public OutPeopleLastCaptureVO searchCommunityOutPeopleLastCapture(String peopleId) {
        OutPeopleLastCaptureVO vo = new OutPeopleLastCaptureVO();
        PeopleRecognize peopleRecognize = peopleRecognizeMapper.searchCommunityOutPeopleLastCapture(peopleId);
        if (peopleRecognize != null) {
            vo.setDeviceId(peopleRecognize.getDeviceid());
            vo.setDeviceName(platformService.getCameraDeviceName(peopleRecognize.getDeviceid()));
            vo.setPicture(innerService.httpHostNameToIp(peopleRecognize.getBurl()).getHttp_ip());
            vo.setLastTime(sdf.format(peopleRecognize.getCapturetime()));
        }
        Timestamp lastTime = peopleMapper.getLastTime(peopleId);
        if (lastTime != null) {
            long now = new Date().getTime();
            int day = Math.toIntExact((now - lastTime.getTime()) / (24 * 60 * 60 * 1000));
            vo.setLastDay(day);
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer communityAffirmOut(AffirmOperationDTO param) {
        // 已确认迁出
        if (param.getIsconfirm() == 2) {
            Integer delete = peopleMapper.deleteCommunityByPeopleId(param.getPeopleId());
            if (delete != 1) {
                log.info("Affirm out operation failed");
                return 0;
            }
            Integer update = outPeopleMapper.updateIsconfirm(param);
            if (update != 1) {
                log.info("Affirm out operation failed");
                return 0;
            }
        } else if (param.getIsconfirm() == 3) {     // 已确认未迁出
            Integer update = outPeopleMapper.updateIsconfirm(param);
            if (update != 1) {
                log.info("Affirm out operation failed");
                return 0;
            }
        } else {
            log.info("Affirm out operation failed, because param: isconfirm error");
            return 0;
        }
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer communityAffirmNew(AffirmOperationDTO param) {
        // 已确认迁入
        if (param.getIsconfirm() == 2) {
            // 已确认迁入:预实名
            if (param.getFlag() == 0) {
                Integer integer = peopleMapper.insertCommunityByPeopleId(param);
                if (integer != 1) {
                    log.info("Affirm new operation failed");
                    return 0;
                }
                Integer update = newPeopleMapper.updateIsconfirm(param);
                if (update != 1) {
                    log.info("Affirm new operation failed");
                    return 0;
                }
            } else if (param.getFlag() == 1) {    // 已确认迁入:新增
                Integer update = newPeopleMapper.updateIsconfirm(param);
                if (update != 1) {
                    log.info("Affirm new operation failed");
                    return 0;
                }
            } else {
                log.info("Affirm new operation failed, because param: flag error");
                return 0;
            }
        } else if (param.getIsconfirm() == 3) {     // 已确认未迁入
            Integer update = newPeopleMapper.updateIsconfirm(param);
            if (update != 1) {
                log.info("Affirm new operation failed");
                return 0;
            }
        } else {
            log.info("Affirm new operation failed, because param: isconfirm error");
            return 0;
        }
        return 1;
    }

    public List<PeopleCaptureVO> searchCapture1Month(PeopleCaptureDTO param) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        List<PeopleRecognize> peopleList = peopleRecognizeMapper.searchCapture1Month(param.getPeopleId());
        List<FusionImsi> imsiList = fusionImsiMapper.searchCapture1Month(param.getPeopleId());
        if (peopleList != null && peopleList.size() > 0) {
            for (PeopleRecognize people : peopleList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(people.getCapturetime()));
                vo.setDeviceId(platformService.getCameraDeviceName(people.getDeviceid()));
                vo.setFtpUrl(innerService.httpHostNameToIp(people.getBurl()).getHttp_ip());
                voList.add(vo);
            }
        }
        if (imsiList != null && imsiList.size() > 0) {
            for (FusionImsi imsi : imsiList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(imsi.getReceivetime()));
                vo.setDeviceId(platformService.getImsiDeviceName(imsi.getDeviceid()));
                vo.setImsi(imsi.getImsi());
                voList.add(vo);
            }
        }
        this.listSort(voList);
        int offset = param.getStart();
        int count = param.getLimit();
        int size = voList.size();
        if (offset > -1 && size > (offset + count - 1)) {
            //结束行小于总数，取起始行开始后续count条数据
            voList = voList.subList(offset, offset + count);
        } else {
            //结束行大于总数，则返回起始行开始的后续所有数据
            voList = voList.subList(offset, size);
        }
        return voList;
    }

    public List<PeopleCaptureVO> searchPeopleTrack1Month(String peopleId) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        List<PeopleRecognize> peopleList = peopleRecognizeMapper.searchCapture1Month(peopleId);
        List<FusionImsi> imsiList = fusionImsiMapper.searchCapture1Month(peopleId);
        if (peopleList != null && peopleList.size() > 0) {
            for (PeopleRecognize people : peopleList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(people.getCapturetime()));
                vo.setDeviceId(people.getDeviceid());
                vo.setFtpUrl(innerService.httpHostNameToIp(people.getSurl()).getHttp_ip());
                voList.add(vo);
            }
        }
        if (imsiList != null && imsiList.size() > 0) {
            for (FusionImsi imsi : imsiList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(imsi.getReceivetime()));
                vo.setDeviceId(imsi.getDeviceid());
                vo.setImsi(imsi.getImsi());
                voList.add(vo);
            }
        }
        this.listSort(voList);
        return voList;
    }

    private void listSort(List<PeopleCaptureVO> voList) {
        voList.sort((o1, o2) -> {
            try {
                Date d1 = sdf.parse(o1.getCaptureTime());
                Date d2 = sdf.parse(o2.getCaptureTime());
                return Long.compare(d1.getTime(), d2.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public List<PeopleCaptureCountVO> countDeviceCaptureNum1Month(String peopleId) {
        List<PeopleCaptureCountVO> voList = new ArrayList<>();
        List<DeviceRecognize> deviceRecognizeList = deviceRecognizeMapper.countDeviceCaptureNum1Month(peopleId);
        if (deviceRecognizeList != null && deviceRecognizeList.size() > 0) {
            List<String> deviceIdList = new ArrayList<>();
            for (DeviceRecognize device : deviceRecognizeList) {
                String deviceId = device.getDeviceid();
                if (StringUtils.isNotBlank(deviceId) && !deviceIdList.contains(deviceId)) {
                    deviceIdList.add(deviceId);
                }
            }
            if (deviceIdList.size() > 0) {
                for (String deviceId : deviceIdList) {
                    PeopleCaptureCountVO vo = new PeopleCaptureCountVO();
                    vo.setDeviceId(deviceId);
                    for (DeviceRecognize deviceRecognize : deviceRecognizeList) {
                        if (deviceId.equals(deviceRecognize.getDeviceid())) {
                            vo.setCount(vo.getCount() + deviceRecognize.getCount());
                        }
                    }
                    voList.add(vo);
                }
            }
        }
        return voList;
    }

    public List<PeopleCaptureCountVO> countCaptureNum3Month(String peopleId) {
        List<PeopleCaptureCountVO> voList = new ArrayList<>();
        List<DeviceRecognize> deviceRecognizeList = deviceRecognizeMapper.countCaptureNum3Month(peopleId);
        if (deviceRecognizeList != null && deviceRecognizeList.size() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            int year_end = cal.get(Calendar.YEAR);
            int month_end = cal.get(Calendar.MONTH) + 1;
            int date_end = cal.get(Calendar.DATE);
            String endDate = year_end + "-" + month_end + "-" + date_end;
            cal.add(Calendar.MONTH, -2);
            cal.add(Calendar.DATE, -date_end + 1);
            int year_start = cal.get(Calendar.YEAR);
            int month_start = cal.get(Calendar.MONTH) + 1;
            int date_start = cal.get(Calendar.DATE);
            String startDate = year_start + "-"
                    + (month_start >= 10 ? month_start : "0" + month_start) + "-"
                    + (date_start >= 10 ? date_start : "0" + date_start);
            List<String> dateList = new ArrayList<>();
            dateList.add(startDate);
            try {
                long time = dateFormat.parse(startDate).getTime();
                for (int i = 0; i < 100; i++) {
                    time = time + (24 * 60 * 60 * 1000);
                    if (time > dateFormat.parse(endDate).getTime()) {
                        break;
                    }
                    dateList.add(dateFormat.format(time));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateList.size() > 0) {
                for (String date : dateList) {
                    PeopleCaptureCountVO vo = new PeopleCaptureCountVO();
                    vo.setDate(date);
                    for (DeviceRecognize deviceRecognize : deviceRecognizeList) {
                        if (date.replace("-", "").equals(deviceRecognize.getCurrenttime())) {
                            vo.setCount(vo.getCount() + deviceRecognize.getCount());
                        }
                    }
                    voList.add(vo);
                }
            }
        }
        return voList;
    }
}
