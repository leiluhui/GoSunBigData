package com.hzgc.cloud.dynrepo.util;



import com.hzgc.cloud.dynrepo.bean.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceToIpcs {

    //设备转ipcs
    public static List<String> getIpcs(List<Device> deviceList){
        ArrayList <String> list = new ArrayList <>();
        for (Device device:deviceList){
            list.add(device.getIpc());
        }
        return list;
    }

    //ipcid和设备名称映射关系
    public static Map<String, Device> getIpcMapping(List<Device> deviceList){
        HashMap <String, Device> ipcMapping = new HashMap <>();
        for (Device device:deviceList){
            ipcMapping.put(device.getIpc(),device);
        }
        return ipcMapping;
    }
}
