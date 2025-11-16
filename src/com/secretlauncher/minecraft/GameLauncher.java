// src/com/secretlauncher/minecraft/GameLauncher.java
package com.secretlauncher.minecraft;

import com.secretlauncher.utils.Logger;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class GameLauncher {
    private Process minecraftProcess;
    
    public boolean launchGame(String version, String username, int memoryMB) {
        try {
            Logger.info("🎮 Запуск Minecraft версии: " + version);
            
            // Формируем команду запуска
            List<String> command = new ArrayList<>();
            command.add(getJavaPath());
            command.add("-Xmx" + memoryMB + "M");
            command.add("-Xms" + (memoryMB/2) + "M");
            command.add("-Djava.library.path=natives");
            command.add("-cp");
            command.add(getClasspath(version));
            command.add("net.minecraft.client.main.Main");
            command.add("--username");
            command.add(username);
            command.add("--version");
            command.add(version);
            command.add("--gameDir");
            command.add(getGameDir().getAbsolutePath());
            command.add("--assetsDir");
            command.add(new File(getGameDir(), "assets").getAbsolutePath());
            command.add("--assetIndex");
            command.add(getAssetIndex(version));
            command.add("--accessToken");
            command.add("0");
            command.add("--userType");
            command.add("mojang");
            command.add("--versionType");
            command.add("release");
            
            // Запускаем процесс
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(getGameDir());
            minecraftProcess = pb.start();
            
            // Мониторим вывод
            startOutputMonitoring();
            
            Logger.info("✅ Minecraft успешно запущен");
            return true;
            
        } catch (Exception e) {
            Logger.error("❌ Ошибка запуска Minecraft", e);
            return false;
        }
    }
    
    private String getJavaPath() {
        String javaHome = System.getProperty("java.home");
        return javaHome + File.separator + "bin" + File.separator + "java" + 
               (System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "");
    }
    
    private String getClasspath(String version) throws IOException {
        File versionsDir = new File(getGameDir(), "versions");
        File versionDir = new File(versionsDir, version);
        File jarFile = new File(versionDir, version + ".jar");
        
        if (!jarFile.exists()) {
            throw new IOException("Версия " + version + " не найдена");
        }
        
        // Добавляем библиотеки
        StringBuilder classpath = new StringBuilder();
        classpath.append(jarFile.getAbsolutePath());
        
        File librariesDir = new File(getGameDir(), "libraries");
        addLibrariesToClasspath(librariesDir, classpath);
        
        return classpath.toString();
    }
    
    private void addLibrariesToClasspath(File libDir, StringBuilder classpath) {
        if (!libDir.exists()) return;
        
        File[] libs = libDir.listFiles();
        if (libs == null) return;
        
        for (File lib : libs) {
            if (lib.isFile() && lib.getName().endsWith(".jar")) {
                classpath.append(File.pathSeparator).append(lib.getAbsolutePath());
            } else if (lib.isDirectory()) {
                addLibrariesToClasspath(lib, classpath);
            }
        }
    }
    
    private File getGameDir() {
        return new File(System.getProperty("user.home"), "AppData\\Roaming\\.minecraft");
    }
    
    private String getAssetIndex(String version) {
        // Заглушка - в реальности нужно парсить version.json
        return "1.16";
    }
    
    private void startOutputMonitoring() {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                 new InputStreamReader(minecraftProcess.getInputStream()))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    Logger.info("[MC] " + line);
                }
            } catch (IOException e) {
                Logger.error("Ошибка чтения вывода Minecraft", e);
            }
        }).start();
        
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                 new InputStreamReader(minecraftProcess.getErrorStream()))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    Logger.error("[MC-ERROR] " + line);
                }
            } catch (IOException e) {
                Logger.error("Ошибка чтения ошибок Minecraft", e);
            }
        }).start();
    }
    
    public void stopGame() {
        if (minecraftProcess != null && minecraftProcess.isAlive()) {
            minecraftProcess.destroy();
            Logger.info("⏹️ Minecraft остановлен");
        }
    }
}
