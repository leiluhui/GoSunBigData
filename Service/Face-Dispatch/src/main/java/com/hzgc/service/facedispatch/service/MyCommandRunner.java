package com.hzgc.service.facedispatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandRunner implements CommandLineRunner {
    @Autowired
    FaceFeatureMemoryCache faceFeatureMemoryCache;
    @Override
    public void run(String... strings){
       faceFeatureMemoryCache.writeMemory();
    }
}
