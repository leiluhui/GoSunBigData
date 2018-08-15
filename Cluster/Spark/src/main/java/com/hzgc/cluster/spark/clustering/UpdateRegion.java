package com.hzgc.cluster.spark.clustering;

import com.hzgc.common.faceclustering.table.PeopleSchedulerTable;
import com.hzgc.common.faceclustering.table.PersonRegionTable;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.DeviceQueryService;
import com.hzgc.common.util.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateRegion {
    public static void main(String[] args) {
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        Table personRegion = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME);
        Scan scan = new Scan();
        try {
            ResultScanner resultScanner = peoplescheduler.getScanner(scan);
            for (Result result : resultScanner){
                String regionId = Bytes.toString(result.getRow());
                List<String> ipcidList = getIpcIds(Long.getLong(regionId),"area");
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
    private static List<String> getIpcIds(Long areaId, String level) {
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
