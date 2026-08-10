package com.workflow.bpmn.runtime.impl.activities;

import com.workflow.bpmn.runtime.impl.FlowNode;

public abstract class Activity extends FlowNode {
    public Activity(String name) {
        super(name);
    }
}
