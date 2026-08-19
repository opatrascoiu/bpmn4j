package com.workflow.bpmn.translator.asl.process.activity;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.runtime.impl.activities.ScriptTask;
import com.workflow.bpmn.translator.NativeFactory;
import com.workflow.bpmn.translator.asl.process.ElementTranslator;
import org.bsc.langgraph4j.action.NodeAction;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TScript;
import org.omg.spec.bpmn._20100524.model.TScriptTask;

import java.util.List;
import java.util.stream.Collectors;

public class ScriptTaskTranslator extends ElementTranslator {
    public ScriptTaskTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // Qualified class names
    //
    @Override
    public String nativeParentFlowNode(TFlowNode node) {
        return ScriptTask.class.getName();
    }

    @Override
    public String nativeLGNode(TFlowNode node) {
        return NodeAction.class.getName();
    }

    //
    // Script related
    //
    public String getScript(TFlowNode node) {
        if (node instanceof TScriptTask scriptTask) {
            TScript script = scriptTask.getScript();
            return collectScripts(script.getContent());
        } else {
            throw new IllegalArgumentException("Illegal node type %s expected ScriptTask".formatted(node.getClass().getSimpleName()));
        }

    }

    private String collectScripts(List<Object> content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        return content.stream().map(Object::toString).collect(Collectors.joining(";\n"));
    }

    public String getResultVariable(TFlowNode node) {
        return this.modelRepository.getExtensionAttribute(node, "resultVariable");
    }
}
