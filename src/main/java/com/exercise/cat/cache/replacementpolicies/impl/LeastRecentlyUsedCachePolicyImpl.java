package com.exercise.cat.cache.replacementpolicies.impl;

import com.exercise.cat.cache.Cache;
import com.exercise.cat.cache.replacementpolicies.CacheReplacementPolicy;

public class LeastRecentlyUsedCachePolicyImpl implements CacheReplacementPolicy {

    /**
     * Discards the least recently used items first. This algorithm requires keeping track of what was used when,
     * which is expensive if one wants to make sure the algorithm always discards the least recently used item.
     */
    @Override
    public void replaceCache(Cache cache) {

    }

}
