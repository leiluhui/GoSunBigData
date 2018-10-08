package com.hzgc.service.fusion.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImsiFusion implements CommandLineRunner{

    @Autowired
    private Fusion fusion;

    @Override
    public void run(String... strings) throws Exception {
        fusion.initConsumer();
        Thread thread = new Thread(fusion);
        thread.start();
    }
}
