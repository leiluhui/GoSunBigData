package com.hzgc.compare.worker.compare;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.Feature;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.worker.util.FaceCompareUtil;
import com.hzgc.jniface.*;
import org.apache.log4j.Logger;

import java.util.*;

public class ComparatorsImpl implements Comparators{
    private static Logger log = Logger.getLogger(ComparatorsImpl.class);

    @Override
    public List<String> compareFirst(byte[] feature, int num, String dateStart, String dateEnd) {
        List<String> rowkeys = new ArrayList<>();
        byte[][] queryList = new byte[1][32];
        queryList[0] = feature;
        long start = System.currentTimeMillis();
        ArrayList<CompareRes> array = FaceCompareUtil.getInstanse().faceCompareBit(queryList, 1000);
        log.info("The time call function used is : " + (System.currentTimeMillis() - start));
        List<FeatureInfo> featureInfos = array.get(0).getFeatureInfoList();
        featureInfos.sort((o1, o2) -> o2.getDist() - o1.getDist());
        int count = 0;
        for(FeatureInfo featureInfo : featureInfos){
            String[] s = featureInfo.getKey().split("_");
            if(s[0] .compareTo(dateStart) >= 0 && s[0].compareTo(dateEnd) <= 0){
                rowkeys.add(s[1]);
                count ++;
            }
            if(count >= num){
                break;
            }
        }
        log.info("The time first compare used is : " + (System.currentTimeMillis() - start));
        return rowkeys;
    }

    public List<String> compareFirstNotSamePerson(List<Feature> features, int num, String dateStart, String dateEnd){
        int index = 0;
        byte[][] queryList = new byte[features.size()][32];
        for (Feature feature : features){
            queryList[index] = feature.getFeature1();
            index ++;
        }
        long insertData = System.currentTimeMillis();
        ArrayList<CompareRes> array = FaceCompareUtil.getInstanse().faceCompareBit(queryList, 1000);
        log.info("The time call function used is : " + (System.currentTimeMillis() - insertData));
        List<String> rowkeys = new ArrayList<>();
        for(CompareRes compareResult : array){
            List<FeatureInfo> featureInfos = compareResult.getFeatureInfoList();
            featureInfos.sort((o1, o2) -> o2.getDist() - o1.getDist());
            int count = 0;
            for(FeatureInfo featureInfo : featureInfos){
                String[] s = featureInfo.getKey().split("_");
                if(s[0] .compareTo(dateStart) >= 0 && s[0].compareTo(dateEnd) <= 0){
                    rowkeys.add(s[1]);
                    count ++;
                }
                if(count >= num){
                    break;
                }
            }
        }
        log.info("The time first compare used is : " + (System.currentTimeMillis() - insertData));
        return rowkeys;
    }

    @Override
    public List<String> compareFirstTheSamePerson(List<byte[]> features, int num, String dateStart, String dateEnd) {
        Long start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();

        int index = 0;
        byte[][] queryList = new byte[features.size()][32];
        for (byte[] feature : features){
            queryList[index] = feature;
            index ++;
        }
        Set<String> set = new HashSet<>();
        index = 0;
        ArrayList<CompareRes> array = FaceCompareUtil.getInstanse().faceCompareBit(queryList, 1000);
        log.info("The time call function used is : " + (System.currentTimeMillis() - start));
        for(CompareRes compareResult : array){
            List<String> rowkeys = new ArrayList<>();
            int count = 0;
            List<FeatureInfo> featureInfos = compareResult.getFeatureInfoList();
            featureInfos.sort((o1, o2) -> o2.getDist() - o1.getDist());
            for(FeatureInfo featureInfo : featureInfos){
                String[] s = featureInfo.getKey().split("_");
                if(s[0] .compareTo(dateStart) >= 0 && s[0].compareTo(dateEnd) <= 0){
                    rowkeys.add(s[1]);
                    count ++;
                }
                if(count >= num){
                    break;
                }
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
        result.filterBySim(sim);
        if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))){
            result.sort(sorts);
        } else {
            result.sortBySim();
        }
        log.info("The time used to sort is : " + (System.currentTimeMillis() - compared));
        log.info("Result size of sencond compare is : " + result.getRecords().length);
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
        result.filterBySim(sim);
        if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))) {
            result.sort(sorts);
        } else {
            result.sortBySim();
        }
        log.info("The time used to sort is : " + (System.currentTimeMillis() - compared));
        log.info("Result size of sencond compare is : " + result.getRecords().length);
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
            searchResult.filterBySim(sim);
            if(sorts != null && (sorts.size() > 1 || (sorts.size() == 1 && sorts.get(0) != 4))) {
                searchResult.sort(sorts);
            } else {
                searchResult.sortBySim();
            }
            res.put(feature.getId(), searchResult);
        }
        log.info("The time second compare used is : " + (System.currentTimeMillis() - start));

        return res;
    }
}
