package co.bh.config;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonDeserialize(using = ConfigurationImplDeserializer.class)
@JsonSerialize(using = ConfigurationImplSerializer.class)
public class ConfigurationImpl extends AbstractConfiguration implements Configuration {
    private JsonNode node;

    @JsonIgnore
    public Configuration getDefault() {
        return new ConfigurationImpl();
    }

    public Configuration fromContents(String contents) {
        try {
            ObjectMapper mapper = null;
            if(contents.trim().startsWith("{")) {
                mapper = Json.mapper();
            }
            else {
                mapper = Yaml.mapper();
            }
            this.node = mapper.readTree(contents);
        }
        catch (Exception e) {
            System.out.println("unable to load configuration");
            return null;
        }
        return this;
    }

    public Configuration fromLocation(String location) {
        String data;
        try {
            if (location.toLowerCase().startsWith("http")) {
                data = urlToString(location, null);
            } else {
                final Path path = Paths.get(location);
                if (Files.exists(path)) {
                    data = FileUtils.readFileToString(path.toFile(), "UTF-8");
                } else {
                    InputStream stream = ConfigurationImpl.class.getClassLoader().getResourceAsStream(location);
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(stream, writer, "UTF-8");
                    data = writer.toString();
                }
            }
            ObjectMapper mapper;
            if (data.trim().startsWith("{")) {
                mapper = Json.mapper();
            } else {
                mapper = Yaml.mapper();
            }
            this.node = mapper.readTree(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonNode getNode() {
        return node;
    }

    public JsonNode getNode(String key) {
        if(node == null) {
            return null;
        }
        // support keys with DOT
        if(node.get(key) != null) {
            return node.get(key);
        }
        // split to DOT notation
        String[] parts = key.split("\\.");

        JsonNode last = node;
        for(String part : parts) {
            last = last.get(part);
            if(last == null) {
                break;

            }
        }
        return last;
    }

    public String getString(String key) {
        JsonNode last = getNode(key);

        if(last != null) {
            return last.asText();
        }
        else {
            return null;
        }
    }

    public Boolean getBoolean(String key) {
        JsonNode last = getNode(key);

        if(last != null) {
            try {
                return Boolean.parseBoolean(last.asText());
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public Integer getInteger(String key) {
        JsonNode last = getNode(key);

        if(last != null) {
            try {
                return Integer.parseInt(last.asText());
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public Long getLong(String key) {
        JsonNode last = getNode(key);

        if(last != null) {
            try {
                return Long.parseLong(last.asText());
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public Boolean[] getBooleanArray(String key) {
        JsonNode node = getNode(key);
        if(node != null) {
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
                List<Boolean> output = new ArrayList<Boolean>();
                ArrayNode an = (ArrayNode) node;
                for(JsonNode n : an) {
                    try {
                        output.add(n.asBoolean());
                    }
                    catch (Exception e) {
                        // continue
                    }
                }
                return output.toArray(new Boolean[output.size()]);
            }
        }
        return new Boolean[0];
    }

    public String[] getStringArray(String key) {
        JsonNode node = getNode(key);
        if(node != null) {
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
                List<String> output = new ArrayList<String>();
                ArrayNode an = (ArrayNode) node;
                for(JsonNode n : an) {
                    try {
                        output.add(n.asText());
                    }
                    catch (Exception e) {
                        // continue
                    }
                }
                return output.toArray(new String[output.size()]);
            }
        }
        return new String[0];
    }

    public Integer[] getIntegerArray(String key) {
        JsonNode node = getNode(key);
        if(node != null) {
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
                List<Integer> output = new ArrayList<Integer>();
                ArrayNode an = (ArrayNode) node;
                for(JsonNode n : an) {
                    try {
                        output.add(n.asInt());
                    }
                    catch (Exception e) {
                        // continue
                    }
                }
                return output.toArray(new Integer[output.size()]);
            }
        }
        return new Integer[0];
    }

    public Long[] getLongArray(String key) {
        JsonNode node = getNode(key);
        if(node != null) {
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
                List<Long> output = new ArrayList<Long>();
                ArrayNode an = (ArrayNode) node;
                for(JsonNode n : an) {
                    try {
                        output.add(n.asLong());
                    }
                    catch (Exception e) {
                        // continue
                    }
                }
                return output.toArray(new Long[output.size()]);
            }
        }
        return new Long[0];
    }

    public Double[] getDoubleArray(String key) {
        JsonNode node = getNode(key);
        if(node != null) {
            if(node.getNodeType().equals(JsonNodeType.ARRAY)) {
                List<Double> output = new ArrayList<Double>();
                ArrayNode an = (ArrayNode) node;
                for(JsonNode n : an) {
                    try {
                        output.add(n.asDouble());
                    }
                    catch (Exception e) {
                        // continue
                    }
                }
                return output.toArray(new Double[output.size()]);
            }
        }
        return new Double[0];
    }

    public ConfigurationImpl add(Configuration configuration) {
        merge(getNode(), configuration.getNode());
        return this;
    }

    protected static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);
            if (jsonNode != null && jsonNode.isObject()) {
                merge(jsonNode, updateNode.get(fieldName));
            }
            else {
                if (mainNode instanceof ObjectNode) {
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).put(fieldName, value);
                }
            }

        }

        return mainNode;
    }

    @Override
    public String toString() {
        try {
            return Yaml.pretty().writeValueAsString(node);
        }
        catch (Exception e) {
            return "";
        }
    }
}

