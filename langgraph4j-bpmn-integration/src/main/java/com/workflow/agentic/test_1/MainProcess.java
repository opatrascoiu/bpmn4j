package com.workflow.agentic.test_1;

import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;

import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class MainProcess {
    public Map<String, Object> execute(Map<String, Object> initialState) throws GraphStateException {
        // Make Graph
        StateGraph<ConversationState> stateGraph = makeGraph();

        // Compile Graph
        CompiledGraph<ConversationState> compiledGraph = stateGraph.compile();

        // Run
        Optional<ConversationState> result = compiledGraph.invoke(initialState);
        return result.isPresent() ? result.get().data() : Map.of();
    }

    private StateGraph<ConversationState> makeGraph() throws GraphStateException {
        // Create nodes
        Start start = new Start();
        T1 t1 = new T1();
        End end = new End();

        // Build graph: START -> greeter -> processor -> responder -> END
        StateGraph<ConversationState> stateGraph = new StateGraph<>(ConversationState::new)
                .addNode("Start", node_async(start))
                .addNode("T1", node_async(t1))
                .addNode("End", node_async(end))
                // Define linear flow
                .addEdge(START, "Start")
                .addEdge("Start", "T1")
                .addEdge("T1", "End")
                .addEdge("End", END);
        return stateGraph;
    }

    public static void main(String[] args) throws GraphStateException {
        MainProcess process = new MainProcess();

        // Initial State
        Map<String, Object> initialState = Map.of("input", "Hello, World!");

        System.out.println("Initial State: " + initialState);
        System.out.println("Final State: " + process.execute(initialState));
    }
}
