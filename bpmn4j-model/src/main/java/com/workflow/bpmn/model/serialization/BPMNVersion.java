package com.workflow.bpmn.model.serialization;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BPMNVersion {
    protected static final Map<String, String> BPMN_20_OTHER_NAMESPACES = new LinkedHashMap<>();

    static {
        BPMN_20_OTHER_NAMESPACES.put("http://www.omg.org/spec/BPMN/20100524/DI", "bpmndi");
        BPMN_20_OTHER_NAMESPACES.put("http://www.omg.org/spec/DD/20100524/DC", "dc");
        BPMN_20_OTHER_NAMESPACES.put("http://www.omg.org/spec/DD/20100524/DI", "di");
    }

    public static final BPMNVersion BPMN_20 = new BPMNVersion("2.0", "bpmn/2.0/BPMN20.xsd",
            "bpmn", "http://www.omg.org/spec/BPMN/20100524/MODEL",
            BPMN_20_OTHER_NAMESPACES,
            "org.omg.spec.bpmn._20100524.model"
    );

    public static final BPMNVersion LATEST = BPMN_20;

    protected static final List<BPMNVersion> VALUES = List.of(BPMN_20);

    public static BPMNVersion fromVersion(String key) {
        for (BPMNVersion version : VALUES) {
            if (version.version.equals(key)) {
                return version;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot find BPMN version '%s'", key));
    }

    private final String version;
    private final String schemaLocation;
    private final String prefix;
    private final String namespace;
    private final String javaPackage;
    private final LinkedHashMap<String, String> namespaceToPrefixMap;
    private final LinkedHashMap<String, String> prefixToNamespaceMap;

    BPMNVersion(String version, String schemaLocation, String prefix, String namespace, Map<String, String> otherNamespaces, String javaPackage) {
        this.version = version;
        this.schemaLocation = schemaLocation;
        this.prefix = prefix;
        this.namespace = namespace;
        this.javaPackage = javaPackage;

        this.namespaceToPrefixMap = new LinkedHashMap<>();
        this.prefixToNamespaceMap = new LinkedHashMap<>();
        addMap(namespace, prefix);
        for (Map.Entry<String, String> entry : otherNamespaces.entrySet()) {
            String otherNamespace = entry.getKey();
            String otherPrefix = entry.getValue();
            addMap(otherNamespace, otherPrefix);
        }
    }

    public String getVersion() {
        return this.version;
    }

    public String getSchemaLocation() {
        return this.schemaLocation;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Map<String, String> getNamespaceToPrefixMap() {
        return this.namespaceToPrefixMap;
    }

    public LinkedHashMap<String, String> getPrefixToNamespaceMap() {
        return this.prefixToNamespaceMap;
    }

    public String getJavaPackage() {
        return this.javaPackage;
    }

    private void addMap(String namespace, String prefix) {
        if (StringUtils.isEmpty(namespace)) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }
        if (prefix == null) {
            prefix = "";
        }
        this.namespaceToPrefixMap.put(namespace, prefix);
        this.prefixToNamespaceMap.put(prefix, namespace);
    }
}
