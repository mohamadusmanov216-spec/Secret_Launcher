// src/com/secretlauncher/ui/UIComponents.java
package com.secretlauncher.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class UIComponents {
    
    public static JButton createNeonButton(String text) {
        return new NeonButton(text);
    }
    
    public static JPanel createCardPanel() {
        return new CardPanel();
    }
    
    public static JProgressBar createNeonProgressBar() {
        JProgressBar progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Фон
                g2d.setColor(new Color(30, 30, 45));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Прогресс
                if (getValue() > 0) {
                    int width = (int) ((getWidth() - 4) * ((double) getValue() / getMaximum()));
                    g2d.setColor(new Color(0, 200, 255));
                    g2d.fillRoundRect(2, 2, width, getHeight() - 4, 8, 8);
                    
                    // Неоновое свечение
                    g2d.setColor(new Color(0, 200, 255, 100));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(2, 2, width, getHeight() - 4, 8, 8);
                }
                
                // Текст
                if (isStringPainted()) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getString();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString(text, x, y);
                }
            }
        };
        
        progressBar.setForeground(new Color(0, 200, 255));
        progressBar.setBackground(new Color(30, 30, 45));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return progressBar;
    }
    
    public static JTextField createNeonTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Фон
                g2d.setColor(new Color(30, 30, 45));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Текст
                super.paintComponent(g);
                
                // Обводка
                if (hasFocus()) {
                    g2d.setColor(new Color(0, 200, 255));
                } else {
                    g2d.setColor(new Color(100, 100, 120));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 8, 8);
            }
        };
        
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(0, 200, 255));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return field;
    }
}
