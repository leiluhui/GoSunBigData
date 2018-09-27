package com.hzgc.service.community.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.community.dao.*;
import com.hzgc.service.community.model.CountCommunityPeople;
import com.hzgc.service.community.param.*;
import com.hzgc.service.people.dao.*;
import com.hzgc.service.community.model.DeviceRecognize;
import com.hzgc.service.community.model.FusionImsi;
import com.hzgc.service.people.model.People;
import com.hzgc.service.community.model.PeopleRecognize;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CommunityService {
    @Autowired
    private PeopleMapper peopleMapper;
    @Autowired
    private NewPeopleMapper newPeopleMapper;
    @Autowired
    private OutPeopleMapper outPeopleMapper;
    @Autowired
    private PeopleRecognizeMapper peopleRecognizeMapper;
    @Autowired
    private FusionImsiMapper fusionImsiMapper;
    @Autowired
    private DeviceRecognizeMapper deviceRecognizeMapper;

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
        log.info("Start count community new and out people, community id list:"+ JacksonUtil.toJson(param.getCommunityIdList()));
        List<CountCommunityPeople> totalNewCount = newPeopleMapper.countTotalNewPeople(param);
        List<CountCommunityPeople> totalOutCount = outPeopleMapper.countTotalOutPeople(param);
        List<CountCommunityPeople> confirmNewCount = newPeopleMapper.countConfirmNewPeople(param);
        List<CountCommunityPeople> confirmOutCount = outPeopleMapper.countConfirmOutPeople(param);
        List<NewAndOutPeopleCounVO> voList = new ArrayList<>();
        for (Long communityId : communityIdList){
            NewAndOutPeopleCounVO vo = new NewAndOutPeopleCounVO();
            vo.setCommunityId(communityId);
            // TODO 名字
            vo.setCommunityName(String.valueOf(communityId));
            vo.setMonth(param.getMonth());
            for (CountCommunityPeople suggestNew : totalNewCount){
                if (suggestNew.getCommunity().equals(communityId)){
                    vo.setSuggestNewCount(suggestNew.getCount());
                    break;
                }
            }
            for (CountCommunityPeople suggestOut : totalOutCount){
                if (suggestOut.getCommunity().equals(communityId)){
                    vo.setSuggestOutCount(suggestOut.getCount());
                    break;
                }
            }
            for (CountCommunityPeople confirmNew : confirmNewCount){
                if (confirmNew.getCommunity().equals(communityId)){
                    vo.setConfirmNewCount(confirmNew.getCount());
                    break;
                }
            }
            for (CountCommunityPeople confirmOut : confirmOutCount){
                if (confirmOut.getCommunity().equals(communityId)){
                    vo.setConfirmOutCount(confirmOut.getCount());
                    break;
                }
            }
            voList.add(vo);
        }
       return voList;
    }

    public OutPeopleLastCaptureVO searchCommunityOutPeopleLastCapture(String peopleId) {
        OutPeopleLastCaptureVO vo = new OutPeopleLastCaptureVO();
        PeopleRecognize peopleRecognize = peopleRecognizeMapper.searchCommunityOutPeopleLastCapture(peopleId);
        if (peopleRecognize != null){
            vo.setDeviceId(peopleRecognize.getDeviceid());
            // TODO 名字
            vo.setDeviceName("名字");
            vo.setPicture(peopleRecognize.getSurl());
            vo.setLastTime(sdf.format(peopleRecognize.getCapturetime()));
        }
        Timestamp lastTime = peopleMapper.getLastTime(peopleId);
        if (lastTime != null){
            long now = new Date().getTime();
            int day = Math.toIntExact((lastTime.getTime() - now) / (24 * 60 * 60 * 1000));
            vo.setLastDay(day);
        }
        return vo;
    }

    public List<PeopleCaptureVO> searchCapture1Month(PeopleCaptureDTO param) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        PageHelper.offsetPage(param.getStart(), param.getLimit());
        List<PeopleRecognize> peopleList = peopleRecognizeMapper.searchCapture1Month(param.getPeopleId());
        List<FusionImsi> imsiList = fusionImsiMapper.searchCapture1Month(param.getPeopleId());
        if (peopleList != null && peopleList.size() > 0) {
            for (PeopleRecognize people : peopleList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(people.getCapturetime()));
                vo.setDeviceId(people.getDeviceid());
                vo.setFtpUrl(people.getBurl());
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

    public List<PeopleCaptureVO> searchPeopleTrack1Month(PeopleCaptureDTO param) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        List<PeopleRecognize> peopleList = peopleRecognizeMapper.searchCapture1Month(param.getPeopleId());
        List<FusionImsi> imsiList = fusionImsiMapper.searchCapture1Month(param.getPeopleId());
        if (peopleList != null && peopleList.size() > 0) {
            for (PeopleRecognize people : peopleList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(people.getCapturetime()));
                vo.setDeviceId(people.getDeviceid());
                voList.add(vo);
            }
        }
        if (imsiList != null && imsiList.size() > 0) {
            for (FusionImsi imsi : imsiList) {
                PeopleCaptureVO vo = new PeopleCaptureVO();
                vo.setCaptureTime(sdf.format(imsi.getReceivetime()));
                vo.setDeviceId(imsi.getDeviceid());
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

    public List<PeopleCaptureCountVO> countDeviceCaptureNum1Month(PeopleCaptureDTO param) {
        List<PeopleCaptureCountVO> voList = new ArrayList<>();
        List<DeviceRecognize> deviceRecognizeList = deviceRecognizeMapper.countDeviceCaptureNum1Month(param.getPeopleId());
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

    public List<PeopleCaptureCountVO> countCaptureNum3Month(PeopleCaptureDTO param) {
        List<PeopleCaptureCountVO> voList = new ArrayList<>();
        List<DeviceRecognize> deviceRecognizeList = deviceRecognizeMapper.countCaptureNum3Month(param.getPeopleId());
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
            String startDate = year_start + "-" + month_start + "-" + (date_start >= 10 ? date_start : "0" + date_start);
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
