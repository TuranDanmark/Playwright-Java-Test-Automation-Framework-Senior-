package com.elvira.core.base;

import com.elvira.core.allure.AllureEnvironmentWriter;
import com.elvira.core.lifecyle.TestLifecycleManager;
import com.elvira.utils.TestListener;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
public abstract class BaseTest {

    @BeforeAll
    static void setupAll() {
        AllureEnvironmentWriter.write();
    }

    @BeforeEach
    void setUp() {
        TestLifecycleManager.init();
    }

    @AfterEach
    void tearDown() {
        TestLifecycleManager.cleanup();
    }

    protected Page getPage() {
        return TestLifecycleManager.getPage();
    }
}