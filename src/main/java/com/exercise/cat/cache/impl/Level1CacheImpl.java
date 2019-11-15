package com.exercise.cat.cache.impl;

import com.exercise.cat.cache.Cache;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Level1CacheImpl implements Cache {


    private Map<String, Object> cacheMap;
    private int memoryCapacity;


    public Level1CacheImpl(@Value("${flc.memory.capacity}") int memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
        this.cacheMap = new ConcurrentHashMap<>(memoryCapacity);

    }

    @Override
    public Object get(String key) {
        return cacheMap.get(key);
    }

    @Override
    public boolean containsObject(String key) {
        return cacheMap.containsKey(key);
    }

    @Override
    public void put(String key, Object value) {
        cacheMap.put(key, value);
    }

    @Override
    public void remove(String key) {
        cacheMap.remove(key);
    }

    @Override
    public long getCacheSize() {
        return cacheMap.size();
    }

    @Override
    public void clearCache() {
        this.cacheMap = new ConcurrentHashMap<>(memoryCapacity);
    }

    public boolean hasCapacity() {
        return getCacheSize() < this.memoryCapacity;
    }


}
