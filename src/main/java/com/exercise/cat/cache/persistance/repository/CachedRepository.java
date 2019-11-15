package com.exercise.cat.cache.persistance.repository;

import com.exercise.cat.cache.persistance.entity.CachedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CachedRepository extends JpaRepository<CachedEntity, Long> {

    Optional<CachedEntity> findByKey(String key);

    void deleteByKey(String key);
}
