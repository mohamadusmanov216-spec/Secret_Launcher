// src/com/secretlauncher/network/ServerManager.java
package com.secretlauncher.network;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ServerManager {
    private ArrayList<Server> favoriteServers;
    private ArrayList<Server> recentServers;
    
    public ServerManager() {
        this.favoriteServers = new ArrayList<>();
        this.recentServers = new ArrayList<>();
        loadServers();
    }
    
    public void addFavoriteServer(String name, String ip, int port) {
        Server server = new Server(name, ip, port);
        if (!favoriteServers.contains(server)) {
            favoriteServers.add(server);
            saveServers();
        }
    }
    
    public void removeFavoriteServer(Server server) {
        favoriteServers.remove(server);
        saveServers();
    }
    
    public void addRecentServer(Server server) {
        recentServers.remove(server);
        recentServers.add(0, server);
        
        // Сохраняем только последние 10 серверов
        if (recentServers.size() > 10) {
            recentServers = new ArrayList<>(recentServers.subList(0, 10));
        }
        saveServers();
    }
    
    public boolean connectToServer(String ip, int port) {
        try {
            Server server = new Server("Direct Connect", ip, port);
            return connectToServer(server);
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean connectToServer(Server server) {
        try {
            // Проверяем доступность сервера
            if (!isServerOnline(server)) {
                JOptionPane.showMessageDialog(null, 
                    "❌ Сервер " + server.getName() + " недоступен!", 
                    "Ошибка подключения", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Добавляем в историю
            addRecentServer(server);
            
            // Здесь будет логика запуска Minecraft с подключением к серверу
            launchMinecraftWithServer(server);
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Ошибка при подключении: " + e.getMessage(), 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private boolean isServerOnline(Server server) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(server.getIp(), server.getPort()), 5000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    private void launchMinecraftWithServer(Server server) {
        try {
            // Создаем команду для запуска Minecraft
            String javaPath = getJavaPath();
            String minecraftJar = findMinecraftJar();
            
            if (minecraftJar == null) {
                JOptionPane.showMessageDialog(null,
                    "❌ Файл Minecraft не найден!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Формируем команду запуска
            ArrayList<String> command = new ArrayList<>();
            command.add(javaPath);
            command.add("-jar");
            command.add(minecraftJar);
            command.add("--server");
            command.add(server.getIp());
            command.add("--port");
            command.add(String.valueOf(server.getPort()));
            
            // Запускаем процесс
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(System.getProperty("user.home")));
            Process process = pb.start();
            
            JOptionPane.showMessageDialog(null,
                "🎮 Запускаем Minecraft с подключением к " + server.getName() + "...",
                "Успех",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Ошибка запуска: " + e.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getJavaPath() {
        // Ищем путь к Java
        String javaHome = System.getProperty("java.home");
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            return javaHome + "\\bin\\java.exe";
        } else {
            return javaHome + "/bin/java";
        }
    }
    
    private String findMinecraftJar() {
        // Ищем jar файл Minecraft в стандартных местах
        String[] possiblePaths = {
            System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft\\versions\\",
            System.getProperty("user.home") + "/Library/Application Support/minecraft/versions/",
            System.getProperty("user.home") + "/.minecraft/versions/"
        };
        
        for (String path : possiblePaths) {
            File versionsDir = new File(path);
            if (versionsDir.exists() && versionsDir.isDirectory()) {
                File[] versionDirs = versionsDir.listFiles();
                if (versionDirs != null) {
                    for (File versionDir : versionDirs) {
                        if (versionDir.isDirectory()) {
                            File jarFile = new File(versionDir, versionDir.getName() + ".jar");
                            if (jarFile.exists()) {
                                return jarFile.getAbsolutePath();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private void loadServers() {
        try {
            File serversFile = new File("servers.dat");
            if (serversFile.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serversFile));
                favoriteServers = (ArrayList<Server>) ois.readObject();
                recentServers = (ArrayList<Server>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            // Используем серверы по умолчанию
            loadDefaultServers();
        }
    }
    
    private void saveServers() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("servers.dat"));
            oos.writeObject(favoriteServers);
            oos.writeObject(recentServers);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadDefaultServers() {
        // Популярные серверы по умолчанию
        addFavoriteServer("Hypixel", "mc.hypixel.net", 25565);
        addFavoriteServer("Mineplex", "us.mineplex.com", 25565);
        addFavoriteServer("The Hive", "play.hivemc.com", 25565);
    }
    
    // Геттеры
    public ArrayList<Server> getFavoriteServers() {
        return new ArrayList<>(favoriteServers);
    }
    
    public ArrayList<Server> getRecentServers() {
        return new ArrayList<>(recentServers);
    }
    
    public static class Server implements Serializable {
        private String name;
        private String ip;
        private int port;
        private Date lastConnected;
        
        public Server(String name, String ip, int port) {
            this.name = name;
            this.ip = ip;
            this.port = port;
            this.lastConnected = new Date();
        }
        
        // Геттеры и сеттеры
        public String getName() { return name; }
        public String getIp() { return ip; }
        public int getPort() { return port; }
        public Date getLastConnected() { return lastConnected; }
        
        public void setLastConnected(Date lastConnected) {
            this.lastConnected = lastConnected;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Server server = (Server) obj;
            return port == server.port && ip.equals(server.ip);
        }
        
        @Override
        public String toString() {
            return name + " (" + ip + ":" + port + ")";
        }
    }
}
