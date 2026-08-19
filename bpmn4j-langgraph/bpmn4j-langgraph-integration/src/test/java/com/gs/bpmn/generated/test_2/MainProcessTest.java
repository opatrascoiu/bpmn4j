package com.gs.bpmn.generated.test_2;

import org.bsc.langgraph4j.GraphStateException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MainProcessTest {
    private final MainProcess process = new MainProcess();

    @Test
    void testPath() throws GraphStateException {
        Map<String, Object> initialState = Map.of("path", 1);
        assertThrows(RuntimeException.class, () -> {
            process.execute(initialState);
        });
    }
}