package com.hzgc.service.dynrepo.dao;

import com.hzgc.service.dynrepo.bean.SearchCollection;
import com.hzgc.service.dynrepo.bean.SearchResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class MemoryDao {
    private Map<String, SearchCollection> searchCollectionMap = new ConcurrentHashMap<>();
    private List<ResultInfo> resultInfoList = new CopyOnWriteArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public boolean insertSearchRes(SearchCollection collection) {
        try {
            lock.lock();
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setSearhcId(collection.getSearchResult().getSearchId());
            resultInfo.setTimeStamp(System.currentTimeMillis());
            if (searchCollectionMap.size() <= 100) {
                log.info("Insert search result into memeory, current collection size is:{}", searchCollectionMap.size());
                resultInfoList.add(resultInfo);
                searchCollectionMap.put(resultInfo.getSearhcId(), collection);
            } else {
                log.warn("Current search result is too much, remove last search result");
                removeLastResult();
                resultInfoList.add(resultInfo);
                searchCollectionMap.put(resultInfo.getSearhcId(), collection);
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    public SearchResult getSearchRes(String searchId) {
        try {
            lock.lock();
            if (searchCollectionMap.containsKey(searchId)) {
                log.info("Get search result successfull, search id is:{}", searchId);
                return searchCollectionMap.get(searchId).getSearchResult();
            } else {
                log.warn("Search result is not exists, search id is:{}", searchId);
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    private void removeLastResult() {
        for (int i = 0; i < 50; i++) {
            ResultInfo resultInfo = resultInfoList.get(0);
            searchCollectionMap.remove(resultInfo.getSearhcId());
            resultInfoList.remove(0);
        }
    }
}

@Data
class ResultInfo {
    String searhcId;
    long timeStamp;
}


