package com.workflow.bpmn.runtime.impl.events.start;

import com.workflow.bpmn.runtime.api.events.start.IStartEvent;
import com.workflow.bpmn.runtime.impl.events.Event;

public class StartEvent extends Event implements IStartEvent {
    public StartEvent(String name) {
        super(name);
    }
}
