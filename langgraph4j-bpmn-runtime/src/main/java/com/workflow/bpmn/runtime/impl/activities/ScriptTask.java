package com.workflow.bpmn.runtime.impl.activities;

import com.workflow.bpmn.runtime.api.IExecutionContext;
import com.workflow.bpmn.runtime.api.activities.IScriptTask;
import com.workflow.bpmn.runtime.impl.GroovyRunner;
import com.workflow.bpmn.runtime.impl.log.RuntimeLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ScriptTask extends Activity implements IScriptTask {
    private String script;
    private String resultVariable = "_conclusion";

    public ScriptTask(String name) {
        super(name);
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getResultVariable() {
        return resultVariable;
    }

    public void setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input, IExecutionContext context) {
        Objects.requireNonNull(context, "Missing context");

        RuntimeLogger logger = context.getLogger();
        logger.log("Executing ScriptTask '%s' ...".formatted(this.getName()));
        logger.logEvent(input);

        Object result = executeGroovyScript(script, input, context);
        Map<String, Object> output = new LinkedHashMap<>(input);
        output.put(resultVariable, result);

        logger.log("ScriptTask '%s' executed".formatted(this.getName()));

        return output;
    }

    private Object executeGroovyScript(String script, Map<String, Object> input, IExecutionContext context) {
        try {
            GroovyRunner groovyRunner = new GroovyRunner();
            return groovyRunner.execute(script, input);
        } catch (Exception e) {
            context.getLogger().log("Error executing script: " + e.getMessage());
            throw new RuntimeException("Error executing script", e);
        }
    }
}
