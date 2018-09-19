package com.hzgc.compare.worker.persistence.task;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.worker.common.FaceInfoTable;
import com.hzgc.compare.worker.common.tuple.Triplet;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.memory.cache.MemoryCacheImpl;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import com.hzgc.compare.worker.util.HBaseHelper;
import com.hzgc.compare.worker.util.UuidUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 定期读取内存中的recordToHBase，保存在HBase中，并生成元数据保存入内存的buffer
 */
public class TimeToWrite implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(TimeToWrite.class);
    private Long timeToWrite = 1000L; //任务执行时间间隔，默认1秒

    public TimeToWrite(){
        this.timeToWrite = Config.WORKER_HBASE_WRITE_TIME;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(timeToWrite);
                writeToHBase();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToHBase(){
//        logger.info("To Write record into HBase.");
        MemoryCacheImpl cache = MemoryCacheImpl.getInstance();
        List<FaceObject> recordToHBase = cache.getObjects();
        if(recordToHBase.size() == 0){
            return;
        }
        logger.info("The record num from kafka is :" + recordToHBase.size());
        long start = System.currentTimeMillis();
        List<Triplet<String, String, byte[]>> bufferList = new ArrayList<>();
        try {
            List<Put> putList = new ArrayList<>();
            Table table = HBaseHelper.getTable(FaceInfoTable.TABLE_NAME);
            for (FaceObject record : recordToHBase) {
                String rowkey = record.getDate().replace("-", "") + record.getIpcId() + UuidUtil.generateShortUuid();
                Put put = new Put(Bytes.toBytes(rowkey));
//                put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.INFO, Bytes.toBytes(FaceObjectUtil.objectToJson(record)));
                if(record.getIpcId() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.IPCID, Bytes.toBytes(record.getIpcId()));
                }
                if(record.getIpcId() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.TIMESTAMP, Bytes.toBytes(record.getTimeStamp()));
                }
                if(record.getIpcId() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.DATE, Bytes.toBytes(record.getDate()));
                }
                put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.TIME_SLOT, Bytes.toBytes(record.getTimeSlot()));
                if(record.getAttribute() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.ATTRIBUTE, Bytes.toBytes(FaceObjectUtil.objectToJson(record.getAttribute())));
                }
                if(record.getSurl() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.SURL, Bytes.toBytes(record.getSurl()));
                }
                if(record.getBurl() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.BURL, Bytes.toBytes(record.getBurl()));
                }
                if(record.getRelativePath() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.RELATIVE_PATH, Bytes.toBytes(record.getRelativePath()));
                }
                if(record.getRelativePath_big() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.RELATIVE_PATH_BIG, Bytes.toBytes(record.getRelativePath_big()));
                }
                if(record.getIp() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.IP, Bytes.toBytes(record.getIp()));
                }
                if(record.getHostname() != null) {
                    put.addColumn(FaceInfoTable.CLU_FAMILY, FaceInfoTable.HOSTNAME, Bytes.toBytes(record.getHostname()));
                }

                putList.add(put);
                Triplet<String, String, byte[]> bufferRecord =
                        new Triplet<>(record.getDate(), rowkey, record.getAttribute().getBitFeature());
                bufferList.add(bufferRecord);
            }
            table.put(putList);
            logger.info("Put record to hbase success .");
            logger.info("The Time Used to write to HBase is : " + (System.currentTimeMillis()  - start));
            cache.addBuffer(bufferList);
        } catch (IOException e) {
            e.printStackTrace();
            cache.addFaceObjects(recordToHBase);
            logger.error("Put records to hbase faild.");
        }
    }
}
