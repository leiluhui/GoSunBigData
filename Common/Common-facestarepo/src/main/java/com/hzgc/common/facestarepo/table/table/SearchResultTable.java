package com.hzgc.common.facestarepo.table.table;

public class SearchResultTable {
    public static final String TABLE_SEARCHRES = "searchRes";
    public static final byte[] SEARCHRES_COLUMNFAMILY = "i".getBytes();
    //以图搜图查询历史记录
    public static final byte[] STAREPO_COLUMN_SEARCHMESSAGE = "sm".getBytes();
    //静态库搜索原图
    public static final byte[] STAREPO_COLUMN_PICTURE = "spic".getBytes();
    //搜索记录导出重点人员
    public static final byte[] STAREPO_COLUMN_FILE = "word".getBytes();
}
