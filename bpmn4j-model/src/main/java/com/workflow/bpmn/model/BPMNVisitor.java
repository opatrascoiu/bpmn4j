package com.workflow.bpmn.model;

import org.omg.spec.bpmn._20100524.model.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

public interface BPMNVisitor {
    default void visit(TDefinitions model, VisitorContext modelContext) {
        if (model != null) {
            List<JAXBElement<? extends TRootElement>> jaxbRootElements = model.getRootElement();
            if (jaxbRootElements != null) {
                for (JAXBElement<? extends TRootElement> jaxbElement: jaxbRootElements) {
                    if (jaxbElement != null) {
                        visit(jaxbElement.getValue(), modelContext);
                    }
                }
            }
            List<TImport> importList = model.getImport();
            for (TImport element : importList) {
                visit(element, modelContext);
            }
            List<TExtension> extensionList = model.getExtension();
            for (TExtension element : extensionList) {
                visit(element, modelContext);
            }
            List<BPMNDiagram> diagramList = model.getBPMNDiagram();
            for (BPMNDiagram element : diagramList) {
                visit(element, modelContext);
            }
            List<TRelationship> relationshipList = model.getRelationship();
            for (TRelationship element : relationshipList) {
                visit(element, modelContext);
            }
        }
    }

    default void visit(TBaseElement element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TDataState) {
                visit(((TDataState) element), context);
            } else if (element instanceof TLane) {
                visit(((TLane) element), context);
            } else if (element instanceof TCategoryValue) {
                visit(((TCategoryValue) element), context);
            } else if (element instanceof TResourceParameter) {
                visit(((TResourceParameter) element), context);
            } else if (element instanceof TFlowElement) {
                visit(((TFlowElement) element), context);
            } else if (element instanceof TRelationship) {
                visit(((TRelationship) element), context);
            } else if (element instanceof TRootElement) {
                visit(((TRootElement) element), context);
            } else if (element instanceof TResourceRole) {
                visit(((TResourceRole) element), context);
            } else if (element instanceof TMessageFlowAssociation) {
                visit(((TMessageFlowAssociation) element), context);
            } else if (element instanceof TAssignment) {
                visit(((TAssignment) element), context);
            } else if (element instanceof TResourceParameterBinding) {
                visit(((TResourceParameterBinding) element), context);
            } else if (element instanceof TCorrelationPropertyRetrievalExpression) {
                visit(((TCorrelationPropertyRetrievalExpression) element), context);
            } else if (element instanceof TDataInput) {
                visit(((TDataInput) element), context);
            } else if (element instanceof TConversationAssociation) {
                visit(((TConversationAssociation) element), context);
            } else if (element instanceof TMessageFlow) {
                visit(((TMessageFlow) element), context);
            } else if (element instanceof TInputOutputBinding) {
                visit(((TInputOutputBinding) element), context);
            } else if (element instanceof TLaneSet) {
                visit(((TLaneSet) element), context);
            } else if (element instanceof TAuditing) {
                visit(((TAuditing) element), context);
            } else if (element instanceof TOutputSet) {
                visit(((TOutputSet) element), context);
            } else if (element instanceof TMonitoring) {
                visit(((TMonitoring) element), context);
            } else if (element instanceof TParticipantMultiplicity) {
                visit(((TParticipantMultiplicity) element), context);
            } else if (element instanceof TProperty) {
                visit(((TProperty) element), context);
            } else if (element instanceof TCorrelationSubscription) {
                visit(((TCorrelationSubscription) element), context);
            } else if (element instanceof TComplexBehaviorDefinition) {
                visit(((TComplexBehaviorDefinition) element), context);
            } else if (element instanceof TInputOutputSpecification) {
                visit(((TInputOutputSpecification) element), context);
            } else if (element instanceof TInputSet) {
                visit(((TInputSet) element), context);
            } else if (element instanceof TParticipantAssociation) {
                visit(((TParticipantAssociation) element), context);
            } else if (element instanceof TOperation) {
                visit(((TOperation) element), context);
            } else if (element instanceof TRendering) {
                visit(((TRendering) element), context);
            } else if (element instanceof TConversationNode) {
                visit(((TConversationNode) element), context);
            } else if (element instanceof TParticipant) {
                visit(((TParticipant) element), context);
            } else if (element instanceof TLoopCharacteristics) {
                visit(((TLoopCharacteristics) element), context);
            } else if (element instanceof TDataOutput) {
                visit(((TDataOutput) element), context);
            } else if (element instanceof TCorrelationKey) {
                visit(((TCorrelationKey) element), context);
            } else if (element instanceof TConversationLink) {
                visit((TConversationLink) element, context);
            } else if (element instanceof TCorrelationPropertyBinding) {
                visit(((TCorrelationPropertyBinding) element), context);
            } else if (element instanceof TArtifact) {
                visit(((TArtifact) element), context);
            } else if (element instanceof TDataAssociation) {
                visit(((TDataAssociation) element), context);
            } else if (element instanceof TResourceAssignmentExpression) {
                visit(((TResourceAssignmentExpression) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }

    default void visit(TDataState element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TLane element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getChildLaneSet(), elementContext);
            visit(element.getPartitionElement(), elementContext);
        }
    }
    default void visit(TCategoryValue element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TResourceParameter element, VisitorContext context) {
        if (element != null) {
            visitBaseElementProperties(element, VisitorContext.of(context, element));
        }
    }

    // TFlowElement
    default void visit(TFlowElement element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TFlowNode) {
                visit(((TFlowNode) element), context);
            } else if (element instanceof TDataStoreReference) {
                visit(((TDataStoreReference) element), context);
            } else if (element instanceof TDataObjectReference) {
                visit(((TDataObjectReference) element), context);
            } else if (element instanceof TSequenceFlow) {
                visit(((TSequenceFlow) element), context);
            } else if (element instanceof TDataObject) {
                visit(((TDataObject) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }

    // TFlowNode
    default void visit(TFlowNode element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TChoreographyActivity) {
                visit(((TChoreographyActivity) element), context);
            } else if (element instanceof TActivity) {
                visit(((TActivity) element), context);
            } else if (element instanceof TGateway) {
                visit(((TGateway) element), context);
            } else if (element instanceof TEvent) {
                visit(((TEvent) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }

    // TChoreographyActivity
    default void visit(TChoreographyActivity element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TChoreographyTask) {
                visit(((TChoreographyTask) element), context);
            } else if (element instanceof TSubChoreography) {
                visit(((TSubChoreography) element), context);
            } else if (element instanceof TCallChoreography) {
                visit(((TCallChoreography) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TChoreographyTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitChoreographyActivityProperties(element, elementContext);
        }
    }
    default void visit(TSubChoreography element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitChoreographyActivityProperties(element, elementContext);
            visitJaxbFlowElementList(element.getFlowElement(), elementContext);
            visitJaxbArtifactList(element.getArtifact(), elementContext);
        }
    }
    default void visit(TCallChoreography element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitChoreographyActivityProperties(element, elementContext);
            visitBaseElementList(element.getParticipantAssociation(), elementContext);
        }
    }

    // TActivity
    default void visit(TActivity element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TCallActivity) {
                visit(((TCallActivity) element), context);
            } else if (element instanceof TTask) {
                visit(((TTask) element), context);
            } else if (element instanceof TSubProcess) {
                visit(((TSubProcess) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TCallActivity element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitActivityProperties(element, elementContext);
        }
    }

    // TTask
    default void visit(TTask element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TServiceTask) {
                visit(((TServiceTask) element), context);
            } else if (element instanceof TManualTask) {
                visit(((TManualTask) element), context);
            } else if (element instanceof TSendTask) {
                visit(((TSendTask) element), context);
            } else if (element instanceof TUserTask) {
                visit(((TUserTask) element), context);
            } else if (element instanceof TBusinessRuleTask) {
                visit(((TBusinessRuleTask) element), context);
            } else if (element instanceof TReceiveTask) {
                visit(((TReceiveTask) element), context);
            } else if (element instanceof TScriptTask) {
                visit(((TScriptTask) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitTaskProperties(element, elementContext);
            }
        }
    }
    default void visit(TServiceTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
        }
    }
    default void visit(TManualTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
        }
    }
    default void visit(TSendTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
        }
    }
    default void visit(TUserTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
            visitBaseElementList(element.getRendering(), elementContext);
        }
    }
    default void visit(TBusinessRuleTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
        }
    }
    default void visit(TReceiveTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
        }
    }
    default void visit(TScriptTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitTaskProperties(element, elementContext);
            visit(element.getScript(), elementContext);
        }
    }

    // TSubProcess
    default void visit(TSubProcess element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TTransaction) {
                visit(((TTransaction) element), context);
            } else if (element instanceof TAdHocSubProcess) {
                visit(((TAdHocSubProcess) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitSubProcessProperties(element, elementContext);
            }
        }
    }
    default void visit(TTransaction element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitSubProcessProperties(element, elementContext);
        }
    }
    default void visit(TAdHocSubProcess element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitSubProcessProperties(element, elementContext);
            visit(element.getCompletionCondition(), elementContext);
        }
    }

    // TGateway
    default void visit(TGateway element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TInclusiveGateway) {
                visit(((TInclusiveGateway) element), context);
            } else if (element instanceof TExclusiveGateway) {
                visit(((TExclusiveGateway) element), context);
            } else if (element instanceof TComplexGateway) {
                visit(((TComplexGateway) element), context);
            } else if (element instanceof TParallelGateway) {
                visit(((TParallelGateway) element), context);
            } else if (element instanceof TEventBasedGateway) {
                visit(((TEventBasedGateway) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitGatewayProperties(element, elementContext);
            }
        }
    }
    default void visit(TInclusiveGateway element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGatewayProperties(element, elementContext);
        }
    }
    default void visit(TExclusiveGateway element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGatewayProperties(element, elementContext);
        }
    }
    default void visit(TComplexGateway element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGatewayProperties(element, elementContext);
            visit(element.getActivationCondition(), elementContext);
        }
    }
    default void visit(TParallelGateway element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGatewayProperties(element, elementContext);
        }
    }
    default void visit(TEventBasedGateway element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGatewayProperties(element, elementContext);
        }
    }

    // TEvent
    default void visit(TEvent element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TThrowEvent) {
                visit(((TThrowEvent) element), context);
            } else if (element instanceof TCatchEvent) {
                visit(((TCatchEvent) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }

    // TThrowEvent
    default void visit(TThrowEvent element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TIntermediateThrowEvent) {
                visit(((TIntermediateThrowEvent) element), context);
            } else if (element instanceof TImplicitThrowEvent) {
                visit(((TImplicitThrowEvent) element), context);
            } else if (element instanceof TEndEvent) {
                visit(((TEndEvent) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TIntermediateThrowEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitThrowEventProperties(element, elementContext);
        }
    }
    default void visit(TImplicitThrowEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitThrowEventProperties(element, elementContext);
        }
    }
    default void visit(TEndEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitThrowEventProperties(element, elementContext);
        }
    }

    // TCatchEvent
    default void visit(TCatchEvent element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TBoundaryEvent) {
                visit(((TBoundaryEvent) element), context);
            } else if (element instanceof TStartEvent) {
                visit(((TStartEvent) element), context);
            } else if (element instanceof TIntermediateCatchEvent) {
                visit(((TIntermediateCatchEvent) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TBoundaryEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitCatchEventProperties(element, elementContext);
        }
    }
    default void visit(TStartEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitCatchEventProperties(element, elementContext);
        }
    }
    default void visit(TIntermediateCatchEvent element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitCatchEventProperties(element, elementContext);
        }
    }

    default void visit(TDataStoreReference element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitFlowElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TDataObjectReference element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitFlowElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TSequenceFlow element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitFlowElementProperties(element, elementContext);
            visit(element.getConditionExpression(), elementContext);
        }
    }
    default void visit(TDataObject element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitFlowElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }

    default void visit(TRelationship element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }

    // TRootElement
    default void visit(TRootElement element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TCategory) {
                visit(((TCategory) element), context);
            } else if (element instanceof TCorrelationProperty) {
                visit(((TCorrelationProperty) element), context);
            } else if (element instanceof TSignal) {
                visit(((TSignal) element), context);
            } else if (element instanceof TResource) {
                visit(((TResource) element), context);
            } else if (element instanceof TItemDefinition) {
                visit(((TItemDefinition) element), context);
            } else if (element instanceof TPartnerEntity) {
                visit(((TPartnerEntity) element), context);
            } else if (element instanceof TEscalation) {
                visit(((TEscalation) element), context);
            } else if (element instanceof TPartnerRole) {
                visit(((TPartnerRole) element), context);
            } else if (element instanceof TMessage) {
                visit(((TMessage) element), context);
            } else if (element instanceof TCollaboration) {
                visit(((TCollaboration) element), context);
            } else if (element instanceof TDataStore) {
                visit(((TDataStore) element), context);
            } else if (element instanceof TInterface) {
                visit(((TInterface) element), context);
            } else if (element instanceof TError) {
                visit(((TError) element), context);
            } else if (element instanceof TEndPoint) {
                visit(((TEndPoint) element), context);
            } else if (element instanceof TCallableElement) {
                visit(((TCallableElement) element), context);
            } else if (element instanceof TEventDefinition) {
                visit(((TEventDefinition) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TCategory element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
            visitBaseElementList(element.getCategoryValue(), elementContext);
        }
    }
    default void visit(TCorrelationProperty element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
            visitBaseElementList(element.getCorrelationPropertyRetrievalExpression(), elementContext);
        }
    }
    default void visit(TSignal element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TResource element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
            visitBaseElementList(element.getResourceParameter(), elementContext);
        }
    }
    default void visit(TItemDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TPartnerEntity element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TEscalation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TPartnerRole element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TMessage element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }

    // TCollaboration
    default void visit(TCollaboration element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TGlobalConversation) {
                visit(((TGlobalConversation) element), context);
            } else if (element instanceof TChoreography) {
                visit(((TChoreography) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitCollaborationProperties(element, elementContext);
            }
        }
    }
    default void visit(TGlobalConversation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitCollaborationProperties(element, elementContext);
        }
    }
    // TChoreography
    default void visit(TChoreography element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TGlobalChoreographyTask) {
                visit(((TGlobalChoreographyTask) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitChoreographyProperties(element, elementContext);
            }
        }
    }
    default void visit(TGlobalChoreographyTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitChoreographyProperties(element, elementContext);
        }
    }

    default void visit(TDataStore element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TInterface element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
            visitBaseElementList(element.getOperation(), elementContext);
        }
    }
    default void visit(TError element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }
    default void visit(TEndPoint element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitRootElementProperties(element, elementContext);
        }
    }

    // TCallableElement
    default void visit(TCallableElement element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TProcess) {
                visit(((TProcess) element), context);
            } else if (element instanceof TGlobalTask) {
                visit(((TGlobalTask) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TProcess element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitCallableElementProperties(element, elementContext);

            visit(element.getAuditing(), elementContext);
            visit(element.getMonitoring(), elementContext);
            visitBaseElementList(element.getProperty(), elementContext);
            visitBaseElementList(element.getLaneSet(), elementContext);
            visitJaxbFlowElementList(element.getFlowElement(), elementContext);
            visitJaxbArtifactList(element.getArtifact(), elementContext);
            visitJaxbResourceRoleList(element.getResourceRole(), elementContext);
            visitBaseElementList(element.getCorrelationSubscription(), elementContext);
        }
    }
    // TGlobalTask
    default void visit(TGlobalTask element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TGlobalBusinessRuleTask) {
                visit(((TGlobalBusinessRuleTask) element), context);
            } else if (element instanceof TGlobalUserTask) {
                visit(((TGlobalUserTask) element), context);
            } else if (element instanceof TGlobalManualTask) {
                visit(((TGlobalManualTask) element), context);
            } else if (element instanceof TGlobalScriptTask) {
                visit(((TGlobalScriptTask) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitGlobalTaskProperties(element, elementContext);
            }
        }
    }
    default void visit(TGlobalBusinessRuleTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGlobalTaskProperties(element, elementContext);
        }
    }
    default void visit(TGlobalUserTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGlobalTaskProperties(element, elementContext);
            visitBaseElementList(element.getRendering(), elementContext);
        }
    }
    default void visit(TGlobalManualTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGlobalTaskProperties(element, elementContext);
        }
    }
    default void visit(TGlobalScriptTask element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitGlobalTaskProperties(element, elementContext);
            visit(element.getScript(), elementContext);
        }
    }

    // TEventDefinition
    default void visit(TEventDefinition element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TSignalEventDefinition) {
                visit(((TSignalEventDefinition) element), context);
            } else if (element instanceof TConditionalEventDefinition) {
                visit(((TConditionalEventDefinition) element), context);
            } else if (element instanceof TMessageEventDefinition) {
                visit(((TMessageEventDefinition) element), context);
            } else if (element instanceof TCancelEventDefinition) {
                visit(((TCancelEventDefinition) element), context);
            } else if (element instanceof TErrorEventDefinition) {
                visit(((TErrorEventDefinition) element), context);
            } else if (element instanceof TEscalationEventDefinition) {
                visit(((TEscalationEventDefinition) element), context);
            } else if (element instanceof TTerminateEventDefinition) {
                visit(((TTerminateEventDefinition) element), context);
            } else if (element instanceof TTimerEventDefinition) {
                visit(((TTimerEventDefinition) element), context);
            } else if (element instanceof TCompensateEventDefinition) {
                visit(((TCompensateEventDefinition) element), context);
            } else if (element instanceof TLinkEventDefinition) {
                visit(((TLinkEventDefinition) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TSignalEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TConditionalEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
            visit(element.getCondition(), elementContext);
        }
    }
    default void visit(TMessageEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TCancelEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TErrorEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TEscalationEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TTerminateEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TTimerEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
            visit(element.getTimeDate(), elementContext);
            visit(element.getTimeDuration(), elementContext);
            visit(element.getTimeCycle(), elementContext);
        }
    }
    default void visit(TCompensateEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }
    default void visit(TLinkEventDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitEventDefinitionProperties(element, elementContext);
        }
    }

    // TResourceRole
    default void visit(TResourceRole element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TPerformer) {
                visit(((TPerformer) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitResourceRoleProperties(element, elementContext);
            }
        }
    }
    default void visit(TPerformer element, VisitorContext context) {
        if (element != null) {
            if (element instanceof THumanPerformer) {
                visit(((THumanPerformer) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitPerformerProperties(element, elementContext);
            }
        }
    }
    default void visit(THumanPerformer element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TPotentialOwner) {
                visit(((TPotentialOwner) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                humanPerformerProperties(element, elementContext);
            }
        }
    }
    default void visit(TPotentialOwner element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            humanPerformerProperties(element, elementContext);
        }
    }

    default void visit(TMessageFlowAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TAssignment element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getFrom(), elementContext);
            visit(element.getTo(), elementContext);
        }
    }
    default void visit(TResourceParameterBinding element, VisitorContext context) {
        if (element != null) {
            JAXBElement<? extends TExpression> expression = element.getExpression();
            if (expression != null) {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitBaseElementProperties(element, elementContext);
                visit(expression.getValue(), elementContext);
            }
        }
    }
    default void visit(TCorrelationPropertyRetrievalExpression element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getMessagePath(), elementContext);
        }
    }
    default void visit(TDataInput element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TConversationAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TMessageFlow element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TInputOutputBinding element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TLaneSet element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visitBaseElementList(element.getLane(), elementContext);
        }
    }
    default void visit(TAuditing element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TOutputSet element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TMonitoring element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TParticipantMultiplicity element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TProperty element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TCorrelationSubscription element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visitBaseElementList(element.getCorrelationPropertyBinding(), elementContext);
        }
    }
    default void visit(TComplexBehaviorDefinition element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getCondition(), elementContext);
            visit(element.getEvent(), elementContext);
        }
    }
    default void visit(TInputOutputSpecification element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visitBaseElementList(element.getDataInput(), elementContext);
            visitBaseElementList(element.getDataOutput(), elementContext);
            visitBaseElementList(element.getInputSet(), elementContext);
            visitBaseElementList(element.getOutputSet(), elementContext);
        }
    }
    default void visit(TInputSet element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TParticipantAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TOperation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TRendering element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TParticipant element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }

    // TConversationNode
    default void visit(TConversationNode element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TSubConversation) {
              visit(((TSubConversation) element), context);
            } else if (element instanceof TCallConversation) {
              visit(((TCallConversation) element), context);
            } else if (element instanceof TConversation) {
              visit(((TConversation) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TSubConversation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitConversationNodeProperties(element, elementContext);
            visitJaxbConversationNodeList(element.getConversationNode(), elementContext);
        }
    }
    default void visit(TCallConversation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitConversationNodeProperties(element, elementContext);
            visitBaseElementList(element.getParticipantAssociation(), elementContext);
        }
    }
    default void visit(TConversation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitConversationNodeProperties(element, elementContext);
        }
    }

    // TLoopCharacteristics
    default void visit(TLoopCharacteristics element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TMultiInstanceLoopCharacteristics) {
                visit(((TMultiInstanceLoopCharacteristics) element), context);
            } else if (element instanceof TStandardLoopCharacteristics) {
                visit(((TStandardLoopCharacteristics) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TMultiInstanceLoopCharacteristics element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitLoopCharacteristicsProperties(element, elementContext);
            visit(element.getLoopCardinality(), elementContext);
            visit(element.getInputDataItem(), elementContext);
            visit(element.getOutputDataItem(), elementContext);
            visitBaseElementList(element.getComplexBehaviorDefinition(), elementContext);
            visit(element.getCompletionCondition(), elementContext);
        }
    }
    default void visit(TStandardLoopCharacteristics element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitLoopCharacteristicsProperties(element, elementContext);
            visit(element.getLoopCondition(), elementContext);
        }
    }

    default void visit(TDataOutput element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getDataState(), elementContext);
        }
    }
    default void visit(TCorrelationKey element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TConversationLink element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
        }
    }
    default void visit(TCorrelationPropertyBinding element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitBaseElementProperties(element, elementContext);
            visit(element.getDataPath(), elementContext);
        }
    }

    // TArtifact
    default void visit(TArtifact element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TGroup) {
                visit(((TGroup) element), context);
            } else if (element instanceof TAssociation) {
                visit(((TAssociation) element), context);
            } else if (element instanceof TTextAnnotation) {
                visit(((TTextAnnotation) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(TGroup element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitArtifactProperties(element, elementContext);
        }
    }
    default void visit(TAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitArtifactProperties(element, elementContext);
        }
    }
    default void visit(TTextAnnotation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitArtifactProperties(element, elementContext);
            visit(element.getText(), elementContext);
        }
    }

    // TDataAssociation
    default void visit(TDataAssociation element, VisitorContext context) {
        if (element != null) {
            if (element instanceof TDataOutputAssociation) {
                visit(((TDataOutputAssociation) element), context);
            } else if (element instanceof TDataInputAssociation) {
                visit(((TDataInputAssociation) element), context);
            } else {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitDataAssociationProperties(element, elementContext);
            }
        }
    }
    default void visit(TDataOutputAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitDataAssociationProperties(element, elementContext);
        }
    }
    default void visit(TDataInputAssociation element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visitDataAssociationProperties(element, elementContext);
        }
    }

    default void visit(TResourceAssignmentExpression element, VisitorContext context) {
        if (element != null) {
            JAXBElement<? extends TExpression> expression = element.getExpression();
            if (expression != null) {
                VisitorContext elementContext = VisitorContext.of(context, element);

                visitBaseElementProperties(element, elementContext);
                visit(expression.getValue(), elementContext);
            }
        }
    }

    // Other
    void visit(TDocumentation element, VisitorContext context);
    void visit(TExtensionElements element, VisitorContext context);
    void visit(TScript element, VisitorContext context);
    default void visit(TExpression element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getOtherAttributes(), elementContext);
        }
    }
    void visit(TText element, VisitorContext context);
    void visit(Map<QName, String> otherAttributes, VisitorContext context);
    void visit(TImport element, VisitorContext context);
    default void visit(TExtension element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            for (TDocumentation doc: element.getDocumentation()) {
                visit(doc, elementContext);
            }
        }
    }

    // Diagram
    default void visit(Diagram element, VisitorContext context) {
        if (element != null) {
            if (element instanceof BPMNDiagram) {
                visit(((BPMNDiagram) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(BPMNDiagram element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getBPMNPlane(), elementContext);
            for (BPMNLabelStyle style: element.getBPMNLabelStyle()) {
                visit(style, elementContext);
            }
        }
    }
    default void visit(BPMNLabelStyle element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getFont(), elementContext);
        }
    }
    default void visit(DiagramElement element, VisitorContext context) {
        if (element != null) {
            visit(element.getExtension(), context);

            if (element instanceof Edge) {
                visit(((Edge) element), context);
            } else if (element instanceof Node) {
                visit(((Node) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(Edge element, VisitorContext context) {
        if (element != null) {
            for (Point point: element.getWaypoint()) {
                visit(point, context);
            }

            if (element instanceof BPMNEdge) {
                visit(((BPMNEdge) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(Node element, VisitorContext context) {
        if (element != null) {
            if (element instanceof BPMNLabel) {
                visit(((BPMNLabel) element), context);
            } else if (element instanceof BPMNShape) {
                visit(((BPMNShape) element), context);
            } else if (element instanceof BPMNPlane) {
                visit(((BPMNPlane) element), context);
            } else {
                throw new IllegalArgumentException(String.format("BPMN element '%s' is not supported yet", element.getClass().getSimpleName()));
            }
        }
    }
    default void visit(BPMNEdge element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getBPMNLabel(), elementContext);
        }
    }
    default void visit(BPMNLabel element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getBounds(), elementContext);
        }
    }
    default void visit(BPMNShape element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            visit(element.getBounds(), elementContext);
            visit(element.getBPMNLabel(), elementContext);
        }
    }
    default void visit(BPMNPlane element, VisitorContext context) {
        if (element != null) {
            VisitorContext elementContext = VisitorContext.of(context, element);

            List<JAXBElement<? extends DiagramElement>> jaxbElements = element.getDiagramElement();
            for (JAXBElement<? extends DiagramElement> jaxbElement: jaxbElements) {
                visit(jaxbElement.getValue(), elementContext);
            }
        }
    }
    void visit(Bounds element, VisitorContext context);
    void visit(DiagramElement.Extension element, VisitorContext context);
    void visit(Font element, VisitorContext context);
    void visit(Point element, VisitorContext context);

    private void visitBaseElementList(List<? extends TBaseElement> correlationKey, VisitorContext context) {
        if (correlationKey != null) {
            for (TBaseElement e: correlationKey) {
                visit(e, context);
            }
        }
    }

    private void visitJaxbArtifactList(List<JAXBElement<? extends TArtifact>> artifact, VisitorContext context) {
        if (artifact != null) {
            for (JAXBElement<? extends TArtifact> jaxb: artifact) {
                if (jaxb != null) {
                  visit(jaxb.getValue(), context);
                }
            }
        }
    }

    private void visitJaxbFlowElementList(List<JAXBElement<? extends TFlowElement>> flowElement, VisitorContext context) {
        if (flowElement != null) {
            for (JAXBElement<? extends TFlowElement> jaxb: flowElement) {
                if (jaxb != null) {
                  visit(jaxb.getValue(), context);
                }
            }
        }
    }

    private void visitJaxbResourceRoleList(List<JAXBElement<? extends TResourceRole>> resourceRole, VisitorContext context) {
        if (resourceRole != null) {
            for (JAXBElement<? extends TResourceRole> jaxb: resourceRole) {
                if (jaxb != null) {
                  visit(jaxb.getValue(), context);
                }
            }
        }
    }

    private void visitJaxbEventDefinitionList(List<JAXBElement<? extends TEventDefinition>> eventDefinition, VisitorContext context) {
        if (eventDefinition != null) {
            for (JAXBElement<? extends TEventDefinition> jaxb: eventDefinition) {
                if (jaxb != null) {
                  visit(jaxb.getValue(), context);
                }
            }
        }
    }

    private void visitJaxbConversationNodeList(List<JAXBElement<? extends TConversationNode>> conversationNode, VisitorContext context) {
        if (conversationNode != null) {
            for (JAXBElement<? extends TConversationNode> jaxb: conversationNode) {
                if (jaxb != null) {
                  visit(jaxb.getValue(), context);
                }
            }
        }
    }

    private void visitBaseElementProperties(TBaseElement element, VisitorContext context) {
        visit(element.getOtherAttributes(), context);
        List<TDocumentation> documentation = element.getDocumentation();
        if (documentation != null) {
            for (TDocumentation d: documentation) {
                visit(d, context);
            }
        }
        visit(element.getExtensionElements(), context);
    }

    private void visitFlowElementProperties(TFlowElement element, VisitorContext context) {
        visitBaseElementProperties(element, context);

        visit(element.getAuditing(), context);
        visit(element.getMonitoring(), context);
    }

    private void visitFlowNodeProperties(TFlowNode element, VisitorContext elementContext) {
        visitFlowElementProperties(element, elementContext);
    }

    private void visitChoreographyActivityProperties(TChoreographyActivity element, VisitorContext context) {
        visitFlowNodeProperties(element, context);

        visitBaseElementList(element.getCorrelationKey(), context);
    }

    private void visitActivityProperties(TActivity element, VisitorContext context) {
        visitFlowNodeProperties(element, context);

        visit(element.getIoSpecification(), context);
        visitBaseElementList(element.getProperty(), context);
        visitBaseElementList(element.getDataInputAssociation(), context);
        visitBaseElementList(element.getDataOutputAssociation(), context);
        visitJaxbResourceRoleList(element.getResourceRole(), context);
        JAXBElement<? extends TLoopCharacteristics> loopCharacteristics = element.getLoopCharacteristics();
        if (loopCharacteristics != null) {
            visit(loopCharacteristics.getValue(), context);
        }
    }

    private void visitTaskProperties(TTask element, VisitorContext elementContext) {
        visitActivityProperties(element, elementContext);
    }

    private void visitSubProcessProperties(TSubProcess element, VisitorContext elementContext) {
        visitActivityProperties(element, elementContext);

        visitBaseElementList(element.getLaneSet(), elementContext);
        visitJaxbFlowElementList(element.getFlowElement(), elementContext);
        visitJaxbArtifactList(element.getArtifact(), elementContext);
    }

    private void visitGatewayProperties(TGateway element, VisitorContext elementContext) {
        visitFlowNodeProperties(element, elementContext);
    }

    private void visitEventProperties(TEvent element, VisitorContext context) {
        visitFlowNodeProperties(element, context);

        visitBaseElementList(element.getProperty(), context);
    }

    private void visitThrowEventProperties(TThrowEvent element, VisitorContext context) {
        visitEventProperties(element, context);

        visitBaseElementList(element.getDataInput(), context);
        visitBaseElementList(element.getDataInputAssociation(), context);
        visit(element.getInputSet(), context);
        visitJaxbEventDefinitionList(element.getEventDefinition(), context);
    }

    private void visitCatchEventProperties(TCatchEvent element, VisitorContext context) {
        visitEventProperties(element, context);

        visitBaseElementList(element.getDataOutput(), context);
        visitBaseElementList(element.getDataOutputAssociation(), context);
        visit(element.getOutputSet(), context);
        visitJaxbEventDefinitionList(element.getEventDefinition(), context);
    }

    private void visitRootElementProperties(TRootElement element, VisitorContext context) {
        visitBaseElementProperties(element, context);
    }

    private void visitCollaborationProperties(TCollaboration element, VisitorContext context) {
        visitRootElementProperties(element, context);

        visitBaseElementList(element.getParticipant(), context);
        visitBaseElementList(element.getMessageFlow(), context);
        visitJaxbArtifactList(element.getArtifact(), context);
        visitJaxbConversationNodeList(element.getConversationNode(), context);
        visitBaseElementList(element.getConversationAssociation(), context);
        visitBaseElementList(element.getParticipantAssociation(), context);
        visitBaseElementList(element.getMessageFlowAssociation(), context);
        visitBaseElementList(element.getCorrelationKey(), context);
        visitBaseElementList(element.getConversationLink(), context);
    }

    private void visitChoreographyProperties(TChoreography element, VisitorContext context) {
        visitCollaborationProperties(element, context);

        visitJaxbFlowElementList(element.getFlowElement(), context);
    }

    private void visitCallableElementProperties(TCallableElement element, VisitorContext context) {
        visitRootElementProperties(element, context);

        visit(element.getIoSpecification(), context);
        visitBaseElementList(element.getIoBinding(), context);
    }

    private void visitGlobalTaskProperties(TGlobalTask element, VisitorContext context) {
        visitCallableElementProperties(element, context);

        visitJaxbResourceRoleList(element.getResourceRole(), context);
    }

    private void visitEventDefinitionProperties(TEventDefinition element, VisitorContext context) {
        visitRootElementProperties(element, context);
    }

    private void visitResourceRoleProperties(TResourceRole element, VisitorContext context) {
        visitBaseElementProperties(element, context);

        visitBaseElementList(element.getResourceParameterBinding(), context);
        visit(element.getResourceAssignmentExpression(), context);
    }

    private void visitPerformerProperties(TPerformer element, VisitorContext context) {
        visitResourceRoleProperties(element, context);
    }

    private void humanPerformerProperties(THumanPerformer element, VisitorContext context) {
        visitPerformerProperties(element, context);
    }

    private void visitConversationNodeProperties(TConversationNode element, VisitorContext context) {
        visitBaseElementList(element.getCorrelationKey(), context);
    }

    private void visitLoopCharacteristicsProperties(TLoopCharacteristics element, VisitorContext context) {
        visitBaseElementProperties(element, context);
    }

    private void visitArtifactProperties(TArtifact element, VisitorContext context) {
        visitBaseElementProperties(element, context);
    }

    private void visitDataAssociationProperties(TDataAssociation element, VisitorContext context) {
        visitBaseElementProperties(element, context);
        visit(element.getTransformation(), context);
        visitBaseElementList(element.getAssignment(), context);
    }
}
