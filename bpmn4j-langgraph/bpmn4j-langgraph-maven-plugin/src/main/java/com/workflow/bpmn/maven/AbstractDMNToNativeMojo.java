package com.workflow.bpmn.maven;

import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.translator.IBPMNTranslator;
import com.workflow.bpmn.translator.InputParameters;
import com.workflow.bpmn.translator.asl.BPMNToLGTranslator;
import com.workflow.bpmn.translator.asl.LGFactory;
import com.workflow.bpmn.translator.template.TemplateProvider;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("CanBeFinal")
public abstract class AbstractDMNToNativeMojo extends AbstractDMNMojo {
    @Parameter(required = true, defaultValue = "${project.basedir}/src/main/resources/bpmn")
    public File inputFileDirectory;

    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/bpmn")
    public File outputFileDirectory;

    @Override
    public void execute() throws MojoExecutionException {
        translate(this.inputFileDirectory, this.outputFileDirectory);
    }

    @Override
    protected void checkMandatoryFields() {
        checkMandatoryField(this.project, "project");
        checkMandatoryField(this.inputFileDirectory, "inputFileDirectory");
        checkMandatoryField(this.outputFileDirectory, "outputFileDirectory");
    }

    protected IBPMNTranslator makeTranslator(BuildLogger logger, String templateProviderName) throws Exception {
        // Create and validate arguments
        TemplateProvider templateProvider = makeTemplateProvider(templateProviderName, logger);
        InputParameters inputParameters = makeInputParameters();

        // Create transformer
        LGFactory factory = new LGFactory(inputParameters);
        return new BPMNToLGTranslator(factory);
    }

    @Override
    protected void addSourceRoot(File outputFileDirectory) throws IOException {
        this.project.addCompileSourceRoot(outputFileDirectory.getCanonicalPath());
    }
}
