// src/com/secretlauncher/audio/SoundManager.java
package com.secretlauncher.audio;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class SoundManager {
    private Map<String, Clip> sounds;
    private boolean enabled;
    
    public SoundManager() {
        this.sounds = new HashMap<>();
        this.enabled = true;
        loadSounds();
    }
    
    private void loadSounds() {
        // Загрузка звуков из ресурсов
        try {
            // Здесь будут загружаться звуковые файлы
            // Пока заглушка
        } catch (Exception e) {
            System.err.println("Ошибка загрузки звуков: " + e.getMessage());
        }
    }
    
    public void playSound(String soundName) {
        if (!enabled) return;
        
        Clip clip = sounds.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void playClickSound() {
        playSound("click");
    }
    
    public void playHoverSound() {
        playSound("hover");
    }
    
    public void playSuccessSound() {
        playSound("success");
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    private Clip loadClip(String resourcePath) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream(resourcePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            System.err.println("Ошибка загрузки клипа: " + e.getMessage());
            return null;
        }
    }
}
