package com.exercise.cat.cache.replacementpolicies.impl;

import com.exercise.cat.cache.Cache;
import com.exercise.cat.cache.replacementpolicies.CacheReplacementPolicy;

public class DefaultCachePolicyImpl implements CacheReplacementPolicy {

    @Override
    public void replaceCache(Cache cache) {
        cache.clearCache();
    }
}
