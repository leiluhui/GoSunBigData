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
import java.util.ArrayList;
import java.util.List;

public class CompareSamePerson extends CompareTask {
    //    private static final Logger logger = LoggerFactory.getLogger(CompareSamePerson.class);
    private static Logger log = Logger.getLogger(CompareSamePerson.class);
    private CompareParam param;
    private String dateStart;
    private String dateEnd;

    public CompareSamePerson(CompareParam param, String dateStart, String dateEnd){
        this.param = param;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public SearchResult getSearchResult(){
        return searchResult;
    }

    public boolean isEnd(){
        return isEnd;
    }

    @Override
    public SearchResult compare() {
        List<Feature> features = param.getFeatures();
        float sim = param.getSim();
        int resultCount = param.getResultCount();
        if (resultCount == 0){
            resultCount = resultDefaultCount;
        }
        List<byte[]> feature1List = new ArrayList<>();
        List<float[]> feature2List = new ArrayList<>();
        for (Feature feature : features) {
            feature1List.add(feature.getFeature1());
            feature2List.add(feature.getFeature2());
        }
        SearchResult result;
        Comparators comparators = new ComparatorsImpl();
        List<String> firstCompared = comparators.compareFirstTheSamePerson(feature1List, Config.FIRST_COMPARE_RESULT_COUNT, dateStart, dateEnd);
        //根据对比结果从HBase读取数据
        log.info("Read records from ES with result of first compared.");
        List<FaceObject> objs = ElasticSearchClient.readFromEs(firstCompared, param.getIpcIds());
        // 第二次对比
        log.info("Compare records second.");
        result = comparators.compareSecondTheSamePerson(feature2List, sim, objs, param.getSort());
        //取相似度最高的几个
        log.info("Take the top " + resultCount);
        result = result.take(resultCount);

        return result;
    }
}
