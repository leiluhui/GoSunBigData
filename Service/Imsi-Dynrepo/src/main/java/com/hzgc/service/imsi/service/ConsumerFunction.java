//package com.hzgc.service.imsi.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class ConsumerFunction implements CommandLineRunner {
//
//    @Autowired
//    private ConsumerImsi consumerImsi;
//    @Override
//    public void run(String... strings) throws Exception {
//        //开启消费者
//        Thread thread = new Thread(consumerImsi);
//        thread.start();
//    }
//}
