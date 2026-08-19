package com.workflow.bpmn.translator.asl.process;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.runtime.impl.langgraph4j.ConversationState;
import com.workflow.bpmn.translator.BaseElementTranslator;
import com.workflow.bpmn.translator.NativeFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.omg.spec.bpmn._20100524.model.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ElementTranslator extends BaseElementTranslator {
    protected ElementTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        super(modelRepository, nativeFactory);
    }

    //
    // LG graph
    //
    public boolean isDivergent(Object node) {
        if (node instanceof TGateway gateway) {
            // Check direction
            if (TGatewayDirection.DIVERGING.equals(gateway.getGatewayDirection())) {
                return true;
            }
            // Check incoming and outgoing
            int incomingBranches = gateway.getIncoming().size();
            int outgoingBranches = gateway.getOutgoing().size();
            if (incomingBranches == 1 && outgoingBranches > 1) {
                return true;
            } else if (incomingBranches > 1 && outgoingBranches == 1) {
                return false;
            } else {
                throw new IllegalArgumentException("Cannot infer kind of gateway based on incoming branches %d and outgoing branches %d".formatted(incomingBranches, outgoingBranches));
            }
        }
        return false;
    }

    //
    // Qualified class names
    //
    @Override
    public String actionStateName(TFlowNode node) {
        return ConversationState.class.getName();
    }

    //
    // Common
    //
    @Override
    public String modelName(TBaseElement element) {
        return this.modelRepository.displayName(element);
    }

    @Override
    public String nativePackageName(TBaseElement element) {
        return this.nativeFactory.nativePackageName(this.modelRepository.displayName(element));
    }

    @Override
    public String nativeClassName(TBaseElement element) {
        return this.nativeFactory.nativeClassName(this.modelRepository.displayName(element));
    }

    @Override
    public String nativeQualifiedClassName(TBaseElement element) {
        String nativePackageName = nativePackageName(element);
        String nativeClassName = nativeClassName(element);
        if (StringUtils.isBlank(nativePackageName)) {
            return nativeClassName;
        } else {
            return "%s.%s".formatted(nativePackageName, nativeClassName);
        }
    }

    @Override
    public String nativeVariableName(TBaseElement element) {
        String nativeClassName = nativeClassName(element);
        return "%s%s".formatted(nativeClassName.substring(0, 1).toLowerCase(), nativeClassName.substring(1));
    }

    public String nativeDefaultConstructor(TBaseElement element) {
        return "new %s()".formatted(nativeQualifiedClassName(element));
    }

    @Override
    public String getNativeFileExtension() {
        return this.nativeFactory.getNativeFileExtension();
    }

    @Override
    public String escapeInString(String text) {
        return StringEscapeUtils.escapeJava(text);
    }
}
