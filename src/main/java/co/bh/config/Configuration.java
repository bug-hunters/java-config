package com.java.config;


import com.fasterxml.jackson.databind.JsonNode;

public interface Configuration {
    String getString(String key);

    Boolean getBoolean(String key);

    Integer getInteger(String key);

    Long getLong(String key);

    Boolean[] getBooleanArray(String key);

    String[] getStringArray(String key);

    Integer[] getIntegerArray(String key);

    Long[] getLongArray(String key);

    Double[] getDoubleArray(String key);

    JsonNode getNode(String key);

    JsonNode getNode();
}
