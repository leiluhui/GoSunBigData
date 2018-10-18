package com.hzgc.cluster.dispach;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.compare.CarCompare;
import com.hzgc.cluster.dispach.compare.FaceCompare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ConsumerFunction implements CommandLineRunner {
    @Autowired
    TableCache tableCache;
    @Autowired
    CaptureCache captureCache;
    @Autowired
    FaceCompare faceCompare;
    @Autowired
    CarCompare carCompare;

    @Override
    public void run(String... strings) throws Exception {
        tableCache.loadData();
        ExecutorService pool = Executors.newFixedThreadPool(6);
        pool.submit(carCompare);
        pool.submit(faceCompare);
    }
}
