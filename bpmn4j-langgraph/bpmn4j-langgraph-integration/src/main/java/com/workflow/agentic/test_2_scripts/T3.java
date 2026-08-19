package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.activities.ScriptTask;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class T3 extends ScriptTask implements NodeAction<ConversationState> {
    public T3() {
        super("T3");
        setScript(
            """
            result + 1
            """
        );
        setResultVariable("finalResult");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}