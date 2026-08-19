package com.workflow.bpmn.runtime.impl.activities;

import com.workflow.bpmn.runtime.api.activities.ITask;

public class Task extends Activity implements ITask {
    public Task(String name) {
        super(name);
    }
}
