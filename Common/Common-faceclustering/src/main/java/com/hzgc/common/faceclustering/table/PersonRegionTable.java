package com.hzgc.common.faceclustering.table;

public class PersonRegionTable {
    public static final String TABLE_NAME = "personregion";
    public static final byte[] COLUMNFAMILY = "region".getBytes();
    public static final byte[] REGION_NAME = "regionName".getBytes();          //区域名
    public static final byte[] REGION_IPCIDS="ipcids".getBytes();       //区域底下的设备名称
}
