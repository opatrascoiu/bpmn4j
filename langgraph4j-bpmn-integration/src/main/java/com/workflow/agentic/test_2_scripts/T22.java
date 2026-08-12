package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.activities.ScriptTask;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class T22 extends ScriptTask implements NodeAction<ConversationState> {
    public T22() {
        super("T22");
        setScript(
            """
            result = 22
            """
        );
        setResultVariable("result");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}