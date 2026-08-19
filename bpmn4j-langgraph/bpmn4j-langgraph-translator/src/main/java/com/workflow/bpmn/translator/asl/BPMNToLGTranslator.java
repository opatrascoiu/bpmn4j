package com.workflow.bpmn.translator.asl;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.translator.*;
import com.workflow.bpmn.translator.asl.process.TranslatorFactory;
import com.workflow.bpmn.translator.template.LGTemplateProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TFlowElement;
import org.omg.spec.bpmn._20100524.model.TFlowNode;
import org.omg.spec.bpmn._20100524.model.TProcess;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BPMNToLGTranslator extends AbstractTemplateBasedTranslator implements IBPMNTranslator {
    private TranslatorRegistry translatorRegistry;
    private final NativeFactory nativeFactory;

    public BPMNToLGTranslator(NativeFactory nativeFactory) {
        super(new LGTemplateProvider());
        this.nativeFactory = nativeFactory;
    }

    @Override
    public void translate(File inputFile, File targetFolder) {
        // Read models
        TDefinitions bpmnModel = readBPMN(inputFile);
        BPMNModelRepository repository = new BPMNModelRepository(bpmnModel);
        List<TDefinitions> models = repository.getModels();
        if (models.isEmpty()) {
            throw this.errorHandler.makeTranslationError("Repository is empty");
        }
        if (models.size() > 1) {
            throw this.errorHandler.makeTranslationError("Too many models");
        }
        translatorRegistry = new TranslatorRegistry(new TranslatorFactory(), repository, nativeFactory);

        TDefinitions model = models.get(0);
        // Add missing model name
        if (StringUtils.isBlank(model.getName())) {
            model.setName(inputFile.getName().replace(".bpmn", ""));
        }
        String modelName = repository.displayName(model);

        // Find root processes
        List<TProcess> rootProcesses = repository.findParentProcesses();
        if (rootProcesses.isEmpty()) {
            throw this.errorHandler.makeTranslationError(model, "No process to translate");
        } else if (rootProcesses.size() != 1) {
            throw this.errorHandler.makeTranslationError(model, "Expected only one process");
        }

        // Extract flow elements
        List<TFlowElement> flowElements = new ArrayList<>();
        TProcess process = rootProcesses.get(0);
        collectFlowElements(repository, process, flowElements);
        flowElements.sort(Comparator.comparing(repository::displayName));

        // generate one native class for every flow node (Activity, Gateway, Event)
        for (TFlowElement flowElement : flowElements) {
            generateFlowElement(modelName, flowElement, repository, targetFolder);
        }

        // generate the main process
        generateMainProcess(modelName, process, repository, targetFolder);
    }

    private void generateFlowElement(String modelName, TFlowElement flowElement, BPMNModelRepository repository, File targetFolder) {
        if (flowElement instanceof TFlowNode flowNode) {
            IElementTranslator elementTranslator = translatorRegistry.getTranslator(flowNode);
            String nativeClassName = elementTranslator.nativeClassName(flowElement);
            String nativePackageName = elementTranslator.nativePackageName(flowElement);
            Map<String, Object> inputParameters = Map.of(
                    "modelName", modelName,
                    "nativePackageName", nativePackageName,
                    "element", flowElement,
                    "nativeClassName", nativeClassName,
                    "translator", elementTranslator
            );
            String relativePath = nativePackageName.replace('.', '/');
            File outputFile = makeOutputFile(targetFolder.toPath(), relativePath, nativeClassName, elementTranslator.getNativeFileExtension());
            processTemplate(getBaseTemplatePath(), this.templateProvider.getElementTemplate(flowElement), inputParameters, outputFile);
        }
    }

    private void generateMainProcess(String modelName, TProcess process, BPMNModelRepository repository, File targetFolder) {
        if (process != null) {
            IElementTranslator elementTranslator = translatorRegistry.getTranslator(process);
            String nativeClassName = elementTranslator.nativeClassName(process);
            String nativePackageName = elementTranslator.nativePackageName(process);
            Map<String, Object> inputParameters = Map.of(
                    "modelName", modelName,
                    "nativePackageName", nativePackageName,
                    "element", process,
                    "nativeClassName", nativeClassName,
                    "translator", elementTranslator
            );
            String relativePath = nativePackageName.replace('.', '/');
            File outputFile = makeOutputFile(targetFolder.toPath(), relativePath, nativeClassName, elementTranslator.getNativeFileExtension());
            processTemplate(getBaseTemplatePath(), this.templateProvider.getElementTemplate(process), inputParameters, outputFile);
        }
    }

    protected static InputParameters makeInputParameters() {
        return new InputParameters(Map.of(
                "nativeRootPackage", "com.wokflow.agentic"
        ));
    }

    public static void main(String[] args) {
        List<Pair<String, String>> models = List.of(
                Pair.of("test-1.bpmn", "test-1"),
                Pair.of("test-2-scripts.bpmn", "test-2-scripts")
        );

        File rootFolder = Paths.get("bpmn-test-cases/bpmn/").toFile();
        for (Pair<String, String> model : models) {
            String bpmnFileName = model.getLeft();
            File inputFile = new File(rootFolder, bpmnFileName);
            File targetFolder = new File("langgraph4j-bpmn-translator/target/generated-sources/bpmn/" + model.getRight());
            InputParameters inputParameters = makeInputParameters();

            BPMNToLGTranslator translator = new BPMNToLGTranslator(new LGFactory(makeInputParameters()));
            translator.translate(inputFile, targetFolder);
        }
    }
}
