package com.workflow.bpmn.model.serialization;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import static com.workflow.bpmn.model.serialization.BPMNVersion.BPMN_20;

public class BPMNNamespacePrefixMapper extends NamespacePrefixMapper {
    private final BPMNVersion version = BPMN_20;

    public BPMNNamespacePrefixMapper() {
    }

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        return this.version.getNamespaceToPrefixMap().getOrDefault(namespaceUri, suggestion);
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return this.version.getNamespaceToPrefixMap().keySet().toArray(new String[]{});
    }
}
