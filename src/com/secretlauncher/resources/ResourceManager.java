// src/com/secretlauncher/resources/ResourceManager.java
package com.secretlauncher.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ResourceManager {
    private Map<String, Image> images;
    private Map<String, Font> fonts;
    private Map<String, String> texts;
    
    public ResourceManager() {
        this.images = new HashMap<>();
        this.fonts = new HashMap<>();
        this.texts = new HashMap<>();
        loadResources();
    }
    
    private void loadResources() {
        loadImages();
        loadFonts();
        loadTexts();
    }
    
    private void loadImages() {
        try {
            // Загрузка иконок и изображений
            // Пока заглушки
        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображений: " + e.getMessage());
        }
    }
    
    private void loadFonts() {
        try {
            // Загрузка кастомных шрифтов
            Font neonFont = Font.createFont(Font.TRUETYPE_FONT,
                getClass().getResourceAsStream("/fonts/neon.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(neonFont);
            
            fonts.put("neon", neonFont.deriveFont(14f));
            
        } catch (Exception e) {
            // Используем системные шрифты если кастомные не загрузились
            fonts.put("neon", new Font("Segoe UI", Font.PLAIN, 14));
        }
    }
    
    private void loadTexts() {
        // Загрузка локализованных текстов
        texts.put("title", "Secret Launcher");
        texts.put("play", "Играть");
        texts.put("settings", "Настройки");
        texts.put("exit", "Выход");
    }
    
    public Image getImage(String name) {
        return images.get(name);
    }
    
    public Font getFont(String name) {
        return fonts.get(name);
    }
    
    public String getText(String key) {
        return texts.getOrDefault(key, key);
    }
    
    public ImageIcon getIcon(String name) {
        Image img = getImage(name);
        return img != null ? new ImageIcon(img) : null;
    }
}
