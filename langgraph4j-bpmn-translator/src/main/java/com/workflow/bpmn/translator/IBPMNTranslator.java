package com.workflow.bpmn.translator;

import java.io.File;

public interface IBPMNTranslator {
    void translate(File inputFile, File targetFolder);
}
