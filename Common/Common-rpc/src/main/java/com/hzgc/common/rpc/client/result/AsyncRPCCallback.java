package com.hzgc.common.rpc.client.result;

public interface AsyncRPCCallback {
    void success(Object result);
    void fail(Exception e);
}
