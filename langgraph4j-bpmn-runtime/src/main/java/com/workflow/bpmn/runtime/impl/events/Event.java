package com.workflow.bpmn.runtime.impl.events;

import com.workflow.bpmn.runtime.impl.FlowNode;

public abstract class Event extends FlowNode {
    public Event(String name) {
        super(name);
    }
}
