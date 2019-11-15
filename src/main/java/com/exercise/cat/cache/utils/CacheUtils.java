package com.exercise.cat.cache.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CacheUtils {

    public static String stringToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String toJson = "";

        try {
            toJson = mapper.writeValueAsString(obj);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return toJson;
    }

}
