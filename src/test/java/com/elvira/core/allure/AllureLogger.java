package com.elvira.core.allure;

import io.qameta.allure.Allure;

public class AllureLogger {

    public static void step(String message) {
        Allure.step(message);
    }
}