package com.elvira.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in resources!");
            }

            properties.load(input);
            log("Config loaded successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // 🔹 Получить строку с fallback
    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key, properties.getProperty(key, defaultValue));
        log("Config read: " + key + " = " + value);
        return value;
    }

    public static String get(String key) {
        return get(key, null);
    }

    // 🔹 boolean с fallback
    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val == null) return defaultValue;
        return Boolean.parseBoolean(val);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    // 🔹 int с fallback
    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        try {
            return val != null ? Integer.parseInt(val) : defaultValue;
        } catch (NumberFormatException e) {
            log("Warning: cannot parse int for key " + key + ", using default " + defaultValue);
            return defaultValue;
        }
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    // 🔧 helper для логов
    private static void log(String msg) {
        System.out.println("[ConfigReader] " + msg);
    }
}