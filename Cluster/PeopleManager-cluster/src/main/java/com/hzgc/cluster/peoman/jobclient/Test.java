package com.hzgc.cluster.peoman.jobclient;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("aaaa");
        list.add("bbbb");
        list.add("cccc");
        String b = list.get(list.size()-1);
        System.out.println(b);
    }
}
