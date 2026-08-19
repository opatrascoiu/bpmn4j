package com.workflow.bpmn.runtime.impl;

import groovy.lang.GroovyShell;

import java.util.Map;

public class GroovyRunner {
    public Object execute(String script) throws Exception {
        GroovyShell shell = new GroovyShell();
        return shell.evaluate(script);
    }

    public Object execute(String script, Map<String, Object> bindings) throws Exception {
        GroovyShell shell = new GroovyShell();
        bindings.forEach(shell::setVariable);
        return shell.evaluate(script);
    }
}