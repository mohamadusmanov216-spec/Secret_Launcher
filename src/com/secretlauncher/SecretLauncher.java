// src/com/secretlauncher/SecretLauncher.java
package com.secretlauncher;

import com.secretlauncher.visual.VisualEffects;
import com.secretlauncher.visual.ConfigScreen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SecretLauncher extends JFrame {
    private static final Color NEON_BLUE = new Color(0, 200, 255);
    private static final Color DARK_BG = new Color(10, 10, 25);
    
    private String currentNickname = "Player";
    private List<String> nicknameHistory = new ArrayList<>();
    private VisualEffects visualEffects;
    
    public SecretLauncher() {
        setupWindow();
        createUI();
        loadConfig();
        setVisible(true);
    }
    
    private void setupWindow() {
        setTitle("Secret Launcher");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new RoundRectangle2D.Double(0, 0, 1200, 800, 20, 20));
    }
    
    private void createUI() {
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Верхняя панель
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Центральная панель
        mainPanel.add(createMainContent(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_BLUE));
        topBar.setPreferredSize(new Dimension(1200, 50));
        
        JLabel title = new JLabel("⚡ Secret Launcher", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(NEON_BLUE);
        
        JButton closeBtn = new CloseButton();
        
        topBar.add(title, BorderLayout.CENTER);
        topBar.add(closeBtn, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createMainContent() {
        JPanel content = new JPanel(new GridLayout(2, 2, 20, 20));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Блок смены ника
        content.add(createNickBlock());
        
        // Блок серверов
        content.add(createServerBlock());
        
        // Блок запуска
        content.add(createLaunchBlock());
        
        // Блок настроек
        content.add(createSettingsBlock());
        
        return content;
    }
    
    private JPanel createNickBlock() {
        JPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("👤 СМЕНА НИКА", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        
        JTextField nickField = new JTextField(currentNickname);
        nickField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nickField.setBackground(new Color(30, 30, 45));
        nickField.setForeground(Color.WHITE);
        nickField.setCaretColor(NEON_BLUE);
        
        JButton changeBtn = new NeonButton("Сменить ник");
        changeBtn.addActionListener(e -> changeNickname(nickField.getText()));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(nickField, BorderLayout.CENTER);
        panel.add(changeBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createServerBlock() {
        JPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🌐 СЕТЕВАЯ ИГРА", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        
        JTextField serverField = new JTextField("mc.hypixel.net");
        serverField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        serverField.setBackground(new Color(30, 30, 45));
        serverField.setForeground(Color.WHITE);
        
        JButton connectBtn = new NeonButton("🔗 Подключиться");
        connectBtn.addActionListener(e -> connectToServer(serverField.getText()));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(serverField, BorderLayout.CENTER);
        panel.add(connectBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createLaunchBlock() {
        JPanel panel = new CardPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        
        JLabel title = new JLabel("🎮 ЗАПУСК ИГРЫ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        
        JButton singlePlayer = new NeonButton("🔄 Одиночная игра");
        JButton multiPlayer = new NeonButton("🌐 Сетевая игра");
        JButton settings = new NeonButton("⚙️ Настройки");
        
        panel.add(title);
        panel.add(singlePlayer);
        panel.add(multiPlayer);
        panel.add(settings);
        
        return panel;
    }
    
    private JPanel createSettingsBlock() {
        JPanel panel = new CardPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🎨 ВИЗУАЛЫ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        
        JButton visualSettings = new NeonButton("Открыть настройки визуалов");
        visualSettings.addActionListener(e -> new ConfigScreen().setVisible(true));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(visualSettings, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void changeNickname(String newNick) {
        this.currentNickname = newNick;
        nicknameHistory.add(newNick);
        saveConfig();
        JOptionPane.showMessageDialog(this, "✅ Ник изменен на: " + newNick);
    }
    
    private void connectToServer(String serverIP) {
        // Логика подключения к серверу
        JOptionPane.showMessageDialog(this, "🔄 Подключаемся к серверу: " + serverIP);
    }
    
    private void loadConfig() {
        // Загрузка конфигурации
        try {
            File configFile = new File("secretlauncher.cfg");
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("nickname=")) {
                        currentNickname = line.substring(9);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveConfig() {
        // Сохранение конфигурации
        try {
            PrintWriter writer = new PrintWriter("secretlauncher.cfg");
            writer.println("nickname=" + currentNickname);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SecretLauncher();
        });
    }
}

class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(10, 10, 25);
        Color color2 = new Color(20, 20, 40);
        GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

class CardPanel extends JPanel {
    public CardPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(20, 20, 35, 200));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        g2d.setColor(new Color(0, 200, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
    }
}

class NeonButton extends JButton {
    public NeonButton(String text) {
        super(text);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 200, 255, 80));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setFocusPainted(false);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(0, 200, 255, 120));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(0, 200, 255, 80));
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        g2d.setColor(new Color(0, 200, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
        
        super.paintComponent(g);
    }
}

class CloseButton extends JButton {
    public CloseButton() {
        setText("✕");
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(new Color(255, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setFocusPainted(false);
        
        addActionListener(e -> System.exit(0));
    }
}
