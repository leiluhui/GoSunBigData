package com.hzgc.service.dyncar.service;

import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.compare.SortParam;
import com.hzgc.jniface.CarAttribute;
import com.hzgc.service.dyncar.bean.*;
import com.hzgc.service.dyncar.dao.ElasticSearchDao;
import com.hzgc.service.dyncar.dao.EsSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaptureHistoryService {
    @Autowired
    @SuppressWarnings("unused")
    private ElasticSearchDao elasticSearchDao;
    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;

    public SearchResult getCaptureHistory(CaptureOption option) {
        String sortParam = EsSearchParam.DESC;
        List<SortParam> sortParams = option.getSort()
                .stream().map(param -> SortParam.values()[param]).collect(Collectors.toList());
        System.out.println(JSONUtil.toJson(sortParams));
        for (SortParam s : sortParams) {
            if (s.name().equals(SortParam.TIMEDESC.toString())) {
                sortParam = EsSearchParam.DESC;
            }
        }
        log.info("The current query is default");
        SearchResult searchResult = getDefaultCaptureHistory(option, sortParam);
        if (sortParams.get(0).name().equals(SortParam.IPC.toString())) {
            log.info("The current query needs to be grouped by ipcid");
            searchResult = getCaptureHistory(option, sortParam,searchResult);
        } else if (!sortParams.get(0).name().equals(SortParam.IPC.toString())) {
            log.info("The current query don't needs to be grouped by ipcid");
            searchResult = getCaptureHistory(option, option.getDeviceIpcs(), sortParam,searchResult);
        }
        return searchResult;
    }

    //根据ipcid进行分类
    private SearchResult getCaptureHistory(CaptureOption option, String sortParam, SearchResult searchResult) {
        List <GroupByIpc> groupByIpcs = new ArrayList <>();
        for (String ipcId : option.getDeviceIpcs()) {
            List<CapturedPicture> capturedPictureList = new ArrayList<>();
            SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, ipcId, sortParam);
            SearchHits searchHits = searchResponse.getHits();
            GroupByIpc groupByIpc = new GroupByIpc();
            SearchHit[] hits = searchHits.getHits();
            System.out.println(hits.length);
            System.out.println(searchHits.getTotalHits());
            CapturedPicture capturePicture;
            if (hits.length > 0) {
                for (SearchHit hit : hits) {
                    capturePicture = new CapturedPicture();
                    String surl = (String) hit.getSource().get("surl");
                    String burl = (String) hit.getSource().get("burl");
                    String ipc = (String) hit.getSource().get("ipcid");
                    String ip = (String) hit.getSource().get("ip");
                    String timestamp = (String) hit.getSource().get("timestamp");
                    //参数封装
                    CarAttribute carAttribute = carDataPackage(hit);
                    capturePicture.setCarAttribute(carAttribute);
                    capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl, ip));
                    capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl, ip));
                    capturePicture.setDeviceId(option.getIpcMappingDevice().get(ipc).getId());
                    capturePicture.setDeviceName(option.getIpcMappingDevice().get(ipc).getName());
                    capturePicture.setTimestamp(timestamp);
                    if (ipcId.equals(ipc)) {
                        capturedPictureList.add(capturePicture);
                    }
                }
            }
            groupByIpc.setPictures(capturedPictureList);
            groupByIpc.setDeviceId(option.getIpcMappingDevice().get(ipcId).getId());
            groupByIpc.setTotal(capturedPictureList.size());
            groupByIpc.setDeviceName(option.getIpcMappingDevice().get(ipcId).getName());
            groupByIpcs.add(groupByIpc);
        }
        searchResult.getSingleSearchResult().setDevicePictures(groupByIpcs);
        searchResult.setSearchId(UuidUtil.getUuid());
        log.info("Capture history results:" + JSONUtil.toJson(groupByIpcs));
        return searchResult;
    }

    //默认查询，不进行设别分类
    private SearchResult getDefaultCaptureHistory(CaptureOption option, String sortParam) {
        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        SearchResult searchResult = new SearchResult();
        SingleSearchResult singleSearchResult = new SingleSearchResult();
        List<CapturedPicture> pictures = new ArrayList<>();
        CapturedPicture capturePicture;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                capturePicture = new CapturedPicture();
                String surl = (String) hit.getSource().get("surl");
                String burl = (String) hit.getSource().get("burl");
                String ipcid = (String) hit.getSource().get("ipcid");
                String ip = (String) hit.getSource().get("ip");
                String timestamp = (String) hit.getSource().get("timestamp");
                //参数封装
                CarAttribute carAttribute = carDataPackage(hit);
                capturePicture.setCarAttribute(carAttribute);
                capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl,ip));
                capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl,ip));
                capturePicture.setDeviceId(ipcid);
                capturePicture.setDeviceName(option.getIpcMappingDevice().get(ipcid).getName());
                capturePicture.setTimestamp(timestamp);
                pictures.add(capturePicture);
            }
        }
        singleSearchResult.setSearchId(UuidUtil.getUuid());
        singleSearchResult.setPictures(pictures);
        singleSearchResult.setTotal(pictures.size());
        searchResult.setSingleSearchResult(singleSearchResult);
        searchResult.setSearchId(UuidUtil.getUuid());
        return searchResult;
    }

    //多个ipcid查询
    private SearchResult getCaptureHistory(CaptureOption option, List<String> deviceIds, String sortParam, SearchResult searchResult) {
        ArrayList <GroupByIpc> groupByIpcs = new ArrayList <>();
        GroupByIpc groupByIpc = new GroupByIpc();
        List<CapturedPicture> captureList = new ArrayList<>();
        SearchResponse searchResponse = elasticSearchDao.getCaptureHistory(option, deviceIds, sortParam);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        CapturedPicture capturePicture;
        if (hits.length > 0) {
            for (SearchHit hit : hits) {
                capturePicture = new CapturedPicture();
                String surl = (String) hit.getSource().get("surl");
                String burl = (String) hit.getSource().get("burl");
                String ipc = (String) hit.getSource().get("ipcid");
                String ip = (String) hit.getSource().get("ip");
                String timestamp = (String) hit.getSource().get("timestamp");
                //参数封装
                CarAttribute carAttribute = carDataPackage(hit);
                capturePicture.setCarAttribute(carAttribute);
                capturePicture.setSurl(captureServiceHelper.getFtpUrl(surl,ip));
                capturePicture.setBurl(captureServiceHelper.getFtpUrl(burl,ip));
                capturePicture.setDeviceId(ipc);
                capturePicture.setTimestamp(timestamp);
                capturePicture.setDeviceId(option.getIpcMappingDevice().get(ipc).getId());
                capturePicture.setDeviceName(option.getIpcMappingDevice().get(ipc).getName());
                captureList.add(capturePicture);
            }
        }
        groupByIpc.setTotal((int) searchHits.getTotalHits());
        groupByIpc.setPictures(captureList);
        groupByIpc.setDeviceId(option.getDeviceIds().get(0).toString());
        groupByIpc.setDeviceName(option.getIpcMappingDevice().get(option.getDeviceIpcs().get(0)).getName());
        groupByIpcs.add(groupByIpc);
        searchResult.getSingleSearchResult().setDevicePictures(groupByIpcs);
        return searchResult;
    }

    //数据封装
    private static CarAttribute carDataPackage(SearchHit hit){
        return new CarAttribute((String)hit.getSource().get("vehicle_object_type"),
                (String)hit.getSource().get("belt_maindriver"),
                (String)hit.getSource().get("belt_codriver"),
                (String)hit.getSource().get("brand_name"),
                (String)hit.getSource().get("call_code"),
                (String)hit.getSource().get("vehicle_color"),
                (String)hit.getSource().get("crash_code"),
                (String)hit.getSource().get("danger_code"),
                (String)hit.getSource().get("marker_code"),
                (String)hit.getSource().get("plate_schelter_code"),
                (String)hit.getSource().get("plate_flag_code"),
                (String)hit.getSource().get("plate_licence"),
                (String)hit.getSource().get("plate_destain_code"),
                (String)hit.getSource().get("plate_color_code"),
                (String)hit.getSource().get("plate_type_code"),
                (String)hit.getSource().get("rack_code"),
                (String)hit.getSource().get("sparetire_code"),
                (String)hit.getSource().get("mistake_code"),
                (String)hit.getSource().get("sunroof_code"),
                (String)hit.getSource().get("vehicle_type"));

    }
}