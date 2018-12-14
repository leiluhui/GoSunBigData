package com.hzgc.jniface;

import java.util.List;

public class CompareRes {
    //调用faceCompareBit方法时传入的featureList中的下标
    private String index;
    private List<FeatureInfo> featureInfoList;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<FeatureInfo> getFeatureInfoList() {
        return featureInfoList;
    }

    public void setFeatureInfoList(List<FeatureInfo> featureInfoList) {
        this.featureInfoList = featureInfoList;
    }
}
