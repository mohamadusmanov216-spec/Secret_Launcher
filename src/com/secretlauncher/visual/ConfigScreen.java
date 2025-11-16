package com.secretlauncher.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConfigScreen extends JFrame {
    private static final Color NEON_BLUE = new Color(0, 200, 255);
    private static final Color DARK_BG = new Color(10, 10, 25);
    
    public ConfigScreen() {
        setupWindow();
        createUI();
    }
    
    private void setupWindow() {
        setTitle("🎨 Настройки визуалов - Secret Launcher");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
    }
    
    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, DARK_BG, getWidth(), getHeight(), new Color(20, 20, 40));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                VisualEffects.applyParticleEffect(g2d, getWidth(), getHeight());
            }
        };
        
        // Заголовок
        JLabel title = new JLabel("⚙️ НАСТРОЙКИ ВИЗУАЛОВ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(NEON_BLUE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Панель настроек
        JPanel settingsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        settingsPanel.setOpaque(false);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Элементы настроек
        settingsPanel.add(createSettingRow("🔆 Неоновое свечение", new JCheckBox("", true)));
        settingsPanel.add(createSettingRow("🎯 Эффекты частиц", new JCheckBox("", true)));
        settingsPanel.add(createSettingRow("🌈 Цветовая схема", new JComboBox<>(new String[]{"Синяя", "Фиолетовая", "Зеленая", "Красная"})));
        
        JSlider transparencySlider = new JSlider(0, 100, 80);
        transparencySlider.setBackground(new Color(0, 0, 0, 0));
        settingsPanel.add(createSettingRow("🔍 Прозрачность", transparencySlider));
        
        JSlider animationSlider = new JSlider(0, 100, 60);
        animationSlider.setBackground(new Color(0, 0, 0, 0));
        settingsPanel.add(createSettingRow("⚡ Скорость анимаций", animationSlider));
        
        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton saveBtn = new JButton("💾 Сохранить") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(NEON_BLUE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveBtn.addActionListener(e -> dispose());
        
        JButton closeBtn = new JButton("✕ Закрыть") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 50, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);
        
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createSettingRow(String label, JComponent component) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        
        JLabel title = new JLabel(label);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        title.setForeground(Color.WHITE);
        
        component.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setBackground(new Color(30, 30, 45));
            ((JComboBox<?>) component).setForeground(Color.WHITE);
        }
        
        row.add(title, BorderLayout.WEST);
        row.add(component, BorderLayout.EAST);
        
        return row;
    }
}
