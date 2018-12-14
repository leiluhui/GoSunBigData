package com.hzgc.compare.worker.util;

import com.hzgc.jniface.CompareRes;
import com.hzgc.jniface.FaceCompareFunction;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class FaceCompareUtil {
    private ReentrantLock lock = new ReentrantLock();
    private static FaceCompareUtil compareUtil;
    private static int PRE_ALLOC_NUM = 1000;
    private static int STEP_ALLOC_NUM = 1000;

    private FaceCompareUtil(int pre_alloc_num, int step_alloc_num){
        init(pre_alloc_num, step_alloc_num);
    }

    public static FaceCompareUtil getInstanse(int pre_alloc_num, int step_alloc_num) {
        if(compareUtil == null){
            compareUtil = new FaceCompareUtil(pre_alloc_num, step_alloc_num);
        }
        return compareUtil;
    }

    public static FaceCompareUtil getInstanse(){
        if(compareUtil == null){
            compareUtil = new FaceCompareUtil(PRE_ALLOC_NUM, STEP_ALLOC_NUM);
        }
        return compareUtil;
    }

    public void init(int pre_alloc_num, int step_alloc_num){
        lock.lock();
        try {
            FaceCompareFunction.init(pre_alloc_num, step_alloc_num);
        }finally {
            lock.unlock();
        }
    }

    public void uninit(){
        lock.lock();
        try {
            FaceCompareFunction.uninit();
        }finally {
            lock.unlock();
        }
    }

    public int getMemSize(){
        lock.lock();
        try {
            return FaceCompareFunction.getMemSize();
        }finally {
            lock.unlock();
        }
    }

    public int setBitFeatures(String[] keys, byte[][] bitFeatures){
        lock.lock();
        try {
            return FaceCompareFunction.setBitFeatures(keys, bitFeatures);
        }finally {
            lock.unlock();
        }

    }

    public ArrayList<CompareRes> faceCompareBit(byte[][] queryList, int topN){
        lock.lock();
        try {
            long start = System.currentTimeMillis();
            ArrayList<CompareRes> res = FaceCompareFunction.faceCompareBit(queryList, topN);
            System.out.println("Time : " + (System.currentTimeMillis() - start));
            return res;
        }finally {
            lock.unlock();
        }
    }
}
