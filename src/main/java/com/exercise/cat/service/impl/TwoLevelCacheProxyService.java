package com.exercise.cat.service.impl;

import com.exercise.cat.cache.impl.Level1CacheImpl;
import com.exercise.cat.cache.impl.Level2CacheImpl;
import com.exercise.cat.cache.replacementpolicies.CacheReplacementPolicy;
import com.exercise.cat.service.Service;
import org.springframework.beans.factory.annotation.Autowired;


/*

    This two level Cache Proxy Service was implemented based on some principles from the Hibernate Cache:

    - https://docs.jboss.org/hibernate/orm/3.5/reference/en/html/performance.html
    - Section 20.2. The Second Level Cache, onwards


    1. Whenever the service is trying to get an object from the cache, the very first place the PROXY look for a cached
       copy of the object, is in first level cache (In Memory Cache).

    2. If a cached copy of the object is present in the first level cache, it is returned as result of the "get" method.

    3. If there is no cached object in the first level cache, then second level cache is looked up for the cached object.

    4. If second level cache has cached the object, it is returned as result of the "get" method. But, before returning
       the object, it is stored in the first level cache also, so that next invocation to the "get" method returns the
       object from the first level cache itself, and there will not be need to go to second level cache again.

    5. If the object is not found neither in the first level cache nor the second level cache, then database query is
       executed and the object is stored in both cache levels, before returning as response of get() method.

    6. If some user or process make changes directly in database, then there is no way that second level cache update
       itself until “timeToLiveSeconds” duration has passed for that cache region. In this case, it is good idea to
       invalidate whole cache and re-build the entire cache once again.

 */

@org.springframework.stereotype.Service
public class TwoLevelCacheProxyService implements Service {

    private Level1CacheImpl firstLevelCache;

    @Autowired
    private Level2CacheImpl secondLevelCache;

    private CacheReplacementPolicy cacheReplacementPolicy;

    @Autowired
    public TwoLevelCacheProxyService(Level1CacheImpl firstLevelCache,
                                     Level2CacheImpl secondLevelCache,
                                     CacheReplacementPolicy crp) {

        this.firstLevelCache = firstLevelCache;
        this.secondLevelCache = secondLevelCache;
        this.cacheReplacementPolicy = crp;
    }


    @Override
    public synchronized void put(String newKey, Object newValue) {

        if (firstLevelCache.hasCapacity()) {

            //--If FLC contains the object, update the reference
            firstLevelCache.put(newKey, newValue);

            //--Remove from the SLC (if present), since that object has become stale
            if (secondLevelCache.containsObject(newKey)) {
                secondLevelCache.remove(newKey);
            }
        }
        else {
            secondLevelCache.put(newKey, newValue);
        }

        //--At this point, if the FLC doesn't contain the object and doesn't have capacity,
        //--Implement an strategy to eliminate some objects from the FLC and SLC
        //--The SLC is using the FileSystem/DB, so we can adjust the condition to run this only when
        //--The SLC has certain size. Maybe (FLC.size * n)
        if (!firstLevelCache.hasCapacity() && secondLevelCache.getCacheSize() > firstLevelCache.getCacheSize()) {

            //- cache replacement policies
            //- https://en.wikipedia.org/wiki/Cache_replacement_policies

            cacheReplacementPolicy.replaceCache(firstLevelCache);

            //Since SLC is a File System Cache, we can replace the cache at this moment, or implement a different strategy
            //cacheReplacementPolicy.replaceCache(secondLevelCache);
        }

    }


    @Override
    public synchronized Object get(String key) {
        if (firstLevelCache.containsObject(key)) {

            return firstLevelCache.get(key);
        }
        else if (secondLevelCache.containsObject(key)) {

            Object cachedObj = secondLevelCache.get(key);

            firstLevelCache.put(key, cachedObj);

            return cachedObj;
        }
        return null;
    }

}
