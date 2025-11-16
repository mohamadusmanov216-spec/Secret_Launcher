// src/com/secretlauncher/installer/JavaInstaller.java
package com.secretlauncher.installer;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class JavaInstaller {
    
    public static boolean installJavaAutomatically(JTextArea logArea) {
        try {
            logArea.append("🔧 Начинаем автоматическую установку Java...\n");
            
            // URL для скачивания Java (замени на актуальный)
            String javaUrl = "https://javadl.oracle.com/webapps/download/AutoDL?BundleId=248242_ce59cff5c23f4e2eaf4e778a117d4c5b";
            String tempDir = System.getProperty("java.io.tmpdir");
            String installerPath = tempDir + "\\java_installer.exe";
            
            logArea.append("📥 Скачиваем установщик Java...\n");
            
            // Скачиваем установщик
            downloadFile(javaUrl, installerPath, logArea);
            
            logArea.append("⚙️ Запускаем установку Java...\n");
            
            // Запускаем установщик в тихом режиме
            Process process = Runtime.getRuntime().exec(installerPath + " /s");
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                logArea.append("✅ Java успешно установлена!\n");
                
                // Удаляем временный файл
                new File(installerPath).delete();
                
                return true;
            } else {
                logArea.append("❌ Ошибка установки Java. Код: " + exitCode + "\n");
                return false;
            }
            
        } catch (Exception e) {
            logArea.append("❌ Ошибка при установке Java: " + e.getMessage() + "\n");
            return false;
        }
    }
    
    private static void downloadFile(String urlString, String filePath, JTextArea logArea) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(filePath)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalRead = 0;
            long fileSize = connection.getContentLengthLong();
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                
                if (fileSize > 0) {
                    int progress = (int) ((totalRead * 100) / fileSize);
                    logArea.append("📥 Прогресс загрузки: " + progress + "%\n");
                }
            }
        }
        
        connection.disconnect();
    }
    
    public static boolean isJavaInstalled() {
        try {
            Process process = Runtime.getRuntime().exec("java -version");
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String getJavaVersion() {
        try {
            Process process = Runtime.getRuntime().exec("java -version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = reader.readLine();
            return line != null ? line : "Unknown";
        } catch (Exception e) {
            return "Not installed";
        }
    }
}
