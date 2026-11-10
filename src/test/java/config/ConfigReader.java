package config;

import java.io.InputStream;
import java.util.Properties;

// Singleton: config.properties is loaded once and shared by UI and API tests
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties props = new Properties();

    private ConfigReader() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {
        return System.getProperty(key, props.getProperty(key));
    }
}
