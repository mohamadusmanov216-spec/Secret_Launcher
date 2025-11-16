// SecretLauncherInstaller.java
package com.secretlauncher.installer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class SecretLauncherInstaller extends JFrame {
    
    // НЕОНОВЫЕ ЦВЕТА
    private static final Color NEON_BLUE = new Color(0, 200, 255);
    private static final Color DARK_BG = new Color(10, 10, 25);
    private static final Color CARD_BG = new Color(20, 20, 35);
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private List<File> installedFiles = new ArrayList<>();
    
    public SecretLauncherInstaller() {
        setupWindow();
        createUI();
        setVisible(true);
    }
    
    private void setupWindow() {
        setTitle("Secret Launcher Installer");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        // Закругленные углы
        setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 20, 20));
    }
    
    private void createUI() {
        // Главная панель с градиентом
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Верхняя панель с неоновой полосой и логотипом SL
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Центральная панель с экранами установки
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        
        // Добавляем экраны установки
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
        
        // Логотип SL неоновый
        JLabel logo = new JLabel("⚡ SL", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(NEON_BLUE);
        logo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));
        
        // Кнопка закрытия
        JButton closeBtn = new CloseButton();
        
        topBar.add(logo, BorderLayout.WEST);
        topBar.add(closeBtn, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createWelcomeScreen() {
        JPanel panel = new TransparentPanel();
        panel.setLayout(new BorderLayout());
        
        // Заголовок с иконкой
        JLabel title = new JLabel("🎮 Добро пожаловать в Secret Launcher!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        // Описание
        JTextArea desc = new JTextArea("Этот Мастер поможет вам выполнить установку\nSecret Launcher на ваш компьютер.\n\nДля продолжения установки, нажмите \"Продолжить\" 🚀");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(Color.LIGHT_GRAY);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setAlignmentX(CENTER_ALIGNMENT);
        
        // Кнопки
        JPanel buttonPanel = createButtonPanel("Продолжить", "Отмена");
        
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
        
        // Текст лицензии
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
        
        // Чекбоксы
        JCheckBox acceptBox = new JCheckBox("✅ Я принимаю условия лицензионного соглашения");
        acceptBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        acceptBox.setForeground(Color.WHITE);
        acceptBox.setOpaque(false);
        
        JCheckBox rejectBox = new JCheckBox("❌ Я не согласен с пунктами лицензионного соглашения");
        rejectBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rejectBox.setForeground(Color.WHITE);
        rejectBox.setOpaque(false);
        
        // Взаимоисключающие чекбоксы
        acceptBox.addActionListener(e -> {
            if (acceptBox.isSelected()) rejectBox.setSelected(false);
        });
        
        rejectBox.addActionListener(e -> {
            if (rejectBox.isSelected()) acceptBox.setSelected(false);
        });
        
        JPanel checkPanel = new JPanel(new GridLayout(2, 1));
        checkPanel.setOpaque(false);
        checkPanel.add(acceptBox);
        checkPanel.add(rejectBox);
        
        // Кнопки
        JPanel buttonPanel = createButtonPanel("Назад", "Продолжить", "Отмена");
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(checkPanel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Другие методы создания экранов...
    private JPanel createButtonPanel(String... buttons) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        for (String text : buttons) {
            NeonButton btn = new NeonButton(text);
            panel.add(btn);
        }
        
        return panel;
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

// Градиентная панель
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

// Неоновая кнопка
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
        
        // Закругленный прямоугольник
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        // Неоновая обводка
        g2d.setColor(new Color(0, 200, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
        
        super.paintComponent(g);
    }
}

// Прозрачная панель
class TransparentPanel extends JPanel {
    public TransparentPanel() {
        setOpaque(false);
    }
}

// Кнопка закрытия
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
