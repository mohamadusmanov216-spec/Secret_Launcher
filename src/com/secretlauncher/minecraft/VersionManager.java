// src/com/secretlauncher/minecraft/VersionManager.java
package com.secretlauncher.minecraft;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class VersionManager {
    private File minecraftDir;
    private List<MinecraftVersion> availableVersions;
    
    public VersionManager() {
        this.minecraftDir = new File(System.getProperty("user.home"), 
                                   "AppData\\Roaming\\.minecraft");
        this.availableVersions = new ArrayList<>();
        loadAvailableVersions();
    }
    
    public void loadAvailableVersions() {
        File versionsDir = new File(minecraftDir, "versions");
        if (!versionsDir.exists()) return;
        
        File[] versionDirs = versionsDir.listFiles();
        if (versionDirs == null) return;
        
        for (File versionDir : versionDirs) {
            if (versionDir.isDirectory()) {
                String versionId = versionDir.getName();
                File jarFile = new File(versionDir, versionId + ".jar");
                
                if (jarFile.exists()) {
                    availableVersions.add(new MinecraftVersion(versionId, jarFile));
                }
            }
        }
        
        // Сортируем версии по убыванию (новые сначала)
        availableVersions.sort((v1, v2) -> v2.getId().compareTo(v1.getId()));
    }
    
    public boolean downloadVersion(String versionId, JProgressBar progressBar) {
        // Здесь будет логика скачивания версии через Mojang API
        // Пока заглушка
        return true;
    }
    
    public List<MinecraftVersion> getAvailableVersions() {
        return new ArrayList<>(availableVersions);
    }
    
    public MinecraftVersion getLatestVersion() {
        return availableVersions.isEmpty() ? null : availableVersions.get(0);
    }
    
    public static class MinecraftVersion {
        private String id;
        private File jarFile;
        private String type; // release, snapshot, etc.
        private Date releaseTime;
        
        public MinecraftVersion(String id, File jarFile) {
            this.id = id;
            this.jarFile = jarFile;
        }
        
        // Геттеры и сеттеры
        public String getId() { return id; }
        public File getJarFile() { return jarFile; }
        public String getType() { return type; }
        public Date getReleaseTime() { return releaseTime; }
        
        public void setType(String type) { this.type = type; }
        public void setReleaseTime(Date releaseTime) { this.releaseTime = releaseTime; }
        
        @Override
        public String toString() {
            return id + (type != null ? " (" + type + ")" : "");
        }
    }
}
