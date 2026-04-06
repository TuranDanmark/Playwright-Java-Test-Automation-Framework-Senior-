package com.elvira.pages;

import com.elvira.core.allure.AllureLogger;
import com.elvira.core.context.ContextManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public abstract class BasePage {

    protected final Page page;

    public BasePage() {
        this.page = ContextManager.get().getPage();
    }

    // 🔹 Page waits
    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForUrl(String urlPart) {
        page.waitForURL(url -> url.contains(urlPart));
    }

    // 🔹 Element actions + AllureLogger
    protected void click(Locator locator, String stepDescription) {
        AllureLogger.step(stepDescription);
        locator.waitFor();
        locator.click();
    }

    protected void type(Locator locator, String text, String stepDescription) {
        AllureLogger.step(stepDescription);
        locator.waitFor();
        locator.fill(text);
    }

    public void waitVisible(Locator locator, String stepDescription, int timeoutMs) {
        AllureLogger.step(stepDescription);
        locator.waitFor(new Locator.WaitForOptions().setTimeout((double) timeoutMs));
    }

    protected boolean isVisible(Locator locator) {
        return locator.isVisible();
    }
}