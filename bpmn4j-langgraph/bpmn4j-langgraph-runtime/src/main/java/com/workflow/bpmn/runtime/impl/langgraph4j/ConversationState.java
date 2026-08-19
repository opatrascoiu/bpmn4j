package com.workflow.bpmn.runtime.impl.langgraph4j;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.impl.ExecutionContext;
import org.bsc.langgraph4j.state.AgentState;

import java.util.Map;

public class ConversationState extends AgentState {
    private final IExecutionContext context;

    public ConversationState(Map<String, Object> initData) {
        super(initData);
        this.context = new ExecutionContext();
    }

    public IExecutionContext getContext() {
        return context;
    }
}
