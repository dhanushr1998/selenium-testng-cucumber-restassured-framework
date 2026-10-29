package com.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class FrameworkConfig {
    private static final String CONFIG_FILE = "/config.properties";
    private static final FrameworkConfig INSTANCE = load();

    private final Properties properties;

    private FrameworkConfig(Properties properties) {
        this.properties = properties;
    }

    public static FrameworkConfig getInstance() {
        return INSTANCE;
    }

    public String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase(Locale.ROOT);
    }

    public String getDriverPath() {
        return getProperty("driver.path", "");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    public boolean shouldMaximizeBrowser() {
        return getBooleanProperty("browser.maximize", false);
    }

    public boolean isBrowserKeepOpen() {
        return getBooleanProperty("browser.keepOpen", false);
    }

    public int getRetryCount() {
        return getNonNegativeIntProperty("retry.count", 0);
    }

    public long getActionDelayMs() {
        return getNonNegativeLongProperty("action.delay.ms", 0);
    }

    private String getProperty(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue)).trim();
    }

    private boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, Boolean.toString(defaultValue));
        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
            throw new IllegalArgumentException("Config property '" + key + "' must be true or false");
        }
        return Boolean.parseBoolean(value);
    }

    private int getNonNegativeIntProperty(String key, int defaultValue) {
        String value = getProperty(key, Integer.toString(defaultValue));
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException("Config property '" + key + "' must not be negative");
            }
            return parsedValue;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Config property '" + key + "' must be a whole number", exception);
        }
    }

    private long getNonNegativeLongProperty(String key, long defaultValue) {
        String value = getProperty(key, Long.toString(defaultValue));
        try {
            long parsedValue = Long.parseLong(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException("Config property '" + key + "' must not be negative");
            }
            return parsedValue;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Config property '" + key + "' must be a whole number", exception);
        }
    }

    private static FrameworkConfig load() {
        Properties properties = new Properties();
        try (InputStream input = FrameworkConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Missing test configuration: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load test configuration: " + CONFIG_FILE, exception);
        }
        return new FrameworkConfig(properties);
    }
}
