package com.workflow.bpmn.model.serialization;

import com.workflow.bpmn.model.log.BuildLogger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public abstract class BPMNSerializer {
    protected static final Map<BPMNVersion, JAXBContext> JAXB_CONTEXTS = new HashMap<>();

    public static boolean isBPMNFile(File file) {
        return file != null && file.isFile() && file.getName().endsWith(BPMNConstants.BPMN_FILE_EXTENSION);
    }

    static {
        try {
            JAXBContext value = JAXBContext.newInstance(BPMNVersion.BPMN_20.getJavaPackage());
            JAXB_CONTEXTS.put(BPMNVersion.BPMN_20, value);
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Cannot create JAXB Context", e);
        }
    }

    protected final BuildLogger logger;

    protected BPMNSerializer(BuildLogger logger) {
        this.logger = logger;
    }

    protected JAXBContext getJAXBContext(BPMNVersion bpmnVersion) {
        JAXBContext context = JAXB_CONTEXTS.get(bpmnVersion);
        if (context == null) {
            throw new IllegalArgumentException(String.format("Cannot find context for '%s'", bpmnVersion.getVersion()));
        }
        return context;
    }

    protected Schema getSchema(BPMNVersion BPMNVersion) throws URISyntaxException, SAXException, MalformedURLException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URI schemaURI = getClass().getClassLoader().getResource(BPMNVersion.getSchemaLocation()).toURI();
        Schema schema = sf.newSchema(schemaURI.toURL());
        return schema;
    }
}
