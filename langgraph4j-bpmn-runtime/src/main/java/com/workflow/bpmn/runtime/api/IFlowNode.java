package com.workflow.bpmn.runtime.api;

import java.util.Map;

public interface IFlowNode {
    Map<String, Object> execute(Map<String, Object> input, IExecutionContext context);
}
