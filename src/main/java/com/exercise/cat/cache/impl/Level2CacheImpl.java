package com.exercise.cat.cache.impl;

import com.exercise.cat.cache.Cache;
import com.exercise.cat.cache.persistance.entity.CachedEntity;
import com.exercise.cat.cache.persistance.repository.CachedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.exercise.cat.cache.utils.CacheUtils.stringToJson;

@Component("level2Cache")
public class Level2CacheImpl implements Cache {

    @Autowired
    private CachedRepository repository;

    @Override
    public Object get(String key) {

        Optional<CachedEntity> ce = repository.findByKey(key);

        if (ce.isPresent()) {
            return ce.get().getObj();
        }

        return null;
    }

    @Override
    public boolean containsObject(String key) {
        return get(key) != null;
    }

    /**
     * This method inserts or updates a reference
     *
     * @param key
     * @param value
     */
    @Override
    public void put(String key, Object value) {

        //Maybe to implement a NONSQL DB for text searching?

        CachedEntity ce = CachedEntity.builder()
                .key(key)
                .obj(stringToJson(value))
                .build();

        repository.saveAndFlush(ce);
    }

    @Override
    public void remove(String key) {
        repository.deleteByKey(key);
    }

    @Override
    public long getCacheSize() {
        return repository.count();
    }

    @Override
    public void clearCache() {
        repository.deleteAll();
    }

}
