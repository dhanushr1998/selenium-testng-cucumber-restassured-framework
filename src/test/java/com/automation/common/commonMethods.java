package com.automation.common;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class commonMethods {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

    private final WebDriverWait wait;

    /** Creates shared Selenium helpers for the supplied browser session. */
    public commonMethods(WebDriver driver) {
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /** Waits for and returns the first displayed element matching a locator. */
    public WebElement getVisibleElement(By locator) {
        return wait.until(currentDriver -> currentDriver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
    }

    /** Waits for the element identified by a locator to become visible. */
    public WebElement waitForVisibleElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits for the element identified by a locator to become clickable. */
    public WebElement waitForClickableElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Clicks an element after Selenium determines that it is clickable. */
    public void clickWhenReady(By locator) {
        waitForClickableElement(locator).click();
    }

    /** Clears a text field and types the supplied value after it becomes visible. */
    public void clearAndType(By locator, String value) {
        WebElement element = getVisibleElement(locator);
        element.sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
        element.sendKeys(value);
    }

    /** Returns the text of an element after it becomes visible. */
    public String getVisibleText(By locator) {
        return waitForVisibleElement(locator).getText();
    }

    /** Waits until the element's attribute has the requested value. */
    public boolean waitForAttributeValue(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /** Waits until the element's visible text matches the requested text. */
    public boolean waitForText(By locator, String text) {
        return wait.until(ExpectedConditions.textToBe(locator, text));
    }
}
