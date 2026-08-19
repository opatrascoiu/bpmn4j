<#if nativePackageName?has_content>
package ${nativePackageName};

</#if>
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

public class ${nativeClassName} {
    public Map<String, Object> execute(Map<String, Object> input) throws GraphStateException {
        // Create Graph
        StateGraph<ConversationState> stateGraph = makeGraph();

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

    private StateGraph<ConversationState> makeGraph() throws GraphStateException {
        // Create nodes
        <#assign nodes = translator.flowNodes(element)>
        <#list nodes as node>
        ${translator.nativeQualifiedClassName(node)} ${translator.nativeVariableName(node)} = ${translator.nativeDefaultConstructor(node)};
        </#list>

        // Build graph: START -> ... -> END
        StateGraph<ConversationState> stateGraph = new StateGraph<>(ConversationState::new)
                // Add nodes
                <#list nodes as node>
                <#if translator.mapsToLGNode(node)>
                .addNode("${translator.modelName(node)}", node_async(${translator.nativeVariableName(node)}))
                </#if>
                </#list>
                // Define linear flow
                .addEdge(START, "${translator.firstNodeModelName(element)}")
                <#assign edges = translator.sequenceFlows(element)>
                <#list edges as edge>
                <#if translator.mapsToNormalLGEdge(edge)>
                .addEdge("${translator.startNodeModelName(edge)}", "${translator.endNodeModelName(edge)}")
                <#elseif translator.mapsToConditionalLGEdge(edge)>
                .addConditionalEdges("${translator.startNodeModelName(edge)}", AsyncEdgeAction.edge_async(${translator.endNodeVariableName(edge)}), Map.of(${translator.conditionalEdgeMappings(edge)}))
                </#if>
                </#list>
                .addEdge("${translator.lastNodeModelName(element)}", END);
        return stateGraph;
    }
}
