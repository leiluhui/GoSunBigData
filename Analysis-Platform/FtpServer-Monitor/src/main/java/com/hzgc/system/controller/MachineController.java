package com.hzgc.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hzgc.common.controller.BaseController;
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

import com.hzgc.system.domain.MachineDO;
import com.hzgc.system.service.MachineService;
import com.hzgc.common.utils.PageUtils;
import com.hzgc.common.utils.Query;
import com.hzgc.common.utils.R;

/**
 * 物理机器
 * 
 * @author liang
 * @email 1992lcg@163.com
 * @date 2018-12-07 18:35:53
 */
 
@Controller
@RequestMapping("/sys/machine")
public class MachineController  extends BaseController {
	@Autowired
	private MachineService machineService;

	@GetMapping("")
	@RequiresPermissions("sys:machine:machine")
	String Machine(){
	    return "system/machine/machine";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:machine:machine")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<MachineDO> machineList = machineService.list(query);
		int total = machineService.count(query);
		PageUtils pageUtils = new PageUtils(machineList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("sys:machine:add")
	String add(){
	    return "system/machine/add";
	}


//	sys/machine/edit/139
	@GetMapping("/edit/{machineId}")
	@RequiresPermissions("sys:machine:edit")
	String edit(@PathVariable("machineId") Integer machineId,Model model){
		MachineDO machine = machineService.get(machineId);
		model.addAttribute("machine", machine);
	    return "system/machine/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("sys:machine:add")
	public R save( MachineDO machine){

		final Date date = new Date();
		machine.setGmtCreate(date);
		machine.setGmtModified(date);
		machine.setUserIdCreate(getUserId());
		if(machineService.save(machine)>0){
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
	public R update( MachineDO machine){
		machineService.update(machine);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("sys:machine:remove")
	public R remove( Integer machineId){
		if(machineService.remove(machineId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("sys:machine:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] machineIds){
		machineService.batchRemove(machineIds);
		return R.ok();
	}
	
}
