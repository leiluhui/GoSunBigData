package com.hzgc.cluster.dispach.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class BufferQueue<T> {
    private List<T> list = new ArrayList<>();
    private ReentrantLock writeLock = new ReentrantLock();

    public void push(List<T> datas){
        writeLock.lock();
        try {
            list.addAll(datas);
        }finally {
            writeLock.unlock();
        }
    }

    public void push(T data){
        writeLock.lock();
        try {
            list.add(data);
        }finally {
            writeLock.unlock();
        }
    }

    public T get() {
        writeLock.lock();
        if(list.size() == 0){
            writeLock.unlock();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        try {
            T res = list.remove(0);
            return res;
        }finally {
            writeLock.unlock();
        }
    }

}