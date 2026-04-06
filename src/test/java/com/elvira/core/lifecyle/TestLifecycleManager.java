package com.elvira.core.lifecyle;

import com.elvira.core.artifacts.TestArtifacts;
import com.elvira.core.browser.BrowserFactory;
import com.elvira.core.config.ConfigReader;
import com.elvira.core.context.ContextManager;
import com.elvira.core.context.TestContext;
import com.elvira.core.tracing.TracingManager;
import com.elvira.utils.PageEventListener;
import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestLifecycleManager {

    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThread = new ThreadLocal<>();

    public static void init() {

        log("=== INIT START ===");

        TestContext ctx = new TestContext();

        try {
            // ✅ Playwright
            if (playwrightThread.get() == null) {
                log("Creating Playwright instance");
                playwrightThread.set(Playwright.create());
            }

            Playwright playwright = playwrightThread.get();
            ctx.setPlaywright(playwright);

            // ✅ Browser
            if (browserThread.get() == null) {
                log("Creating Browser instance");
                browserThread.set(BrowserFactory.createBrowser(playwright));
            }

            Browser browser = browserThread.get();
            ctx.setBrowser(browser);

            // ✅ testId
            String testId = generateTestId();

            BrowserContext context = browser.newContext(
                    new Browser.NewContextOptions()
                            .setViewportSize(
                                    ConfigReader.getInt("viewportWidth"),
                                    ConfigReader.getInt("viewportHeight")
                            )
                            .setRecordVideoDir(Paths.get("target/videos/" + testId))
            );

            ctx.setContext(context);

            // ✅ tracing (fail-safe)
            try {
                TracingManager.start(context);
            } catch (Exception e) {
                log("Tracing start failed: " + e.getMessage());
            }

            // ✅ page
            Page page = context.newPage();
            ctx.setPage(page);

            PageEventListener.attach(page);

            // ✅ navigation
            page.navigate(ConfigReader.get("baseUrl"));
            page.setDefaultTimeout(ConfigReader.getInt("timeout"));

            ContextManager.set(ctx);

            log("=== INIT SUCCESS ===");

        } catch (Exception e) {

            log("INIT FAILED: " + e.getMessage());

            // 💥 rollback если init упал
            safeCleanup(ctx);

            throw e;
        }
    }

    public static void cleanup() {

        log("=== CLEANUP START ===");

        TestContext ctx = ContextManager.get();

        if (ctx == null) {
            log("Context is null");
            return;
        }

        safeCleanup(ctx);

        TestArtifacts.clear();
        ContextManager.unload();

        log("=== CLEANUP END ===");
    }

    public static Page getPage() {
        TestContext ctx = ContextManager.get();

        if (ctx == null || ctx.getPage() == null) {
            throw new IllegalStateException("Page is not initialized");
        }

        return ctx.getPage();
    }

    // 🔥 безопасный cleanup (главное улучшение)
    private static void safeCleanup(TestContext ctx) {

        // tracing
        try {
            if (ctx.getContext() != null) {
                TracingManager.stop(ctx.getContext(), generateTraceName());
            }
        } catch (Exception e) {
            log("Tracing stop failed: " + e.getMessage());
        }

        // context
        try {
            if (ctx.getContext() != null) {
                ctx.getContext().close();
            }
        } catch (Exception e) {
            log("Context close failed: " + e.getMessage());
        }
    }

    private static String generateTestId() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
    }

    private static String generateTraceName() {
        return "trace-" + System.currentTimeMillis() + ".zip";
    }

    private static void log(String msg) {
        System.out.println("[Lifecycle] " + msg);
    }
}