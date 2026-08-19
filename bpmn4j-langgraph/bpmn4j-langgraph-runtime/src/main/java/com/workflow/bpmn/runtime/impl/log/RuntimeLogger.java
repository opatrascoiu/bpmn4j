package com.workflow.bpmn.runtime.impl.log;

import java.util.Map;

public interface RuntimeLogger {
    default void logEvent(Map<String, Object> input) {
        log("Received event: " + input);
    }

    void log(String message);
}
