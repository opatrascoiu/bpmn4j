package com.workflow.bpmn.translator;

import com.workflow.bpmn.model.error.BPMNSemanticError;
import org.apache.commons.lang3.StringUtils;

public abstract class NativeFactory {
    protected static final String TAB = "    ";
    public static final String TAB_2 = TAB + TAB;
    public static final String TAB_3 = TAB_2 + TAB;
    protected static final String TAB_4 = TAB_3 + TAB;
    private final InputParameters inputParameters;

    protected NativeFactory(InputParameters inputParameters) {
        this.inputParameters = inputParameters;
    }

    //
    // Names
    //
    public String nativePackageName(String bpmnName) {
        String nativeRootPackage = this.inputParameters.getNativeRootPackage();
        if (StringUtils.isBlank(nativeRootPackage)) {
            return "";
        } else {
            return nativeRootPackage;
        }
    }

    public String nativeClassName(String bpmnNodeName) {
        String name = nativeFriendlyName(bpmnNodeName);
        StringBuilder result = new StringBuilder();
        if (Character.isJavaIdentifierStart(name.charAt(0))) {
            result.append(Character.toUpperCase(name.charAt(0)));
        } else {
            result.append('_');
        }
        boolean skippedPrevious = false;
        for (int i = 1; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch == '_') {
                skippedPrevious = true;
            } else {
                if (skippedPrevious) {
                    ch = Character.toUpperCase(ch);
                }
                result.append(ch);
                skippedPrevious = false;
            }
        }
        return result.toString();
    }

    protected String nativeFriendlyName(String name) {
        if (name == null || name.isBlank()) {
            throw new BPMNSemanticError("Missing name for BPMN element");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                sb.append(ch);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    public String getNativeFileExtension() {
        return ".java";
    }
}
