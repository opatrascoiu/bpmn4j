package com.workflow.bpmn.translator.asl.process;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.translator.IElementTranslator;
import com.workflow.bpmn.translator.ITranslatorFactory;
import com.workflow.bpmn.translator.NativeFactory;
import com.workflow.bpmn.translator.asl.process.activity.ScriptTaskTranslator;
import com.workflow.bpmn.translator.asl.process.activity.TaskTranslator;
import com.workflow.bpmn.translator.asl.process.event.EndEventTranslator;
import com.workflow.bpmn.translator.asl.process.event.StartEventTranslator;
import com.workflow.bpmn.translator.asl.process.gateway.ExclusiveGatewayTranslator;

public class TranslatorFactory implements ITranslatorFactory {
    //
    // Processes
    //
    @Override
    public IElementTranslator makeProcessTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new ProcessTranslator(modelRepository, nativeFactory);
    }

    //
    // Activities
    //
    @Override
    public ElementTranslator makeTaskTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new TaskTranslator(modelRepository, nativeFactory);
    }

    @Override
    public ElementTranslator makeScriptTaskTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new ScriptTaskTranslator(modelRepository, nativeFactory);
    }

    //
    // Gateways
    //
    @Override
    public IElementTranslator makeExclusiveGatewayTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new ExclusiveGatewayTranslator(modelRepository, nativeFactory);
    }

    //
    // Events
    //
    @Override
    public ElementTranslator makeStartEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new StartEventTranslator(modelRepository, nativeFactory);
    }

    @Override
    public ElementTranslator makeEndEventTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        return new EndEventTranslator(modelRepository, nativeFactory);
    }
}
