package com.hzgc.service.clustering.service;

import com.hzgc.common.faceclustering.table.PeopleSchedulerTable;
import com.hzgc.common.faceclustering.table.PersonRegionTable;
import com.hzgc.common.hbase.HBaseHelper;
import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.DeviceQueryService;
import com.hzgc.common.util.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Component
public class ScheduledService {

    @Autowired
    private DeviceQueryService deviceQueryService;

    @Scheduled(cron = "*/5 * * * * *")
    public void UpdateRegion(){
        log.info("Start to update the region tables!!!");
        Table peoplescheduler = HBaseHelper.getTable(PeopleSchedulerTable.TABLE_NAME);
        Table personRegion = HBaseHelper.getTable(PersonRegionTable.TABLE_NAME);
        Scan scan = new Scan();
        try {
            ResultScanner resultScanner = peoplescheduler.getScanner(scan);
            for (Result result : resultScanner){
                String regionId = Bytes.toString(result.getRow());
                log.info("RegionId is : " + regionId);
                List<String> ipcidList = getIpcIds(Long.valueOf(regionId));
                log.info("IpcidList is : " + ipcidList);
                String regionName = Bytes.toString(result.getValue(PeopleSchedulerTable.COLUMNFAMILY, PeopleSchedulerTable.REGIONNAME));
                Put put = new Put(Bytes.toBytes(regionId));
                put.addColumn(PersonRegionTable.COLUMNFAMILY,PersonRegionTable.REGION_NAME,Bytes.toBytes(regionName));
                put.addColumn(PersonRegionTable.COLUMNFAMILY,PersonRegionTable.REGION_IPCIDS,Bytes.toBytes(JSONUtil.toJson(ipcidList)));
                personRegion.put(put);
            }
            log.info("Update the region tables successfully!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  List<String> getIpcIds(Long areaId) {
        log.info("aaaaaaaaaaaaaa");
        log.info("bbbbbbbbbbbbbb");
        log.info("areaId is : " + areaId);
        List<Long> deviceIdList = deviceQueryService.query_device_id(areaId, "area");
        log.info("DeviceIdList's size is  : " + deviceIdList.size());
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
        log.info("IpcIdList is : " + ipcIdList);
        return ipcIdList;
    }
}
