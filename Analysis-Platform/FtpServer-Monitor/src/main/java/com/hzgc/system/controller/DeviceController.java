package com.hzgc.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzgc.system.domain.DeviceDO;
import com.hzgc.system.service.DeviceService;
import com.hzgc.common.utils.PageUtils;
import com.hzgc.common.utils.Query;
import com.hzgc.common.utils.R;

/**
 * 设备表
 * created by liang on 2018/12/12
 */
@Controller
@RequestMapping("/sys/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@GetMapping()
	@RequiresPermissions("sys:machine:machine")
	String Device(){
	    return "system/device/device";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:machine:machine")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DeviceDO> deviceList = deviceService.list(query);
		int total = deviceList.size();
		PageUtils pageUtils = new PageUtils(deviceList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("sys:machine:add")
	String add(){
	    return "system/device/add";
	}


	@GetMapping("/edit/{deviceId}")
	@RequiresPermissions("sys:machine:edit")
	String edit(@PathVariable("deviceId") String deviceId,Model model){
		DeviceDO device = deviceService.get(deviceId);
		model.addAttribute("device", device);
	    return "system/device/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:machine:add")
	public R save( DeviceDO device){
		if(deviceService.save(device)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:machine:edit")
	public R update( DeviceDO device){
		deviceService.update(device);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("sys:machine:remove")
	public R remove( String deviceId){
		if(deviceService.remove(deviceId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:machine:batchRemove")
	public R remove(@RequestParam("ids[]") String[] deviceIds){
		deviceService.batchRemove(deviceIds);
		return R.ok();
	}
	
}
