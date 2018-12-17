package com.hzgc.system.controller;

import com.hzgc.charts.domain.Car;
import com.hzgc.charts.service.CarService;
import com.hzgc.charts.service.FaceService;
import com.hzgc.charts.service.PersonService;
import com.hzgc.common.controller.BaseController;
import com.hzgc.common.utils.PageUtils;
import com.hzgc.common.utils.Query;
import com.hzgc.common.utils.R;
import com.hzgc.system.domain.MachineDO;
import com.hzgc.system.service.ChartService;
import com.hzgc.system.service.MachineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 图形
 * created by liang on 2018/12/12
 */
@Controller
@RequestMapping("/sys/chart")
public class ChartController extends BaseController {
	@Autowired
	private MachineService machineService;

	@Autowired
	private CarService carService;

	@Autowired
	private PersonService personService;

	@Autowired
	private FaceService faceService;

	@Autowired
	private ChartService chartService;

	@GetMapping("")
	@RequiresPermissions("sys:chart:chart")
	String Machine(Model model){

		Map<String, Object> query = new HashMap<>();
		int total = machineService.count(query);
		model.addAttribute("ftpNum", total);

		List<Long> todayNumber = chartService.getTodayNumber();


		System.err.println(todayNumber);


		model.addAttribute("carTotalNum", carService.findTotalNum());

		model.addAttribute("personTotalNum", personService.findTotalNum());

		model.addAttribute("faceTotalNum", faceService.findTotalNum());
	    return "system/chart/chart";
	}


	@GetMapping("/display/{chartType}")
	@ResponseBody
	List<Map<String, Object>> viewChartsPlay(@PathVariable("chartType") String chartType){

		List<Map<String, Object>> list = new ArrayList<>();
		if(chartType.equals("COUNT_PIE")){
			list = chartService.findTotalNum();
		}else if (chartType.equals("TODAY_PIE")){
			list = chartService.findTodayNum();
		}
		return list;
	}


	@GetMapping("/display/line/{chartType}")
	@ResponseBody
	Map<String, Object> viewLineChartsPlay(@PathVariable("chartType") String chartType){

		Map<String, Object> map = new HashMap<>();

		 if(chartType.equals("COUNT_LINE")){
			map = chartService.findTodayLineNum();
		}
		return map;
	}



}
