package com.workflow.bpmn.runtime.impl.gateway;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.api.gateway.IExclusiveGateway;
import com.workflow.bpmn.runtime.impl.log.RuntimeLogger;

import java.util.Map;
import java.util.Objects;

public class ExclusiveGateway extends Gateway implements IExclusiveGateway {
    public ExclusiveGateway(String name) {
        super(name);
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input, IExecutionContext context) {
        Objects.requireNonNull(context, "Missing context");

        RuntimeLogger logger = context.getLogger();
        logger.logEvent(input);
        logger.log("ExclusiveGateway '%s' executed".formatted(this.getName()));
        return input;
    }
}
