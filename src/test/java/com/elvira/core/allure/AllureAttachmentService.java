package com.elvira.core.allure;

import com.elvira.core.artifacts.TestArtifacts;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureAttachmentService {

    // 📸 Screenshot
    public static void attachScreenshot(Page page) {
        if (page == null) return;

        try {
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );

            Allure.addAttachment(
                    "Screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

        } catch (Exception e) {
            logError("Failed to attach screenshot", e);
        }
    }

    // 📦 Trace (STOP + ATTACH)
    public static void attachTrace(Page page, String testName) {
        if (page == null) return;

        try {
            Path traceDir = Paths.get("target/traces");
            Files.createDirectories(traceDir);

            Path tracePath = traceDir.resolve(testName + "-" + System.currentTimeMillis() + ".zip");

            page.context().tracing().stop(
                    new Tracing.StopOptions().setPath(tracePath)
            );

            Allure.addAttachment(
                    "Trace",
                    "application/zip",
                    Files.newInputStream(tracePath),
                    ".zip"
            );

        } catch (Exception e) {
            logError("Failed to attach trace", e);
        }
    }

    // 🎥 Video
    public static void attachVideo(Page page) {
        if (page == null || page.video() == null) return;

        try {
            Path videoPath = page.video().path();

            Allure.addAttachment(
                    "Video",
                    "video/webm",
                    Files.newInputStream(videoPath),
                    ".webm"
            );

        } catch (Exception e) {
            logError("Failed to attach video", e);
        }
    }

    // 🌐 URL
    public static void attachPageUrl(Page page) {
        if (page == null) return;

        try {
            Allure.addAttachment(
                    "URL",
                    "text/plain",
                    page.url()
            );
        } catch (Exception e) {
            logError("Failed to attach URL", e);
        }
    }

    // ❗ Error
    public static void attachError(Throwable throwable) {
        if (throwable == null) return;

        try {
            Allure.addAttachment(
                    "Error",
                    "text/plain",
                    throwable.getMessage()
            );
        } catch (Exception e) {
            logError("Failed to attach error", e);
        }
    }

    //
    public static void attachConsoleLogs() {
        try {
            String logs = String.join("\n", TestArtifacts.getConsoleLogs());
            Allure.addAttachment("Console Logs", "text/plain", logs);
        } catch (Exception e) { logError("Failed to attach console logs", e); }
    }

    //
    public static void attachNetworkLogs() {
        try {
            String logs = String.join("\n", TestArtifacts.getNetworkLogs());
            Allure.addAttachment("Network Logs", "text/plain", logs);
        } catch (Exception e) { logError("Failed to attach network logs", e); }
    }

    // 🧠 Centralized logging (очень важно)
    private static void logError(String message, Exception e) {
        System.err.println("[ALLURE ATTACHMENT ERROR] " + message);
        e.printStackTrace();
    }
}