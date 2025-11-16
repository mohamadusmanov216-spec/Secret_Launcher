package com.secretlauncher.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class VisualEffects {
    private static final Color NEON_BLUE = new Color(0, 200, 255);
    private static final Color NEON_PURPLE = new Color(180, 70, 255);
    private static final Random random = new Random();
    
    public static void applyNeonGlow(JComponent component) {
        component.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 2));
    }
    
    public static void applyParticleEffect(Graphics2D g2d, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Создаем частицы
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int size = random.nextInt(3) + 1;
            float alpha = random.nextFloat() * 0.5f + 0.3f;
            
            Color particleColor = random.nextBoolean() ? NEON_BLUE : NEON_PURPLE;
            g2d.setColor(new Color(
                particleColor.getRed(),
                particleColor.getGreen(), 
                particleColor.getBlue(),
                (int)(alpha * 255)
            ));
            
            g2d.fillOval(x, y, size, size);
        }
    }
    
    public static GradientPaint createNeonGradient(int width, int height) {
        return new GradientPaint(
            0, 0, new Color(0, 100, 200),
            width, height, new Color(100, 0, 200)
        );
    }
    
    public static void drawRoundedRect(Graphics2D g2d, int x, int y, int width, int height, int arc) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(20, 20, 35, 200));
        g2d.fillRoundRect(x, y, width, height, arc, arc);
        
        g2d.setColor(NEON_BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, arc, arc);
    }
}
