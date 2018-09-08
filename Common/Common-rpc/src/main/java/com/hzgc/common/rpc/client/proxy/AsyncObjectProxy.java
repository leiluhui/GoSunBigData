package com.hzgc.common.rpc.client.proxy;

import com.hzgc.common.rpc.client.result.RPCFuture;

public interface AsyncObjectProxy {
    RPCFuture call(String funcName, Object... args);
}