package com.hzgc.service.facedispatch.util;

import com.hzgc.common.util.basic.UuidUtil;
import com.hzgc.service.facedispatch.bean.Device;
import com.hzgc.service.facedispatch.bean.Dispatch;
import com.hzgc.service.facedispatch.bean.Rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IpcIdsUtil {

    public static List<String> toDeviceIdList(List<Device> list) {
        List<String> li = new ArrayList<>();
        for (Device dev : list) {
            String i = dev.getId();
            li.add(i);
        }
        return li;
    }


    //数据解析存储
    public static Map<String, Dispatch> toDispatchMap(Dispatch dispatch) {
        if (null != dispatch) {
            Rule rule = dispatch.getRule();
            String ruleId = null;
            //设置ruleId
            if (null == dispatch.getRule().getRuleId()) {
                rule.setRuleId(UuidUtil.getUuid());
                ruleId = rule.getRuleId();
            }
            Map<String, Dispatch> dispatchMap = new LinkedHashMap<>();
            dispatchMap.put(ruleId, dispatch);
            return dispatchMap;
        } else {
            return null;
        }
    }
}
