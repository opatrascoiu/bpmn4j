package com.workflow.bpmn.translator;

import java.util.Map;

public class InputParameters {
    private final String nativeRootPackage;

    public InputParameters(Map<String, String> map) {
        this.nativeRootPackage = map.get("nativeRootPackage");
    }

    public String getNativeRootPackage() {
        return nativeRootPackage;
    }
}
