package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);

    public static Map<String, Object> jsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while parsing JSON to Map", e);
        }
        return Collections.emptyMap();
    }

    public static Map<String, Object> jsonToMap(InputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            logger.error("Error occurred while accessing Stream", e);
        }
        return Collections.emptyMap();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Error occuured while parsing JSON to Record", e);
        }
        return null;
    }

    public static <T> T fromJson(String filename, Class<T> clazz, boolean isFile) {
        InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null){
            logger.error("Resource not found: {}", filename);
            return null;
        }

        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            logger.error("Error occuured while accessing Stream", e);
        }
        return null;
    }

}
