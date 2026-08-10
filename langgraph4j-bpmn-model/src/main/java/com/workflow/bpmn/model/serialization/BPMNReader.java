package com.workflow.bpmn.model.serialization;

import com.workflow.bpmn.model.error.ErrorHandler;
import com.workflow.bpmn.model.log.BuildLogger;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.workflow.bpmn.model.serialization.BPMNVersion.VALUES;

public class BPMNReader extends BPMNSerializer {
    private final ErrorHandler errorHandler = ErrorHandler.instance();

    private final boolean validateSchema;

    public BPMNReader(BuildLogger logger, boolean validateSchema) {
        super(logger);
        this.validateSchema = validateSchema;
    }

    public List<TDefinitions> readModels(List<File> files) {
        List<TDefinitions> models = new ArrayList<>();
        if (files == null) {
            throw this.errorHandler.makeSemanticError("Missing BPMN files");
        } else {
            for (File file : files) {
                if (isBPMNFile(file)) {
                    TDefinitions model = read(file);
                    models.add(model);
                } else {
                    this.logger.warn(String.format("Skipping file '%s", file == null ? null : file.getAbsoluteFile()));
                }
            }
            return models;
        }
    }

    public List<TDefinitions> readModels(File file) {
        List<TDefinitions> models = new ArrayList<>();
        if (file == null) {
            throw this.errorHandler.makeSemanticError("Missing BPMN file");
        } else if (isBPMNFile(file)) {
            TDefinitions model = read(file);
            models.add(model);
            return models;
        } else if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (isBPMNFile(child)) {
                    TDefinitions model = read(child);
                    models.add(model);
                }
            }
            return models;
        } else {
            throw this.errorHandler.makeSemanticError(String.format("Invalid BPMN file %s", file.getAbsoluteFile()));
        }
    }

    public TDefinitions read(File input) {
        try {
            this.logger.info(String.format("Reading BPMN '%s' ...", input.getAbsolutePath()));

            TDefinitions result = transform(readObject(input));

            this.logger.info("BPMN read.");
            return result;
        } catch (Exception e) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot read BPMN from '%s'", input.getAbsolutePath()), e);
        }
    }

    public TDefinitions read(InputStream input) {
        try {
            this.logger.info(String.format("Reading BPMN '%s' ...", input.toString()));

            TDefinitions result = transform(readObject(input));

            this.logger.info("BPMN read.");
            return result;
        } catch (Exception e) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot read BPMN from '%s'", input.toString()), e);
        }
    }

    public TDefinitions read(URL input) {
        try {
            this.logger.info(String.format("Reading BPMN '%s' ...", input.toString()));

            TDefinitions result = transform(readObject(input));

            this.logger.info("BPMN read.");
            return result;
        } catch (Exception e) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot read BPMN from '%s'", input.toString()), e);
        }
    }

    public TDefinitions read(Reader input) {
        try {
            this.logger.info(String.format("Reading BPMN '%s' ...", input.toString()));

            TDefinitions result = transform(readObject(input));

            this.logger.info("BPMN read.");
            return result;
        } catch (Exception e) {
            throw this.errorHandler.makeSemanticError(String.format("Cannot read BPMN from '%s'", input.toString()), e);
        }
    }

    private TDefinitions transform(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof TDefinitions) {
            return (TDefinitions) value;
        } else {
            throw this.errorHandler.makeSemanticError(String.format("'%s' is not supported", value.getClass()));
        }
    }

    public Object readObject(File input) throws Exception {
        DocumentBuilder builder = makeDocumentBuilder();
        Document doc = builder.parse(input);
        return readObject(doc);
    }

    public Object readObject(URL input) throws Exception {
        DocumentBuilder builder = makeDocumentBuilder();
        Document doc = builder.parse(input.openStream());
        return readObject(doc);
    }

    public Object readObject(InputStream input) throws Exception {
        DocumentBuilder builder = makeDocumentBuilder();
        Document doc = builder.parse(input);
        return readObject(doc);
    }

    public Object readObject(Reader input) throws Exception {
        DocumentBuilder builder = makeDocumentBuilder();
        Document doc = builder.parse(new InputSource(input));
        return readObject(doc);
    }

    public Object readObject(Document doc) throws Exception {
        BPMNVersion BPMNVersion = inferBPMNVersion(doc);
        Unmarshaller unmarshaller = makeUnmarshaller(BPMNVersion);
        JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshaller.unmarshal(doc);
        return jaxbElement.getValue();
    }

    private DocumentBuilder makeDocumentBuilder() throws Exception {
        DocumentBuilderFactory dbFactory = XMLUtil.makeDocumentBuilderFactory();
        dbFactory.setNamespaceAware(true);
        return dbFactory.newDocumentBuilder();
    }

    private BPMNVersion inferBPMNVersion(Document doc) {
        BPMNVersion BPMNVersion = null;
        Element definitions = doc.getDocumentElement();
        NamedNodeMap attributes = definitions.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            String nodeValue = item.getNodeValue();
            for (BPMNVersion version : VALUES) {
                if (version.getNamespace().equals(nodeValue)) {
                    BPMNVersion = version;
                    break;
                }
            }
        }
        if (BPMNVersion == null) {
            throw new IllegalArgumentException(String.format("Cannot infer BPMN version for input '%s'", doc.getDocumentURI()));
        }

        return BPMNVersion;
    }

    private Unmarshaller makeUnmarshaller(BPMNVersion BPMNVersion) throws Exception {
        JAXBContext context = getJAXBContext(BPMNVersion);
        Unmarshaller u = context.createUnmarshaller();
        if (this.validateSchema) {
            setSchema(u, BPMNVersion);
        }
        return u;
    }

    private void setSchema(Unmarshaller u, BPMNVersion BPMNVersion) throws Exception {
        Schema schema = getSchema(BPMNVersion);
        u.setSchema(schema);
    }
}
