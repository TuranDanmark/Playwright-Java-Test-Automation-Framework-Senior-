package com.elvira.utils;

import com.elvira.core.lifecyle.TestLifecycleManager;
import com.elvira.core.allure.AllureAttachmentService;
import com.microsoft.playwright.Page;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        Page page = safeGetPage();

        log("Test failed: " + testName + ", preparing attachments");

        // ✅ Все аттачи fail-safe
        tryAttach(() -> AllureAttachmentService.attachScreenshot(page));
        tryAttach(() -> AllureAttachmentService.attachTrace(page, testName));
        tryAttach(() -> AllureAttachmentService.attachVideo(page));
        tryAttach(() -> AllureAttachmentService.attachPageUrl(page));
        tryAttach(() -> AllureAttachmentService.attachError(cause));
        tryAttach(AllureAttachmentService::attachConsoleLogs);
        tryAttach(AllureAttachmentService::attachNetworkLogs);

        log("Attachments finished for test: " + testName);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        // Optional: можно автоматически attach traces/video для успешных тестов, если нужно
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log("Test aborted: " + context.getDisplayName());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log("Test disabled: " + context.getDisplayName() + " reason: " + reason.orElse("none"));
    }

    // 🔧 helpers
    private Page safeGetPage() {
        try {
            return TestLifecycleManager.getPage();
        } catch (Exception e) {
            log("Failed to get Page: " + e.getMessage());
            return null;
        }
    }

    private void tryAttach(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log("Attachment failed: " + e.getMessage());
        }
    }

    private void log(String message) {
        System.out.println("[TestListener] " + message);
    }
}