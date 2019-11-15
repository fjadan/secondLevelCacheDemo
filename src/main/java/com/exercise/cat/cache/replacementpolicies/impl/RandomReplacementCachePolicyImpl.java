package com.exercise.cat.cache.replacementpolicies.impl;

import com.exercise.cat.cache.Cache;
import com.exercise.cat.cache.replacementpolicies.CacheReplacementPolicy;

public class RandomReplacementCachePolicyImpl implements CacheReplacementPolicy {


    /**
     * Randomly selects a candidate item and discards it to make space when necessary. This algorithm does not
     * require keeping any information about the access history.
     */
    @Override
    public void replaceCache(Cache cache) {

    }

}
