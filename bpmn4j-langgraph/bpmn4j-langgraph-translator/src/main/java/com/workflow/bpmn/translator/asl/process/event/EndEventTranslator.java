package com.workflow.bpmn.translator.asl.process.event;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.runtime.impl.events.end.EndEvent;
import com.workflow.bpmn.translator.NativeFactory;
import com.workflow.bpmn.translator.asl.process.ElementTranslator;
import org.bsc.langgraph4j.action.NodeAction;
import org.omg.spec.bpmn._20100524.model.TFlowNode;

public class EndEventTranslator extends ElementTranslator {
    public EndEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // Qualified class names
    //
    @Override
    public String nativeParentFlowNode(TFlowNode node) {
        return EndEvent.class.getName();
    }

    @Override
    public String nativeLGNode(TFlowNode node) {
        return NodeAction.class.getName();
    }
}
