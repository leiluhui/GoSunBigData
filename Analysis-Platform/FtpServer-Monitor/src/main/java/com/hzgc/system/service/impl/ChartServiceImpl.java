package com.hzgc.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.hzgc.charts.dao.CarRepository;
import com.hzgc.charts.dao.FaceRepository;
import com.hzgc.charts.dao.PersonRepository;
import com.hzgc.charts.domain.Car;
import com.hzgc.charts.domain.Face;
import com.hzgc.charts.domain.Person;
import com.hzgc.charts.service.CarService;
import com.hzgc.charts.service.FaceService;
import com.hzgc.charts.service.PersonService;
import com.hzgc.common.utils.StringUtils;
import com.hzgc.system.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ChartServiceImpl implements ChartService {
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FaceRepository faceRepository;

	@Autowired
	private CarService carService;

	@Autowired
	private PersonService personService;

	@Autowired
	private FaceService faceService;


	public int count(Map<String, Object> map){
		String starttime = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		String endtime = DateUtil.format(DateUtil.endOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		List<Car> byTimestampBetween = carRepository.findByTimestampBetween(starttime, endtime);


		return byTimestampBetween.size();

	}

	@Override
	public List<Long> getTodayNumber() {

		ArrayList<Long> todayNumber = new ArrayList<>();

		String starttime = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		String endtime = DateUtil.format(DateUtil.endOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		List<Car> carList = carRepository.findByTimestampBetween(starttime, endtime);

		List<Person> personList = personRepository.findByTimestampBetween(starttime, endtime);

		List<Face> faceList = faceRepository.findByTimestampBetween(starttime, endtime);

		todayNumber.add(Long.parseLong(carList.size() + ""));
		todayNumber.add(Long.parseLong(personList.size() + ""));
		todayNumber.add(Long.parseLong(faceList.size() + ""));

		return todayNumber;
	}

	@Override
	public Map<String, Object> findTodayLineNum() {
		HashMap<String, Object> map = new HashMap<>();

		String  beforeHourDate0 = DateUtil.format(DateUtil.offsetHour(new Date(), 0), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate1 = DateUtil.format(DateUtil.offsetHour(new Date(), -1), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate2 = DateUtil.format(DateUtil.offsetHour(new Date(), -2), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate3 = DateUtil.format(DateUtil.offsetHour(new Date(), -3), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate4 = DateUtil.format(DateUtil.offsetHour(new Date(), -4), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate5 = DateUtil.format(DateUtil.offsetHour(new Date(), -5), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate6 = DateUtil.format(DateUtil.offsetHour(new Date(), -6), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate7 = DateUtil.format(DateUtil.offsetHour(new Date(), -7), "yyyy-MM-dd HH") +":00:00";


		String  beforeHour0 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), 0), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour1 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -1), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour2 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -2), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour3 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -3), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour4 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -4), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour5 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -5), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour6 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -6), "yyyy-MM-dd HH") +":00:00", 11, 20);


		ArrayList<String> hourDateList = new ArrayList<>();
		hourDateList.add(beforeHourDate0);
		hourDateList.add(beforeHourDate1);
		hourDateList.add(beforeHourDate2);
		hourDateList.add(beforeHourDate3);
		hourDateList.add(beforeHourDate4);
		hourDateList.add(beforeHourDate5);
		hourDateList.add(beforeHourDate6);
		hourDateList.add(beforeHourDate7);

		ArrayList<Long> carList = new ArrayList<>();

		ArrayList<Long> faceList = new ArrayList<>();

		ArrayList<Long> personList = new ArrayList<>();

		ArrayList<String> hourList = new ArrayList<>();

		for (int i = hourDateList.size() - 1; i > 0; i-- ){

			System.err.println(hourDateList.get(i));
			System.err.println(hourDateList.get(i - 1));

			List<Car> findCarList = carRepository.findByTimestampBetween(hourDateList.get(i), hourDateList.get(i - 1));
			carList.add(Long.parseLong(findCarList.size() + ""));

			List<Person> findPersonList = personRepository.findByTimestampBetween(hourDateList.get(i), hourDateList.get(i - 1));
			personList.add(Long.parseLong(findPersonList.size() + ""));

			List<Face> findFaceList = faceRepository.findByTimestampBetween(hourDateList.get(i), hourDateList.get(i - 1));
			faceList.add(Long.parseLong(findFaceList.size() + ""));

		}

		map.put("carList", carList);
		map.put("faceList", faceList);
		map.put("personList", personList);


		hourList.add(beforeHour6);
		hourList.add(beforeHour5);
		hourList.add(beforeHour4);
		hourList.add(beforeHour3);
		hourList.add(beforeHour2);
		hourList.add(beforeHour1);
		hourList.add(beforeHour0);
		map.put("hourList", hourList);


		return map;
	}

	@Override
	public List<Map<String, Object>> findTodayNum() {

		ArrayList<Long> todayNumber = new ArrayList<>();

		String starttime = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		String endtime = DateUtil.format(DateUtil.endOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
		List<Car> carList = carRepository.findByTimestampBetween(starttime, endtime);

		List<Person> personList = personRepository.findByTimestampBetween(starttime, endtime);

		List<Face> faceList = faceRepository.findByTimestampBetween(starttime, endtime);

		List<Map<String, Object>> list = new ArrayList<>();

		Map<String, Object> faceMap = new HashMap<>();
		faceMap.put("name", "人脸");
		faceMap.put("value", faceList.size());

		Map<String, Object> carMap = new HashMap<>();
		carMap.put("name", "车辆");
		carMap.put("value", carList.size());

		Map<String, Object> personMap = new HashMap<>();
		personMap.put("name", "行人");
		personMap.put("value", personList.size());

		list.add(faceMap);
		list.add(carMap);
		list.add(personMap);

		return list;

	}

	@Override
	public List<Map<String, Object>> findTotalNum() {
		List<Map<String, Object>> list = new ArrayList<>();

			Map<String, Object> faceMap = new HashMap<>();
			faceMap.put("name", "人脸");
			faceMap.put("value", faceService.findTotalNum());

			Map<String, Object> carMap = new HashMap<>();
			carMap.put("name", "车辆");
			carMap.put("value", carService.findTotalNum());

			Map<String, Object> personMap = new HashMap<>();
			personMap.put("name", "行人");
			personMap.put("value", personService.findTotalNum());

			list.add(faceMap);
			list.add(carMap);
			list.add(personMap);

		return list;
	}

	public static void main(String[] args) {
//		Date beforeHour0 = DateUtil.offsetHour(new Date(), 0);
//		Date beforeHour1 = DateUtil.offsetHour(new Date(), -1);
//		Date beforeHour2 = DateUtil.offsetHour(new Date(), -2);
//		Date beforeHour3 = DateUtil.offsetHour(new Date(), -3);
//		Date beforeHour4 = DateUtil.offsetHour(new Date(), -4);
//		Date beforeHour5 = DateUtil.offsetHour(new Date(), -5);
//		Date beforeHour6 = DateUtil.offsetHour(new Date(), -6);


		String  beforeHourDate0 = DateUtil.format(DateUtil.offsetHour(new Date(), 0), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate1 = DateUtil.format(DateUtil.offsetHour(new Date(), -1), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate2 = DateUtil.format(DateUtil.offsetHour(new Date(), -2), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate3 = DateUtil.format(DateUtil.offsetHour(new Date(), -3), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate4 = DateUtil.format(DateUtil.offsetHour(new Date(), -4), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate5 = DateUtil.format(DateUtil.offsetHour(new Date(), -5), "yyyy-MM-dd HH") +":00:00";
		String  beforeHourDate6 = DateUtil.format(DateUtil.offsetHour(new Date(), -6), "yyyy-MM-dd HH") +":00:00";


		String  beforeHour0 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), 0), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour1 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -1), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour2 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -2), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour3 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -3), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour4 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -4), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour5 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -5), "yyyy-MM-dd HH") +":00:00", 11, 20);
		String  beforeHour6 = StringUtils.substring(DateUtil.format(DateUtil.offsetHour(new Date(), -6), "yyyy-MM-dd HH") +":00:00", 11, 20);


		System.err.println(beforeHourDate6);
		System.err.println(beforeHour0);
	}
}
