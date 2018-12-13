package com.hzgc.compare.worker.memory.manager;

import com.hzgc.compare.worker.util.FaceCompareUtil;
import org.apache.log4j.Logger;

import java.util.TimerTask;

public class ShowMemory extends TimerTask {
    private static Logger log = Logger.getLogger(MemoryManager.class);

    @Override
    public void run() {
        log.info("The size used to compare is " + FaceCompareUtil.getInstanse().getMemSize());
    }
}
