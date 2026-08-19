package com.workflow.bpmn.translator;

import com.workflow.bpmn.model.BPMNModelRepository;

public interface ITranslatorFactory {
    // Processes
    IElementTranslator makeProcessTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);

    // Activities
    IElementTranslator makeTaskTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);
    IElementTranslator makeScriptTaskTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);

    // Gateways
    IElementTranslator makeExclusiveGatewayTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);

    // Events
    IElementTranslator makeStartEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);
    IElementTranslator makeEndEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory);
}
