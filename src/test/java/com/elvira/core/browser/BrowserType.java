package com.elvira.core.browser;

public enum BrowserType {
    CHROMIUM,
    FIREFOX,
    WEBKIT;

    // ✅ Получение enum с безопасным fallback
    public static BrowserType fromOrDefault(String value, BrowserType defaultType) {
        if (value == null || value.isBlank()) return defaultType;
        try {
            return BrowserType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultType;
        }
    }

    // ✅ Строковое представление для логов / Allure
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}