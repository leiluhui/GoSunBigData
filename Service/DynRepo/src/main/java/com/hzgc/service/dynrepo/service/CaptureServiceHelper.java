package com.hzgc.service.dynrepo.service;

import com.hzgc.common.table.dynrepo.DynamicTable;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.bean.platform.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 动态库实现类
 */
@Component
class CaptureServiceHelper {

    @Autowired
    @SuppressWarnings("unused")
    private Environment environment;

    @Autowired
    @SuppressWarnings("unused")
    private DeviceQueryService queryService;

    /**
     * 通过排序参数进行排序
     *
     * @param result 查询结果
     * @param option 查询结果的查询参数
     */
    void sortByParamsAndPageSplit(SearchResult result, SearchResultOption option) {
        List<Integer> paramListInt = option.getSortParam();
        List<SortParam> paramList = paramListInt.stream().map(param -> SortParam.values()[param]).collect(toList());
        List<Boolean> isAscArr = new ArrayList<>();
        List<String> sortNameArr = new ArrayList<>();
        for (SortParam aParamList : paramList) {
            switch (aParamList) {
                case TIMEASC:
                    isAscArr.add(true);
                    sortNameArr.add("timeStamp");
                    break;
                case TIMEDESC:
                    isAscArr.add(false);
                    sortNameArr.add("timeStamp");
                    break;
                case SIMDESC:
                    isAscArr.add(false);
                    sortNameArr.add("similarity");
                    break;
                case SIMDASC:
                    isAscArr.add(true);
                    sortNameArr.add("similarity");
                    break;
            }
        }
        if (paramList.contains(SortParam.IPC)) {
            groupByIpc(result);
            for (SingleSearchResult singleResult : result.getSingleResults()) {
                for (GroupByIpc groupByIpc : singleResult.getDevicePictures()) {
                    CapturePictureSortUtil.sort(groupByIpc.getPictures(), sortNameArr, isAscArr);
                    groupByIpc.setPictures(pageSplit(groupByIpc.getPictures(), option));
                }
                singleResult.setPictures(null);
            }
        } else {
            for (SingleSearchResult singleResult : result.getSingleResults()) {
                CapturePictureSortUtil.sort(singleResult.getPictures(), sortNameArr, isAscArr);
                singleResult.setPictures(pageSplit(singleResult.getPictures(), option));
            }
        }
    }

    /**
     * 根据设备ID进行归类
     *
     * @param result 历史查询结果
     */
    private void groupByIpc(SearchResult result) {
        for (SingleSearchResult singleResult : result.getSingleResults()) {
            List<GroupByIpc> list = new ArrayList<>();
            Map<String, List<CapturedPicture>> map =
                    singleResult.getPictures().stream().collect(Collectors.groupingBy(CapturedPicture::getDeviceId));
            for (String key : map.keySet()) {
                GroupByIpc groupByIpc = new GroupByIpc();
                groupByIpc.setDeviceId(key);
                groupByIpc.setPictures(map.get(key));
                groupByIpc.setTotal(map.get(key).size());
                list.add(groupByIpc);
            }
            singleResult.setDevicePictures(list);
        }
    }

    /**
     * 对图片对象列表进行分页返回
     *
     * @param capturedPictures 待分页的图片对象列表
     * @param option           查询结果的查询参数
     * @return 返回分页查询结果
     */
    List<CapturedPicture> pageSplit(List<CapturedPicture> capturedPictures, SearchResultOption option) {
        int offset = option.getStart();
        int count = option.getLimit();
        List<CapturedPicture> subCapturePictureList;
        int totalPicture = capturedPictures.size();
        if (offset > -1 && totalPicture > (offset + count - 1)) {
            //结束行小于总数，取起始行开始后续count条数据
            subCapturePictureList = capturedPictures.subList(offset, offset + count);
        } else {
            //结束行大于总数，则返回起始行开始的后续所有数据
            subCapturePictureList = capturedPictures.subList(offset, totalPicture);
        }
        addDeviceName(subCapturePictureList);
        return subCapturePictureList;
    }

    List<CapturedPicture> pageSplit(List<CapturedPicture> capturedPictures, int offset, int count) {
        List<CapturedPicture> subCapturePictureList;
        int totalPicture = capturedPictures.size();
        if (offset >= 0 && totalPicture > (offset + count - 1) && count > 0) {
            //结束行小于总数，取起始行开始后续count条数据
            subCapturePictureList = capturedPictures.subList(offset, offset + count);
        } else {
            //结束行大于总数，则返回起始行开始的后续所有数据
            subCapturePictureList = capturedPictures.subList(offset, totalPicture);
        }
        addDeviceName(subCapturePictureList);
        return subCapturePictureList;
    }

    void addDeviceName(List<CapturedPicture> capturedPictureList) {
        if (environment.getProperty("call.external.service", boolean.class)) {
            if (capturedPictureList != null && capturedPictureList.size() > 0) {
                List<String> ipcList = new ArrayList<>();
                for (CapturedPicture picture : capturedPictureList) {
                    ipcList.add(picture.getDeviceId());
                }
                Map<String, DeviceDTO> ipcMapping = queryService.getDeviceInfoByBatch(ipcList);
                for (CapturedPicture picture : capturedPictureList) {
                    if (ipcMapping.containsKey(picture.getDeviceId())) {
                        picture.setDeviceName(ipcMapping.get(picture.getDeviceId()).getName());
                    }
                }
            }
        }
    }

    SearchResult parseResultOnePerson(ResultSet resultSet, SearchOption option, String searchId) {
        SingleSearchResult singleSearchResult = new SingleSearchResult();
        SearchResult searchResult = new SearchResult();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<CapturedPicture> capturedPictureList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                //小图ftpurl
                String surl = resultSet.getString(DynamicTable.FTPURL);
                //设备id
                String ipcid = resultSet.getString(DynamicTable.IPCID);
                //相似度
                Float similaritys = resultSet.getFloat(DynamicTable.SIMILARITY);
                //时间戳
                Timestamp timestamp = resultSet.getTimestamp(DynamicTable.TIMESTAMP);
                //大图ftpurl
                String burl = surlToBurl(surl);
                //图片对象
                CapturedPicture capturedPicture = new CapturedPicture();
                capturedPicture.setSurl(getFtpUrl(surl));
                capturedPicture.setBurl(getFtpUrl(burl));
                capturedPicture.setDeviceId(ipcid);
                capturedPicture.setTime(format.format(timestamp));
                capturedPicture.setSimilarity(similaritys);
                capturedPictureList.add(capturedPicture);
            }
            addDeviceName(capturedPictureList);
            List<String> imageIdList = new ArrayList<>();
            for (int i = 0; i < option.getImages().size(); i++) {
                imageIdList.add(option.getImages().get(i).getImageID());
            }
            singleSearchResult.
                    setImageNames(imageIdList);
            singleSearchResult.setSearchId(searchId);
            singleSearchResult.setPictures(capturedPictureList);
            singleSearchResult.setTotal(capturedPictureList.size());
            searchResult.setSearchId(searchId);
            List<SingleSearchResult> singleList = new ArrayList<>();
            singleList.add(singleSearchResult);
            searchResult.setSingleResults(singleList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    SearchResult parseResultNotOnePerson(ResultSet resultSet, SearchOption option, String searchId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, List<CapturedPicture>> mapSet = new HashMap<>();
        SearchResult searchResult = new SearchResult();
        List<SingleSearchResult> singleResultList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                //小图ftpurl
                String surl = resultSet.getString(DynamicTable.FTPURL);
                //设备id
                String ipcid = resultSet.getString(DynamicTable.IPCID);
                //相似度
                Float similaritys = resultSet.getFloat(DynamicTable.SIMILARITY);
                //时间戳
                Timestamp timestamp = resultSet.getTimestamp(DynamicTable.TIMESTAMP);
                //group id
                String id = resultSet.getString(DynamicTable.GROUP_FIELD);
                //大图ftpurl
                String burl = surlToBurl(surl);
                //图片对象
                CapturedPicture capturedPicture = new CapturedPicture();
                capturedPicture.setSurl(getFtpUrl(surl));
                capturedPicture.setBurl(getFtpUrl(burl));
                capturedPicture.setDeviceId(ipcid);
                capturedPicture.setTime(format.format(timestamp));
                capturedPicture.setSimilarity(similaritys);
                if (mapSet.containsKey(id)) {
                    mapSet.get(id).add(capturedPicture);
                } else {
                    List<CapturedPicture> pictureList = new ArrayList<>();
                    pictureList.add(capturedPicture);
                    mapSet.put(id, pictureList);
                }
            }
            searchResult.setSearchId(searchId);
            for (int i = 0; i < option.getImages().size(); i++) {
                SingleSearchResult singleSearchResult = new SingleSearchResult();
                String picId = option.getImages().get(i).getImageID();
                if (mapSet.containsKey(picId)) {
                    addDeviceName(mapSet.get(picId));
                    singleSearchResult.setPictures(mapSet.get(picId));
                    singleSearchResult.setTotal(mapSet.get(picId).size());
                    List<String> list = new ArrayList<>();
                    list.add(picId);
                    singleSearchResult.setImageNames(list);
                    singleSearchResult.setSearchId(picId);
                    singleResultList.add(singleSearchResult);
                }


            }
            searchResult.setSingleResults(singleResultList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
     * ftpUrl中的HostName转为IP
     *
     * @param ftpUrl 带HostName的ftpUrl
     * @return 带IP的ftpUrl
     */
    String getFtpUrl(String ftpUrl) {

        String hostName = ftpUrl.substring(ftpUrl.indexOf("/") + 2, ftpUrl.lastIndexOf(":"));
        String ftpServerIP = environment.getProperty(hostName);
        if (IsEmpty.strIsRight(ftpServerIP)) {
            return ftpUrl.replace(hostName, ftpServerIP);
        }
        return ftpUrl;
    }

    /**
     * 小图ftpUrl转大图ftpUrl
     *
     * @param surl 小图ftpUrl
     * @return 大图ftpUrl
     */
    String surlToBurl(String surl) {
        StringBuilder burl = new StringBuilder();
        String s1 = surl.substring(0, surl.lastIndexOf("_") + 1);
        String s2 = surl.substring(surl.lastIndexOf("."));
        burl.append(s1).append(0).append(s2);
        return burl.toString();
    }
}

