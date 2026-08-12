package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.gateway.ExclusiveGateway;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.EdgeAction;

import java.util.Map;

// Edge action for conditional routing
class Gate11 extends ExclusiveGateway implements EdgeAction<ConversationState> {
    public Gate11() {
        super("Gate11");
    }

    @Override
    public String apply(ConversationState state) {
        Map<String, Object> context = state.data();
        String variableName = "branch";
        if (condition1(context, variableName)) {
            return "T21"; // Route to T21
        } else if (condition2(context, variableName)) {
            return "T22"; // Route to T22
        } else if (condition3(context, variableName)) {
            return "T23"; // Route to T23
        } else {
            return "T21"; // Default route
        }
    }

    private boolean condition1(Map<String, Object> context, String variableName) {
        return "branch 1".equals(context.get(variableName));
    }

    private boolean condition2(Map<String, Object> context, String variableName) {
        return "branch 2".equals(context.get(variableName));
    }

    private boolean condition3(Map<String, Object> context, String variableName) {
        return "branch 3".equals(context.get(variableName));
    }
}