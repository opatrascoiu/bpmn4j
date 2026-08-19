package com.workflow.bpmn.maven;

import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.translator.InputParameters;
import com.workflow.bpmn.translator.template.TemplateProvider;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Map;

public abstract class AbstractDMNMojo extends AbstractFileTransformerMojo {
    @Parameter(required = false)
    public Map<String, String> inputParameters;

    protected TemplateProvider makeTemplateProvider(String templateProviderClassName, BuildLogger logger) throws Exception {
        Class<?> templateProviderClass = Class.forName(templateProviderClassName);
        try {
            return (TemplateProvider) templateProviderClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Cannot build template provider '%s'", templateProviderClass));
        }
    }

    @Override
    protected InputParameters makeInputParameters() {
        return new InputParameters(this.inputParameters);
    }
}
