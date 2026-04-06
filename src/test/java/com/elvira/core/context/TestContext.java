package com.elvira.core.context;

import com.microsoft.playwright.*;

public class TestContext {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public Playwright getPlaywright() { return playwright; }
    public void setPlaywright(Playwright playwright) { this.playwright = playwright; }

    public Browser getBrowser() { return browser; }
    public void setBrowser(Browser browser) { this.browser = browser; }

    public BrowserContext getContext() { return context; }
    public void setContext(BrowserContext context) { this.context = context; }

    public Page getPage() { return page; }
    public void setPage(Page page) { this.page = page; }
}