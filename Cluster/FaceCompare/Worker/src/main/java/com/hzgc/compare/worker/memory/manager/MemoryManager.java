package com.hzgc.compare.worker.memory.manager;

import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.common.tuple.Pair;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class MemoryManager {
    //    private static final Logger logger = LoggerFactory.getLogger(MemoryManager.class);
    private static Logger log = Logger.getLogger(MemoryManager.class);
    private int cacheNumMax; //内存中存储数据的上限，默认值1000万，根据实际内存设置
    private Long checkTime; //内存检查时间间隔， 默认30分钟
    private long recordTimeOut; //一级过期时间，单位： 天 ， 默认12个月，为了一次就删除到0.8以下，需要根据实际情况设置好这个值
    private SimpleDateFormat sdf;
    public MemoryManager(){
        init();
    }
    /**
     * 根据conf中的参数，来设置MemeryManager需要的参数
     */
    private void init(){
        cacheNumMax = Config.WORKER_CACHE_SIZE_MAX;
        checkTime = Config.WORKER_MEMORY_CHECK_TIME;
        recordTimeOut = Config.WORKER_RECORD_TIME_OUT;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void reLoadParam(){
        init();
    }

    /**
     * 启动定期任务，检查内存数据是否达到上限，如果是，调用remove
     */
    public void startToCheck(){
        log.info("Start to check memory.");
        new Timer().schedule(new TimeToCheckMemory(this), 0, checkTime);
    }

    /**
     * 遍历内存中的缓存，删除时间超过一级过期时间的数据，并保存下当前有效时间的最小值
     * 然后检查数据，如果还是不符合要求，删除超过二级过期时间的数据，
     * 二级过期时间设置为一级过期时间减十天，以次类推
     * 直到数据量减少到阈值的80%以下
     */
    void remove() {
        log.info("To remove records time out.");
        removeTimeOut(recordTimeOut);
    }

    private void removeTimeOut(long timeOut){
        long count = 0L;
        MemoryCacheImpl cache = MemoryCacheImpl.getInstance();
        Map<String, List<Pair<String, byte[]>>> records = cache.getCacheRecords();
        List<String> keyList = new ArrayList<>();
        keyList.addAll(records.keySet());
        for(String key : keyList){
            try {
                long time = sdf.parse(key).getTime();
                if(System.currentTimeMillis() - time > timeOut * 24L * 60 * 60 * 1000){
                    records.remove(key);
                } else {
                    count += records.get(key).size();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println("The Num of Memory Cache is : " + count);
        if(count > cacheNumMax * 0.9){
            removeTimeOut(timeOut - 1);
        }
    }

    public void timeToCheckFlush(){
        log.info("Start to flush buffer");
        new Timer().schedule(new TimeToFlushBuffer(), 5000, 5000);
    }

    public void toShowMemory(){
        new Timer().schedule(new ShowMemory(), 30000, 30000);
    }

    /**
     * 判断该时间是否在内存中（与当前有效时间的最小值对比）
     * @param time
     * @return
     */
    public boolean isOutOfTime(String time){
        String oldest = "";
        MemoryCacheImpl cache = MemoryCacheImpl.getInstance();
        Map<String, List<Pair<String, byte[]>>> records = cache.getCacheRecords();
//        Set<Triplet<A1, A2, String>> keySet = new HashSet<>();
//        keySet.addAll(records.keySet());
        for(String key : records.keySet()){
            if(oldest.compareTo(key) < 0){
                oldest = key;
            }
        }
        return time.compareTo(oldest) >= 0;
    }

    public void setRecordTimeOut(int days){
        this.recordTimeOut = days;
    }
}
