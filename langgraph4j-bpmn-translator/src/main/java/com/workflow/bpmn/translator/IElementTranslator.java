package com.workflow.bpmn.translator;

import org.omg.spec.bpmn._20100524.model.TBaseElement;
import org.omg.spec.bpmn._20100524.model.TFlowNode;

public interface IElementTranslator {
    //
    // Qualified class names
    //
    String nativeParentFlowNode(TFlowNode node);

    String nativeLGNode(TFlowNode node);

    String actionStateName(TFlowNode node);

    //
    // Names
    //
    String modelName(TBaseElement element);

    String nativePackageName(TBaseElement element);

    String nativeClassName(TBaseElement element);

    String nativeQualifiedClassName(TBaseElement element);

    String nativeVariableName(TBaseElement element);

    String nativeDefaultConstructor(TBaseElement element);

    String getNativeFileExtension();

    String escapeInString(String text);
}
