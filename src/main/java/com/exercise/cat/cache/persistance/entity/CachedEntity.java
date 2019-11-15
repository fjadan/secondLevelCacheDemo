package com.exercise.cat.cache.persistance.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class CachedEntity {

    @Id
    @Column(name = "KEY")
    private String key;

    //--for a real DB change to LOB
    @Column(name = "OBJECT")
    private String obj;


    public CachedEntity() {
    }


    @Builder
    public CachedEntity(String key,
                        String obj) {

        this.key = key;
        this.obj = obj;
    }
}
