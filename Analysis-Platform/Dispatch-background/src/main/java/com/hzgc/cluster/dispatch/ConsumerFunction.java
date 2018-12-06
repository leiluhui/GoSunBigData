package com.hzgc.cluster.dispatch;

import com.hzgc.cluster.dispatch.cache.CaptureCache;
import com.hzgc.cluster.dispatch.cache.TableCache;
import com.hzgc.cluster.dispatch.compare.*;
import com.hzgc.common.service.api.bean.CameraQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.jniface.FaceFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    CarCompareForLive carCompareForLive;
    @Autowired
    FaceCompareForLive faceCompareForLive;
    @Autowired
    PersonCompareForLive personCompareForLive;
    @Autowired
    FaceCompareForWhite faceCompareForWhite;
    @Autowired
    private PlatformService platformService;

    @Override
    public void run(String... strings) throws Exception {
//        List<String> ipcIds = new ArrayList<>();
//        ipcIds.add("3F064E2PAG00143");
//        Map<String, CameraQueryDTO> map = platformService.getCameraInfoByBatchIpc(ipcIds);
//        System.out.println(map);
        tableCache.loadData();
        tableCache.loadDispatchWhite();
        tableCache.loadDispatchLive();
        FaceFunction.init();
//        tableCache.showCarInfo();
//        tableCache.showFaceInfo();
//        tableCache.showFeatures();
//        tableCache.showMacInfo();
        ExecutorService pool = Executors.newFixedThreadPool(7);
//        List<FaceObject> list = captureCache.getFace();
        pool.submit(carCompare); //黑名单车辆布控
        pool.submit(faceCompare); //黑名单人脸布控
        pool.submit(macCompare); //黑名单Mac布控
        pool.submit(carCompareForLive); //活体检测车辆布控
        pool.submit(faceCompareForLive); //活体检测人脸布控
        pool.submit(personCompareForLive); //活体检测行人布控
        pool.submit(faceCompareForWhite); //白名单人脸布控
    }
}
