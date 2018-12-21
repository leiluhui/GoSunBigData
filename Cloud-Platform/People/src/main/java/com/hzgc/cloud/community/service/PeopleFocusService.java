package com.hzgc.cloud.community.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzgc.cloud.community.dao.DeviceRecognizeMapper;
import com.hzgc.cloud.community.dao.RecognizeRecordMapper;
import com.hzgc.cloud.community.model.DeviceRecognize;
import com.hzgc.cloud.community.model.RecognizeRecord;
import com.hzgc.cloud.community.param.PeopleCaptureCountVO;
import com.hzgc.cloud.community.param.PeopleCaptureDTO;
import com.hzgc.cloud.community.param.PeopleCaptureVO;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.util.basic.ImsiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class PeopleFocusService {
    @Autowired
    @SuppressWarnings("unused")
    private RecognizeRecordMapper recognizeRecordMapper;

    @Autowired
    @SuppressWarnings("unused")
    private DeviceRecognizeMapper deviceRecognizeMapper;

    @Autowired
    @SuppressWarnings("unused")
    private PlatformService platformService;

    @Autowired
    @SuppressWarnings("unused")
    private InnerService innerService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ResponseResult<List<PeopleCaptureVO>> searchCapture1Month(PeopleCaptureDTO param) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        Page page = PageHelper.offsetPage(param.getStart(), param.getLimit(), true);
        List<RecognizeRecord> records = recognizeRecordMapper.searchCapture1Month(param);
        PageInfo info = new PageInfo(page.getResult());
        if (records != null && records.size() > 0) {
            for (RecognizeRecord record : records) {
                if (record != null) {
                    PeopleCaptureVO vo = new PeopleCaptureVO();
                    switch (param.getSearchType()) {
                        case 0:
                            // 返回:人脸抓拍、IMSI码、车辆全部抓拍识别记录
                            switch (record.getType()) {
                                case 1:
                                    // 人脸抓拍识别记录
                                    vo.setCaptureType(0);
                                    vo.setRecordId(record.getId());
                                    vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                    vo.setCameraDeviceId(platformService.getCameraDeviceName(record.getDeviceid()));
                                    vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                                    vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                                    break;
                                case 2:
                                    // IMSI码识别记录
                                    vo.setCaptureType(1);
                                    vo.setRecordId(record.getId());
                                    vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                    vo.setImsiDeviceId(platformService.getImsiDeviceName(record.getDeviceid()));
                                    vo.setImsi(ImsiUtil.toMac(record.getImsi()));
                                    break;
                                case 3:
                                    // 车辆抓拍识别记录
                                    vo.setCaptureType(2);
                                    vo.setRecordId(record.getId());
                                    vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                    vo.setCameraDeviceId(platformService.getCameraDeviceName(record.getDeviceid()));
                                    vo.setPlate(record.getPlate());
                                    vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                                    vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                                    break;
                                default:
                                    break;
                            }
                            voList.add(vo);
                            break;
                        case 1:
                            // 返回:人脸抓拍识别记录
                            if (record.getType() == 1){
                                // 人脸抓拍识别记录
                                vo.setCaptureType(0);
                                vo.setRecordId(record.getId());
                                vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                vo.setCameraDeviceId(platformService.getCameraDeviceName(record.getDeviceid()));
                                vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                                vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                                voList.add(vo);
                            }
                            break;
                        case 2:
                            // 返回:IMSI码识别记录
                            if (record.getType() == 2){
                                vo.setCaptureType(1);
                                vo.setRecordId(record.getId());
                                vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                vo.setImsiDeviceId(platformService.getImsiDeviceName(record.getDeviceid()));
                                vo.setImsi(ImsiUtil.toMac(record.getImsi()));
                                voList.add(vo);
                            }
                            break;
                        case 3:
                            // 返回:车辆抓拍识别记录
                            if (record.getType() == 3){
                                vo.setCaptureType(2);
                                vo.setRecordId(record.getId());
                                vo.setCaptureTime(sdf.format(record.getCapturetime()));
                                vo.setCameraDeviceId(platformService.getCameraDeviceName(record.getDeviceid()));
                                vo.setPlate(record.getPlate());
                                vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                                vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                                voList.add(vo);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return ResponseResult.init(voList, info.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer deleteRecognizeRecord(List<String> idList) {
        for (String id : idList) {
            int status = recognizeRecordMapper.deleteByPrimaryKey(id);
            if (status != 1) {
                throw new RuntimeException("删除抓拍识别记录失败！");
            }
        }
        return 1;
    }

    public List<PeopleCaptureVO> searchPeopleTrack1Month(String peopleId) {
        List<PeopleCaptureVO> voList = new ArrayList<>();
        List<RecognizeRecord> records = recognizeRecordMapper.searchPeopleTrack1Month(peopleId);
        if (records != null && records.size() > 0) {
            for (RecognizeRecord record : records) {
                if (record != null) {
                    PeopleCaptureVO vo = new PeopleCaptureVO();
                    switch (record.getType()) {
                        case 1:
                            // 人脸抓拍识别记录
                            vo.setCaptureType(0);
                            vo.setRecordId(record.getId());
                            vo.setCaptureTime(sdf.format(record.getCapturetime()));
                            vo.setCameraDeviceId(record.getDeviceid());
                            vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                            vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                            break;
                        case 2:
                            // IMSI码识别记录
                            vo.setCaptureType(1);
                            vo.setRecordId(record.getId());
                            vo.setCaptureTime(sdf.format(record.getCapturetime()));
                            vo.setImsiDeviceId(record.getDeviceid());
                            vo.setImsi(record.getImsi());
                            break;
                        case 3:
                            // 车辆抓拍识别记录
                            vo.setCaptureType(2);
                            vo.setRecordId(record.getId());
                            vo.setCaptureTime(sdf.format(record.getCapturetime()));
                            vo.setCameraDeviceId((record.getDeviceid()));
                            vo.setPlate(record.getPlate());
                            vo.setBurl(innerService.httpHostNameToIp(record.getBurl()).getHttp_ip());
                            vo.setSurl(innerService.httpHostNameToIp(record.getSurl()).getHttp_ip());
                            break;
                        default:
                            break;
                    }
                    voList.add(vo);
                }
            }
        }
        return voList;
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
