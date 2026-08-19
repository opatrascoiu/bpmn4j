package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.events.start.StartEvent;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class Start extends StartEvent implements NodeAction<ConversationState> {
    public Start() {
        super("Start");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}
