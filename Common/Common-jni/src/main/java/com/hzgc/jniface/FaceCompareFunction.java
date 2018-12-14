package com.hzgc.jniface;

import java.io.Serializable;
import java.util.ArrayList;

public class FaceCompareFunction implements Serializable {
    static {
//        System.loadLibrary("JNILIB");
        System.load("/opt/GsFaceCompare/lib/libjniFaceCompareFunction.so");
    }

    public static native void init(int pre_alloc_num, int step_alloc_num);

    public static native void uninit();

    public static native int getMemSize();

    public static native int setBitFeatures(String[] keys, byte[][] bitFeatures);

    public static native ArrayList<CompareRes> faceCompareBit(byte[][] queryList, int topN);
}
