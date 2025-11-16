// src/com/secretlauncher/config/LauncherConfig.java
package com.secretlauncher.config;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.prefs.Preferences;

public class LauncherConfig {
    private static final String CONFIG_FILE = "launcher_config.json";
    private Properties properties;
    private Preferences prefs;
    
    // Настройки по умолчанию
    private static final Map<String, String> DEFAULT_CONFIG = Map.of(
        "launcher.name", "Secret Launcher",
        "launcher.version", "1.0.0",
        "theme.color", "neon_blue",
        "animation.enabled", "true",
        "particles.enabled", "true",
        "auto.update", "true",
        "java.path", "",
        "minecraft.path", "",
        "memory.allocated", "2048",
        "window.width", "1200",
        "window.height", "800",
        "language", "ru_RU"
    );
    
    public LauncherConfig() {
        this.properties = new Properties();
        this.prefs = Preferences.userRoot().node("secretlauncher");
        loadConfig();
    }
    
    public void loadConfig() {
        try {
            // Сначала пробуем загрузить из файла
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                FileInputStream fis = new FileInputStream(configFile);
                properties.loadFromXML(fis);
                fis.close();
            } else {
                // Используем настройки по умолчанию
                setDefaults();
                saveConfig();
            }
            
            // Загружаем чувствительные данные из реестра/преференсов
            loadSensitiveData();
            
        } catch (Exception e) {
            setDefaults();
            System.err.println("Ошибка загрузки конфига: " + e.getMessage());
        }
    }
    
    public void saveConfig() {
        try {
            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            properties.storeToXML(fos, "Secret Launcher Configuration");
            fos.close();
            
            // Сохраняем чувствительные данные отдельно
            saveSensitiveData();
            
        } catch (Exception e) {
            System.err.println("Ошибка сохранения конфига: " + e.getMessage());
        }
    }
    
    private void setDefaults() {
        for (Map.Entry<String, String> entry : DEFAULT_CONFIG.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
    }
    
    private void loadSensitiveData() {
        // Загружаем пароли/токены из безопасного хранилища
        properties.setProperty("auth.token", prefs.get("auth_token", ""));
        properties.setProperty("user.password", decrypt(prefs.get("user_password", "")));
    }
    
    private void saveSensitiveData() {
        // Сохраняем пароли/токены в безопасное хранилище
        prefs.put("auth_token", properties.getProperty("auth.token", ""));
        prefs.put("user_password", encrypt(properties.getProperty("user.password", "")));
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key, DEFAULT_CONFIG.getOrDefault(key, ""));
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    public int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            return Integer.parseInt(DEFAULT_CONFIG.getOrDefault(key, "0"));
        }
    }
    
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public Color getColorProperty(String key) {
        String colorStr = getProperty(key);
        try {
            return Color.decode(colorStr);
        } catch (NumberFormatException e) {
            return Color.BLUE; // Fallback color
        }
    }
    
    // Специфичные геттеры для часто используемых свойств
    public String getLauncherName() { return getProperty("launcher.name"); }
    public String getTheme() { return getProperty("theme.color"); }
    public boolean isAnimationEnabled() { return getBooleanProperty("animation.enabled"); }
    public int getAllocatedMemory() { return getIntProperty("memory.allocated"); }
    public Dimension getWindowSize() { 
        return new Dimension(getIntProperty("window.width"), getIntProperty("window.height")); 
    }
    
    // Утилиты для шифрования (упрощенные)
    private String encrypt(String data) {
        // В реальной реализации использовать AES шифрование
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    
    private String decrypt(String data) {
        try {
            return new String(Base64.getDecoder().decode(data));
        } catch (Exception e) {
            return "";
        }
    }
}
