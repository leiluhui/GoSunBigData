package com.hzgc.jniface;

import java.io.Serializable;

public class FaceFeatureInfo implements Serializable {
    //对应被比对特征值集合的下表
    private int index;

    //分数
    private Float score;

    //汉明距离
    private int dist;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Float getScore() { return score; }

    public void setScore(Float score) { this.score = score; }

    public int getDist() { return dist; }

    public void setDist(int dist) { this.dist = dist; }
}
