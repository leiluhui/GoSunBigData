package com.hzgc.collect.controller;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        File file = new File("/opt/abcadfads");
        file.createNewFile();
    }
}
