package com.workflow.bpmn.translator.template;

import org.omg.spec.bpmn._20100524.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class LGTemplateProvider implements TemplateProvider {
    private static final Map<Class<?>, String> ELEMENT_TEMPLATES = new LinkedHashMap<>();

    static {
        // Processes
        ELEMENT_TEMPLATES.put(TProcess.class, "process.ftl");

        // Activities
        ELEMENT_TEMPLATES.put(TTask.class, "flow-node.ftl");
        ELEMENT_TEMPLATES.put(TScriptTask.class, "activities/script-task.ftl");

        // Gateways
        ELEMENT_TEMPLATES.put(TExclusiveGateway.class, "gateways/exclusive-gateway.ftl");

        // Events
        ELEMENT_TEMPLATES.put(TStartEvent.class, "flow-node.ftl");
        ELEMENT_TEMPLATES.put(TEndEvent.class, "flow-node.ftl");
    }

    @Override
    public String baseTemplatePath() {
        return "/templates/bpmn/lg";
    }

    @Override
    public String getElementTemplate(TBaseElement element) {
        if (ELEMENT_TEMPLATES.containsKey(element.getClass())) {
            return ELEMENT_TEMPLATES.get(element.getClass());
        } else {
            throw new IllegalArgumentException("No template registered for flow element type: %s".formatted(element.getClass().getSimpleName()));
        }
    }
}
