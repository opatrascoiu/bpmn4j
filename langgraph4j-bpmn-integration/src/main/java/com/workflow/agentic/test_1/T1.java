package com.workflow.agentic.test_1;

import com.workflow.bpmn.runtime.impl.activities.Task;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class T1 extends Task implements NodeAction<ConversationState> {
    public T1() {
        super("T1");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) {
        return execute(state.data(), state.getContext());
    }
}