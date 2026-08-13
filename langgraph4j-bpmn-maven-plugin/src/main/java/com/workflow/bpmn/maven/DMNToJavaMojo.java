package com.workflow.bpmn.maven;

import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.translator.IBPMNTranslator;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@SuppressWarnings("CanBeFinal")
@Mojo(name = "bpmn-to-java", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DMNToJavaMojo extends AbstractDMNToNativeMojo {
    @Parameter(required = true, defaultValue = "com.workflow.bpmn.translator.template.LGTemplateProvider")
    public String templateProvider;

    @Override
    protected IBPMNTranslator makeTranslator(BuildLogger logger) throws Exception {
        return super.makeTranslator(logger, this.templateProvider);
    }
}
