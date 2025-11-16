// src/com/secretlauncher/mods/ModManager.java
package com.secretlauncher.mods;

import com.secretlauncher.utils.Logger;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class ModManager {
    private File modsDir;
    private List<ModInfo> installedMods;
    
    public ModManager() {
        this.modsDir = new File(System.getProperty("user.home"), 
                              "AppData\\Roaming\\.minecraft\\mods");
        this.installedMods = new ArrayList<>();
        scanInstalledMods();
    }
    
    public void scanInstalledMods() {
        installedMods.clear();
        
        if (!modsDir.exists()) {
            modsDir.mkdirs();
            return;
        }
        
        File[] modFiles = modsDir.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".jar"));
        
        if (modFiles != null) {
            for (File modFile : modFiles) {
                ModInfo modInfo = extractModInfo(modFile);
                installedMods.add(modInfo);
            }
        }
        
        Logger.info("Найдено модов: " + installedMods.size());
    }
    
    private ModInfo extractModInfo(File modFile) {
        // Упрощенная версия - в реальности нужно парсить mods.toml или fabric.mod.json
        String name = modFile.getName().replace(".jar", "");
        return new ModInfo(name, "1.0.0", modFile, true);
    }
    
    public boolean installMod(File modFile) {
        try {
            File dest = new File(modsDir, modFile.getName());
            com.secretlauncher.utils.FileUtils.copyFile(modFile, dest);
            scanInstalledMods();
            return true;
        } catch (Exception e) {
            Logger.error("Ошибка установки мода", e);
            return false;
        }
    }
    
    public boolean removeMod(ModInfo mod) {
        if (mod.getFile().delete()) {
            installedMods.remove(mod);
            return true;
        }
        return false;
    }
    
    public List<ModInfo> getInstalledMods() {
        return new ArrayList<>(installedMods);
    }
    
    public static class ModInfo {
        private String name;
        private String version;
        private File file;
        private boolean enabled;
        
        public ModInfo(String name, String version, File file, boolean enabled) {
            this.name = name;
            this.version = version;
            this.file = file;
            this.enabled = enabled;
        }
        
        public String getName() { return name; }
        public String getVersion() { return version; }
        public File getFile() { return file; }
        public boolean isEnabled() { return enabled; }
        
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        
        @Override
        public String toString() {
            return name + " v" + version + (enabled ? " ✅" : " ❌");
        }
    }
}
