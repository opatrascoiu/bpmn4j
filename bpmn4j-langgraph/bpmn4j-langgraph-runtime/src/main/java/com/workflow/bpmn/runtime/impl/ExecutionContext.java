package com.workflow.bpmn.runtime.impl;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import com.workflow.bpmn.runtime.impl.log.RuntimeLogger;
import com.workflow.bpmn.runtime.impl.log.Slf4jRuntimeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionContext implements IExecutionContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationState.class);
    private static final RuntimeLogger RUNTIME_LOGGER = new Slf4jRuntimeLogger(LOGGER);

    private final RuntimeLogger logger;

    public ExecutionContext() {
        this.logger = RUNTIME_LOGGER;
    }

    public ExecutionContext(RuntimeLogger logger) {
        this.logger = logger;
    }

    @Override
    public RuntimeLogger getLogger() {
        return logger;
    }
}
