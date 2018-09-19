package com.hzgc.common.rpc.util;

import org.apache.zookeeper.CreateMode;

import java.util.Map;

public class Constant {
    private String rootPath = "/gosunrpc";
    private String nodePath = rootPath + "/worker";
    private CreateMode createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
    private Map<String, String> param;
    private boolean exitIfFaild = false;

    public Constant(String rootPath, String nodePath){
        this.rootPath = rootPath;
        this.nodePath = rootPath + "/" + nodePath;
        param = null;
    }

    public Constant(String rootPath, String nodePath, CreateMode createMode){
        this.rootPath = rootPath;
        this.nodePath = rootPath + "/" + nodePath;
        this.createMode = createMode;
        param = null;
    }

    public Constant(String rootPath, String nodePath, CreateMode createMode, Map<String, String> param){
        this.rootPath = rootPath;
        this.nodePath = rootPath + "/" + nodePath;
        this.createMode = createMode;
        this.param = param;
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

    public CreateMode getCreateMode() {
        return createMode;
    }

    public void setCreateMode(CreateMode createMode) {
        this.createMode = createMode;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public void setExitIfFaild(boolean exitIfFaild) {
        this.exitIfFaild = exitIfFaild;
    }

    public boolean isExitIfFaild() {
        return exitIfFaild;
    }

    @Override
    public String toString() {
        return "Constant{" +
                "rootPath='" + rootPath + '\'' +
                ", nodePath='" + nodePath + '\'' +
                ", createMode=" + createMode +
                ", param=" + param +
                ", exitIfFaild=" + exitIfFaild +
                '}';
    }
}
