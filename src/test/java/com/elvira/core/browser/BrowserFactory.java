package com.elvira.core.browser;

import com.elvira.core.config.ConfigReader;
import com.microsoft.playwright.*;
import com.microsoft.playwright.BrowserType;

import java.util.ArrayList;
import java.util.List;

public class BrowserFactory {

    /**
     * Создать Browser на основе конфигурации
     */
    public static Browser createBrowser(Playwright playwright) {
        String browserName = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        int timeout = ConfigReader.getInt("browserTimeout"); // optional
        int slowMo = ConfigReader.getInt("slowMo"); // optional

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setTimeout(timeout > 0 ? timeout : 30000)
                .setSlowMo(slowMo > 0 ? slowMo : 0)
                .setArgs(buildArgs());

        Browser browser;

        switch (browserName) {
            case "firefox":
                log("Launching Firefox, headless=" + headless);
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                log("Launching WebKit, headless=" + headless);
                browser = playwright.webkit().launch(options);
                break;
            case "chromium":
            default:
                log("Launching Chromium, headless=" + headless);
                browser = playwright.chromium().launch(options);
                break;
        }

        log("Browser launched successfully: " + browserName);
        return browser;
    }

    /**
     * Дополнительные аргументы для браузера (extendable)
     */
    private static List<String> buildArgs() {
        List<String> args = new ArrayList<>();
        if (ConfigReader.getBoolean("disableSandbox")) {
            args.add("--no-sandbox");
            args.add("--disable-setuid-sandbox");
        }
        if (ConfigReader.getBoolean("disableGpu")) {
            args.add("--disable-gpu");
        }
        // здесь можно добавить proxy, user-agent, remote-debugging и др.
        return args;
    }

    private static void log(String message) {
        System.out.println("[BrowserFactory] " + message);
    }
}