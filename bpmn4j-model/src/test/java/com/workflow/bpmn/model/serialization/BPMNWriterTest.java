package com.workflow.bpmn.model.serialization;

import com.workflow.bpmn.model.AbstractTest;
import com.workflow.bpmn.model.log.NopBuildLogger;
import org.junit.jupiter.api.Test;
import org.omg.spec.bpmn._20100524.model.TDefinitions;

import java.io.File;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BPMNWriterTest extends AbstractTest {
    private final BPMNWriter writer = new BPMNWriter(new NopBuildLogger());

    @Test
    void testWrite() throws Exception {
        // Read model
        URI resource = resource("bpmn/input/test-1.bpmn");
        File inputFile = new File(resource);
        TDefinitions inputModel = readBPMN(inputFile);

        // Write model
        File outputFile = new File("target/test-1-output.bpmn");
        writer.write(inputModel, outputFile);

        // Compare files TODO compare content of files - write an XMLUnit evaluator
        TDefinitions outputModel = readBPMN(outputFile);
        assertEquals(inputModel.getName(), outputModel.getName());
    }
}