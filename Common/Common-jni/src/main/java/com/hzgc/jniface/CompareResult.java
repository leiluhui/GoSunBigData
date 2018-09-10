package com.hzgc.jniface;

import java.util.ArrayList;

public class CompareResult {

    //调用faceCompareBit方法时传入的featureList中的下标
    private String index;

    //比对结果集合
    private ArrayList<FaceFeatureInfo> faceFeatureInfoArrayList;

    public String getIndex() { return index; }

    public void setIndex(String index) { this.index = index; }

    public ArrayList<FaceFeatureInfo> getPictureInfoArrayList() { return faceFeatureInfoArrayList; }

    public void setPictureInfoArrayList(ArrayList<FaceFeatureInfo> faceFeatureInfoArrayList) { this.faceFeatureInfoArrayList = faceFeatureInfoArrayList; }
}
