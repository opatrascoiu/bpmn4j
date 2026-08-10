package com.workflow.bpmn.runtime.impl;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.api.IFlowNode;
import com.workflow.bpmn.runtime.impl.log.RuntimeLogger;

import java.util.Map;
import java.util.Objects;

public abstract class FlowNode implements IFlowNode {
    private final String name;

    public FlowNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input, IExecutionContext context) {
        Objects.requireNonNull(context, "Missing context");

        RuntimeLogger logger = context.getLogger();
        logger.log("Executing FlowNode '%s' ...".formatted(this.getName()));
        logger.logEvent(input);
        logger.log("FlowNode '%s' executed".formatted(this.getName()));
        return input;
    }

}
