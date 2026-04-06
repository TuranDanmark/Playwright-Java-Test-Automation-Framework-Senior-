package com.elvira.core.allure;

import java.io.File;
import java.io.PrintWriter;

import com.elvira.core.config.Environment;

public class AllureEnvironmentWriter {

    public static void write() {
        try {
            File dir = new File("target/allure-results");
            if (!dir.exists() && !dir.mkdirs()) {
                log("Failed to create directory: " + dir.getAbsolutePath());
                return;
            }

            File file = new File(dir, "environment.properties");

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Browser=" + System.getProperty("browser", "chromium"));
                writer.println("Headless=" + System.getProperty("headless", "false"));
                writer.println("Environment=" + Environment.current().name());
                writer.println("OS=" + System.getProperty("os.name"));
                writer.println("Java=" + System.getProperty("java.version"));
                log("Allure environment.properties written successfully");
            }

        } catch (Exception e) {
            log("Failed to write environment.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void log(String message) {
        System.out.println("[AllureEnvironmentWriter] " + message);
    }
}