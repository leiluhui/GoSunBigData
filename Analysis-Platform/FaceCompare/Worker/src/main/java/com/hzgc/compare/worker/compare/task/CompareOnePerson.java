package com.hzgc.compare.worker.compare.task;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.CompareParam;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.compare.Comparators;
import com.hzgc.compare.worker.compare.ComparatorsImpl;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.persistence.ElasticSearchClient;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import org.apache.log4j.Logger;

import java.util.List;

public class CompareOnePerson extends CompareTask {
    //    private static final Logger logger = LoggerFactory.getLogger(CompareOnePerson.class);
    private static Logger log = Logger.getLogger(CompareOnePerson.class);
    private CompareParam param;
    private String dateStart;
    private String dateEnd;


    public CompareOnePerson(CompareParam param, String dateStart, String dateEnd){
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
        List<String> ipcIds = param.getIpcIds();
        param.setIpcIds(null);
        log.info(FaceObjectUtil.objectToJson(param));
        byte[] feature1 = param.getFeatures().get(0).getFeature1();
        float[] feature2 = param.getFeatures().get(0).getFeature2();
        float sim = param.getSim();
        int resultCount = param.getResultCount();
        if (resultCount == 0){
            resultCount = resultDefaultCount;
        }
        SearchResult result;

        Comparators comparators = new ComparatorsImpl();
        List<String> ids =  comparators.compareFirst(feature1, Config.FIRST_COMPARE_RESULT_COUNT, dateStart, dateEnd);
        //根据对比结果从ES读取数据
        log.info("Read records from ES with result of first compared.");
//            List<FaceObject> objs =  client.readFromHBase(firstCompared);
        List<FaceObject> objs = ElasticSearchClient.readFromEs(ids, ipcIds);
        // 第二次对比
        log.info("Compare records second.");
        result = comparators.compareSecond(feature2, sim, objs, param.getSort());
        //取相似度最高的几个
        log.info("Take the top " + resultCount);
        result = result.take(resultCount);

        return result;
    }
}
