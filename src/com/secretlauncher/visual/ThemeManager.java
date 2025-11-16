// src/com/secretlauncher/visual/ThemeManager.java
package com.secretlauncher.visual;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ThemeManager {
    private Map<String, Theme> themes;
    private String currentTheme;
    
    public ThemeManager() {
        this.themes = new HashMap<>();
        this.currentTheme = "neon_blue";
        initializeThemes();
    }
    
    private void initializeThemes() {
        // Неоновая синяя тема
        themes.put("neon_blue", new Theme(
            "Neon Blue",
            new Color(10, 10, 25),      // background
            new Color(20, 20, 35),      // card
            new Color(0, 200, 255),     // primary (неоновый синий)
            new Color(100, 220, 255),   // accent
            new Color(255, 255, 255),   // text
            new Color(200, 200, 200),   // text_secondary
            new Color(255, 50, 50),     // error
            new Color(50, 255, 50),     // success
            true,                       // glow
            true                        // particles
        ));
        
        // Неоновая фиолетовая тема
        themes.put("neon_purple", new Theme(
            "Neon Purple", 
            new Color(15, 10, 25),
            new Color(25, 15, 35),
            new Color(180, 70, 255),
            new Color(200, 100, 255),
            Color.WHITE,
            new Color(200, 200, 200),
            new Color(255, 50, 50),
            new Color(50, 255, 50),
            true,
            true
        ));
        
        // Темная тема
        themes.put("dark", new Theme(
            "Dark",
            new Color(20, 20, 20),
            new Color(40, 40, 40),
            new Color(60, 60, 60),
            new Color(80, 80, 80),
            Color.WHITE,
            new Color(180, 180, 180),
            new Color(255, 80, 80),
            new Color(80, 255, 80),
            false,
            false
        ));
    }
    
    public void applyTheme(JComponent component) {
        Theme theme = themes.get(currentTheme);
        if (theme == null) return;
        
        if (component instanceof JPanel) {
            component.setBackground(theme.getBackgroundColor());
        } else if (component instanceof JButton) {
            component.setBackground(theme.getPrimaryColor());
            component.setForeground(theme.getTextColor());
        } else if (component instanceof JLabel) {
            component.setForeground(theme.getTextColor());
        } else if (component instanceof JTextField) {
            component.setBackground(theme.getCardColor());
            component.setForeground(theme.getTextColor());
        }
    }
    
    public void applyThemeToFrame(JFrame frame) {
        Theme theme = themes.get(currentTheme);
        if (theme == null) return;
        
        // Применяем тему ко всем компонентам фрейма
        applyThemeToContainer(frame.getContentPane());
    }
    
    private void applyThemeToContainer(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComponent) {
                applyTheme((JComponent) comp);
            }
            if (comp instanceof Container) {
                applyThemeToContainer((Container) comp);
            }
        }
    }
    
    public void setTheme(String themeName) {
        if (themes.containsKey(themeName)) {
            this.currentTheme = themeName;
        }
    }
    
    public Theme getCurrentTheme() {
        return themes.get(currentTheme);
    }
    
    public List<String> getAvailableThemes() {
        return new ArrayList<>(themes.keySet());
    }
    
    public static class Theme {
        private String name;
        private Color backgroundColor;
        private Color cardColor;
        private Color primaryColor;
        private Color accentColor;
        private Color textColor;
        private Color textSecondaryColor;
        private Color errorColor;
        private Color successColor;
        private boolean glowEffects;
        private boolean particleEffects;
        
        public Theme(String name, Color backgroundColor, Color cardColor, 
                    Color primaryColor, Color accentColor, Color textColor,
                    Color textSecondaryColor, Color errorColor, Color successColor,
                    boolean glowEffects, boolean particleEffects) {
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.cardColor = cardColor;
            this.primaryColor = primaryColor;
            this.accentColor = accentColor;
            this.textColor = textColor;
            this.textSecondaryColor = textSecondaryColor;
            this.errorColor = errorColor;
            this.successColor = successColor;
            this.glowEffects = glowEffects;
            this.particleEffects = particleEffects;
        }
        
        // Геттеры
        public String getName() { return name; }
        public Color getBackgroundColor() { return backgroundColor; }
        public Color getCardColor() { return cardColor; }
        public Color getPrimaryColor() { return primaryColor; }
        public Color getAccentColor() { return accentColor; }
        public Color getTextColor() { return textColor; }
        public Color getTextSecondaryColor() { return textSecondaryColor; }
        public Color getErrorColor() { return errorColor; }
        public Color getSuccessColor() { return successColor; }
        public boolean hasGlowEffects() { return glowEffects; }
        public boolean hasParticleEffects() { return particleEffects; }
        
        public GradientPaint createBackgroundGradient(int width, int height) {
            return new GradientPaint(
                0, 0, backgroundColor,
                width, height, darken(backgroundColor, 0.2f)
            );
        }
        
        private Color darken(Color color, float factor) {
            return new Color(
                Math.max((int)(color.getRed() * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0),
                color.getAlpha()
            );
        }
    }
}
