package com.workflow.bpmn.translator;

import com.workflow.bpmn.model.BPMNModelRepository;
import org.omg.spec.bpmn._20100524.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranslatorRegistry {
    private final Map<Class<?>, IElementTranslator> translatorMap = new LinkedHashMap<>();

    public TranslatorRegistry(ITranslatorFactory translatorFactory, BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        // Processes
        translatorMap.put(TProcess.class, translatorFactory.makeProcessTranslator(modelRepository, nativeFactory));

        // Activities
        translatorMap.put(TTask.class, translatorFactory.makeTaskTranslator(modelRepository, nativeFactory));
        translatorMap.put(TScriptTask.class, translatorFactory.makeScriptTaskTranslator(modelRepository, nativeFactory));

        // Gateways
        translatorMap.put(TExclusiveGateway.class, translatorFactory.makeExclusiveGatewayTranslator(modelRepository, nativeFactory));

        // Events
        translatorMap.put(TStartEvent.class, translatorFactory.makeStartEventTranslator(modelRepository, nativeFactory));
        translatorMap.put(TEndEvent.class, translatorFactory.makeEndEventTranslator(modelRepository, nativeFactory));
    }

    public IElementTranslator getTranslator(TBaseElement element) {
        IElementTranslator elementTranslator = translatorMap.get(element.getClass());
        if (elementTranslator == null) {
            throw new IllegalArgumentException("No translator registered for element type: %s".formatted(element.getClass().getSimpleName()));
        }
        return elementTranslator;
    }
}
