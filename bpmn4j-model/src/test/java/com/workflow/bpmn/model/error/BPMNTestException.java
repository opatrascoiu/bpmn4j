package com.workflow.bpmn.model.error;

public class BPMNTestException extends RuntimeException {
    public BPMNTestException(String message) {
        super(message);
    }

    public BPMNTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BPMNTestException(Throwable cause) {
        super(cause);
    }
}
