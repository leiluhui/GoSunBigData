package com.hzgc.common.util.basic;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    /**
     * list分页
     *
     * @param sourceList 分页对象
     * @param offset     分页偏移量
     * @param limit      分页数量
     * @param <T>        对象范型
     * @return 分页结果
     */
    public static <T> List<T> pageSplit(List<T> sourceList, int offset, int limit) {
        if (sourceList == null || sourceList.size() == 0 || offset <= -1 || limit <= -1) {
            return null;
        }
        int size = sourceList.size();
        if (offset + 1 <= size) {
            if (offset + limit + 1 <= size) {
                return sourceList.subList(offset, offset + limit);
            } else {
                return sourceList.subList(offset, size);
            }
        } else {
            return new ArrayList<>();
        }
    }

}
