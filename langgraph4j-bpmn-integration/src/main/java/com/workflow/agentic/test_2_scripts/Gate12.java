package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.activities.Task;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class Gate12 extends Task implements NodeAction<ConversationState> {
    public Gate12() {
        super("Gate12");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}