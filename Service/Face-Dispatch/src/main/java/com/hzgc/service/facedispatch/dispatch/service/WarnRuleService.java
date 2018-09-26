package com.hzgc.service.facedispatch.dispatch.service;

import com.hzgc.common.service.api.bean.DeviceDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.service.facedispatch.dispatch.bean.*;
import com.hzgc.service.facedispatch.dispatch.dao.HBaseDao;
import com.hzgc.service.facedispatch.dispatch.util.IdsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class WarnRuleService {

    @Autowired
    @SuppressWarnings("unused")
    private HBaseDao hBaseDao;

    @Autowired
    private PlatformService platformService;

    public void configRules(List<String> ipcIDs, List<Warn> warns) {
        hBaseDao.configRules(ipcIDs, warns);
    }

    /**
     * 删除设备的布控规则
     *
     * @param ipcIDs 设备 ipcID 列表
     */
    public void deleteRules(List<String> ipcIDs) {
        hBaseDao.deleteRules(ipcIDs);
    }

    //存储原数据
    public ResponseResult<String> saveOriginData(Map<String, Dispatch> map) throws IOException {
        return this.hBaseDao.saveOriginData(map);
    }

    //根据ruleId进行全部参数查询
    public ResponseResult<Dispatch> searchByRuleId(String id) throws IOException {
        Map<String, Dispatch> map = hBaseDao.searchByRuleId();
            if (map.containsKey(id)){
                Dispatch dispatch = map.get(id);
                List<Warn> warnList = dispatch.getRule().getWarns();
                List <Device> deviceList = dispatch.getDevices();
                String[] strings = new String[warnList.size()];
                for (int i = 0; i < warnList.size(); i++) {
                    strings[i] = (warnList.get(i)).getObjectType();
                }
                log.info("Strings is " + JacksonUtil.toJson(strings));
                Map<String, Map<String, String>> responseResult = hBaseDao.getObjectTypeName(strings);
                Map<String, String> m = responseResult.get("restbody");
                for (Warn warn : warnList) {
                    for (String s : m.keySet()) {
                        if (warn.getObjectType().equals(s)) {
                            if (null != m.get(s)) {
                                warn.setObjectTypeName(m.get(s));
                            }
                        }
                    }
                }
                //查询ipcid
                List<String> list = IdsUtil.toDeviceIdList(deviceList);
                Map<String, DeviceDTO> mapDTO = platformService.getDeviceInfoByBatchId(list);
                //动态获取需要删除的设备对应的id
                ArrayList<String> delIds = new ArrayList <>();
                //需要更新的id
                ArrayList <String> ids = new ArrayList <>();
                Iterator<Device> iterator = deviceList.iterator();
                while (iterator.hasNext()){
                    Device device = iterator.next();
                    //数据库中的设备id
                    String dataId = device.getId();
                    //查看是否存在这个设备,不存在就删除
                    if (!mapDTO.containsKey(dataId)){
                        delIds.add(dataId);
                        iterator.remove();
                        log.info("Device is deleted , device id is : " + dataId);
                    }else {
                        //动态设置设备名称
                        String deviceName = mapDTO.get(dataId).getName();
                        device.setName(deviceName);
                    }
                }
                //删除设备不存在的id
                if (delIds.size() > 0){
                    log.info("Device id has been deleted");
                    hBaseDao.deleteRules(delIds);
                }
                //同步设备表
                hBaseDao.updateRule(dispatch);
                //动态获取设备名称
                for (Device device:deviceList) {
                    String deviceId = device.getId();
                    if (mapDTO.containsKey(deviceId)) {

                    }
                }
                return ResponseResult.init(dispatch);
            }
        log.info("Id query Data is null");
        return ResponseResult.error(RestErrorCode.RECORD_NOT_EXIST);
    }

    //修改规则
    public ResponseResult<Boolean> updateRule(Dispatch dispatch) throws IOException {
        return hBaseDao.updateRule(dispatch);
    }

    //删除规则
    public List<String> delRules(RuleIds<String> ruleIds) throws IOException {
        return hBaseDao.delRules(ruleIds);
    }

    //分页获取规则列表
    public ResponseResult<List> getRuleList(PageBean pageBean) throws IOException {
        return hBaseDao.getRuleList(pageBean);
    }

    //获取某个规则绑定的所有设备
    public ResponseResult<List> getDeviceList(String rule_id) throws IOException {
        return hBaseDao.getDeviceList(rule_id);
    }

}