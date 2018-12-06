package com.hzgc.cloud.dynrepo.dao;

import com.hzgc.compare.CompareParam;
import com.hzgc.compare.client.CompareClient;
import com.hzgc.jniface.PictureData;
import com.hzgc.cloud.dynrepo.bean.CapturedPicture;
import com.hzgc.cloud.dynrepo.bean.SearchOption;
import com.hzgc.cloud.dynrepo.bean.SearchResult;
import com.hzgc.cloud.dynrepo.bean.SingleSearchResult;
import com.hzgc.cloud.dynrepo.service.CaptureServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class FaceCompareClient {

    @Autowired
    @SuppressWarnings("unused")
    private CaptureServiceHelper captureServiceHelper;
    private CompareClient client;
    private String zkAddress;

    public FaceCompareClient(@Value("${zk.address}")String zkAddress){
        log.info("Create rpc client , zkAddress is : " + zkAddress);
        this.zkAddress = zkAddress;
        client = new CompareClient();
        client.createService(zkAddress);
    }

    private boolean reConnect(){
        log.info("Reconnect to service.");
        client = new CompareClient();
        return client.createService(zkAddress);
    }

    private void connectionCheck(){
        boolean res = client.check();
        for(int i = 0; i < 3; i ++){
            res = reConnect();
            if(res){
                break;
            }
        }
    }

    public SearchResult compare(CompareParam param, SearchOption option, String searchId){
        boolean isTheSame = option.isSinglePerson();
        SearchResult searchResult = new SearchResult();
        List<SingleSearchResult> singleList = new ArrayList<>();
        searchResult.setSearchId(searchId);
        long start = System.currentTimeMillis();
        //单人单图检索
        if(option.getImages().size() == 1){
            com.hzgc.compare.SearchResult result = client.retrievalOnePerson(param);
            log.info("Time used to compare is :" + (System.currentTimeMillis() - start));
//            result = result.take(option.getStart(), option.getLimit());
            SingleSearchResult singleSearchResult = new SingleSearchResult();
            List<CapturedPicture> capturedPictures = captureServiceHelper.getCapturedPictures(result, option);
            singleSearchResult.setPictureDatas(option.getImages());
            singleSearchResult.setSearchId(searchId);
            singleSearchResult.setPictures(capturedPictures);
            singleSearchResult.setTotal(capturedPictures.size());
            singleList.add(singleSearchResult);
            //单人多图检索
        } else if(option.getImages().size() > 1 && isTheSame){
            com.hzgc.compare.SearchResult result = client.retrievalSamePerson(param);
            log.info("Time used to compare is :" + (System.currentTimeMillis() - start));
//            result = result.take(option.getStart(), option.getLimit());
            SingleSearchResult singleSearchResult = new SingleSearchResult();
            List<CapturedPicture> capturedPictures = captureServiceHelper.getCapturedPictures(result, option);
            singleSearchResult.setPictureDatas(option.getImages());
            singleSearchResult.setSearchId(searchId);
            singleSearchResult.setPictures(capturedPictures);
            singleSearchResult.setTotal(capturedPictures.size());
            singleList.add(singleSearchResult);
            //多人多图检索
        } else if (option.getImages().size() > 1 && !isTheSame){
            Map<String, com.hzgc.compare.SearchResult> map =  client.retrievalNotSamePerson(param);
            log.info("Time used to compare is :" + (System.currentTimeMillis() - start));
            for(Map.Entry<String, com.hzgc.compare.SearchResult> entry : map.entrySet()){
                com.hzgc.compare.SearchResult searchResult1 = entry.getValue();
//                searchResult1 = searchResult1.take(option.getStart(), option.getLimit());
                SingleSearchResult singleSearchResult = new SingleSearchResult();
                List<CapturedPicture> capturedPictures = captureServiceHelper.getCapturedPictures(searchResult1, option);
                singleSearchResult.setPictures(capturedPictures);
                singleSearchResult.setTotal(capturedPictures.size());
                singleSearchResult.setSearchId(searchId);
                List<PictureData> pictureDatas = new ArrayList<>();
                for(PictureData pictureData : option.getImages()){
                    if(entry.getKey().equals(pictureData.getImageID())){
                        pictureDatas.add(pictureData);
                        break;
                    }
                }
                singleSearchResult.setPictureDatas(pictureDatas);
                singleList.add(singleSearchResult);
            }
        }
        searchResult.setSingleResults(singleList);
        return searchResult;
    }

}
