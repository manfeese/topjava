package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import java.util.concurrent.TimeUnit;

public class LoggerStopwatch extends Stopwatch {

    private final Logger log;
    private final StringBuilder logBuilder;

    public LoggerStopwatch(Logger log, StringBuilder logBuilder) {
        this.log = log;
        this.logBuilder = logBuilder;
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    private void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        String message = String.format("Test \"%s\" - %s, spent %d milliseconds",
                testName, status, TimeUnit.NANOSECONDS.toMillis(nanos));

        log.info(message);
        logBuilder.append(message + "\n");
    }
}
