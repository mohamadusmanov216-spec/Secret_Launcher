// src/com/secretlauncher/config/SettingsManager.java
package com.secretlauncher.config;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.prefs.Preferences;

public class SettingsManager {
    private Preferences prefs;
    private Properties properties;
    
    public SettingsManager() {
        this.prefs = Preferences.userRoot().node("secretlauncher");
        this.properties = new Properties();
        loadSettings();
    }
    
    public void loadSettings() {
        // Загрузка из реестра/преференсов
        properties.setProperty("theme", prefs.get("theme", "neon_blue"));
        properties.setProperty("language", prefs.get("language", "ru_RU"));
        properties.setProperty("memory", prefs.get("memory", "2048"));
        properties.setProperty("fullscreen", prefs.get("fullscreen", "false"));
        properties.setProperty("sound", prefs.get("sound", "true"));
        properties.setProperty("auto_update", prefs.get("auto_update", "true"));
    }
    
    public void saveSettings() {
        prefs.put("theme", properties.getProperty("theme"));
        prefs.put("language", properties.getProperty("language"));
        prefs.put("memory", properties.getProperty("memory"));
        prefs.put("fullscreen", properties.getProperty("fullscreen"));
        prefs.put("sound", properties.getProperty("sound"));
        prefs.put("auto_update", properties.getProperty("auto_update"));
    }
    
    public String getSetting(String key) {
        return properties.getProperty(key);
    }
    
    public void setSetting(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public int getMemorySetting() {
        try {
            return Integer.parseInt(getSetting("memory"));
        } catch (NumberFormatException e) {
            return 2048;
        }
    }
    
    public boolean getBooleanSetting(String key) {
        return Boolean.parseBoolean(getSetting(key));
    }
}
