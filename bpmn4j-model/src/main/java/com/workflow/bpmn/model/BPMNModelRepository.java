package com.workflow.bpmn.model;

import com.workflow.bpmn.model.error.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.omg.spec.bpmn._20100524.model.*;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BPMNModelRepository {
    private final List<TDefinitions> models = new ArrayList<>();
    private final ErrorHandler errorHandler = ErrorHandler.instance();

    // Process caches
    private List<TProcess> allRootProcesses = new LinkedList<>();
    private Map<TProcess, TDefinitions> rootProcessToModelMap = new LinkedHashMap<>();

    // Flow Element caches
    private final Map<String, TFlowElement> idToFlowElementMap = new LinkedHashMap<>();
    private final Map<TFlowElement, TProcess> flowElementToProcessMap = new LinkedHashMap<>();
    private final Map<TFlowElement, TFlowNode> flowElementToSubProcessMap = new LinkedHashMap<>();

    // Message caches
    private final Map<String, TMessage> idToMessageMap = new LinkedHashMap<>();

    // BoundaryEvent caches
    private final Set<TBoundaryEvent> allBoundaryEvents = new LinkedHashSet<>();
    private final Map<TActivity, List<TBoundaryEvent>> activityToDirectBoundaryEventsMap = new LinkedHashMap<>();

    public BPMNModelRepository(List<TDefinitions> models) {
        if (models != null) {
            this.models.addAll(models);
        }
        populateCaches();
    }

    public BPMNModelRepository(TDefinitions model) {
        if (model != null) {
            this.models.add(model);
        }
        populateCaches();
    }

    private void populateCaches() {
        populateProcessCaches();
        populateIdToFlowElementMap();
        populateFlowElementToProcessMap();
        populateFlowElementToSubProcessMap();
        populateIdToMessageMap();
        populateBoundaryEventsCaches();
    }

    private void populateProcessCaches() {
        for (TDefinitions model: this.models) {
            List<JAXBElement<? extends TRootElement>> jaxbRootElements = model.getRootElement();
            if (jaxbRootElements != null) {
                List<TRootElement> rootElements = jaxbRootElements.stream().map(JAXBElement::getValue).collect(Collectors.toList());
                for (TRootElement rootElement: rootElements) {
                    if (rootElement instanceof TProcess) {
                        TProcess process = (TProcess) rootElement;
                        this.allRootProcesses.add(process);
                        this.rootProcessToModelMap.put(process, model);
                    }
                }
            }
        }
    }

    private void populateIdToFlowElementMap() {
        List<TProcess> parentProcesses = this.allRootProcesses;
        for (TProcess process: parentProcesses) {
            List<TFlowElement> processFlowElements = getAllFlowElements(process);
            for (TFlowElement element: processFlowElements) {
                // Add entry to index
                String key = element.getId();
                if (this.idToFlowElementMap.get(key) == null) {
                    this.idToFlowElementMap.put(key, element);
                } else {
                    throw this.errorHandler.makeSemanticError(findModel(element), element, String.format("Duplicated FlowElement '%s'", key));
                }
            }
        }
    }

    private void populateFlowElementToProcessMap() {
        List<TProcess> parentProcesses = this.allRootProcesses;
        for (TProcess process: parentProcesses) {
            List<TFlowElement> processFlowElements = getFlowElements(process);
            for (TFlowElement element: processFlowElements) {
                // Add entry to cache
                if (this.flowElementToProcessMap.get(element) == null) {
                    this.flowElementToProcessMap.put(element, process);
                } else {
                    throw this.errorHandler.makeSemanticError(findModel(element), element, String.format("Duplicated FlowElement '%s'", displayName(element)));
                }
            }
        }
    }

    private void populateFlowElementToSubProcessMap() {
        List<TProcess> parentProcesses = this.allRootProcesses;
        for (TProcess process: parentProcesses) {
            List<TFlowElement> processFlowElements = getFlowElements(process);
            for (TFlowElement element: processFlowElements) {
                if (element instanceof TSubProcess) {
                    List<TFlowElement> flowElements = getFlowElements((TSubProcess) element);
                    for (TFlowElement child: flowElements) {
                        // Add entry to cache
                        if (this.flowElementToSubProcessMap.get(child) == null) {
                            this.flowElementToSubProcessMap.put(child, (TFlowNode) element);
                        } else {
                            throw this.errorHandler.makeSemanticError(findModel(child), child, String.format("Duplicated FlowElement '%s'", displayName(child)));
                        }
                    }
                }
            }
        }
    }

    private void populateIdToMessageMap() {
        for (TDefinitions model: this.models) {
            List<JAXBElement<? extends TRootElement>> rootElements = model.getRootElement();
            for (JAXBElement<? extends TRootElement> rootElement: rootElements) {
                if (rootElement != null) {
                    TRootElement value = rootElement.getValue();
                    if (value instanceof TMessage) {
                        this.idToMessageMap.put(value.getId(), (TMessage) value);
                    }
                }
            }
        }
    }

    private void populateBoundaryEventsCaches() {
        List<TProcess> parentProcesses = findParentProcesses();
        for (TProcess process: parentProcesses) {
            for (JAXBElement<? extends TFlowElement> jaxbElement: process.getFlowElement()) {
                TFlowElement value = jaxbElement.getValue();
                // Add boundary event
                if (value instanceof TBoundaryEvent) {
                    QName activityRef = ((TBoundaryEvent) value).getAttachedToRef();
                    TFlowElement element = this.idToFlowElementMap.get(activityRef.getLocalPart());
                    if (element instanceof TActivity) {
                        List<TBoundaryEvent> boundaryEvents = this.activityToDirectBoundaryEventsMap.get(element);
                        if (boundaryEvents == null) {
                            boundaryEvents = new ArrayList<>();
                            this.activityToDirectBoundaryEventsMap.put((TActivity) element, boundaryEvents);
                        }
                        boundaryEvents.add((TBoundaryEvent) value);
                        this.allBoundaryEvents.add((TBoundaryEvent) value);
                    } else {
                        throw this.errorHandler.makeSemanticError(findModel(element), element, String.format("Expected TActivity, found '%s'", element == null ? null : element.getClass().getSimpleName()));
                    }
                }
            }
        }
    }

    public List<TDefinitions> getModels() {
        return this.models;
    }

    public TDefinitions findModel(TBaseElement element) {
        if (this.models.size() == 1) {
            return this.models.get(0);
        }

        TDefinitions model = null;
        if (element instanceof TFlowElement) {
            TProcess process = this.flowElementToProcessMap.get(element);
            if (process == null) {
                TFlowNode subProcess = this.flowElementToSubProcessMap.get(element);
                process = this.flowElementToProcessMap.get(subProcess);
            }
            if (process == null) {
                throw this.errorHandler.makeSemanticError(String.format("Cannot find parent process for element '%s'", displayName(element)));
            } else {
                model = this.rootProcessToModelMap.get(process);
            }
        } else if (element instanceof TProcess) {
            model = this.rootProcessToModelMap.get(element);
        }
        if (model == null) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot find model for process '%s'", displayName(element)));
        }
        return model;
    }

    public List<TProcess> findParentProcesses() {
        return this.allRootProcesses;
    }

    public List<TFlowElement> getFlowElements(TProcess process) {
        List<TFlowElement> flowElements = new ArrayList<>();
        if (process == null) {
            return flowElements;
        }
        collectChildFlowElements(process.getFlowElement(), flowElements);
        return flowElements;
    }

    public List<TFlowElement> getFlowElements(TSubProcess process) {
        List<TFlowElement> flowElements = new ArrayList<>();
        if (process == null) {
            return flowElements;
        }
        collectChildFlowElements(process.getFlowElement(), flowElements);
        return flowElements;
    }

    public List<TFlowElement> getAllFlowElements(TProcess process) {
        List<TFlowElement> allFlowElements = new ArrayList<>();
        if (process == null) {
            return allFlowElements;
        }

        List<TFlowElement> processFlowElements = getFlowElements(process);
        allFlowElements.addAll(processFlowElements);
        for (TFlowElement flowElement: processFlowElements) {
            if (flowElement instanceof TSubProcess) {
                List<TFlowElement> subProcessFlowElements = getAllFlowElements((TSubProcess) flowElement);
                allFlowElements.addAll(subProcessFlowElements);
            }
        }
        return allFlowElements;
    }

    public List<TFlowElement> getAllFlowElements(TSubProcess process) {
        List<TFlowElement> allFlowElements = new ArrayList<>();
        if (process == null) {
            return allFlowElements;
        }

        List<TFlowElement> processFlowElements = getFlowElements(process);
        allFlowElements.addAll(processFlowElements);
        for (TFlowElement flowElement: processFlowElements) {
            if (flowElement instanceof TSubProcess) {
                List<TFlowElement> subProcessFlowElements = getAllFlowElements((TSubProcess) flowElement);
                allFlowElements.addAll(subProcessFlowElements);
            }
        }
        return allFlowElements;
    }

    private void collectChildFlowElements(List<JAXBElement<? extends TFlowElement>> jaxbFlowElements, List<TFlowElement> flowElements) {
        if (jaxbFlowElements != null) {
            for (JAXBElement<? extends TFlowElement> jaxb : jaxbFlowElements) {
                TFlowElement value = jaxb.getValue();
                if (value != null) {
                    flowElements.add(value);
                }
            }
        }
    }

    public List<TFlowNode> previousElements(TFlowNode element) {
        if (element instanceof TStartEvent) {
            TFlowNode subProcess = this.flowElementToSubProcessMap.get(element);
            if (subProcess == null) {
                return previousDirectElements(element);
            } else {
                return previousDirectElements(subProcess);
            }
        } else {
            return previousDirectElements(element);
        }
    }

    public TFlowElement subProcessOf(TFlowElement element) {
        return this.flowElementToSubProcessMap.get(element);
    }

    public List<TFlowNode> nextElements(TFlowNode element) {
        List<TFlowNode> result = new ArrayList<>();
        if (element instanceof TSubProcess) {
            List<TFlowElement> flowElements = getFlowElements((TSubProcess) element);
            for (TFlowElement child : flowElements) {
                if (child instanceof TStartEvent) {
                    result.add((TFlowNode) child);
                }
            }
        } else if (element instanceof TEndEvent) {
            TFlowNode subProcess = this.flowElementToSubProcessMap.get(element);
            if (subProcess == null) {
                return nextDirectElements(element);
            } else {
                return nextDirectElements(subProcess);
            }
        } else {
            return nextDirectElements(element);
        }
        return result;
    }

    private List<TFlowNode> previousDirectElements(TFlowNode element) {
        List<TFlowNode> result = new ArrayList<>();
        List<QName> incoming = element.getIncoming();
        for (QName qName: incoming) {
            TFlowElement sequenceFlow = this.idToFlowElementMap.get(qName.getLocalPart());
            TDefinitions model = findModel(element);
            if (sequenceFlow == null) {
                throw this.errorHandler.makeSemanticError(model, element, String.format("Cannot find flow node '%s'", qName.getLocalPart()));
            }
            if (sequenceFlow instanceof TSequenceFlow) {
                Object sourceRef = ((TSequenceFlow) sequenceFlow).getSourceRef();
                if (sourceRef instanceof TFlowNode) {
                    result.add((TFlowNode) sourceRef);
                } else {
                    throw this.errorHandler.makeSemanticError(model, element, String.format("Expected TFlowNode, found '%s'", sourceRef.getClass()));
                }
            }
        }
        return result;
    }

    public List<TFlowNode> nextDirectElements(TFlowNode element) {
        List<TFlowNode> result = new ArrayList<>();
        List<QName> outgoing = element.getOutgoing();
        for (QName qName: outgoing) {
            TFlowElement sequenceFlow = this.idToFlowElementMap.get(qName.getLocalPart());
            TDefinitions model = findModel(element);
            if (sequenceFlow == null) {
                throw this.errorHandler.makeSemanticError(model, element, String.format("Cannot find flow node '%s'", qName.getLocalPart()));
            }
            if (sequenceFlow instanceof TSequenceFlow) {
                Object targetRef = ((TSequenceFlow) sequenceFlow).getTargetRef();
                if (targetRef instanceof TFlowNode) {
                    result.add((TFlowNode) targetRef);
                } else {
                    throw this.errorHandler.makeSemanticError(model, element, String.format("Expected TFlowNode, found '%s'", targetRef.getClass()));
                }
            }
        }
        return result;
    }

    public TFlowElement findSequenceFlow(QName qName) {
        TFlowElement element = this.idToFlowElementMap.get(qName.getLocalPart());
        if (element == null) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot find sequence flow '%s'", qName));
        }
        return element;
    }

    public void collectBoundaryTimerEventsFor(List<TFlowNode> nodes, List<TFlowNode> result) {
        for (TFlowNode node: nodes) {
            result.add(node);
            List<TBoundaryEvent> boundaryEvents = findBoundaryEvents(node);
            for (TBoundaryEvent boundaryEvent: boundaryEvents) {
                if (isCatchTimerEvent(boundaryEvent)) {
                    result.add(boundaryEvent);
                }
            }
        }
    }

    //
    // Activities
    //
    public String getCallableElement(TFlowNode node) {
        if (node instanceof TCallActivity) {
            return ((TCallActivity) node).getCalledElement().getLocalPart();
        } else {
            return null;
        }
    }

    public boolean isFlowNode(TFlowElement element) {
        return element instanceof TFlowNode;
    }

    public boolean isStartMessageEvent(TFlowElement node) {
        return node instanceof TStartEvent && getEventDefinition((TFlowNode) node) instanceof TMessageEventDefinition;
    }

    public boolean isBoundaryMessageEvent(TFlowElement n) {
        return n instanceof TBoundaryEvent && hasMessageEventDefinition((TBoundaryEvent) n);
    }

    public boolean isProcessStartNode(TFlowNode element) {
        // Check type
        if (element instanceof TBoundaryEvent) {
            return false;
        }
        // Check incoming
        if (!isEmpty(element.getIncoming())) {
            return false;
        }
        // Check if included in a SubProcess
        TFlowNode parent = this.flowElementToSubProcessMap.get(element);
        return parent == null;
    }

    public boolean isProcessEndNode(TFlowNode element) {
        // Check type
        // TODO nested levels
        if (element instanceof TSubProcess) {
            return false;
        }
        // Check outgoing
        if (!isEmpty(element.getOutgoing())) {
            return false;
        }
        // Check if included of SubProcess
        TFlowNode parent = this.flowElementToSubProcessMap.get(element);
        if (parent == null) {
            return true;
        } else if (isEmpty(parent.getOutgoing())) {
            return true;
        } else {
            return false;
        }
    }

    public TFlowNode startProcessNode(List<TFlowElement> elements) {
        List<TFlowElement> startNodes = elements.stream().filter(e -> e instanceof TFlowNode && isProcessStartNode((TFlowNode) e)).collect(Collectors.toList());
        if (startNodes.isEmpty()) {
            throw this.errorHandler.makeTranslationError(String.format("Cannot find start node in '%s", elements));
        } else if (startNodes.size() == 1) {
            return (TFlowNode) startNodes.get(0);
        } else {
            throw this.errorHandler.makeTranslationError(String.format("Not supported multiple start nodes for '%s", elements));
        }
    }

    private boolean isEmpty(List<QName> incoming) {
        return incoming == null || incoming.isEmpty();
    }

    public boolean containsParallelGateways(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::isParallelGateway);
    }

    public boolean isParallelGateway(TFlowElement element) {
        return element instanceof TParallelGateway;
    }

    //
    // Message Events
    //
    public boolean containsMessages(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::hasMessage);
    }

    public boolean containsBoundaryMessages(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::isBoundaryMessageEvent);
    }

    public boolean hasMessage(TFlowElement element) {
        return hasCatchMessage(element) || hasThrowMessage(element);
    }

    public boolean hasCatchMessage(TFlowElement element) {
        if (element instanceof TCatchEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TCatchEvent) element);
            return eventDefinition instanceof TMessageEventDefinition;
        } else if (element instanceof TReceiveTask) {
            TMessage message = findMessage((TReceiveTask) element);
            return message != null;
        }
        return false;
    }

    public boolean hasIntermediateCatchMessage(TFlowElement element) {
        if (element instanceof TIntermediateCatchEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TCatchEvent) element);
            return eventDefinition instanceof TMessageEventDefinition;
        } else if (element instanceof TReceiveTask) {
            TMessage message = findMessage((TReceiveTask) element);
            return message != null;
        }
        return false;
    }

    public boolean hasBoundaryMessages(TFlowElement element) {
        List<TBoundaryEvent> boundaryEvents = findBoundaryEvents(element);
        return boundaryEvents != null && !boundaryEvents.isEmpty();
    }

    public boolean hasThrowMessage(TFlowElement element) {
        if (element instanceof TThrowEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TThrowEvent) element);
            return eventDefinition instanceof TMessageEventDefinition;
        } else if (element instanceof TSendTask) {
            TMessage message = findMessage((TSendTask) element);
            return message != null;
        }
        return false;
    }

    private boolean hasMessageEventDefinition(TBoundaryEvent element) {
        TEventDefinition eventDefinition = getEventDefinition(element);
        return eventDefinition instanceof TMessageEventDefinition;
    }

    public List<String> findBoundaryMessages() {
        List<String> names = new ArrayList<>();
        for (TBoundaryEvent be: this.allBoundaryEvents) {
            TEventDefinition eventDefinition = getEventDefinition(be);
            if (eventDefinition instanceof TMessageEventDefinition) {
                String messageName = getMessageName((TMessageEventDefinition) eventDefinition);
                names.add(messageName);
            }
        }
        return names;
    }

    public List<TBoundaryEvent> findBoundaryEvents(TFlowElement activity) {
        List<TBoundaryEvent> boundaryEvents = this.activityToDirectBoundaryEventsMap.get(activity);
        if (boundaryEvents == null) {
            boundaryEvents = new ArrayList<>();
        }
        TFlowNode parentSubProcess = this.flowElementToSubProcessMap.get(activity);
        if (parentSubProcess != null) {
            List<TBoundaryEvent> parentBoundaryEvents = this.activityToDirectBoundaryEventsMap.get(parentSubProcess);
            if (parentBoundaryEvents != null) {
                boundaryEvents.addAll(parentBoundaryEvents);
            }
        }
        return boundaryEvents;
    }

    public String getCatchMessageName(TFlowElement element) {
        String messageName = null;
        if (element instanceof TStartEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TStartEvent) element);
            if (eventDefinition instanceof TMessageEventDefinition) {
                messageName = getMessageName((TMessageEventDefinition) eventDefinition);
            }
        } else if (element instanceof TIntermediateCatchEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TIntermediateCatchEvent) element);
            if (eventDefinition instanceof TMessageEventDefinition) {
                messageName = getMessageName((TMessageEventDefinition) eventDefinition);
            }
        } else if (element instanceof TReceiveTask) {
            messageName = getMessageName((TReceiveTask) element);
        }
        return messageName;
    }

    public String getThrowMessageName(TFlowElement element) {
        String messageName = null;
        if (element instanceof TThrowEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TThrowEvent) element);
            if (eventDefinition instanceof TMessageEventDefinition) {
                messageName = getMessageName((TMessageEventDefinition) eventDefinition);
            }
        } else if (element instanceof TSendTask) {
            messageName = getMessageName((TSendTask) element);
        }
        return messageName;
    }

    public String getMessageName(TMessageEventDefinition eventDefinition) {
        QName messageRef = eventDefinition.getMessageRef();
        TMessage message = findMessage(messageRef);
        if (message == null) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot find message for '%s'", messageRef));
        }
        return message.getName();
    }

    public String getMessageName(TReceiveTask task) {
        QName messageRef = task.getMessageRef();
        TMessage message = findMessage(messageRef);
        if (message == null) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot find message for '%s'", messageRef));
        }
        return message.getName();
    }

    public String getMessageName(TSendTask task) {
        QName messageRef = task.getMessageRef();
        TMessage message = findMessage(messageRef);
        if (message == null) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot find message for '%s'", messageRef));
        }
        return message.getName();
    }

    public TMessage findMessage(TMessageEventDefinition eventDefinition) {
        QName messageRef = eventDefinition.getMessageRef();
        return findMessage(messageRef);
    }

    public TMessage findMessage(TReceiveTask task) {
        QName messageRef = task.getMessageRef();
        return findMessage(messageRef);
    }

    public TMessage findMessage(TSendTask task) {
        QName messageRef = task.getMessageRef();
        return findMessage(messageRef);
    }

    private TMessage findMessage(QName messageRef) {
        return this.idToMessageMap.get(messageRef.getLocalPart());
    }

    public String getCorrelationKey(TMessage message) {
        return extractMandatoryBPMNExtensionProperty(message, IBPMNExtensions.CORRELATION_KEY);
    }

    //
    // Timer Events
    //
    public boolean containsBoundaryTimerEvent(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::isBoundaryTimerEvent);
    }

    public boolean isBoundaryTimerEvent(TFlowElement n) {
        return n instanceof TBoundaryEvent && hasTimerEventDefinition((TBoundaryEvent) n);
    }

    public boolean hasTimerEventDefinition(TBoundaryEvent element) {
        TEventDefinition eventDefinition = getEventDefinition(element);
        return eventDefinition instanceof TTimerEventDefinition;
    }

    public boolean isStartTimerEvent(TFlowElement element) {
        return element instanceof TStartEvent
                && getEventDefinition((TEvent) element) instanceof TTimerEventDefinition;
    }

    public boolean isCatchTimerEvent(TFlowElement element) {
        return isStartTimerEvent(element)
                || element instanceof TBoundaryEvent && getEventDefinition((TEvent) element) instanceof TTimerEventDefinition
                || element instanceof TIntermediateCatchEvent && getEventDefinition((TEvent) element) instanceof TTimerEventDefinition
                ;
    }

    public String getTimerExpression(TFlowElement element) {
        String expressionText = null;
        if (element instanceof TStartEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TStartEvent) element);
            if (eventDefinition instanceof TTimerEventDefinition) {
                expressionText = getTimerExpression(element, ((TTimerEventDefinition) eventDefinition));
            }
        } else if (element instanceof TIntermediateCatchEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TIntermediateCatchEvent) element);
            if (eventDefinition instanceof TTimerEventDefinition) {
                expressionText = getTimerExpression(element, ((TTimerEventDefinition) eventDefinition));
            }
        } else if (element instanceof TBoundaryEvent) {
            TEventDefinition eventDefinition = getEventDefinition((TBoundaryEvent) element);
            if (eventDefinition instanceof TTimerEventDefinition) {
                expressionText = getTimerExpression(element, ((TTimerEventDefinition) eventDefinition));
            }
        }
        return expressionText;
    }

    public String getTimerExpression(TFlowElement element, TTimerEventDefinition definition) {
        TExpression expression = definition.getTimeDate();
        if (expression == null) {
            expression = definition.getTimeDuration();
        }
        if (expression == null) {
            expression = definition.getTimeCycle();
        }
        return getExpressionText(element, expression);
    }

    //
    // Error Events
    //
    public boolean containsErrorEvent(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::hasErrorEvent);
    }

    public boolean hasErrorEvent(TFlowElement n) {
        return n instanceof TEvent && hasErrorEventDefinition((TEvent) n);
    }

    private boolean hasErrorEventDefinition(TEvent element) {
        TEventDefinition eventDefinition = getEventDefinition(element);
        return eventDefinition instanceof TErrorEventDefinition;
    }

    //
    // Escalation Events
    //
    public boolean containsEscalationEvents(List<TFlowElement> nodes) {
        if (nodes == null) {
            return false;
        }
        return nodes.stream().anyMatch(this::hasEscalationEvent);
    }

    private boolean hasEscalationEvent(TFlowElement element) {
        return element instanceof TEvent && getEventDefinition((TEvent) element) instanceof TEscalationEventDefinition;
    }

    public TEventDefinition getEventDefinition(TFlowNode element) {
        TDefinitions model = findModel(element);
        if (element instanceof TCatchEvent) {
            List<JAXBElement<? extends TEventDefinition>> eventDefinition = ((TCatchEvent) element).getEventDefinition();
            if (eventDefinition != null) {
                int size = eventDefinition.size();
                if (size == 0) {
                    return null;
                } else if (size == 1) {
                    return eventDefinition.get(0).getValue();
                } else {
                    throw this.errorHandler.makeSemanticError(model, element, String.format("Expected 1 event definition, found '%d' for event '%s'", size, element.getId()));
                }
            }
            throw this.errorHandler.makeSemanticError(model, element, String.format("Missing event definitions for event '%s'", element.getId()));
        } else if (element instanceof TThrowEvent) {
            List<JAXBElement<? extends TEventDefinition>> eventDefinition = ((TThrowEvent) element).getEventDefinition();
            if (eventDefinition != null) {
                int size = eventDefinition.size();
                if (size == 0) {
                    return null;
                } else if (size == 1) {
                    return eventDefinition.get(0).getValue();
                } else {
                    throw this.errorHandler.makeSemanticError(model, element, String.format("Expected 1 event definition, found '%d' for event '%s'", size, element.getId()));
                }
            }
            throw this.errorHandler.makeSemanticError(model, element, String.format("Missing event definitions for event '%s'", element.getId()));
        } else {
            return null;
        }
    }


    public String extractMandatoryBPMNExtensionProperty(TBaseElement element, String propertyName) {
        String value = extractBPMNExtensionProperty(element, propertyName);
        if (value == null) {
            throw this.errorHandler.makeSemanticError(findModel(element), element, String.format("Cannot find extension property '%s' for element '%s'", propertyName, displayName(element)));
        } else {
            return value;
        }
    }

    public String extractBPMNExtensionProperty(TBaseElement element, String propertyName, String defaultValue) {
        String value = extractBPMNExtensionProperty(element, propertyName);
        return value == null ? defaultValue : value;
    }

    private String extractBPMNExtensionProperty(TBaseElement element, String propertyName) {
        String localName = "properties";
        TExtensionElements extensionElements = element.getExtensionElements();
        if (extensionElements != null) {
            List<Object> extensions = extensionElements.getAny();
            for (Object extension : extensions) {
                if (extension instanceof Element) {
                    if (localName.equals(((Element) extension).getLocalName())) {
                        NodeList propertyNodeList = ((Element) extension).getChildNodes();
                        for (int i=0; i<propertyNodeList.getLength(); i++) {
                            Node propertyNode = propertyNodeList.item(i);
                            NamedNodeMap attributes = propertyNode.getAttributes();
                            if (attributes != null) {
                                Node nameAttribute = attributes.getNamedItem("name");
                                if (propertyName.equals(nameAttribute.getNodeValue())) {
                                    Node valueAttribute = attributes.getNamedItem("value");
                                    return valueAttribute.getNodeValue();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //
    // Expression
    //
    public String getConditionExpression(TFlowElement element, BPMNModelRepository repository) {
        if (element instanceof TSequenceFlow) {
            TExpression conditionExpression = ((TSequenceFlow) element).getConditionExpression();
            return getExpressionText(element, conditionExpression);
        }

        return null;
    }

    private String getExpressionText(TFlowElement element, TExpression conditionExpression) {
        if (conditionExpression == null) {
            return null;
        }

        TDefinitions model = findModel(element);
        List<Serializable> contentList = conditionExpression.getContent();
        if (contentList.size() == 0) {
            return null;
        } else if (contentList.size() == 1) {
            Serializable serializable = contentList.get(0);
            String content = String.format("%s", serializable);
            return StringEscapeUtils.escapeJava(content);
        } else {
            throw this.errorHandler.makeSemanticError(model, element, String.format("Not supported expression for flow '%s'", displayName(element)));
        }
    }

    public String typeName(TBaseElement element) {
        return element == null ? null : element.getClass().getSimpleName();
    }

    public String displayName(TBaseElement element) {
        if (element instanceof TFlowElement) {
            return displayName((TFlowElement) element);
        } else if (element instanceof TProcess) {
            return displayName((TProcess) element);
        } else {
            return element.getId();
        }
    }

    public String displayName(TFlowElement element) {
        if (element == null) {
            return null;
        }

        String displayName = element.getName();
        if (StringUtils.isEmpty(displayName)) {
            displayName = element.getId();
        }
        return displayName;
    }

    public String displayName(TProcess element) {
        if (element == null) {
            return null;
        }

        String displayName = element.getName();
        if (StringUtils.isEmpty(displayName)) {
            displayName = element.getId();
        }
        return displayName;
    }

    public String displayName(TDefinitions element) {
        if (element == null) {
            return null;
        }

        String displayName = element.getName();
        if (StringUtils.isEmpty(displayName)) {
            displayName = element.getId();
        }
        return displayName;
    }

    public String modelName(TBaseElement element) {
        TDefinitions model = findModel(element);
        if (model == null) {
            throw this.errorHandler.makeSemanticError(model, element, String.format("Cannot find model for element '%s'", displayName(element)));
        }
        return displayName(model);
    }

    public String getExtensionAttribute(TBaseElement node, String attributeName) {
        if (node == null || StringUtils.isBlank(attributeName)) {
            return null;
        }

        Map<QName, String> otherAttributes = node.getOtherAttributes();
        if (otherAttributes != null) {
            for (Map.Entry<QName, String> entry : otherAttributes.entrySet()) {
                if (entry.getKey().getLocalPart().equals(attributeName)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}
