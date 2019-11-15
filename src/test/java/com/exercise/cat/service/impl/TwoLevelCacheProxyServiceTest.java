package com.exercise.cat.service.impl;

import com.exercise.cat.cache.impl.Level1CacheImpl;
import com.exercise.cat.cache.impl.Level2CacheImpl;
import com.exercise.cat.cache.replacementpolicies.CacheReplacementPolicy;
import com.exercise.cat.cache.replacementpolicies.impl.DefaultCachePolicyImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwoLevelCacheProxyServiceTest {

    private Level1CacheImpl firstLevelCache;

    @Mock
    private Level2CacheImpl secondLevelCache;

    private TwoLevelCacheProxyService tlcps;

    private CacheReplacementPolicy cacheReplacementPolicy;

    @Before
    public void initializeTestData() {
        MockitoAnnotations.initMocks(this);

        firstLevelCache = Mockito.spy(new Level1CacheImpl(3));
        cacheReplacementPolicy = Mockito.spy(new DefaultCachePolicyImpl());
        tlcps = Mockito.spy(new TwoLevelCacheProxyService(firstLevelCache, secondLevelCache, cacheReplacementPolicy));
    }

    @Test
    public void put_shouldFirstPutObjectOnTheFLC() {

        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());

        verify(firstLevelCache, times(3)).put(anyString(), any());
        verify(secondLevelCache, times(0)).put(anyString(), any());

    }

    @Test
    public void put_shouldPutObjectOnTheSLC_WhenFLC_Is_Full() {

        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());

        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());
        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());

        verify(firstLevelCache, times(3)).put(anyString(), any());
        verify(secondLevelCache, times(6)).put(anyString(), any());

    }

    @Test
    public void put_shouldCallReplaceCacheStrategyAfterFLC_IsFull() {

        when(secondLevelCache.getCacheSize()).thenReturn(10L);

        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());

        //--Here the FLC has reached its capacity and SLC already has 10 elements (refer the above "when")
        //--So, the replacement cache strategy is being executed

        tlcps.put("decemberReports", new Object());
        tlcps.put("invoices", new Object());
        tlcps.put("catalogs", new Object());

        //--Here the FLC has reached its capacity again and SLC already has 10 elements (refer the above "when")
        //--So, the replacement cache strategy is being executed

        tlcps.put("2020 templates", new Object());

        verify(firstLevelCache, times(7)).put(anyString(), any());
        verify(cacheReplacementPolicy, times(2)).replaceCache(any());

    }

    @Test
    public void get_shouldSearchOnTheFLC_first() {

        when(secondLevelCache.containsObject("catalogs")).thenReturn(true);
        when(secondLevelCache.get("catalogs")).thenReturn("catalogs");

        tlcps.put("novemberReports", new Object());
        tlcps.put("managerList", new Object());
        tlcps.put("providerList", new Object());
        tlcps.put("decemberReports", new Object());
        tlcps.put("invoices", new Object());
        tlcps.put("catalogs", new Object());

        verify(firstLevelCache, times(3)).put(anyString(), any());
        verify(secondLevelCache, times(3)).put(anyString(), any());

        tlcps.get("novemberReports");
        tlcps.get("managerList");
        tlcps.get("catalogs");

        verify(firstLevelCache, times(2)).get(anyString());
        verify(secondLevelCache, times(1)).get(anyString());

        //--3 previous put and an extra one
        verify(firstLevelCache, times(4)).put(anyString(), any());

    }

}