package com.hzgc.cluster.dispatch.model;

public class DispatchIpc {
    private String defid;

    private String ipc;

    public String getDefid() {
        return defid;
    }

    public void setDefid(String defid) {
        this.defid = defid == null ? null : defid.trim();
    }

    public String getIpc() {
        return ipc;
    }

    public void setIpc(String ipc) {
        this.ipc = ipc == null ? null : ipc.trim();
    }
}