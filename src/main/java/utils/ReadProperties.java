package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading properties from a configuration file. Loads properties from
 * 'config.properties' file in the classpath.
 */
public class ReadProperties {

  /** The properties object containing configuration values. */
  private final Properties properties;

  private static final Logger logger = LogManager.getLogger(ReadProperties.class);

  /**
   * Constructs a new ReadProperties instance. Loads the configuration from 'config.properties' file
   * in the classpath. Throws IllegalStateException if the file is not found.
   */
  public ReadProperties() {
    properties = new Properties();
    try (InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream("config.properties"); ) {
      if (inputStream == null) {
        throw new IllegalStateException("config.properties file not found in classpath");
      }
      properties.load(inputStream);
    } catch (IOException | IllegalStateException e) {
      logger.error("Error while reading properties file ", e);
    }
  }

  /**
   * Gets a property value by its key.
   *
   * @param key the property key
   * @return the property value with leading and trailing whitespace removed
   */
  public String getProperty(String key) {
    return properties.getProperty(key).trim();
  }
}
