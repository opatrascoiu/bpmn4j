package com.workflow.bpmn.model.log;

public interface BuildLogger {
    void debug(String charSequence);

    void debug(String charSequence, Throwable throwable);

    void info(String charSequence);

    void info(String charSequence, Throwable throwable);

    void warn(String charSequence);

    void warn(String charSequence, Throwable throwable);

    void error(String charSequence);

    void error(String charSequence, Throwable throwable);
}
