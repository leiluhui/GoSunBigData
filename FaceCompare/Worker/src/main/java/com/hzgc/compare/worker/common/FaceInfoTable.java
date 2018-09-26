package com.hzgc.compare.worker.common;

import org.apache.hadoop.hbase.util.Bytes;

public class FaceInfoTable {
    public static String TABLE_NAME = "faceData11";
    public static byte[] CLU_FAMILY = Bytes.toBytes("face");
    public static byte[] FEATURE = Bytes.toBytes("feature");
    public static byte[] TIMESTAMP = Bytes.toBytes("timestamp");
    public static byte[] DATE = Bytes.toBytes("date");
    public static byte[] INFO = Bytes.toBytes("info");
    public static byte[] IPCID = Bytes.toBytes("ipcid");
    public static byte[] TIME_SLOT = Bytes.toBytes("timeSlot");
    public static byte[] ATTRIBUTE = Bytes.toBytes("attribute");
    public static byte[] SURL = Bytes.toBytes("surl");
    public static byte[] BURL = Bytes.toBytes("burl");
    public static byte[] RELATIVE_PATH = Bytes.toBytes("relativePath");
    public static byte[] RELATIVE_PATH_BIG = Bytes.toBytes("relativePath_big");
    public static byte[] IP = Bytes.toBytes("ip");
    public static byte[] HOSTNAME = Bytes.toBytes("hostname");

}
