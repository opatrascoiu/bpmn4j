package com.workflow.bpmn.translator.asl.process;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.translator.NativeFactory;
import org.omg.spec.bpmn._20100524.model.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessTranslator extends ElementTranslator {
    public ProcessTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // Qualified class names
    //
    @Override
    public String nativeParentFlowNode(TFlowNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String nativeLGNode(TFlowNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //
    // LG graph
    //
    public List<TFlowNode> flowNodes(TProcess node) {
        List<TFlowNode> result = new ArrayList<>();
        for (JAXBElement<? extends TFlowElement> flowElement : node.getFlowElement()) {
            if (flowElement.getValue() instanceof TFlowNode flowNode) {
                result.add(flowNode);
            }
        }
        return result;
    }

    public List<TSequenceFlow> sequenceFlows(TProcess node) {
        List<TSequenceFlow> result = new ArrayList<>();
        for (JAXBElement<? extends TFlowElement> flowElement : node.getFlowElement()) {
            if (flowElement.getValue() instanceof TSequenceFlow flowNode) {
                result.add(flowNode);
            }
        }
        return result;
    }

    public String firstNodeModelName(TProcess process) {
        List<TFlowNode> startNodes = new ArrayList<>();
        for (JAXBElement<? extends TFlowElement> jaxbElement : process.getFlowElement()) {
            if (jaxbElement.getValue() instanceof TFlowNode flowNode) {
                if (flowNode.getIncoming().isEmpty()) {
                    startNodes.add(flowNode);
                }
            }
        }
        if (startNodes.size() == 1) {
            TFlowNode startFlowNode = startNodes.get(0);
            return this.modelRepository.displayName(startFlowNode);
        } else {
            throw new IllegalArgumentException("Cannot find one single start node for %s".formatted(this.modelRepository.displayName(process)));
        }
    }

    public String lastNodeModelName(TProcess process) {
        List<TFlowNode> endNodes = new ArrayList<>();
        for (JAXBElement<? extends TFlowElement> jaxbElement : process.getFlowElement()) {
            if (jaxbElement.getValue() instanceof TFlowNode flowNode) {
                if (flowNode.getOutgoing().isEmpty()) {
                    endNodes.add(flowNode);
                }
            }
        }
        if (endNodes.size() == 1) {
            TFlowNode endNode = endNodes.get(0);
            return this.modelRepository.displayName(endNode);
        } else {
            throw new IllegalArgumentException("Cannot find one single end node for %s".formatted(this.modelRepository.displayName(process)));
        }
    }

    public String startNodeModelName(TSequenceFlow sequenceFlow) {
        Object ref = sequenceFlow.getSourceRef();
        if (ref instanceof TFlowNode flowNode) {
            return this.modelRepository.displayName(flowNode);
        } else {
            throw new IllegalArgumentException("Cannot find start node for sequence flow %s".formatted(this.modelRepository.displayName(sequenceFlow)));
        }
    }

    public String endNodeModelName(TSequenceFlow sequenceFlow) {
        Object ref = sequenceFlow.getTargetRef();
        if (ref instanceof TFlowNode flowNode) {
            return this.modelRepository.displayName(flowNode);
        } else {
            throw new IllegalArgumentException("Cannot find start node for sequence flow %s".formatted(this.modelRepository.displayName(sequenceFlow)));
        }
    }

    public String endNodeVariableName(TSequenceFlow sequenceFlow) {
        Object ref = sequenceFlow.getTargetRef();
        if (ref instanceof TFlowNode flowNode) {
            return nativeVariableName(flowNode);
        } else {
            throw new IllegalArgumentException("Cannot find start node for sequence flow %s".formatted(this.modelRepository.displayName(sequenceFlow)));
        }
    }

    public boolean mapsToLGNode(TBaseElement element) {
        if (element instanceof TGateway gateway) {
            return !isDivergent(gateway);
        } else {
            return true;
        }
    }

    public boolean mapsToNormalLGEdge(TSequenceFlow flow) {
        Object sourceRef = flow.getSourceRef();
        Object targetRef = flow.getTargetRef();
        return !isDivergent(sourceRef) && !isDivergent(targetRef);
    }

    public boolean mapsToConditionalLGEdge(TSequenceFlow flow) {
        Object sourceRef = flow.getSourceRef();
        Object targetRef = flow.getTargetRef();
        return !isDivergent(sourceRef) && isDivergent(targetRef);
    }

    public String conditionalEdgeMappings(TSequenceFlow sequenceFlow) {
        Object targetRef = sequenceFlow.getTargetRef();
        if (isDivergent(targetRef)) {
            if (targetRef instanceof TExclusiveGateway gateway) {
                List<String> targetNodeNames = new ArrayList<>();
                List<QName> outgoingFlows = gateway.getOutgoing();
                for (QName qName : outgoingFlows) {
                    TFlowElement gEdge = this.modelRepository.findSequenceFlow(qName);
                    if (gEdge instanceof TSequenceFlow gSequenceFlow) {
                        String targetNodeName = this.modelRepository.displayName((TBaseElement) gSequenceFlow.getTargetRef());
                        targetNodeNames.add(targetNodeName);
                    }
                }
                if (!targetNodeNames.isEmpty()) {
                    return targetNodeNames.stream().map(e -> "\"%s\", \"%s\"".formatted(e, e)).collect(Collectors.joining(", "));
                }
                throw new IllegalArgumentException("Cannot find all the outgoing targets of gateway '%s'".formatted(this.modelRepository.displayName(gateway)));
            }
        }
        throw new IllegalArgumentException("Cannot find all the outgoing targets of sequence flow '%s'".formatted(this.modelRepository.displayName(sequenceFlow)));
    }
}
