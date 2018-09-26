package com.hzgc.compare.worker.compare.task;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.CompareParam;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.compare.Comparators;
import com.hzgc.compare.worker.compare.ComparatorsImpl;
import com.hzgc.compare.worker.persistence.ElasticSearchClient;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import javafx.util.Pair;
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
        System.out.println(FaceObjectUtil.objectToJson(param));
        byte[] feature1 = param.getFeatures().get(0).getFeature1();
        float[] feature2 = param.getFeatures().get(0).getFeature2();
        float sim = param.getSim();
        int resultCount = param.getResultCount();
        if (resultCount == 0){
            resultCount = resultDefaultCount;
        }
        SearchResult result;
//        HBaseClient client = new HBaseClient();
//        ElasticSearchClient client = new ElasticSearchClient();
        Comparators comparators = new ComparatorsImpl();
        // 根据条件过滤
        log.info("To filter the records from memory.");
        List<Pair<String, byte[]>> dataFilterd =  comparators.filter(dateStart, dateEnd);
        if(dataFilterd.size() == 0){
            return new SearchResult();
        }
        if(dataFilterd.size() > hbaseReadMax){
            // 若过滤结果太大，则需要第一次对比
            log.info("The result of filter is too bigger , to compare it first.");
            List<String> ids =  comparators.compareFirst(feature1, hbaseReadMax, dataFilterd);
            //根据对比结果从HBase读取数据
            log.info("Read records from ES with result of first compared.");
//            List<FaceObject> objs =  client.readFromHBase(firstCompared);
            List<FaceObject> objs = ElasticSearchClient.readFromEs(ids);
            // 第二次对比
            log.info("Compare records second.");
            result = comparators.compareSecond(feature2, sim, objs, param.getSort());
            //取相似度最高的几个
            log.info("Take the top " + resultCount);
            result = result.take(resultCount);
        }else {
            //若过滤结果比较小，则直接进行第二次对比
            log.info("Read records from ES with result of filter.");
//            List<FaceObject> objs = client.readFromHBase2(dataFilterd);
            List<FaceObject> objs = ElasticSearchClient.readFromEs2(dataFilterd);
//            System.out.println("过滤结果" + objs.size() + " , " + objs.get(0));
            log.info("Compare records second directly.");
            result = comparators.compareSecond(feature2, sim, objs, param.getSort());
            //取相似度最高的几个
            log.info("Take the top " + resultCount);
            result = result.take(resultCount);
        }
//        System.out.println("对比结果2" + result.getRecords().length + " , " + result.getRecords()[0]);
        return result;
    }
}
