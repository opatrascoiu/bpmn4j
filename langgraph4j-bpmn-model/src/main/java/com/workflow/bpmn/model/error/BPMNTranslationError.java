package com.workflow.bpmn.model.error;

public class BPMNTranslationError extends RuntimeException {
    public BPMNTranslationError(String message) {
        super(message);
    }

    public BPMNTranslationError(String message, Throwable cause) {
        super(message, cause);
    }

    public BPMNTranslationError(Throwable cause) {
        super(cause);
    }
}
