package com.hzgc.compare.worker.compare.task;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.CompareParam;
import com.hzgc.compare.Feature;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.compare.Comparators;
import com.hzgc.compare.worker.compare.ComparatorsImpl;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.persistence.ElasticSearchClient;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareNotSamePerson implements Runnable {
    //    private static final Logger logger = LoggerFactory.getLogger(CompareNotSamePerson.class);
    private static Logger log = Logger.getLogger(CompareNotSamePerson.class);
    private int resultDefaultCount = 20;
    private CompareParam param;
    private String dateStart;
    private String dateEnd;
    private boolean isEnd = false;
    private int esReacMax = Config.FIRST_COMPARE_RESULT_COUNT / 2;
    Map<String, SearchResult> searchResult;

    public Map<String, SearchResult> getSearchResult(){
        return searchResult;
    }

    public boolean isEnd(){
        return isEnd;
    }

    public CompareNotSamePerson(CompareParam param, String dateStart, String dateEnd){
        this.param = param;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }


    public Map<String, SearchResult> compare() {
        Map<String, SearchResult> result = new HashMap<>();
        List<Feature> features = param.getFeatures();
        float sim = param.getSim();
        int resultCount = param.getResultCount();
        if (resultCount <= 0 || resultCount > 50){
            resultCount = resultDefaultCount;
        }
        // 根据条件过滤
        Comparators comparators = new ComparatorsImpl();

        List<String> ids = comparators.compareFirstNotSamePerson(features, esReacMax, dateStart, dateEnd);
        //根据对比结果从HBase读取数据
        log.info("Read records from ES  with result of first compared.");
//            List<FaceObject> objs = client.readFromHBase(Rowkeys);
        List<FaceObject> objs = ElasticSearchClient.readFromEs(ids, param.getIpcIds());
        log.info("Compare records second.");
        Map<String, SearchResult> resultTemp = comparators.compareSecondNotSamePerson(features, sim, objs, param.getSort());
        log.info("Take the top " + resultCount);
        for(Map.Entry<String, SearchResult> searchResult : resultTemp.entrySet()){
            //取相似度最高的几个
            SearchResult searchResult1 = searchResult.getValue().take(resultCount);
            result.put(searchResult.getKey(), searchResult1);
        }
        return result;
    }

    @Override
    public void run() {
        searchResult = compare();
        isEnd = true;
    }
}
