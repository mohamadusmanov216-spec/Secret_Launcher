// src/com/secretlauncher/MainLauncher.java
package com.secretlauncher;

import com.secretlauncher.installer.SecretLauncherInstaller;
import com.secretlauncher.utils.Logger;

public class MainLauncher {
    public static void main(String[] args) {
        Logger.info("🚀 Запуск Secret Launcher...");
        
        // Проверяем аргументы командной строки
        if (args.length > 0 && args[0].equals("--install")) {
            Logger.info("Запуск в режиме установки");
            new SecretLauncherInstaller();
        } else {
            Logger.info("Запуск основного лаунчера");
            new SecretLauncher();
        }
    }
}
