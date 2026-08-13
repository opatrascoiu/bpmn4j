package com.workflow.bpmn.translator.asl.process.event;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.runtime.impl.events.start.StartEvent;
import com.workflow.bpmn.translator.NativeFactory;
import com.workflow.bpmn.translator.asl.process.ElementTranslator;
import org.bsc.langgraph4j.action.NodeAction;
import org.omg.spec.bpmn._20100524.model.TFlowNode;

public class StartEventTranslator extends ElementTranslator {
    public StartEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // Qualified class names
    //
    @Override
    public String nativeParentFlowNode(TFlowNode node) {
        return StartEvent.class.getName();
    }

    @Override
    public String nativeLGNode(TFlowNode element) {
        return NodeAction.class.getName();
    }
}
