package com.workflow.bpmn.runtime.impl.events.end;

import com.workflow.bpmn.runtime.api.events.start.IStartEvent;
import com.workflow.bpmn.runtime.impl.events.Event;

public class EndEvent extends Event implements IStartEvent {
    public EndEvent(String name) {
        super(name);
    }
}
