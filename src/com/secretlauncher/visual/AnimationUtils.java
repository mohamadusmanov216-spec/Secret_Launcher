// src/com/secretlauncher/visual/AnimationUtils.java
package com.secretlauncher.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class AnimationUtils {
    private static final Random random = new Random();
    
    public static void fadeIn(JComponent component, int duration) {
        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long currentTime = System.currentTimeMillis();
            float progress = (float)(currentTime - startTime) / duration;
            
            if (progress >= 1.0f) {
                component.setVisible(true);
                timer.stop();
            } else {
                float alpha = progress;
                setComponentAlpha(component, alpha);
            }
        });
        
        component.setVisible(false);
        timer.start();
    }
    
    public static void slideIn(JComponent component, int startX, int duration) {
        Point originalLocation = component.getLocation();
        component.setLocation(startX, originalLocation.y);
        
        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long currentTime = System.currentTimeMillis();
            float progress = (float)(currentTime - startTime) / duration;
            
            if (progress >= 1.0f) {
                component.setLocation(originalLocation);
                timer.stop();
            } else {
                int currentX = startX + (int)((originalLocation.x - startX) * progress);
                component.setLocation(currentX, originalLocation.y);
            }
        });
        
        timer.start();
    }
    
    public static void pulseEffect(JComponent component, int duration) {
        Color originalColor = component.getBackground();
        Timer timer = new Timer(50, null);
        final long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long currentTime = System.currentTimeMillis();
            float progress = (float)(currentTime - startTime) / duration;
            
            if (progress >= 1.0f) {
                component.setBackground(originalColor);
                timer.stop();
            } else {
                float pulse = (float)(0.5f + 0.5f * Math.sin(progress * Math.PI * 4));
                Color pulseColor = new Color(
                    originalColor.getRed(),
                    originalColor.getGreen(),
                    originalColor.getBlue(),
                    (int)(pulse * 255)
                );
                component.setBackground(pulseColor);
            }
        });
        
        timer.start();
    }
    
    public static void neonGlow(JComponent component, Color glowColor, int duration) {
        Timer timer = new Timer(30, null);
        final long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long currentTime = System.currentTimeMillis();
            float progress = (float)(currentTime - startTime) / duration;
            
            if (progress >= 1.0f) {
                component.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                timer.stop();
            } else {
                float glowIntensity = (float)(0.5f + 0.5f * Math.sin(progress * Math.PI * 2));
                Color borderColor = new Color(
                    glowColor.getRed(),
                    glowColor.getGreen(), 
                    glowColor.getBlue(),
                    (int)(glowIntensity * 150)
                );
                component.setBorder(BorderFactory.createLineBorder(borderColor, 2));
            }
        });
        
        timer.start();
    }
    
    public static void createRippleEffect(Component parent, Point clickPoint) {
        JWindow rippleWindow = new JWindow((Window)parent);
        rippleWindow.setSize(parent.getSize());
        rippleWindow.setLocation(parent.getLocationOnScreen());
        rippleWindow.setBackground(new Color(0, 0, 0, 0));
        
        JPanel ripplePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Рисуем ripple эффект
                float progress = 0f;
                int maxRadius = (int)Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
                
                for (int i = 0; i < 3; i++) {
                    float alpha = 0.7f - i * 0.2f;
                    int radius = (int)(maxRadius * progress);
                    
                    g2d.setColor(new Color(0, 200, 255, (int)(alpha * 255 * (1 - progress))));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(
                        clickPoint.x - radius,
                        clickPoint.y - radius,
                        radius * 2,
                        radius * 2
                    );
                    
                    progress += 0.3f;
                }
            }
        };
        
        rippleWindow.add(ripplePanel);
        rippleWindow.setVisible(true);
        
        Timer timer = new Timer(20, e -> {
            rippleWindow.repaint();
        });
        timer.setRepeats(false);
        timer.start();
        
        Timer closeTimer = new Timer(500, e -> {
            rippleWindow.dispose();
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
    }
    
    private static void setComponentAlpha(JComponent component, float alpha) {
        // Для прозрачности компонентов
        component.setOpaque(alpha == 1.0f);
        component.repaint();
    }
    
    public static void shakeComponent(JComponent component, int intensity) {
        Point originalLocation = component.getLocation();
        Timer timer = new Timer(50, null);
        int[] offsets = {intensity, -intensity, intensity/2, -intensity/2, 0};
        int index = 0;
        
        timer.addActionListener(e -> {
            if (index < offsets.length) {
                int offset = offsets[index++];
                component.setLocation(originalLocation.x + offset, originalLocation.y);
            } else {
                component.setLocation(originalLocation);
                timer.stop();
            }
        });
        
        timer.start();
    }
}
