package com.workflow.bpmn.runtime.impl.gateway;

import com.workflow.bpmn.runtime.impl.FlowNode;

public abstract class Gateway extends FlowNode {
    public Gateway(String name) {
        super(name);
    }
}
