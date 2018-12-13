package com.hzgc.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hzgc.common.utils.StringUtils;
import com.hzgc.system.config.ZkClient;
import com.hzgc.system.transform.DeviceInfo;
import com.hzgc.system.transform.FtpMonitorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.hzgc.system.dao.DeviceDao;
import com.hzgc.system.domain.DeviceDO;
import com.hzgc.system.service.DeviceService;



@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	private DeviceDao deviceDao;

	private static String FTP_MONITOR = "/ftp_monitor";

	private  List<DeviceDO> deviceDOS = new ArrayList<>();

	@Autowired
	private ZkClient zkClient;
	
	@Override
	public DeviceDO get(String deviceId){

		DeviceDO deviceDO = new DeviceDO();
		for(int i = 0; i < deviceDOS.size(); i ++ ){
			if(deviceDOS.get(i).getDeviceId().equals(deviceId)){
				return deviceDOS.get(i);
			}
		}
		return deviceDO;
	}
	
	@Override
	public List<DeviceDO> list(Map<String, Object> map){
		deviceDOS.clear();

		if(zkClient.isExistNode(FTP_MONITOR) && zkClient.isExistNode(FTP_MONITOR +"/"+ map.get("machineIp"))){

			List<String> nodeList = zkClient.getChildrenNode(FTP_MONITOR + "/" + map.get("machineIp"));

			nodeList.stream().forEach(ee -> {

				String machine = zkClient.getNodeData(FTP_MONITOR + "/" + map.get("machineIp") + "/" + ee);
				if(StringUtils.isNoneBlank(machine)){
					FtpMonitorInfo ftpMonitorInfo = JSON.parseObject(machine, new TypeReference<FtpMonitorInfo>() {});
					List<DeviceInfo> deviceInfos = ftpMonitorInfo.getDeviceInfos();

						deviceInfos.stream().forEach(e -> {
							DeviceDO deviceDO = new DeviceDO(UUID.randomUUID().toString().replace("-", ""), null, null, e.getDeviceSn(), e.getDeviceType(), ftpMonitorInfo.getClientIp(), ftpMonitorInfo.getClientHostname(),ftpMonitorInfo.getContainer(), e.getLastDataTime());

							deviceDOS.add(deviceDO);
						}
					);
				}
			});
		}
		return deviceDOS;
	}
	
	@Override
	public int count(Map<String, Object> map){
		return deviceDao.count(map);
	}
	
	@Override
	public int save(DeviceDO device){
		return deviceDao.save(device);
	}
	
	@Override
	public int update(DeviceDO device){
		return deviceDao.update(device);
	}
	
	@Override
	public int remove(String deviceId){
		return deviceDao.remove(deviceId);
	}
	
	@Override
	public int batchRemove(String[] deviceIds){
		return deviceDao.batchRemove(deviceIds);
	}
	
}
