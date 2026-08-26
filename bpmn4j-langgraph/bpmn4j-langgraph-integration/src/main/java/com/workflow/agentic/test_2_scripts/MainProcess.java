package com.workflow.agentic.test_2_scripts;

import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncEdgeAction;

import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

public class MainProcess {
    public Map<String, Object> execute(Map<String, Object> input) throws GraphStateException {
        // Create Graph
        StateGraph<ConversationState> stateGraph = createGraph();

        // Compile Graph
        CompiledGraph<ConversationState> compiledGraph = stateGraph.compile();

        // Execute
        Optional<ConversationState> result = compiledGraph.invoke(input);
        if (result.isPresent()) {
            return result.get().data();
        } else {
            return Map.of();
        }
    }

    private StateGraph<ConversationState> createGraph() throws GraphStateException {
        // Create nodes
        Start start = new Start();
        T1 t1 = new T1();
        Gate11 gate11 = new Gate11();
        T21 t21 = new T21();
        T22 t22 = new T22();
        T23 t23 = new T23();
        Gate12 gate12 = new Gate12();
        T3 t3 = new T3();
        End end = new End();

        // Build graph: START -> ... -> END
        StateGraph<ConversationState> stateGraph = new StateGraph<>(ConversationState::new)
                .addNode("Start", node_async(start))
                .addNode("T1", node_async(t1))
                .addNode("T21", node_async(t21))
                .addNode("T22", node_async(t22))
                .addNode("T23", node_async(t23))
                .addNode("Gate12", node_async(gate12))
                .addNode("T3", node_async(t3))
                .addNode("End", node_async(end))
                // Define linear flow
                .addEdge(START, "Start")
                .addEdge("Start", "T1")
                .addConditionalEdges("T1", AsyncEdgeAction.edge_async(gate11), Map.of("T21", "T21", "T22", "T22", "T23", "T23"))
                .addEdge("T21", "Gate12")
                .addEdge("T22", "Gate12")
                .addEdge("T23", "Gate12")
                .addEdge("Gate12", "T3")
                .addEdge("T3", "End")
                .addEdge("End", END);
        return stateGraph;
    }

    public static void main(String[] args) throws GraphStateException {
        // Start time
        long startTime = System.nanoTime();

        // Execute
        Map<String, Object> initialState = Map.of("path", 2);
        MainProcess mainProcess = new MainProcess();

        // Start time
        long endTime = System.nanoTime();
        System.out.printf("mainProcess time: %,d ns%n", endTime - startTime);

        System.out.println("Initial State: " + initialState);
        System.out.println("Final State: " + mainProcess.execute(initialState));
    }

}
