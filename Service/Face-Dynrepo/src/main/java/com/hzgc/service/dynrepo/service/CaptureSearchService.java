package com.hzgc.service.dynrepo.service;

import com.hzgc.service.dynrepo.bean.*;
import com.hzgc.service.dynrepo.dao.ElasticSearchDao;
import com.hzgc.service.dynrepo.dao.SparkJDBCDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Slf4j
public class CaptureSearchService {
    @Autowired
    @SuppressWarnings("unused")
    private SparkJDBCDao sparkJDBCDao;
    @Autowired
    @SuppressWarnings("unused")
    private ElasticSearchDao esDao;
    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;

    public SearchResult searchPicture(SearchOption option, String searchId) throws SQLException {
        SearchResult searchResult = null;
        ResultSet resultSet;
        long start = System.currentTimeMillis();
        SearchCallBack searchCallBack = sparkJDBCDao.searchPicture(option);
        log.info("Start search picture, execute query total time is:" + (System.currentTimeMillis() - start));
        resultSet = searchCallBack.getResultSet();
        if (resultSet != null) {
            if (option.isSinglePerson() || option.getImages().size() == 1) {
                searchResult = captureServiceHelper.parseResultOnePerson(resultSet, option, searchId);
            } else {
                searchResult = captureServiceHelper.parseResultNotOnePerson(resultSet, option, searchId);
            }
            //存储搜索历史记录
            SearchCollection collection = new SearchCollection();
            collection.setSearchOption(option);
            collection.setSearchResult(searchResult);
            if (searchResult.getSingleResults().size() > 0) {
                for (SingleSearchResult singleResult : searchResult.getSingleResults()) {
                    singleResult.setPictures(captureServiceHelper.pageSplit(singleResult.getPictures(),
                            option.getStart(),
                            option.getLimit()));
                }
            }
        } else {
            log.info("Start search picture, search result set is null");
        }
        sparkJDBCDao.closeConnection(searchCallBack.getConnection(), searchCallBack.getStatement());
        return searchResult;
    }

    /**
     * 查询设备最后一次抓拍时间
     *
     * @param ipcId 设备ID
     * @return 最后抓拍时间
     */
    public String getLastCaptureTime(String ipcId) {
        log.info("Start query last capture time, get ipcId is:" + ipcId);
        if (!StringUtils.isBlank(ipcId)) {
            String time = esDao.getLastCaptureTime(ipcId);
            log.info("Get query last capture time successful, time is:" + time);
            return time;
        }
        log.info("Get query last capture time failure, ipcId is null");
        return null;
    }

}
