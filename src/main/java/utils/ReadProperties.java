package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadProperties {
    Properties properties;
    private static final Logger logger = LogManager.getLogger(ReadProperties.class);

    public ReadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");) {
            if (inputStream == null){
                throw new IllegalStateException("config.properties file not found in classpath");
            }
            properties.load(inputStream);
        } catch (IOException | IllegalStateException e) {
            logger.error("Error while reading properties file ", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key).trim();
    }
}
