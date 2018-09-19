package com.hzgc.compare.worker.persistence;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.compare.worker.common.FaceInfoTable;
import com.hzgc.compare.worker.persistence.task.TimeToWrite;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import com.hzgc.compare.worker.util.HBaseHelper;
import javafx.util.Pair;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责与HBas交互，定期插入数据，以及读取第一次比较结果
 */
public class HBaseClient {
    private static final Logger logger = LoggerFactory.getLogger(HBaseClient.class);

    /**
     * 启动任务，定期读取内存中的recordToHBase，保存在HBase中，并生成元数据保存入内存的buffer
     */
    public void timeToWrite(){
        logger.info("Start a time task to deal with records from recordToHBase.");
        TimeToWrite task = new TimeToWrite();
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * 根据第一次比较的结果，查询HBase中的数据
     * @param rowkeys 第一次对比的结果，rowkey集合
     * @return 返回结果集
     */
    public List<FaceObject> readFromHBase(List<String> rowkeys){
        logger.info("The size of rowkeys is " + rowkeys.size());
        List<FaceObject> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            Table table = HBaseHelper.getTable(FaceInfoTable.TABLE_NAME);
            long getTable = System.currentTimeMillis();
            logger.info("The time used to get table is : " + (getTable - start));
            List<Get> gets = new ArrayList<>();
            for(String rowkey : rowkeys){
                gets.add(new Get(Bytes.toBytes(rowkey)));
            }
            Result[]  results = table.get(gets);
            for (Result result : results){//对返回的结果集进行操作
                list.add(analyseResult(result));
//                for (Cell kv : result.rawCells()) {
//                    FaceObject value = FaceObjectUtil.jsonToObject(Bytes.toString(CellUtil.cloneValue(kv))) ;
//                    list.add(value);
//                }
            }
            logger.info("The time used to get data from hbase is : " + (System.currentTimeMillis() - getTable));
            logger.info("The size of result is " + list.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据过滤结果，查询HBase中的数据
     * @param records 过滤后的内存数据
     * @return 返回结果集
     */
    public List<FaceObject> readFromHBase2(List<Pair<String, byte[]>> records){
        logger.info("The size of records is " + records.size());
        List<FaceObject> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        try {
            Table table = HBaseHelper.getTable(FaceInfoTable.TABLE_NAME);
            if(table == null){
                logger.warn(" Get the table " + FaceInfoTable.TABLE_NAME + " faild .");
                return null;
            }
            long getTable = System.currentTimeMillis();
            logger.info("The time used to get table is : " + (getTable - start));
            List<Get> gets = new ArrayList<>();
            for(Pair<String, byte[]> record : records){
                gets.add(new Get(Bytes.toBytes(record.getKey())));
            }
            Result[]  results = table.get(gets);
            for (Result result : results){//对返回的结果集进行操作
                list.add(analyseResult(result));
//                for (Cell kv : result.rawCells()) {
//                    FaceObject value = FaceObjectUtil.jsonToObject(Bytes.toString(CellUtil.cloneValue(kv))) ;
//                    list.add(value);
//                }
            }
            logger.info("The time used to get data from hbase is : " + (System.currentTimeMillis() - getTable));
            logger.info("The size of result is " + list.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private FaceObject analyseResult(Result result){
        FaceObject value = new FaceObject();
        value.setIpcId(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.IPCID)));
        value.setTimeStamp(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.TIMESTAMP)));
        value.setDate(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.DATE)));
        value.setTimeSlot(Bytes.toInt(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.TIME_SLOT)));
        value.setAttribute(FaceObjectUtil.jsonToAttribute(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.ATTRIBUTE))));
        value.setSurl(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.SURL)));
        value.setBurl(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.BURL)));
        value.setRelativePath(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.RELATIVE_PATH)));
        value.setRelativePath_big(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.RELATIVE_PATH_BIG)));
        value.setIp(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.IP)));
        value.setHostname(Bytes.toString(result.getValue(FaceInfoTable.CLU_FAMILY, FaceInfoTable.HOSTNAME)));
        return value;
    }

//    /**
//     * 对比结束，根据结果查询HBase数据
//     * @param compareRes
//     * @return
//     */
//    public SearchResult readFromHBase2(SearchResult compareRes){
//        logger.info("The size of compareRes is " + compareRes.getRecords().length);
//        long start = System.currentTimeMillis();
//        Connection conn = HBaseHelper.getHBaseConnection();
//        try {
//            Table table = conn.getTable(TableName.valueOf(FaceInfoTable.TABLE_NAME));
//            List<Get> gets = new ArrayList<>();
//            for(SearchResult.Record record : compareRes.getRecords()){
//                gets.add(new Get(Bytes.toBytes((String) record.getValue())));
//            }
//            Result[]  results = table.get(gets);
//            int index = 0;
//            for (Result result : results){//对返回的结果集进行操作
//                if(result.rawCells() == null || result.rawCells().length == 0 ){
//                    logger.warn("This Object From HBase is Null");
//                }
//                for (Cell kv : result.rawCells()) {
//                    FaceObject object = FaceObjectUtil.jsonToObject(Bytes.toString(CellUtil.cloneValue(kv))) ;
//                    String rowkey = Bytes.toString(CellUtil.cloneRow(kv));
//                    if(! rowkey.equals(compareRes.getRecords()[index].getValue())){
//                        logger.warn("Get data from HBase error.");
//                    }
//                    compareRes.getRecords()[index] = new SearchResult.Record(compareRes.getRecords()[index].getKey(), object);
//                }
//                index ++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        logger.info("The time used to get result is : " + (System.currentTimeMillis() - start));
//        return compareRes;
//    }

    //    /**
//     * 根据第一次比较的结果，查询HBase中的数据
//     * @param data
//     * @return
//     */
//    public Map<Feature, List<FaceObject>> readFromHBase(Map<Feature, List<String>> data){
//        Map<Feature, List<FaceObject>> result = new HashMap<>();
//        Table table = HBaseHelper.getTable(FaceInfoTable.TABLE_NAME);
//        if(table == null){
//            logger.warn(" Get the table " + FaceInfoTable.TABLE_NAME + " faild .");
//            return null;
//        }
//        List<Get> gets = new ArrayList<>();
//        for(Map.Entry<Feature, List<String>> entry : data.entrySet()){
//            List<String> rowkeys = entry.getValue();
//            for(String rowkey : rowkeys){
//                Get get = new Get(Bytes.toBytes(rowkey));
//                if(!gets.contains(get)) {
//                    gets.add(get);
//                }
//            }
//        }
//        Map<String, FaceObject> temp = new HashMap<>();
//        Result[]  results = new Result[0];
//        try {
//            results = table.get(gets);
//            for (Result res : results){//对返回的结果集进行操作
//                for (Cell kv : res.rawCells()) {
//                    FaceObject value = FaceObjectUtil.jsonToObject(Bytes.toString(CellUtil.cloneValue(kv))) ;
//                    String key = Bytes.toString(CellUtil.cloneRow(kv));
//                    temp.put(key, value);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for(Map.Entry<Feature, List<String>> entry : data.entrySet()){
//            List<String> rowkeys = entry.getValue();
//            for(String rowkey : rowkeys){
//                FaceObject face = temp.get(rowkey);
//                List<FaceObject> list = result.get(entry.getKey());
//                if(list == null){
//                    list = new ArrayList<>();
//                    result.put(entry.getKey(), list);
//                }
//                list.add(face);
//            }
//        }
//
//        return result;
//    }
}
