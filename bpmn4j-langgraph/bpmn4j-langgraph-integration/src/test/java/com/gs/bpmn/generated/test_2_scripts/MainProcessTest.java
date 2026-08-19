package com.gs.bpmn.generated.test_2_scripts;

import org.bsc.langgraph4j.GraphStateException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainProcessTest {
    private final MainProcess process = new MainProcess();

    @Test
    void testPath1() throws GraphStateException {
        Map<String, Object> initialState = Map.of("path", 1);
        Map<String, Object> finalState = process.execute(initialState);

        assertEquals(4, finalState.size());
        assertEquals(1, finalState.get("path"));
        assertEquals("branch 1", finalState.get("branch"));
        assertEquals(21, finalState.get("result"));
        assertEquals(22, finalState.get("finalResult"));
    }

    @Test
    void testPath2() throws GraphStateException {
        Map<String, Object> initialState = Map.of("path", 2);
        Map<String, Object> finalState = process.execute(initialState);

        assertEquals(4, finalState.size());
        assertEquals(2, finalState.get("path"));
        assertEquals("branch 2", finalState.get("branch"));
        assertEquals(22, finalState.get("result"));
        assertEquals(23, finalState.get("finalResult"));
    }

    @Test
    void testPath3() throws GraphStateException {
        Map<String, Object> initialState = Map.of("path", 3);
        Map<String, Object> finalState = process.execute(initialState);

        assertEquals(4, finalState.size());
        assertEquals(3, finalState.get("path"));
        assertEquals("branch 3", finalState.get("branch"));
        assertEquals(23, finalState.get("result"));
        assertEquals(24, finalState.get("finalResult"));
    }

    @Test
    void testPath5() throws GraphStateException {
        Map<String, Object> initialState = Map.of("path", 5);
        Map<String, Object> finalState = process.execute(initialState);

        assertEquals(4, finalState.size());
        assertEquals(5, finalState.get("path"));
        assertEquals("branch 1", finalState.get("branch"));
        assertEquals(21, finalState.get("result"));
        assertEquals(22, finalState.get("finalResult"));
    }
}