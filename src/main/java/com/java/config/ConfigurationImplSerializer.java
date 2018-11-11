package com.java.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ConfigurationImplSerializer extends JsonSerializer<ConfigurationImpl> {
    @Override
    public void serialize(ConfigurationImpl value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeObject(value.getNode());
    }
}
