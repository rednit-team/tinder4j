package com.rednit.tinder4j.internal.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rednit.tinder4j.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DataArray implements Iterable<Object> {

    private static final ObjectMapper mapper;
    private static final TypeReference<List<Object>> type;

    static {
        mapper = new ObjectMapper();
        type = new TypeReference<>() {
        };
    }

    private final List<Object> data;

    public DataArray(List<Object> data) {
        this.data = data;
    }

    public static DataArray empty() {
        return new DataArray(new ArrayList<>());
    }

    public static DataArray fromCollection(Collection<?> collection) {
        return empty().addAll(collection);
    }

    public static DataArray fromJson(String json) {
        try {
            return new DataArray(mapper.readValue(json, type));
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    public static DataArray fromJson(InputStream json) {
        try {
            return new DataArray(mapper.readValue(json, type));
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    public boolean isNull(int index) {
        return data.get(index) == null;
    }

    public boolean isType(int index, Class<?> clazz) {
        return clazz.isAssignableFrom(data.get(index).getClass());
    }

    public int length() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public DataObject getObject(int index) {
        return new DataObject((Map<String, Object>) data.get(index));
    }

    @SuppressWarnings("unchecked")
    public DataArray getArray(int index) {
        return new DataArray((List<Object>) data.get(index));
    }

    public DataArray add(Object value) {
        data.add(value);
        return this;
    }

    public DataArray addAll(Collection<?> values) {
        values.forEach(this::add);
        return this;
    }

    public DataArray addAll(DataArray array) {
        return addAll(array.data);
    }

    public DataArray insert(int index, Object value) {
        data.add(index, value);
        return this;
    }

    public DataArray remove(int index) {
        data.remove(index);
        return this;
    }

    public DataArray remove(Object value) {
        data.remove(value);
        return this;
    }

    public List<Object> toList() {
        return data;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e);
        }
    }

    @Override
    public Iterator<Object> iterator() {
        return data.iterator();
    }
}
