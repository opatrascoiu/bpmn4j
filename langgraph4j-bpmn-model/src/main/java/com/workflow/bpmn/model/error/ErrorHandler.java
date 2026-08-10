package com.workflow.bpmn.model.error;

import org.apache.commons.lang3.StringUtils;
import org.omg.spec.bpmn._20100524.model.TBaseElement;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

public class ErrorHandler {
    private static final ErrorHandler INSTANCE = new ErrorHandler();

    public static ErrorHandler instance() {
        return INSTANCE;
    }

    private ErrorHandler() {
    }

    public RuntimeException makeSemanticError(String errorMessage) {
        return new BPMNSemanticError(errorMessage);
    }

    public RuntimeException makeSemanticError(String errorMessage, Exception e) {
        return new BPMNSemanticError(errorMessage, e);
    }

    public RuntimeException makeSemanticError(TDefinitions model, TBaseElement element, String errorMessage) {
        String location = location(model, element);
        if (StringUtils.isEmpty(location)) {
            return new BPMNSemanticError(errorMessage);
        } else {
            String fullErrorMessage = String.format("%s: %s", location, errorMessage);
            return new BPMNSemanticError(fullErrorMessage);
        }
    }

    public RuntimeException makeTranslationError(String errorMessage) {
        return new BPMNTranslationError(errorMessage);
    }

    public RuntimeException makeTranslationError(String errorMessage, Exception e) {
        return new BPMNTranslationError(errorMessage, e);
    }

    public RuntimeException makeTranslationError(TDefinitions model, String errorMessage) {
        return makeTranslationError(model, null, errorMessage);
    }

    public RuntimeException makeTranslationError(TDefinitions model, TBaseElement element, String errorMessage) {
        String location = location(model, element);
        if (StringUtils.isEmpty(location)) {
            return new BPMNTranslationError(errorMessage);
        } else {
            String fullErrorMessage = String.format("%s: %s", location, errorMessage);
            return new BPMNTranslationError(fullErrorMessage);
        }
    }

    private String location(TDefinitions model, TBaseElement element) {
        String modelDisplayName = displayName(model);
        String elementDisplayName = displayName(element);
        return String.format("(model='%s', element='%s')", modelDisplayName, elementDisplayName);
    }

    private String displayName(TDefinitions model) {
        if (model == null) {
            return null;
        }

        String displayName = model.getName();
        if (StringUtils.isEmpty(displayName)) {
            displayName = model.getId();
        }
        return displayName;
    }

    private String displayName(TBaseElement element) {
        if (element == null) {
            return null;
        }

        return element.getId();
    }
}
