package com.workflow.bpmn.translator.template;

import org.omg.spec.bpmn._20100524.model.TBaseElement;

public interface TemplateProvider {
    //  Base template path for the templates
    String baseTemplatePath();

    // Template path for the flow element templates
    String getElementTemplate(TBaseElement element);
}