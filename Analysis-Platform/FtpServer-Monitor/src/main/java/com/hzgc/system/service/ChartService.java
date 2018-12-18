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
public interface ChartService {


	List<Long> getTodayNumber();



	List<Map<String, Object>> findTotalNum();

	List<Map<String, Object>> findTodayNum();

	Map<String, Object> findTodayLineNum();
}
