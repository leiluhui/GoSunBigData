package com.hzgc.compare.worker.compare;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.Feature;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.jniface.CompareResult;
import com.hzgc.jniface.FaceFeatureInfo;
import com.hzgc.jniface.FaceFunction;
import com.hzgc.jniface.FaceUtil;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.util.*;

public class ComparatorsImpl implements Comparators{
//    private static final Logger logger = LoggerFactory.getLogger(ComparatorsImpl.class);
    private static Logger log = Logger.getLogger(ComparatorsImpl.class);

    public List<Pair<String, byte[]>> filter(String dateStart, String dateEnd) {
        List<Pair<String, byte[]>> result = new ArrayList<>();
        Map<String, List<Pair<String, byte[]>>> cacheRecords =
                MemoryCacheImpl.getInstance().getCacheRecords();
//        Set<Triplet<String, String, String>> temp = new HashSet<>();
//        temp.addAll(cacheRecords.keySet());
        Iterator<String> iterator =  cacheRecords.keySet().iterator();
        Long start = System.currentTimeMillis();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.compareTo(dateStart) >= 0 &&
                    key.compareTo(dateEnd) <= 0) {
                result.addAll(cacheRecords.get(key));
            }
        }
        log.info("The time used to filter is : " + (System.currentTimeMillis() - start));
        log.info("The size of filtere result is : " + result.size());
        return result;
    }

    @Override
    public List<String> compareFirst(byte[] feature, int num, List<Pair<String, byte[]>> data) {
        Long start = System.currentTimeMillis();
//        FeatureCompared.compareFirst(data, feature, num);
        List<String> rowkeys = new ArrayList<>();
        byte[][] diku = new byte[data.size()][32];
        int index = 0;
        for(Pair<String, byte[]> pair : data){
            diku[index] = pair.getValue();
            index ++;
        }

        byte[][] queryList = new byte[1][32];
        queryList[0] = feature;
        log.info("The time insert dataes when first compare used is : " + (System.currentTimeMillis() - start));
        ArrayList<CompareResult> array = FaceFunction.faceCompareBit(diku, queryList, num);
        for(FaceFeatureInfo featureInfo : array.get(0).getPictureInfoArrayList()){
            String rowkey = data.get(featureInfo.getIndex()).getKey();
            rowkeys.add(rowkey);
        }
        log.info("The time first compare used is : " + (System.currentTimeMillis() - start));
        return rowkeys;
    }

    public List<String> compareFirstNotSamePerson(List<Feature> features, int num, List<Pair<String, byte[]>> data){
        Long start = System.currentTimeMillis();
        byte[][] diku = new byte[data.size()][32];
        int index = 0;
        for(Pair<String, byte[]> pair : data){
            diku[index] = pair.getValue();
            index ++;
        }

        byte[][] queryList = new byte[features.size()][32];
        index = 0;
        for (Feature feature : features){
            queryList[index] = feature.getFeature1();
            index ++;
        }
        log.info("The time insert dataes when first compare used is : " + (System.currentTimeMillis() - start));
        FaceFunction.init();
        ArrayList<CompareResult> array = FaceFunction.faceCompareBit(diku, queryList, num);
        List<String> rowkeys = new ArrayList<>();
        for(CompareResult compareResult : array){
            for(FaceFeatureInfo faceFeatureInfo : compareResult.getPictureInfoArrayList()){
                String rowkey = data.get(faceFeatureInfo.getIndex()).getKey();
                if(!rowkeys.contains(rowkey)) {
                    rowkeys.add(rowkey);
                }
            }
        }
        log.info("The time first compare used is : " + (System.currentTimeMillis() - start));
        return rowkeys;
    }

    @Override
    public List<String> compareFirstTheSamePerson(List<byte[]> features, int num, List<Pair<String, byte[]>> data) {
        Long start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();
        byte[][] diku = new byte[data.size()][32];
        int index = 0;
        for(Pair<String, byte[]> pair : data){
            diku[index] = pair.getValue();
            index ++;
        }

        byte[][] queryList = new byte[features.size()][32];
        index = 0;
        for (byte[] feature : features){
            queryList[index] = feature;
            index ++;
        }
        Set<String> set = new HashSet<>();
        index = 0;
        log.info("The time insert dataes when first compare used is : " + (System.currentTimeMillis() - start));
        ArrayList<CompareResult> array = FaceFunction.faceCompareBit(diku, queryList, num * 3);
        for(CompareResult compareResult : array){
            List<String> rowkeys = new ArrayList<>();
            for(FaceFeatureInfo faceFeatureInfo : compareResult.getPictureInfoArrayList()){
                String rowkey = data.get(faceFeatureInfo.getIndex()).getKey();
                rowkeys.add(rowkey);
            }
            if(index == 0) {
                set.addAll(rowkeys);
            }else {
                set.retainAll(rowkeys);
            }
            index ++;
        }
        result.addAll(set);
        log.info("The time first compare used is : " + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public SearchResult compareSecond(float[] feature, float sim, List<FaceObject> datas, List<Integer> sorts) {
        if(datas == null || datas.size() == 0){
            return new SearchResult();
        }
        Long start = System.currentTimeMillis();
        SearchResult.Record[] records = new SearchResult.Record[datas.size()];
        int index = 0;
        for(FaceObject faceobj : datas){
            float[] history = faceobj.getAttribute().getFeature();
            float simple = FaceUtil.featureCompare(feature, history);
            SearchResult.Record record = new SearchResult.Record(simple, faceobj);
            records[index] = record;
            index ++;
        }

        SearchResult result = new SearchResult(records);
        long compared = System.currentTimeMillis();
        log.info("The time second compare used is : " + (compared - start));
        if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))){
            result.sort(sorts);
        } else {
            result.sortBySim();
        }
        result.filterBySim(sim);
        log.info("The time used to sort is : " + (System.currentTimeMillis() - compared));
        return result;
    }

    @Override
    public SearchResult compareSecondTheSamePerson(List<float[]> features, float sim, List<FaceObject> datas, List<Integer> sorts) {
        if(datas == null || datas.size() == 0){
            return new SearchResult();
        }
        Long start = System.currentTimeMillis();
        SearchResult.Record[] records = new SearchResult.Record[datas.size()];
        int index = 0;
        for (FaceObject faceobj : datas) {
            float simple = 100.0f;
            for(float[] feature : features) {
                float[] history = faceobj.getAttribute().getFeature();
                float temp = FaceUtil.featureCompare(feature, history);
                if(simple > temp){
                    simple = temp;
                }
            }
            SearchResult.Record record = new SearchResult.Record(simple, faceobj);
            records[index] = record;
            index++;
        }

        SearchResult result = new SearchResult(records);
        long compared = System.currentTimeMillis();
        log.info("The time second compare used is : " + (compared - start));
        if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))) {
            result.sort(sorts);
        } else {
            result.sortBySim();
        }
        result.filterBySim(sim);
        log.info("The time used to sort is : " + (System.currentTimeMillis() - compared));
        return result;
    }

    @Override
    public Map<String, SearchResult> compareSecondNotSamePerson(List<Feature> features, float sim,
                                                                List<FaceObject> datas, List<Integer> sorts) {
        if(datas == null || datas.size() == 0){
            return new HashMap<>();
        }
        Map<String, SearchResult> res = new HashMap<>();
        Long start = System.currentTimeMillis();
        for(Feature feature : features) {
            SearchResult.Record[] records = new SearchResult.Record[datas.size()];
            int index = 0;
            for (FaceObject faceobj : datas) {
                float[] history = faceobj.getAttribute().getFeature();
                float simple = FaceUtil.featureCompare(feature.getFeature2(), history);
                SearchResult.Record record = new SearchResult.Record(simple, faceobj);
                records[index] = record;
                index++;
            }
            SearchResult searchResult = new SearchResult(records);
            if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))) {
                searchResult.sort(sorts);
            } else {
                searchResult.sortBySim();
            }
            searchResult.filterBySim(sim);
            res.put(feature.getId(), searchResult);
        }
        log.info("The time second compare used is : " + (System.currentTimeMillis() - start));

        return res;
    }
}
