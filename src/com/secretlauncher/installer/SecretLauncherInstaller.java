// src/com/secretlauncher/installer/SecretLauncherInstaller.java
package com.secretlauncher.installer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SecretLauncherInstaller extends JFrame {
    private static final Color NEON_BLUE = new Color(0, 200, 255);
    private static final Color DARK_BG = new Color(10, 10, 25);
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private InstallManager installManager;
    private String installPath;
    
    public SecretLauncherInstaller() {
        setupWindow();
        createUI();
        installManager = new InstallManager();
        setVisible(true);
    }
    
    private void setupWindow() {
        setTitle("Secret Launcher Installer");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 20, 20));
    }
    
    private void createUI() {
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        cardPanel.add(createWelcomeScreen(), "welcome");
        cardPanel.add(createLicenseScreen(), "license"); 
        cardPanel.add(createPathScreen(), "path");
        cardPanel.add(createInstallScreen(), "install");
        cardPanel.add(createTasksScreen(), "tasks");
        cardPanel.add(createJavaScreen(), "java");
        cardPanel.add(createFinishScreen(), "finish");
        
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, NEON_BLUE));
        topBar.setPreferredSize(new Dimension(900, 60));
        
        JLabel logo = new JLabel("⚡ Secret Launcher Installer", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(NEON_BLUE);
        logo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));
        
        JButton closeBtn = new CloseButton();
        
        topBar.add(logo, BorderLayout.WEST);
        topBar.add(closeBtn, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createWelcomeScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🎮 Добро пожаловать в Secret Launcher!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        JTextArea desc = new JTextArea("Этот Мастер поможет вам выполнить установку\nSecret Launcher на ваш компьютер.\n\nДля продолжения установки, нажмите \"Продолжить\" 🚀");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(Color.LIGHT_GRAY);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel buttonPanel = createButtonPanel(false, "Продолжить", "Отмена");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(desc, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createLicenseScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("📄 Лицензионное соглашение", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JTextArea license = new JTextArea(
            "Secret Launcher - Лицензионное соглашение\n\n" +
            "🔹 Бесплатное ПО. Можно устанавливать, тестировать без ограничений\n" +
            "🔹 Можно распространять копии при условии сохранения оригинального архива\n" +
            "🔹 Не требует личных данных\n" +
            "🔹 Автоматические обновления\n\n" +
            "Нажимая \"Принимаю\", вы соглашаетесь с условиями использования."
        );
        license.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        license.setForeground(Color.LIGHT_GRAY);
        license.setEditable(false);
        license.setOpaque(false);
        license.setLineWrap(true);
        license.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(license);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(NEON_BLUE, 1));
        
        JCheckBox acceptBox = new JCheckBox("✅ Я принимаю условия лицензионного соглашения");
        acceptBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        acceptBox.setForeground(Color.WHITE);
        acceptBox.setOpaque(false);
        
        JPanel buttonPanel = createButtonPanel(true, "Назад", "Продолжить", "Отмена");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(acceptBox, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPathScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("📁 Выбор папки установки", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.setOpaque(false);
        
        installPath = System.getenv("APPDATA") + "\\SecretLauncher";
        JTextField pathField = new JTextField(installPath);
        pathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pathField.setBackground(new Color(30, 30, 45));
        pathField.setForeground(Color.WHITE);
        
        JButton browseBtn = new JButton("Обзор...");
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        pathPanel.add(pathField, BorderLayout.CENTER);
        pathPanel.add(browseBtn, BorderLayout.EAST);
        
        JPanel buttonPanel = createButtonPanel(true, "Назад", "Продолжить", "Отмена");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(pathPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createInstallScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🔄 Установка файлов", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setForeground(NEON_BLUE);
        progressBar.setBackground(new Color(30, 30, 45));
        
        JTextArea logArea = new JTextArea();
        logArea.setBackground(new Color(20, 20, 35));
        logArea.setForeground(Color.LIGHT_GRAY);
        logArea.setEditable(false);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTasksScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🎯 Дополнительные задачи", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel tasksPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        tasksPanel.setOpaque(false);
        
        JCheckBox launchCheck = new JCheckBox("🚀 Запустить Secret Launcher после установки");
        JCheckBox desktopCheck = new JCheckBox("📁 Создать ярлык на рабочем столе");
        JCheckBox gpuCheck = new JCheckBox("🎮 Добавить лучшие настройки GPU для игры");
        JCheckBox javaCheck = new JCheckBox("☕ Автоматически установить Java (рекомендуется)");
        
        for (JCheckBox check : new JCheckBox[]{launchCheck, desktopCheck, gpuCheck, javaCheck}) {
            check.setSelected(true);
            check.setForeground(Color.WHITE);
            check.setOpaque(false);
            tasksPanel.add(check);
        }
        
        JPanel buttonPanel = createButtonPanel(true, "Назад", "Продолжить", "Отмена");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(tasksPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createJavaScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("☕ Установка Java", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JProgressBar javaProgress = new JProgressBar();
        javaProgress.setForeground(NEON_BLUE);
        javaProgress.setStringPainted(true);
        
        JLabel statusLabel = new JLabel("Подготовка к установке Java...", SwingConstants.CENTER);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(javaProgress, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFinishScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("🎉 Установка завершена!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        JTextArea message = new JTextArea(
            "Secret Launcher успешно установлен на ваш компьютер!\n\n" +
            "✅ Файлы установлены\n" +
            "✅ Java настроена\n" +
            "✅ Ярлыки созданы\n" +
            "✅ Готов к использованию!\n\n" +
            "Спасибо что выбрали Secret Launcher! 🚀"
        );
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        message.setForeground(Color.LIGHT_GRAY);
        message.setEditable(false);
        message.setOpaque(false);
        message.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel buttonPanel = createButtonPanel(false, "Завершить");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(message, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel(boolean showBack, String... buttons) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        for (String text : buttons) {
            NeonButton btn = new NeonButton(text);
            
            if (text.equals("Продолжить")) {
                btn.addActionListener(e -> nextScreen());
            } else if (text.equals("Назад")) {
                btn.addActionListener(e -> prevScreen());
            } else if (text.equals("Отмена")) {
                btn.addActionListener(e -> System.exit(0));
            } else if (text.equals("Завершить")) {
                btn.addActionListener(e -> System.exit(0));
            }
            
            panel.add(btn);
        }
        
        return panel;
    }
    
    private void nextScreen() {
        cardLayout.next(cardPanel);
    }
    
    private void prevScreen() {
        cardLayout.previous(cardPanel);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new SecretLauncherInstaller();
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

class TransparentPanel extends JPanel {
    public TransparentPanel() {
        setOpaque(false);
    }
}
