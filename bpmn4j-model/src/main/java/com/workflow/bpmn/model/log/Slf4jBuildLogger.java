package com.workflow.bpmn.model.log;

import org.slf4j.Logger;

public final class Slf4jBuildLogger implements BuildLogger {
    private final Logger logger;

    public Slf4jBuildLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String charSequence) {
        this.logger.debug(charSequence);
    }

    @Override
    public void debug(String charSequence, Throwable throwable) {
        this.logger.debug(charSequence, throwable);
    }

    @Override
    public void info(String charSequence) {
        this.logger.info(charSequence);
    }

    @Override
    public void info(String charSequence, Throwable throwable) {
        this.logger.info(charSequence, throwable);
    }

    @Override
    public void warn(String charSequence) {
        this.logger.warn(charSequence);
    }

    @Override
    public void warn(String charSequence, Throwable throwable) {
        this.logger.warn(charSequence, throwable);
    }

    @Override
    public void error(String charSequence) {
        this.logger.error(charSequence);
    }

    @Override
    public void error(String charSequence, Throwable t) {
        this.logger.error(charSequence, t);
    }
}
