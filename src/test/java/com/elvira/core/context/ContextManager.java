package com.elvira.core.context;

public class ContextManager {

    // ThreadLocal для тестового контекста (safe для параллельных тестов)
    private static final ThreadLocal<TestContext> context = new ThreadLocal<>();

    /**
     * Установить TestContext для текущего потока.
     * Если уже установлен — кидает исключение.
     */
    public static void set(TestContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("Cannot set null TestContext");
        }
        if (context.get() != null) {
            throw new IllegalStateException(
                    "TestContext is already set for this thread. " +
                    "Possible double init or parallel test conflict"
            );
        }
        context.set(ctx);
        log("TestContext set for thread: " + Thread.currentThread().getName());
    }

    /**
     * Получить TestContext для текущего потока.
     * Если ещё не установлен — кидает исключение.
     */
    public static TestContext get() {
        TestContext ctx = context.get();
        if (ctx == null) {
            throw new IllegalStateException(
                    "TestContext is not initialized. Did you forget to call init()?"
            );
        }
        return ctx;
    }

    /**
     * Очистить TestContext из текущего потока.
     * Используется в cleanup().
     */
    public static void unload() {
        if (context.get() != null) {
            log("Unloading TestContext for thread: " + Thread.currentThread().getName());
            context.remove();
        }
    }

    // 🔧 Вспомогательный метод для логов
    private static void log(String message) {
        System.out.println("[ContextManager] " + message);
    }
}