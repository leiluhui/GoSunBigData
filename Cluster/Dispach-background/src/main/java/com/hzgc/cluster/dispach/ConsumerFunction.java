package com.hzgc.cluster.dispach;

import com.hzgc.cluster.dispach.cache.CaptureCache;
import com.hzgc.cluster.dispach.cache.TableCache;
import com.hzgc.cluster.dispach.compare.CarCompare;
import com.hzgc.cluster.dispach.compare.FaceCompare;
import com.hzgc.cluster.dispach.compare.MacCompare;
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
    @Autowired
    MacCompare macCompare;

    @Override
    public void run(String... strings) throws Exception {
        tableCache.loadData();
        ExecutorService pool = Executors.newFixedThreadPool(6);
        pool.submit(carCompare);
        pool.submit(faceCompare);
        pool.submit(macCompare);
    }
}
