package com.automation.hooks;

import java.lang.reflect.Method;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class ActionDelayListener implements WebDriverListener {
    private static final Set<String> ACTION_METHODS = Set.of(
            "click", "sendKeys", "clear", "submit", "get", "navigateTo",
            "navigateBack", "navigateForward", "refresh", "perform");

    private final long delayMs;

    public ActionDelayListener(long delayMs) {
        this.delayMs = delayMs;
    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        if (delayMs == 0 || !ACTION_METHODS.contains(method.getName())
                || !(target instanceof WebDriver || target instanceof WebElement
                || target instanceof WebDriver.Navigation)) {
            return;
        }

        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while applying the configured browser action delay", exception);
        }
    }
}
