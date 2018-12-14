package com.hzgc.jniface;

public class FeatureInfo {
    //对应被比对特征值集合相对应的esId
    private String key;

    //分数
    private Float score;

    //汉明距离
    private int dist;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
}
