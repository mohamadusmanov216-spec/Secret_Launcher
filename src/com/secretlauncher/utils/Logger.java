// src/com/secretlauncher/utils/Logger.java
package com.secretlauncher.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE = "secret_launcher.log";
    private static final SimpleDateFormat dateFormat = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public enum Level {
        INFO, WARN, ERROR, DEBUG
    }
    
    public static void info(String message) {
        log(Level.INFO, message);
    }
    
    public static void warn(String message) {
        log(Level.WARN, message);
    }
    
    public static void error(String message) {
        log(Level.ERROR, message);
    }
    
    public static void error(String message, Exception e) {
        log(Level.ERROR, message + ": " + e.getMessage());
        e.printStackTrace();
    }
    
    public static void debug(String message) {
        log(Level.DEBUG, message);
    }
    
    private static void log(Level level, String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Вывод в консоль
        System.out.println(logMessage);
        
        // Запись в файл
        writeToFile(logMessage);
    }
    
    private static void writeToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Не удалось записать в лог: " + e.getMessage());
        }
    }
    
    public static void clearOldLogs() {
        File logFile = new File(LOG_FILE);
        if (logFile.exists() && logFile.length() > 10 * 1024 * 1024) { // 10MB
            logFile.delete();
        }
    }
}
