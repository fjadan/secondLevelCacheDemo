package com.exercise.cat.cache.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Level1CacheImplTest {

    @Test
    public void getCacheSize_shouldBeZeroIfCacheIsEmpty() {
        Level1CacheImpl level1Cache = new Level1CacheImpl(10);

        assertEquals(0, level1Cache.getCacheSize());
    }

    @Test
    public void getCacheSize_shouldBeEqualsToTheAmountCachedObjects() {
        Level1CacheImpl level1Cache = new Level1CacheImpl(10);

        level1Cache.put("novemberReports", new Object());
        level1Cache.put("managerList", new Object());
        level1Cache.put("providerList", new Object());

        assertEquals(3, level1Cache.getCacheSize());
    }

    @Test
    public void hasCapacity_shouldBeCalculatedBasedOnSizeAndCachedObjects() {
        Level1CacheImpl level1Cache = new Level1CacheImpl(3);

        level1Cache.put("novemberReports", new Object());

        assertEquals(true, level1Cache.hasCapacity());

        level1Cache.put("managerList", new Object());
        level1Cache.put("providerList", new Object());

        assertEquals(false, level1Cache.hasCapacity());
    }

}