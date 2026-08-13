package com.workflow.bpmn.translator.asl.process.gateway;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.runtime.impl.activities.Task;
import com.workflow.bpmn.runtime.impl.gateway.ExclusiveGateway;
import com.workflow.bpmn.translator.NativeFactory;
import com.workflow.bpmn.translator.asl.process.ElementTranslator;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;
import org.omg.spec.bpmn._20100524.model.*;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ExclusiveGatewayTranslator extends ElementTranslator {
    public ExclusiveGatewayTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // Qualified class names
    //
    @Override
    public String nativeParentFlowNode(TFlowNode node) {
        if (isDivergent(node)) {
            return ExclusiveGateway.class.getName();
        } else {
            return Task.class.getName();
        }
    }

    @Override
    public String nativeLGNode(TFlowNode node) {
        if (isDivergent(node)) {
            return EdgeAction.class.getName();
        } else {
            return NodeAction.class.getName();
        }
    }

    //
    // Gateway related
    //
    public List<Branch> branches(TFlowNode node) {
        return node.getOutgoing().stream().map(qn -> toBranch(qn)).filter(Objects::nonNull).toList();
    }

    public Branch defaultBranch(TFlowNode node) {
        if (node instanceof TExclusiveGateway gateway) {
            Object object = gateway.getDefault();
            if (object instanceof TSequenceFlow sequenceFlow) {
                Object targetRef = sequenceFlow.getTargetRef();
                if (targetRef instanceof TFlowNode flowNode) {
                    return new Branch("", flowNode.getName());
                }
            }
        }
        return null;
    }

    private Branch toBranch(QName qn) {
        TFlowElement flowElement = this.modelRepository.findSequenceFlow(qn);
        if (flowElement instanceof TSequenceFlow sequenceFlow) {
            TExpression conditionExpression = sequenceFlow.getConditionExpression();
            if (conditionExpression != null) {
                List<Serializable> content = conditionExpression.getContent();
                if (content != null && content.size() == 1) {
                    String text = content.get(0).toString();
                    Object targetRef = sequenceFlow.getTargetRef();
                    if (targetRef instanceof TFlowNode node) {
                        return new Branch(text, node.getName());
                    }
                }
            }
        }
        return null;
    }
}
