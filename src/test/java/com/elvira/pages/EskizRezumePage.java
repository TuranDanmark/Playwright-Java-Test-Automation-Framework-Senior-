package com.elvira.pages;

import com.elvira.core.config.ConfigReader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import com.microsoft.playwright.*;

public class EskizRezumePage extends BasePage {

    @Step("Open main page")
    public EskizRezumePage open() {
        page.navigate(ConfigReader.get("baseUrl"));
        return this;
    }

    @Step("Open 'Сайты'")
    public EskizRezumePage openSites() {
        click(page.getByRole(AriaRole.BUTTON, 
              new Page.GetByRoleOptions().setName("Сайты")),
              "Click 'Сайты' button");
        return this;
    }

    @Step("Open 'Экспресс сайты'")
    public EskizRezumePage openExpressSites() {
        click(page.getByRole(AriaRole.LINK, 
              new Page.GetByRoleOptions().setName("ЭКСПРЕСС САЙТЫ").setExact(true)),
              "Click 'ЭКСПРЕСС САЙТЫ'");
        return this;
    }

    @Step("Open order section")
    public EskizRezumePage openOrderPage() {
        click(page.getByRole(AriaRole.TAB, 
              new Page.GetByRoleOptions().setName("Пять причин заказать себе сайт")),
              "Open order section");
        return this;
    }

    @Step("Open career section")
    public EskizRezumePage openCareer() {
        click(page.getByRole(AriaRole.LINK, 
              new Page.GetByRoleOptions().setName("Карьера")),
              "Open career section");
        return this;
    }

    @Step("Attach resume")
    public EskizRezumePage attachResume() {
        click(page.getByRole(AriaRole.LINK, 
              new Page.GetByRoleOptions().setName("Прикрепить резюме")),
              "Click 'Прикрепить резюме'");
        return this;
    }

    public Locator resumeFormTitle() {
        return page.getByRole(AriaRole.HEADING, 
              new Page.GetByRoleOptions().setName("Отправить резюме"));
    }
}