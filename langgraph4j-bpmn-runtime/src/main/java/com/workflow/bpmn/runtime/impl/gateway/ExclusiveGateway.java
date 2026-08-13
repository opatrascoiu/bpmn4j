package com.workflow.bpmn.runtime.impl.gateway;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.api.gateway.IExclusiveGateway;
import com.workflow.bpmn.runtime.impl.JakartaElEvaluator;
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

    protected Boolean elCondition(String text, Map<String, Object> bindings) {
        try {
            JakartaElEvaluator evaluator = new JakartaElEvaluator();
            Object value = evaluator.evaluate(text, bindings);
            if (value instanceof Boolean bool) {
                return bool;
            } else if (value instanceof String string) {
                return Boolean.parseBoolean(string.trim());
            } else {
                throw new IllegalArgumentException(String.format("%s is not a boolean", text));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot evaluate %s".formatted(text), e);
        }
    }

}
