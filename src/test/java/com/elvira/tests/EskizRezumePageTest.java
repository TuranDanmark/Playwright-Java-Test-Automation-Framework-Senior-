package com.elvira.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.elvira.core.base.BaseTest;
import com.elvira.core.extension.RetryExtension;
import com.elvira.pages.EskizRezumePage;
import io.qameta.allure.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Eskiz App")
@Feature("Resume Submission")
@ExtendWith(RetryExtension.class)
public class EskizRezumePageTest extends BaseTest {

    @Tag("regression")
    @Test
    @Story("User opens resume submission form")
    @Description("Verify that resume submission form is displayed after navigation")
    @Severity(SeverityLevel.CRITICAL)
    void shouldOpenResumeForm() {

        EskizRezumePage eskizPage = new EskizRezumePage()
                .open()
                .openSites()
                .openExpressSites()
                .openOrderPage()
                .openCareer()
                .attachResume();

        Allure.step("Verify resume submission form is visible", () -> {
            eskizPage.waitVisible(eskizPage.resumeFormTitle(), "Resume Form Title", 5000);
            assertThat(eskizPage.resumeFormTitle()).isVisible();
        });
    }
}