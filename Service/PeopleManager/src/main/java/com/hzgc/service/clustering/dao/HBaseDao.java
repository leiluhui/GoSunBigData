package com.hzgc.service.clustering.dao;

import com.google.gson.Gson;
import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.faceclustering.ClusteringAttribute;
import com.hzgc.common.faceclustering.table.ClusteringTable;
import com.hzgc.common.faceclustering.table.PeopleSchedulerTable;
import com.hzgc.common.faceclustering.table.PersonRegionTable;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.DeviceQueryService;
import com.hzgc.common.util.json.JSONUtil;
import com.hzgc.common.util.object.ObjectUtil;
import com.hzgc.service.clustering.bean.export.Regular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Repository
public class HBaseDao {


    public HBaseDao() {
        HBaseHelper.getHBaseConnection();
    }

    public List<FaceObject> getAlarmInfo(List<String> rowKeys) {
        Gson gson = new Gson();
        List<Get> listGet = new ArrayList<>();
        List<FaceObject> faceList = new ArrayList<>();
        Table peopleCompareTable = HBaseHelper.getTable(ClusteringTable.TABLE_PEOPLECOMPARE);
        try {
            for (String rowKey : rowKeys) {
                Get get = new Get(Bytes.toBytes(rowKey));
                listGet.add(get);
            }
            Result[] results = peopleCompareTable.get(listGet);
            for(Result result : results) {
                String json = Bytes.toString(result.getValue(ClusteringTable.PEOPELCOMPARE_COLUMNFAMILY, ClusteringTable.PEOPELCOMPARE_COLUMNDATA));
                FaceObject faceObject = gson.fromJson(json, FaceObject.class);
                faceList.add(faceObject);
            }
            return faceList;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<ClusteringAttribute> getClustering(String region, String time, byte[] colName) {
        List<ClusteringAttribute> clusteringAttributeList = new ArrayList<>();
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + region));
        try {
            Result result = clusteringInfoTable.get(get);
            byte[] bytes = result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, colName);
            if (bytes != null) {
                clusteringAttributeList = (List<ClusteringAttribute>) ObjectUtil.byteToObject(bytes);
            } else {
                log.info("No clustering in the database to be delete");
                return clusteringAttributeList;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return clusteringAttributeList;
    }

    public boolean putClustering(String region, String time, byte[] colName, List<ClusteringAttribute> clusteringAttributeList) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Put put = new Put(Bytes.toBytes(time + "-" + region));
        try {
            byte[] clusteringInfoByte = ObjectUtil.objectToByte(clusteringAttributeList);
            put.addColumn(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, colName, clusteringInfoByte);
            clusteringInfoTable.put(put);
            return true;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> detailClusteringSearch_Hbase(String clusterId, String time) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_DETAILINFO);
        Get get = new Get(Bytes.toBytes(time + "-" + clusterId));
        List<Integer> alarmInfoList = new ArrayList<>();
        try {
            Result result = clusteringInfoTable.get(get);
            alarmInfoList = (List<Integer>) ObjectUtil.byteToObject(result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES));
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return alarmInfoList;
    }

    public Map<String, Integer> getTotleNum(String startTime, String endTime) {
        Table clusteringInfoTable = HBaseHelper.getTable(ClusteringTable.TABLE_ClUSTERINGINFO);
        Map<String, Integer> map = new HashMap<>();
        Scan scan = new Scan();
        scan.addColumn(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES);
        scan.setStartRow(Bytes.toBytes(startTime));
        scan.setStopRow(Bytes.toBytes(endTime));
        try {
            ResultScanner results = clusteringInfoTable.getScanner(scan);
            for (Result result : results) {
                byte[] bytes = result.getValue(ClusteringTable.ClUSTERINGINFO_COLUMNFAMILY, ClusteringTable.ClUSTERINGINFO_COLUMN_YES);
                List<ClusteringAttribute> clusteringAttributeList = (List<ClusteringAttribute>) ObjectUtil.byteToObject(bytes);
                int size = clusteringAttributeList.size();
                String rowkey = new String(result.getRow());
                map.put(rowkey, size);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Integer saveRegular(Regular regular) {
        Table clusteringInfoTable = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        String rowkey = regular.getRegionID();
        String regionName = regular.getRegionName();
        String sim = regular.getSim();
        String moveInCount = regular.getMoveInCount();
        String moveOutDays = regular.getMoveOutDays();
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME, Bytes.toBytes(regionName));
        put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.SIM, Bytes.toBytes(sim));
        put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINCOUNT, Bytes.toBytes(moveInCount));
        put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEOUTDAYS, Bytes.toBytes(moveOutDays));
        put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINLASTRUNTIME, Bytes.toBytes(time));
        try {
            clusteringInfoTable.put(put);
            log.info("Put data to hbase succeed !!!");
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public List<Regular> searchPlan(String regionID) {
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        List<Regular> regularList = new ArrayList<>();
        if (regionID == null) {
            Scan scan = new Scan();
            try {
                ResultScanner resultScanner = peoplescheduler.getScanner(scan);
                for (Result result : resultScanner) {
                    Regular regular = new Regular();
                    regular.setRegionID(Bytes.toString(result.getRow()));
                    regular.setRegionName(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME)));
                    regular.setSim(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.SIM)));
                    regular.setMoveInCount(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINCOUNT)));
                    regular.setMoveOutDays(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEOUTDAYS)));
                    regularList.add(regular);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Get get = new Get(Bytes.toBytes(regionID));
            try {
                Result result = peoplescheduler.get(get);
                Regular regular = new Regular();
                regular.setRegionID(Bytes.toString(result.getRow()));
                regular.setRegionName(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME)));
                regular.setSim(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.SIM)));
                regular.setMoveInCount(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINCOUNT)));
                regular.setMoveOutDays(Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEOUTDAYS)));
                regularList.add(regular);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return regularList;
    }

    public Integer modifyPlan(String regionID, String sim, String moveInCount, String moveOutDays) {
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        Get get = new Get(Bytes.toBytes(regionID));
        try {
            Result result = peoplescheduler.get(get);
            String moveInLastRunTime = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINLASTRUNTIME));
            String regionName = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME));
            Put put = new Put(Bytes.toBytes(regionID));
            put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME, Bytes.toBytes(regionName));
            put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.SIM, Bytes.toBytes(sim));
            put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINLASTRUNTIME, Bytes.toBytes(moveInLastRunTime));
            put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEINCOUNT, Bytes.toBytes(moveInCount));
            put.addColumn(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.MOVEOUTDAYS, Bytes.toBytes(moveOutDays));
            peoplescheduler.put(put);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public Integer deletePlan(List<String> regionID) {
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        for (String region : regionID) {
            Delete delete = new Delete(Bytes.toBytes(region));
            try {
                peoplescheduler.delete(delete);
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        }
        return 0;
    }


    /**
     * 更新PersonRegionTable
     */
    public void UpdataPersonRegionTable(){
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        Table personRegion = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME);
        Scan scan = new Scan();
        try {
            ResultScanner resultScanner = peoplescheduler.getScanner(scan);
            for (Result result : resultScanner){
                String regionId = Bytes.toString(result.getRow());
                List<String> ipcidList = getIpcIds(Long.getLong(regionId),"d");
                String regionName = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME));
                Put put = new Put(Bytes.toBytes(regionId));
                put.addColumn(PersonRegionTable.COLUMNFAMILY,PersonRegionTable.REGION_NAME,Bytes.toBytes(regionName));
                put.addColumn(PersonRegionTable.COLUMNFAMILY,PersonRegionTable.REGION_IPCIDS,Bytes.toBytes(JSONUtil.toJson(ipcidList)));
                personRegion.put(put);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getIpcIds(Long areaId, String level) {
        DeviceQueryService deviceQueryService = new DeviceQueryService();
        List<Long> deviceIdList = deviceQueryService.query_device_id(areaId, level);
        List<String> ipcIdList = new ArrayList<>();
        if (!deviceIdList.isEmpty()) {
            Map<String, DeviceDTO> deviceDTOMap = deviceQueryService.getDeviceInfoByBatchId(deviceIdList);
            for (Map.Entry<String, DeviceDTO> entry : deviceDTOMap.entrySet()) {
                String ipcId = entry.getValue().getSerial();
                if (!StringUtils.isBlank(ipcId)){
                    ipcIdList.add(ipcId);
                }
            }
        }
        return ipcIdList;
    }
}
