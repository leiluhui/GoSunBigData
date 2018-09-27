package com.hzgc.compare.worker.persistence;

import java.io.Serializable;

public class RecordToFlush implements Serializable{
    private String first;
    private String second;
    private byte[] third;

    public RecordToFlush(){}

    public RecordToFlush(String first, String second, byte[] third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public byte[] getThird() {
        return third;
    }

    public void setThird(byte[] third) {
        this.third = third;
    }

}
