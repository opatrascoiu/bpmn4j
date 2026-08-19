package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.activities.ScriptTask;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;

public class T1 extends ScriptTask implements NodeAction<ConversationState> {
    public T1() {
        super("T1");
        setScript(
                """
                def branch = "";
                if (path == 1) {
                   branch = "branch 1";
                } else if (path == 2) {
                   branch = "branch 2";
                } else if (path == 3) {
                   branch = "branch 3";
                } else {
                }
                """
        );
        setResultVariable("branch");
    }

    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        return execute(state.data(), state.getContext());
    }
}