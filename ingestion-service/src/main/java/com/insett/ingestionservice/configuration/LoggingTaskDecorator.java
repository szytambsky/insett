package com.insett.ingestionservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

@Configuration
public class LoggingTaskDecorator implements TaskDecorator {

    private static final Logger log = LoggerFactory.getLogger(LoggingTaskDecorator.class);

    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            log.info("Before execution {}", runnable);
            runnable.run();
            log.info("After execution {}", runnable);
        };
    }
}
