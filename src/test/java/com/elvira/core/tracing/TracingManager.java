package com.elvira.core.tracing;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TracingManager {

    /**
     * Запустить tracing для контекста.
     * Fail-safe: если start упадёт, тест не сломается
     */
    public static void start(BrowserContext context) {
        if (context == null) {
            log("Tracing start skipped: context is null");
            return;
        }
        try {
            context.tracing().start(
                    new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true)
                            .setSources(true)
            );
            log("Tracing started for context");
        } catch (Exception e) {
            log("Tracing start failed: " + e.getMessage());
        }
    }

    /**
     * Остановить tracing и сохранить в файл.
     * Fail-safe: не ломает тест, если stop упадёт
     */
    public static void stop(BrowserContext context, String testName) {
        if (context == null) {
            log("Tracing stop skipped: context is null");
            return;
        }

        try {
            if (testName == null || testName.isEmpty()) {
                testName = generateTraceName();
            }

            Path tracesDir = Paths.get("target/traces");
            if (!Files.exists(tracesDir)) {
                Files.createDirectories(tracesDir);
            }

            Path path = tracesDir.resolve(testName + ".zip");

            context.tracing().stop(
                    new Tracing.StopOptions().setPath(path)
            );

            log("Tracing stopped and saved to " + path);
        } catch (Exception e) {
            log("Tracing stop failed: " + e.getMessage());
        }
    }

    /**
     * Генерируем уникальное имя для trace
     */
    public static String generateTraceName() {
        return "trace-" + System.currentTimeMillis();
    }

    private static void log(String message) {
        System.out.println("[TracingManager] " + message);
    }
}