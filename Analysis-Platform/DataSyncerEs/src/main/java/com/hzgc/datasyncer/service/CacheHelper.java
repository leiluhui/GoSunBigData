package com.hzgc.datasyncer.service;

public interface CacheHelper<K, V> {
    public String generateKey(String prefix, K key);

    public void cacheInfo(K key, V value);

    public V getInfo(K key);
}
