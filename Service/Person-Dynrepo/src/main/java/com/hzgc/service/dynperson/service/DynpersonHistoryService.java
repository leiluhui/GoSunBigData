package com.hzgc.service.dynperson.service;

import com.hzgc.common.collect.util.CollectUrlUtil;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.bean.UrlInfo;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.facedynrepo.PersonTable;
import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.jniface.PersonAttributes;
import com.hzgc.service.dynperson.bean.CaptureOption;
import com.hzgc.service.dynperson.bean.DevicePictures;
import com.hzgc.service.dynperson.bean.Pictures;
import com.hzgc.service.dynperson.bean.SingleResults;
import com.hzgc.service.dynperson.dao.ElasticSearchDao;
import com.hzgc.service.dynperson.dao.EsSearchParam;
import com.hzgc.service.dynperson.util.DeviceToIpcs;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DynpersonHistoryService {

    @Autowired
    private PlatformService platformService;
    @Autowired
    @SuppressWarnings("unused")
    private ElasticSearchDao elasticSearchDao;
    @Autowired
    private InnerService innerService;

    public SingleResults getCaptureHistory(CaptureOption captureOption) {
        String sortParam = EsSearchParam.DESC;
        log.info("The current query don't needs to be grouped by ipcid");
        SingleResults singleResults = getCaptureHistory(captureOption, DeviceToIpcs.getIpcs(captureOption.getDevices()), sortParam);
        return singleResults;
    }

    private SingleResults getDefaultCaptureHistory(CaptureOption captureOption, String sortParam) {
        SingleResults singleResults = new SingleResults();
        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(captureOption, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        int totalCount = (int) searchHits.getTotalHits();
        List <Pictures> picturesList = new ArrayList <>();
        for (SearchHit hit : searchHits) {
            Pictures pictures = new Pictures();
            String sabsolutepath = (String) hit.getSource().get(PersonTable.SABSOLUTEPATH);
            String babsolutepath = (String) hit.getSource().get(PersonTable.BABSOLUTEPATH);
            String ipcid = (String) hit.getSource().get(PersonTable.IPCID);
            String timestamp = (String) hit.getSource().get(PersonTable.TIMESTAMP);
            String hostname = (String) hit.getSource().get(PersonTable.HOSTNAME);
            UrlInfo urlInfo = innerService.hostName2Ip(hostname);
            pictures.setSabsolutepath(CollectUrlUtil.toHttpPath(urlInfo.getIp(), urlInfo.getPort(), sabsolutepath));
            pictures.setBabsolutepath(CollectUrlUtil.toHttpPath(urlInfo.getIp(), urlInfo.getPort(), babsolutepath));
            pictures.setDeviceId(ipcid);
            pictures.setDeviceName(captureOption.getIpcMapping().get(ipcid).getDeviceName());
            pictures.setTime(timestamp);

            List <PersonAttributes> personAttributes = getPersonAttributes(hit);

            pictures.setPersonAttributes(personAttributes);
            picturesList.add(pictures);
        }
        singleResults.setTotal(totalCount);
        singleResults.setPictures(picturesList);
        singleResults.setSearchId(UuidUtil.getUuid());
        return singleResults;
    }


    private SingleResults getCaptureHistory(CaptureOption captureOption, List <String> deviceIpcs, String sortParam) {
        SingleResults singleResults = new SingleResults();
        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(captureOption, deviceIpcs, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        int totalCount = (int) searchHits.getTotalHits();
        SearchHit[] hits = searchHits.getHits();
        List <Pictures> picturesList = new ArrayList <>();
        Pictures pictures;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                pictures = new Pictures();
                String sabsolutepath = (String) hit.getSource().get(PersonTable.SABSOLUTEPATH);
                String babsolutepath = (String) hit.getSource().get(PersonTable.BABSOLUTEPATH);
                String ipc = (String) hit.getSource().get(PersonTable.IPCID);
                String timestamp = (String) hit.getSource().get(PersonTable.TIMESTAMP);
                String hostname = (String) hit.getSource().get(PersonTable.HOSTNAME);
                UrlInfo urlInfo = innerService.hostName2Ip(hostname);
                pictures.setSabsolutepath(CollectUrlUtil.toHttpPath(urlInfo.getIp(), urlInfo.getPort(), sabsolutepath));
                pictures.setBabsolutepath(CollectUrlUtil.toHttpPath(urlInfo.getIp(), urlInfo.getPort(), babsolutepath));
                pictures.setDeviceId(ipc);
                pictures.setDeviceName(captureOption.getIpcMapping().get(ipc).getDeviceName());
                pictures.setTime(timestamp);
                pictures.setLocation(getLocation(ipc));
                List <PersonAttributes> personAttributes = getPersonAttributes(hit);
                pictures.setPersonAttributes(personAttributes);
                picturesList.add(pictures);
            }
        }
        singleResults.setDeviceTotal(captureOption.getIpcMapping().entrySet().size());
        singleResults.setPictures(picturesList);
        singleResults.setSearchId(UuidUtil.getUuid());
        singleResults.setTotal(totalCount);
        return singleResults;
    }

    private SingleResults getCaptureHistory(CaptureOption captureOption, String sortParam) {
        SingleResults singleResults = new SingleResults();
        List <DevicePictures> devicePicturesList = new ArrayList <>();
        for (String ipcId : DeviceToIpcs.getIpcs(captureOption.getDevices())) {
            DevicePictures devicePictures = new DevicePictures();
            List <Pictures> pictureList = new ArrayList <>();
            SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(captureOption, ipcId, sortParam);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            int totalCount = (int) searchHits.getTotalHits();
            Pictures pictures;
            if (totalCount > 0) {
                for (SearchHit hit : hits) {
                    pictures = new Pictures();
                    String sabsolutepath = (String) hit.getSource().get(PersonTable.SABSOLUTEPATH);
                    String babsolutepath = (String) hit.getSource().get(PersonTable.BABSOLUTEPATH);
                    String ipc = (String) hit.getSource().get(PersonTable.IPCID);
                    String timestamp = (String) hit.getSource().get(PersonTable.TIMESTAMP);
                    String hostname = (String) hit.getSource().get(PersonTable.HOSTNAME);
//                    Map <String, String> ftpIpMapping = ftpRegisterClient.getFtpIpMapping();
//                    String ip = ftpIpMapping.get(hostname);
//                    pictures.setSabsolutepath(ConverFtpurl.toHttpPath(ip,ftpPort,sabsolutepath));
//                    pictures.setBabsolutepath(ConverFtpurl.toHttpPath(ip,ftpPort,babsolutepath));
                    pictures.setSabsolutepath(sabsolutepath);
                    pictures.setBabsolutepath(babsolutepath);
                    if (null != captureOption.getIpcMapping().get(ipc)) {
                        pictures.setDeviceId(ipc);
                        pictures.setDeviceName(captureOption.getIpcMapping().get(ipc).getDeviceName());
                    }

                    List <PersonAttributes> personAttributes = getPersonAttributes(hit);
                    pictures.setPersonAttributes(personAttributes);

                    pictures.setTime(timestamp);
                    if (ipcId.equals(ipc)) {
                        pictureList.add(pictures);
                    }
                }
            } else {
                pictures = new Pictures();
                pictureList.add(pictures);
            }
            devicePictures.setDeviceId(ipcId);
            devicePictures.setDeviceName(captureOption.getIpcMapping().get(ipcId).getDeviceName());
            devicePictures.setPictures(pictureList);
            devicePictures.setTotal(pictureList.size());
            devicePicturesList.add(devicePictures);

            singleResults.setTotal((int) searchHits.getTotalHits());
            singleResults.setSearchId(UuidUtil.getUuid());
            singleResults.setDevicePicturesList(devicePicturesList);
        }
        return singleResults;
    }

    private String getLocation(String ipc) {
        //查询相机位置
        ArrayList <String> list = new ArrayList <>();
        list.add(ipc);
        Map <String, CameraQueryDTO> cameraInfoByBatchIpc = platformService.getCameraInfoByBatchIpc(list);
        CameraQueryDTO cameraQueryDTO = cameraInfoByBatchIpc.get(ipc);
        return cameraQueryDTO.getRegion() + cameraQueryDTO.getCommunity();
    }

    private List <PersonAttributes> getPersonAttributes(SearchHit hit) {
        List <PersonAttributes> personAttributes = new ArrayList <>();
        PersonAttributes personAttribute = new PersonAttributes();
        personAttribute.setAge((String) hit.getSource().get(PersonTable.AGE));
        personAttribute.setHair((String) hit.getSource().get(PersonTable.HAIR));
        personAttribute.setBaby((String) hit.getSource().get(PersonTable.BABY));
        personAttribute.setBag((String) hit.getSource().get(PersonTable.BAG));
        personAttribute.setBottomcolor((String) hit.getSource().get(PersonTable.BOTTOMCOLOR));
        personAttribute.setBottomtype((String) hit.getSource().get(PersonTable.BOTTOMTYPE));
        personAttribute.setCartype((String) hit.getSource().get(PersonTable.CARTYPE));
        personAttribute.setHat((String) hit.getSource().get(PersonTable.HAT));
        personAttribute.setKnapsack((String) hit.getSource().get(PersonTable.KNAPSACK));
        personAttribute.setMessengerbag((String) hit.getSource().get(PersonTable.MESSENGERBAG));
        personAttribute.setOrientation((String) hit.getSource().get(PersonTable.ORIENTATION));
        personAttribute.setSex((String) hit.getSource().get(PersonTable.SEX));
        personAttribute.setShoulderbag((String) hit.getSource().get(PersonTable.SHOULDERBAG));
        personAttribute.setUmbrella((String) hit.getSource().get(PersonTable.UMBRELLA));
        personAttribute.setUppercolor((String) hit.getSource().get(PersonTable.UPPERCOLOR));
        personAttribute.setUppertype((String) hit.getSource().get(PersonTable.UPPERTYPE));

        personAttributes.add(personAttribute);
        return personAttributes;
    }
}
