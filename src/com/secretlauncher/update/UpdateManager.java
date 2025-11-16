// src/com/secretlauncher/update/UpdateManager.java
package com.secretlauncher.update;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class UpdateManager {
    private static final String UPDATE_URL = "https://api.secretlauncher.com/update";
    private static final String VERSION_FILE = "version.info";
    
    public boolean checkForUpdates() {
        try {
            String currentVersion = getCurrentVersion();
            String latestVersion = getLatestVersion();
            
            return !currentVersion.equals(latestVersion);
        } catch (Exception e) {
            return false;
        }
    }
    
    public void downloadUpdate(JProgressBar progressBar, JTextArea logArea) {
        new Thread(() -> {
            try {
                logArea.append("🔍 Проверяем обновления...\n");
                
                String latestVersion = getLatestVersion();
                String downloadUrl = getDownloadUrl(latestVersion);
                
                logArea.append("📥 Найдена новая версия: " + latestVersion + "\n");
                logArea.append("💾 Скачиваем обновление...\n");
                
                // Скачиваем файл обновления
                String tempFile = downloadUpdateFile(downloadUrl, progressBar, logArea);
                
                logArea.append("⚙️ Устанавливаем обновление...\n");
                
                // Устанавливаем обновление
                installUpdate(tempFile, logArea);
                
                logArea.append("✅ Обновление успешно установлено!\n");
                
            } catch (Exception e) {
                logArea.append("❌ Ошибка обновления: " + e.getMessage() + "\n");
            }
        }).start();
    }
    
    private String getCurrentVersion() throws IOException {
        File versionFile = new File(VERSION_FILE);
        if (versionFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(versionFile));
            String version = reader.readLine();
            reader.close();
            return version != null ? version : "1.0.0";
        }
        return "1.0.0";
    }
    
    private String getLatestVersion() throws IOException {
        URL url = new URL(UPDATE_URL + "/version");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String version = reader.readLine();
        reader.close();
        
        return version != null ? version : "1.0.0";
    }
    
    private String getDownloadUrl(String version) throws IOException {
        URL url = new URL(UPDATE_URL + "/download?version=" + version);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String downloadUrl = reader.readLine();
        reader.close();
        
        return downloadUrl;
    }
    
    private String downloadUpdateFile(String downloadUrl, JProgressBar progressBar, JTextArea logArea) 
            throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        String tempDir = System.getProperty("java.io.tmpdir");
        String fileName = "secret_launcher_update_" + System.currentTimeMillis() + ".jar";
        String filePath = tempDir + File.separator + fileName;
        
        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(filePath)) {
            
            long fileSize = conn.getContentLengthLong();
            byte[] buffer = new byte[8192];
            long totalRead = 0;
            int bytesRead;
            
            progressBar.setMaximum((int) fileSize);
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                
                final int progress = (int) totalRead;
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(progress);
                    logArea.append("📥 Загружено: " + (progress / 1024 / 1024) + " MB\n");
                });
            }
        }
        
        return filePath;
    }
    
    private void installUpdate(String updateFile, JTextArea logArea) throws IOException {
        // Создаем батник для обновления
        String batContent = createUpdateScript(updateFile);
        String batPath = System.getProperty("java.io.tmpdir") + "\\update_launcher.bat";
        
        try (PrintWriter writer = new PrintWriter(batPath)) {
            writer.println(batContent);
        }
        
        // Запускаем батник
        Runtime.getRuntime().exec("cmd /c start " + batPath);
        
        // Завершаем текущее приложение
        System.exit(0);
    }
    
    private String createUpdateScript(String updateFile) {
        return "@echo off\n" +
               "echo 🚀 Обновление Secret Launcher...\n" +
               "timeout /t 2 /nobreak >nul\n" +
               "taskkill /f /im javaw.exe >nul 2>&1\n" +
               "timeout /t 1 /nobreak >nul\n" +
               "copy \"" + updateFile + "\" \"SecretLauncher.jar\" >nul\n" +
               "start javaw -jar SecretLauncher.jar\n" +
               "del \"" + updateFile + "\" >nul\n" +
               "del \"%~f0\" >nul";
    }
}
