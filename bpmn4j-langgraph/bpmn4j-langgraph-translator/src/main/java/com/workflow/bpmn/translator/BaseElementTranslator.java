package com.workflow.bpmn.translator;

import com.workflow.bpmn.model.BPMNModelRepository;
import com.workflow.bpmn.model.error.ErrorHandler;

public abstract class BaseElementTranslator implements IElementTranslator {
    protected final ErrorHandler errorHandler = ErrorHandler.instance();
    protected final BPMNModelRepository modelRepository;
    protected final NativeFactory nativeFactory;

    protected BaseElementTranslator(BPMNModelRepository modelRepository, NativeFactory nativeFactory) {
        this.modelRepository = modelRepository;
        this.nativeFactory = nativeFactory;
    }
}
