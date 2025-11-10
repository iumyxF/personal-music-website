package com.xs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Jackson Object Mapper
 */
public class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String writeValueAsString(Object value) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.readValue(content, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(InputStream src, Class<T> valueType) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.readValue(src, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    public static <T> List<T> readValueAsList(String content, Class<T> elementType) {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.readValue(content,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
