package com.automation.pages;

import com.automation.common.commonMethods;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExamplePage {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By ELEMENTS_CARD_LINK = By.xpath("//div/a[@href='/elements']");
    private static final By CARDS = By.xpath("//div[contains(@class, 'card')]");
    private static final By PRACTICE_FORM_LINK = By.xpath("//a[normalize-space()='Practice Form']");
    private static final By MAIN_HEADER = By.cssSelector(".main-header");

    private final WebDriver driver;
    private final commonMethods commonMethods;

    public ExamplePage(WebDriver driver) {
        this.driver = driver;
        this.commonMethods = new commonMethods(driver);
    }

    public void open() {
        driver.get("https://demoqa.com/");
    }

    public boolean verifyCardVisibility() {
        return driver.findElement(ELEMENTS_CARD_LINK).isDisplayed();
    }

    public String getCardText() {
        return commonMethods.getVisibleText(ELEMENTS_CARD_LINK);
    }

    public void clickCard(String cardTitle) {
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        WebElement card = wait.until(driver -> driver.findElements(CARDS).stream()
                .filter(element -> element.getText().contains(cardTitle))
                .findFirst()
                .orElse(null));
        wait.until(ExpectedConditions.elementToBeClickable(card)).click();
    }

    public boolean isFormsPageLoaded() {
        return commonMethods.getVisibleText(MAIN_HEADER).equals("Forms");
    }

    public void openPracticeForm() {
        commonMethods.clickWhenReady(PRACTICE_FORM_LINK);
        commonMethods.waitForText(MAIN_HEADER, "Practice Form");
    }

    public boolean isFormFieldDisplayed(String fieldName, String expectedType) {
        String fieldId = switch (fieldName) {
            case "Name" -> "firstName";
            case "Email" -> "userEmail";
            default -> null;
        };
        if (fieldId == null) {
            return false;
        }

        return driver.findElements(By.id(fieldId)).stream()
                .anyMatch(field -> field.isDisplayed()
                        && expectedType.equalsIgnoreCase(field.getAttribute("type")));
    }
}
