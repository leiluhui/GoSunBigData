package com.hzgc.common.rpc.util;

public class Constant {
    private String rootPath = "/gosunrpc";
    private String nodePath = rootPath + "/worker";

    public Constant(String rootPath, String nodePath){
        this.rootPath = rootPath;
        this.nodePath = rootPath + "/" + nodePath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }
}
