package com.java.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Yaml;

import java.io.IOException;

public class ConfigurationImplDeserializer extends JsonDeserializer<ConfigurationImpl> {
    @Override
    public ConfigurationImpl deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ConfigurationImpl config = new ConfigurationImpl();
        JsonNode node = jp.getCodec().readTree(jp);

        String yaml = Yaml.mapper().writeValueAsString(node);
        config.fromContents(yaml);
        return config;
    }
}