package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for JSON operations using Jackson ObjectMapper. Provides methods for converting
 * between JSON strings, input streams, and Java objects.
 */
public class JsonUtils {

  /** The ObjectMapper instance used for JSON operations. */
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static final Logger logger = LogManager.getLogger(JsonUtils.class);

  /**
   * Converts a JSON string to a Map.
   *
   * @param json the JSON string to convert
   * @return a Map representation of the JSON, or an empty map if conversion fails
   */
  public static Map<String, Object> jsonToMap(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    } catch (JsonProcessingException e) {
      logger.error("Error occurred while parsing JSON to Map", e);
    }
    return Collections.emptyMap();
  }

  /**
   * Converts a JSON file from the classpath to a Map.
   *
   * @param filename the name of the JSON file in the classpath
   * @return a Map representation of the JSON, or an empty map if conversion fails
   */
  public static Map<String, Object> jsonFileToMap(String filename) {
    try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filename)) {

      if (inputStream == null) {
        logger.error("Resource not found: {}" , filename);
        return Collections.emptyMap();
      }
      return objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
    } catch (IOException e) {
      logger.error("Error occurred while accessing Stream", e);
    }
    return Collections.emptyMap();
  }

   /**
   * Converts a Map to a JSON string.
   *
   * @param map the Map
   * @return a JSON String representation of the Map, or an empty string if conversion fails
   */
  public static String mapToJson(Map<String, Object> map) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    } catch (JsonProcessingException e) {
      logger.error("Error occurred while converting Map to JSON", e);
    }
    return "";
  }

  /**
   * Converts a JSON string to an object of the specified class.
   *
   * @param <T> the type of object to convert to
   * @param json the JSON string to convert
   * @param clazz the class of the target object
   * @return an instance of the target class, or null if conversion fails
   */
  public static <T> T fromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      logger.error("Error occurred while parsing JSON to Record", e);
    }
    return null;
  }

  /**
   * Reads and converts a JSON file from the classpath to an object of the specified class.
   *
   * @param <T> the type of object to convert to
   * @param filename the name of the JSON file in the classpath
   * @param clazz the class of the target object
   * @param isFile whether the input is a file (unused parameter)
   * @return an instance of the target class, or null if the file is not found or conversion fails
   */
  public static <T> T fromJson(String filename, Class<T> clazz, boolean isFile) {
    InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filename);
    if (inputStream == null) {
      logger.error("Resource not found: {}", filename);
      return null;
    }

    try {
      return objectMapper.readValue(inputStream, clazz);
    } catch (IOException e) {
      logger.error("Error occurred while accessing Stream", e);
    }
    return null;
  }
}
