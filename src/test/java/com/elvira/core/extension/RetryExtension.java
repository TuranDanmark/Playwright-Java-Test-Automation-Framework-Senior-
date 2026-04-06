package com.elvira.core.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import io.qameta.allure.Allure;

public class RetryExtension implements TestExecutionExceptionHandler {

    private static final int MAX_RETRIES = Integer.getInteger("test.retry.max", 2);

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create("com.elvira", "RetryExtension");

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        int currentRetry = getRetryCount(context);

        if (currentRetry < MAX_RETRIES) {
            incrementRetryCount(context);
            System.out.println("Retrying test: " + context.getDisplayName()
                    + " | attempt: " + (currentRetry + 1));

            // ✅ Log retry to Allure
            Allure.step("Retry attempt " + (currentRetry + 1) + " for test: " + context.getDisplayName());

            throw throwable; // JUnit перезапустит тест
        }

        throw throwable; // последняя попытка
    }

    private int getRetryCount(ExtensionContext context) {
        return context.getStore(NAMESPACE)
                .getOrDefault(context.getUniqueId(), Integer.class, 0);
    }

    private void incrementRetryCount(ExtensionContext context) {
        context.getStore(NAMESPACE)
                .put(context.getUniqueId(), getRetryCount(context) + 1);
    }
}