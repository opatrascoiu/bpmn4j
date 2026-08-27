package com.gs.bpmn.generated.test_3_loop;

import org.bsc.langgraph4j.GraphStateException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainProcessTest {
    private final MainProcess process = new MainProcess();

    @Test
    void testLoop() throws GraphStateException {
        Map<String, Object> initialState = Map.of();
        Map<String, Object> finalState = process.execute(initialState);

        assertEquals(1, finalState.size());
        assertEquals(6, finalState.get("counter"));
    }
}