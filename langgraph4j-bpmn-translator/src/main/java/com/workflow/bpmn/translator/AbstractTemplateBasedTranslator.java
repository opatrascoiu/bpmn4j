package com.workflow.bpmn.translator;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.model.error.ErrorHandler;
import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.model.log.Slf4jBuildLogger;
import com.workflow.bpmn.model.serialization.BPMNReader;
import com.workflow.bpmn.translator.template.TemplateProvider;
import freemarker.core._TemplateModelException;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TProcess;
import org.omg.spec.bpmn._20100524.model.TSubProcess;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractTemplateBasedTranslator implements IBPMNTranslator {
    protected static final BuildLogger LOGGER = new Slf4jBuildLogger(LoggerFactory.getLogger(AbstractTemplateBasedTranslator.class));

    private static final Version VERSION = new Version("2.3.23");

    private final BPMNReader reader = new BPMNReader(LOGGER, false);
    protected final ErrorHandler errorHandler = ErrorHandler.instance();
    protected final TemplateProvider templateProvider;

    protected AbstractTemplateBasedTranslator(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    protected TDefinitions readBPMN(File inputFile) {
        return this.reader.read(inputFile);
    }

    protected BPMNModelRepository readModels(File inputFile) {
        TDefinitions bpmnModel = readBPMN(inputFile);
        BPMNModelRepository repository = new BPMNModelRepository(bpmnModel);
        List<TDefinitions> models = repository.getModels();
        if (models.isEmpty()) {
            throw this.errorHandler.makeTranslationError("Repository is empty");
        }
        if (models.size() > 1) {
            throw this.errorHandler.makeTranslationError("Too many models");
        }
        return repository;
    }

    protected void processTemplate(String baseTemplatePath, String templateName, Map<String, Object> params, File outputFile) {
        try {
            Configuration cfg = makeConfiguration(baseTemplatePath);
            Template template = cfg.getTemplate("/" + templateName);

            try (Writer fileWriter = new FileWriter(outputFile)) {
                template.process(params, fileWriter);
            }
        } catch (IOException | TemplateException e) {
            handleError(String.format("Cannot process template '%s'", templateName), e);
        }
    }

    private Configuration makeConfiguration(String basePackagePath) {
        Configuration cfg = new Configuration(VERSION);

        // Some recommended settings:
        cfg.setIncompatibleImprovements(VERSION);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setNumberFormat("#");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Where do we load the templates from:
        cfg.setClassForTemplateLoading(this.getClass(), basePackagePath);
        return cfg;
    }

    protected File makeOutputFile(Path outputPath, String relativeFilePath, String fileName, String fileExtension) {
        String absoluteFilePath = outputPath.toAbsolutePath().toString();
        if (!StringUtils.isBlank(relativeFilePath)) {
            absoluteFilePath += "/" + relativeFilePath;
        }
        absoluteFilePath += "/" + fileName + fileExtension;
        File outputFile = new File(absoluteFilePath);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    protected String getBaseTemplatePath() {
        return this.templateProvider.baseTemplatePath();
    }

    protected void handleError(String errorMessage, Throwable e) {
        if (e instanceof _TemplateModelException) {
            e = e.getCause();
        }
        LOGGER.error(errorMessage, e);
    }

    protected void collectFlowElements(BPMNModelRepository repository, TProcess process, List<TFlowElement> flowElements) {
        List<TFlowElement> children = repository.getFlowElements(process);
        for (TFlowElement element: children) {
            flowElements.add(element);

            if (element instanceof TSubProcess) {
                collectFlowElements(repository, (TSubProcess) element, flowElements);
            }
        }
    }

    private void collectFlowElements(BPMNModelRepository repository, TSubProcess process, List<TFlowElement> flowElements) {
        List<TFlowElement> children = repository.getFlowElements(process);
        flowElements.addAll(children);
    }
}
