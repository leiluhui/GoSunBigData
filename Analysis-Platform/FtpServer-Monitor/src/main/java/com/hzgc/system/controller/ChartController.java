package com.hzgc.system.controller;

import com.hzgc.common.controller.BaseController;
import com.hzgc.common.utils.PageUtils;
import com.hzgc.common.utils.Query;
import com.hzgc.common.utils.R;
import com.hzgc.system.domain.MachineDO;
import com.hzgc.system.service.MachineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物理机器
 * 
 * @author liang
 * @email 1992lcg@163.com
 * @date 2018-12-07 18:35:53
 */
 
@Controller
@RequestMapping("/sys/chart")
public class ChartController extends BaseController {
	@Autowired
	private MachineService machineService;

	@GetMapping("")
	@RequiresPermissions("sys:chart:chart")
	String Machine(Model model){
		Map<String, Object> query = new HashMap<>();
		int total = machineService.count(query);
		model.addAttribute("ftpNumber", total);
	    return "system/chart/chart";
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
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sys:machine:edit")
	public R update( MachineDO machine){
		machineService.update(machine);
		return R.ok();
	}


}
