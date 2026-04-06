package com.elvira.utils;

import com.elvira.core.artifacts.TestArtifacts;
import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;

public class PageEventListener {

    public static void attach(Page page) {
        if (page == null) {
            log("Cannot attach listener: Page is null");
            return;
        }

        try {
            // ✅ Console messages
            page.onConsoleMessage(msg -> safeRun(() ->
                    TestArtifacts.addConsoleLog(formatConsole(msg))
            ));

            // ✅ Network requests
            page.onRequest(req -> safeRun(() ->
                    TestArtifacts.addNetworkLog(formatRequest(req))
            ));

            // ✅ Network responses
            page.onResponse(res -> safeRun(() ->
                    TestArtifacts.addNetworkLog(formatResponse(res))
            ));

            // ✅ JS errors
            page.onPageError(error -> safeRun(() ->
                    TestArtifacts.addError("[JS ERROR] " + error)
            ));

            log("PageEventListener attached to page");

        } catch (Exception e) {
            log("Failed to attach PageEventListener: " + e.getMessage());
        }
    }

    // 🔧 Helpers
    private static void safeRun(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log("Listener callback failed: " + e.getMessage());
        }
    }

    private static String formatConsole(ConsoleMessage msg) {
        return "[CONSOLE] " + msg.type() + " → " + msg.text();
    }

    private static String formatRequest(Request req) {
        return "[REQUEST] " + req.method() + " " + req.url();
    }

    private static String formatResponse(Response res) {
        return "[RESPONSE] " + res.status() + " " + res.url();
    }

    private static void log(String message) {
        System.out.println("[PageEventListener] " + message);
    }
}