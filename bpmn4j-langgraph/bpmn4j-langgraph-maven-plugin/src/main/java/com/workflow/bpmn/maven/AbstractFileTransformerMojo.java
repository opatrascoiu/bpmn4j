package com.workflow.bpmn.maven;

import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.translator.IBPMNTranslator;
import com.workflow.bpmn.translator.InputParameters;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class AbstractFileTransformerMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    public MavenProject project;

    protected void translate(File inputFileDirectory, File outputFileDirectory) throws MojoExecutionException {
        checkMandatoryFields();

        try {
            // Create translator
            IBPMNTranslator translator = makeTranslator(new MavenBuildLogger(this.getLog()));

            // Translate
            this.getLog().info(String.format("Translating '%s' to '%s' ...", inputFileDirectory, outputFileDirectory));
            Files.createDirectories(outputFileDirectory.toPath());
            translator.translate(inputFileDirectory, outputFileDirectory);

            // Add sources
            addSourceRoot(outputFileDirectory);
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }

    protected void checkMandatoryField(Object fieldValue, String fieldName) {
        if (fieldValue == null) {
            throw new IllegalArgumentException(String.format("'%s' is mandatory.", fieldName));
        }
    }

    protected abstract void addSourceRoot(File outputFileDirectory) throws IOException;

    protected abstract IBPMNTranslator makeTranslator(BuildLogger logger) throws Exception;
    protected abstract InputParameters makeInputParameters();

    protected abstract void checkMandatoryFields();
}
