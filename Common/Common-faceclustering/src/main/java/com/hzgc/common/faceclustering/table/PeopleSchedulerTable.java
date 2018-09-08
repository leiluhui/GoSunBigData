package com.hzgc.common.faceclustering.table;

public class PeopleSchedulerTable {
    public static final String TABLE_NAME = "peoplescheduler";
    public static final byte[] COLUMNFAMILY = "rules".getBytes();
    public static final byte[] MOVEINLASTRUNTIME = "moveInLastRunTime".getBytes();
    public static final byte[] REGIONNAME = "regionName".getBytes();
    public static final byte[] SIM = "sim".getBytes();
    public static final byte[] MOVEINCOUNT = "moveInCount".getBytes();
    public static final byte[] MOVEOUTDAYS = "moveOutDays".getBytes();
}
