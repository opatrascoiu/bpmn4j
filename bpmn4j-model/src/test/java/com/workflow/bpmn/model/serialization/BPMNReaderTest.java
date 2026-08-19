package com.workflow.bpmn.model.serialization;

import com.workflow.bpmn.model.AbstractTest;
import com.workflow.bpmn.model.log.NopBuildLogger;
import org.junit.jupiter.api.Test;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BPMNReaderTest extends AbstractTest {
    private final BPMNReader reader = new BPMNReader(new NopBuildLogger(), true);

    @Test
    void testRead() throws MalformedURLException {
        URI resource = resource("bpmn/input/test-1.bpmn");
        TDefinitions model = reader.read(resource.toURL());
        assertNotNull(model);
    }
}