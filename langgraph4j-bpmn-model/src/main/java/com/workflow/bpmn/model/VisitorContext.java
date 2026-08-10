package com.workflow.bpmn.model;

import org.apache.commons.lang3.StringUtils;
import org.omg.spec.bpmn._20100524.model.TBaseElement;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class VisitorContext {
    private final VisitorContext parentContext;
    private final Object element;

    public static VisitorContext of(VisitorContext parentContext, Object currentElement) {
        return new VisitorContext(parentContext, currentElement);
    }

    private VisitorContext(VisitorContext parentContext, Object element) {
        this.parentContext = parentContext;
        this.element = element;
    }

    public String toPath() {
        List<Object> elements = new ArrayList<>();
        VisitorContext context = this;
        do {
            elements.add(context.element);
            context = context.parentContext;
        } while (context != null);
        List<String> pathParts = new ArrayList<>();
        for (int i=elements.size()-1; i>=0; i--) {
            Object element = elements.get(i);
            String elementPart = getElementPart(element);
            pathParts.add(elementPart);
        }
        StringJoiner joiner = new StringJoiner("/");
        for (String pathPart : pathParts) {
            joiner.add(pathPart);
        }
        return joiner.toString();
    }

    private String getElementPart(Object element) {
        String elementPart = element.getClass().getSimpleName();
        String id = null;
        if (element instanceof TDefinitions) {
            id = ((TDefinitions) element).getId();
        }
        if (element instanceof TBaseElement) {
            id = ((TBaseElement) element).getId();
        }
        if (!StringUtils.isEmpty(id)) {
            elementPart = String.format("%s[id='%s']", elementPart, id);
        }
        return elementPart;
    }
}
