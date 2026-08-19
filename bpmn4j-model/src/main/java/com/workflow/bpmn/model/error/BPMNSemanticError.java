package com.workflow.bpmn.model.error;

public class BPMNSemanticError extends RuntimeException {
    public BPMNSemanticError(String message) {
        super(message);
    }

    public BPMNSemanticError(String message, Throwable cause) {
        super(message, cause);
    }

    public BPMNSemanticError(Throwable cause) {
        super(cause);
    }
}
