package com.exercise.cat.cache.impl;

import com.exercise.cat.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.exercise.cat.cache.utils.CacheUtils.stringToJson;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class Level2CacheImplTest {

    @Autowired
    @Qualifier("level2Cache")
    private Cache level2Cache;


    @Test
    public void getCacheSize_shouldBeZeroIfCacheIsEmpty() {

        assertEquals(0, level2Cache.getCacheSize());
    }

    @Test
    public void getCacheSize_shouldBeEqualsToTheAmountCachedObjects() {

        level2Cache.put("novemberReports", new Object());
        level2Cache.put("managerList", new Object());
        level2Cache.put("providerList", new Object());

        assertEquals(3, level2Cache.getCacheSize());
    }

    @Test
    public void put_shouldUpdateObjectIfAlreadyOnTheCache() {

        List<String> reports = new ArrayList<>();

        reports.add("November Report");
        reports.add("December Report");
        reports.add("Sales Report for 2017");

        //--Add new object to the cache
        level2Cache.put("2017 reports", reports);

        String toJson = stringToJson(reports);
        assertEquals(toJson, level2Cache.get("2017 reports"));
        assertEquals(1, level2Cache.getCacheSize());


        //--Assert that since the object is already on the cache, this will be updated instead or new one being inserted
        reports.add("Sales Report for 2017 amendment");
        String newJson = stringToJson(reports);

        level2Cache.put("2017 reports", reports);
        assertEquals(newJson, level2Cache.get("2017 reports"));
        assertEquals(1, level2Cache.getCacheSize());

    }

    @Test
    public void remove_shouldRemoveObjectFromCache() {

        level2Cache.put("novemberReports", new Object());
        level2Cache.put("managerList", new Object());
        level2Cache.put("providerList", new Object());

        assertEquals(3, level2Cache.getCacheSize());

        level2Cache.remove("managerList");
        assertEquals(2, level2Cache.getCacheSize());

    }
}