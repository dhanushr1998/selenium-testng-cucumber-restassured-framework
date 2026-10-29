package com.automation.retry;

import com.automation.config.FrameworkConfig;
import io.cucumber.testng.PickleWrapper;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.IRetryAnalyzer;
import org.testng.Reporter;
import org.testng.ITestResult;

public class ConfigurableRetryAnalyzer implements IRetryAnalyzer {
    private static final ConcurrentMap<String, AtomicInteger> RETRIES_BY_INVOCATION = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        
        int maxRetries = FrameworkConfig.getInstance().getRetryCount();
        String invocationKey = getInvocationKey(result);
        AtomicInteger retryCounter = RETRIES_BY_INVOCATION.computeIfAbsent(
                invocationKey, ignored -> new AtomicInteger());
        int retryNumber = retryCounter.incrementAndGet();
        if (retryNumber <= maxRetries) {
            Reporter.log("Retrying " + invocationKey + " (retry " + retryNumber + " of " + maxRetries + ")", true);
            return true;
        }

        RETRIES_BY_INVOCATION.remove(invocationKey, retryCounter);
        return false;
    }

    private String getInvocationKey(ITestResult result) {
        String parameters = Arrays.stream(result.getParameters())
                .map(parameter -> parameter instanceof PickleWrapper pickle
                        ? pickle.getPickle().getUri() + ":" + pickle.getPickle().getLine()
                                + ":" + pickle.getPickle().getName()
                        : String.valueOf(parameter))
                .reduce((left, right) -> left + "|" + right)
                .orElse("");
        return result.getMethod().getQualifiedName() + "|" + parameters;
    }
}
