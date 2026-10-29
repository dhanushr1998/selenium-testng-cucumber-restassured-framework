package com.automation.hooks;

import com.automation.config.FrameworkConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        FrameworkConfig config = FrameworkConfig.getInstance();
        configureDriverPath(config.getBrowser(), config.getDriverPath());
        WebDriver driver = switch (config.getBrowser()) {
            case "chrome" -> createChromeDriver(config.isHeadless());
            case "firefox" -> createFirefoxDriver(config.isHeadless());
            case "edge" -> createEdgeDriver(config.isHeadless());
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + config.getBrowser() + "'. Supported browsers: chrome, firefox, edge");
        };

        if (config.shouldMaximizeBrowser()) {
            driver.manage().window().maximize();
        }

        return new EventFiringDecorator<>(new ActionDelayListener(config.getActionDelayMs())).decorate(driver);
    }

    private static void configureDriverPath(String browser, String driverPath) {
        if (driverPath.isBlank()) {
            return;
        }

        Path executable = Path.of(driverPath);
        if (!Files.isRegularFile(executable)) {
            throw new IllegalArgumentException(
                    "Configured driver.path does not point to a driver file: " + executable.toAbsolutePath());
        }

        String property = switch (browser) {
            case "chrome" -> "webdriver.chrome.driver";
            case "firefox" -> "webdriver.gecko.driver";
            case "edge" -> "webdriver.edge.driver";
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browser + "'. Supported browsers: chrome, firefox, edge");
        };
        System.setProperty(property, executable.toAbsolutePath().toString());
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }
}
