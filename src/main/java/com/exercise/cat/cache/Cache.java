package com.exercise.cat.cache;

public interface Cache {

    Object get(String key);

    boolean containsObject(String key);

    void put(String key, Object value);

    void remove(String key);

    long getCacheSize();

    void clearCache();

}
