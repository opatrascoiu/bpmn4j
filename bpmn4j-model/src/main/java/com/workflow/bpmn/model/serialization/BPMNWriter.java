package com.workflow.bpmn.model.serialization;

import com.workflow.bpmn.model.log.BuildLogger;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.File;

import static com.workflow.bpmn.model.serialization.BPMNVersion.BPMN_20;

public class BPMNWriter extends BPMNSerializer {
    public BPMNWriter(BuildLogger logger) {
        super(logger);
    }

    public void write(TDefinitions model, File output) throws Exception {
        BPMNVersion bpmnVersion = BPMN_20;
        Marshaller marshaller = makeMarshaller(bpmnVersion);
        QName qName = new QName(bpmnVersion.getNamespace(), "definitions");
        JAXBElement<TDefinitions> rootElement = new JAXBElement<>(qName, TDefinitions.class, model);
        marshaller.marshal(rootElement, output);
    }

    private Marshaller makeMarshaller(BPMNVersion BPMNVersion) throws Exception {
        JAXBContext context = getJAXBContext(BPMNVersion);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setSchema(getSchema(BPMN_20));
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new BPMNNamespacePrefixMapper());
        return marshaller;
    }
}

