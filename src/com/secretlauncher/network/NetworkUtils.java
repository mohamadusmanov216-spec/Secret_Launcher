// src/com/secretlauncher/network/NetworkUtils.java
package com.secretlauncher.network;

import com.secretlauncher.utils.Logger;
import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class NetworkUtils {
    
    public static boolean isInternetAvailable() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (responseCode == 200);
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean downloadFile(String url, File destination, ProgressCallback callback) {
        try {
            URL downloadUrl = new URL(url);
            try (ReadableByteChannel rbc = Channels.newChannel(downloadUrl.openStream());
                 FileOutputStream fos = new FileOutputStream(destination)) {
                
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                if (callback != null) callback.onProgress(100);
                return true;
            }
        } catch (IOException e) {
            Logger.error("Ошибка загрузки файла: " + url, e);
            return false;
        }
    }
    
    public static String getExternalIP() {
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                return reader.readLine();
            }
        } catch (IOException e) {
            return "Unknown";
        }
    }
    
    public static boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public interface ProgressCallback {
        void onProgress(int percent);
    }
}
