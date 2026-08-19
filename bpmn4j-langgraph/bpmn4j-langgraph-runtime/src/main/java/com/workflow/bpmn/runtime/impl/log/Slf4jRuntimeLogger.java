package com.workflow.bpmn.runtime.impl.log;

import org.slf4j.Logger;

public class Slf4jRuntimeLogger implements RuntimeLogger {
    private final Logger logger;

    public Slf4jRuntimeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(String message) {
        logger.info(message);
    }
}
