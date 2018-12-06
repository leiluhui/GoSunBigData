package com.hzgc.cloud.community.model;

import java.io.Serializable;

public class CountCommunityPeople implements Serializable {
    private Long community;
    private int count;

    public Long getCommunity() {
        return community;
    }

    public void setCommunity(Long community) {
        this.community = community;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
