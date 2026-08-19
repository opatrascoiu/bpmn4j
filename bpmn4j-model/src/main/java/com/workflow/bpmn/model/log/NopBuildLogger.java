package com.workflow.bpmn.model.log;

public final class NopBuildLogger implements BuildLogger {
    @Override
    public void debug(String msg) {
    }

    @Override
    public void debug(String charSequence, Throwable throwable) {
    }

    @Override
    public void info(String msg) {
    }

    @Override
    public void info(String charSequence, Throwable throwable) {
    }

    @Override
    public void warn(String msg) {
    }

    @Override
    public void warn(String charSequence, Throwable throwable) {
    }

    @Override
    public void error(String msg) {
    }

    @Override
    public void error(String msg, Throwable t) {
    }
}
