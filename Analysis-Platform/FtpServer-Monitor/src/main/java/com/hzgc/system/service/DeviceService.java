package com.hzgc.system.service;

import com.hzgc.system.domain.DeviceDO;

import java.util.List;
import java.util.Map;

/**
 * 设备表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2018-12-11 14:15:03
 */
public interface DeviceService {
	
	DeviceDO get(String deviceId);
	
	List<DeviceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeviceDO device);
	
	int update(DeviceDO device);
	
	int remove(String deviceId);
	
	int batchRemove(String[] deviceIds);
}
