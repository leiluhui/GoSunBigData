package com.hzgc.system.service.impl;

import com.hzgc.system.config.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.hzgc.system.dao.MachineDao;
import com.hzgc.system.domain.MachineDO;
import com.hzgc.system.service.MachineService;



@Service
public class MachineServiceImpl implements MachineService {
	@Autowired
	private MachineDao machineDao;

	@Autowired
	private ZkClient zkClient;
	
	@Override
	public MachineDO get(Integer machineId){
		return machineDao.get(machineId);
	}
	
	@Override
	public List<MachineDO> list(Map<String, Object> map){

		boolean existNode = zkClient.isExistNode("/static_compare_service");
		boolean existNode1 = zkClient.isExistNode("/liang");
		System.err.println(existNode);
		System.err.println(existNode1);
		return machineDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return machineDao.count(map);
	}
	
	@Override
	public int save(MachineDO machine){
		return machineDao.save(machine);
	}
	
	@Override
	public int update(MachineDO machine){
		return machineDao.update(machine);
	}
	
	@Override
	public int remove(Integer machineId){
		return machineDao.remove(machineId);
	}
	
	@Override
	public int batchRemove(Integer[] machineIds){
		return machineDao.batchRemove(machineIds);
	}
	
}
