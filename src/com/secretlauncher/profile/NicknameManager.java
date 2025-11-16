// src/com/secretlauncher/profile/NicknameManager.java
package com.secretlauncher.profile;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class NicknameManager {
    private String currentNickname;
    private List<String> nicknameHistory;
    private Set<String> premiumNicks;
    private File configFile;
    
    public NicknameManager() {
        this.nicknameHistory = new ArrayList<>();
        this.premiumNicks = new HashSet<>();
        this.configFile = new File("nicknames.cfg");
        loadNicknames();
    }
    
    public boolean changeNickname(String newNick) {
        if (isValidNickname(newNick)) {
            // Сохраняем старый ник в историю
            if (currentNickname != null && !currentNickname.equals(newNick)) {
                nicknameHistory.add(0, currentNickname);
                
                // Ограничиваем историю 20 никами
                if (nicknameHistory.size() > 20) {
                    nicknameHistory = new ArrayList<>(nicknameHistory.subList(0, 20));
                }
            }
            
            currentNickname = newNick;
            saveNicknames();
            return true;
        }
        return false;
    }
    
    public boolean isValidNickname(String nick) {
        if (nick == null || nick.trim().isEmpty()) {
            return false;
        }
        
        // Проверяем длину
        if (nick.length() < 3 || nick.length() > 16) {
            return false;
        }
        
        // Проверяем разрешенные символы
        if (!nick.matches("^[a-zA-Z0-9_]+$")) {
            return false;
        }
        
        // Проверяем не занят ли ник (здесь можно добавить проверку через Mojang API)
        return !isNicknameTaken(nick);
    }
    
    private boolean isNicknameTaken(String nick) {
        // Заглушка для проверки занятости ника
        // В реальной реализации здесь будет запрос к Mojang API
        return false;
    }
    
    public String generateRandomNick() {
        String[] prefixes = {"Dark", "Shadow", "Neon", "Cyber", "Ghost", "Stealth", "Quantum", "Atomic"};
        String[] suffixes = {"Warrior", "Hunter", "Slayer", "Master", "Killer", "Destroyer", "Pro", "God"};
        String[] numbers = {"123", "456", "789", "007", "999", "111", "222", "333"};
        
        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        String number = numbers[random.nextInt(numbers.length)];
        
        return prefix + suffix + number;
    }
    
    public void showNicknameChanger(JFrame parent) {
        JDialog dialog = new JDialog(parent, "🎭 Смена ника", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);
        dialog.setShape(new Rectangle(0, 0, 400, 300));
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(10, 10, 25), 
                                                          getWidth(), getHeight(), new Color(20, 20, 40));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Заголовок
        JLabel title = new JLabel("🎭 СМЕНА НИКА", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(0, 200, 255));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Поле ввода ника
        JTextField nickField = new JTextField(currentNickname);
        nickField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nickField.setBackground(new Color(30, 30, 45));
        nickField.setForeground(Color.WHITE);
        nickField.setCaretColor(new Color(0, 200, 255));
        nickField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Кнопка генерации случайного ника
        JButton randomBtn = new JButton("🎲 Случайный ник");
        randomBtn.addActionListener(e -> nickField.setText(generateRandomNick()));
        
        // Кнопки действий
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton applyBtn = new JButton("✅ Применить");
        applyBtn.addActionListener(e -> {
            if (changeNickname(nickField.getText())) {
                JOptionPane.showMessageDialog(dialog, "✅ Ник успешно изменен на: " + nickField.getText());
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "❌ Некорректный ник!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = new JButton("❌ Отмена");
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(applyBtn);
        buttonPanel.add(cancelBtn);
        
        // Панель истории
        JPanel historyPanel = createHistoryPanel();
        
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(nickField, BorderLayout.CENTER);
        mainPanel.add(randomBtn, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(new JScrollPane(historyPanel), BorderLayout.EAST);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 35, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(150, 200));
        
        JLabel title = new JLabel("📜 История:", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String nick : nicknameHistory) {
            listModel.addElement(nick);
        }
        
        JList<String> historyList = new JList<>(listModel);
        historyList.setBackground(new Color(30, 30, 45));
        historyList.setForeground(Color.LIGHT_GRAY);
        historyList.setSelectionBackground(new Color(0, 200, 255));
        historyList.setSelectionForeground(Color.WHITE);
        
        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = historyList.getSelectedValue();
                if (selected != null) {
                    // Можно добавить логику применения выбранного ника
                }
            }
        });
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(historyList), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadNicknames() {
        try {
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                
                if ((line = reader.readLine()) != null) {
                    currentNickname = line;
                }
                
                while ((line = reader.readLine()) != null) {
                    nicknameHistory.add(line);
                }
                
                reader.close();
            } else {
                // Устанавливаем ник по умолчанию
                currentNickname = "Player" + (new Random().nextInt(9000) + 1000);
            }
        } catch (IOException e) {
            currentNickname = "Player" + (new Random().nextInt(9000) + 1000);
        }
    }
    
    private void saveNicknames() {
        try {
            PrintWriter writer = new PrintWriter(configFile);
            writer.println(currentNickname);
            
            for (String nick : nicknameHistory) {
                writer.println(nick);
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Геттеры
    public String getCurrentNickname() {
        return currentNickname;
    }
    
    public List<String> getNicknameHistory() {
        return new ArrayList<>(nicknameHistory);
    }
    
    public boolean isPremiumNick(String nick) {
        return premiumNicks.contains(nick);
    }
}
