package com.rednit.tinder4j.internal.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednit.tinder4j.internal.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DataObject {

    private static final ObjectMapper mapper;
    private static final TypeReference<HashMap<String, Object>> type;

    static {
        mapper = new ObjectMapper();
        type = new TypeReference<>() {
        };
    }

    private final Map<String, Object> data;

    public DataObject(Map<String, Object> data) {
        this.data = data;
    }


    public static DataObject empty() {
        return new DataObject(new HashMap<>());
    }

    public static DataObject fromJson(String json) {
        try {
            Map<String, Object> map = mapper.readValue(json, type);
            return new DataObject(map);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    public static DataObject fromJson(InputStream stream) {
        try {
            Map<String, Object> map = mapper.readValue(stream, type);
            return new DataObject(map);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    public boolean hasKey(String key) {
        return data.containsKey(key);
    }

    public boolean isNull(String key) {
        return data.get(key) == null;
    }

    public boolean isType(String key, Class<?> clazz) {
        return clazz.isAssignableFrom(data.get(key).getClass());
    }

    @SuppressWarnings("unchecked")
    public DataObject getObject(String key) {
        return new DataObject((Map<String, Object>) get(key));
    }

    @SuppressWarnings("unchecked")
    public DataArray getArray(String key) {
        return new DataArray((List<Object>) get(key));
    }

    public Object get(String key) {
        return data.get(key);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public Boolean getBoolean(String key) {
        return get(key, Boolean.class);

    }

    public Integer getInteger(String key) {
        return get(key, Integer.class);
    }

    public Long getLong(String key) {
        return get(key, Long.class);
    }

    public DataObject remove(String key) {
        data.remove(key);
        return this;
    }

    public DataObject put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Collection<Object> values() {
        return data.values();
    }

    public Set<String> keys() {
        return data.keySet();
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e);
        }
    }

    public Map<String, Object> toMap() {
        return data;
    }
}
