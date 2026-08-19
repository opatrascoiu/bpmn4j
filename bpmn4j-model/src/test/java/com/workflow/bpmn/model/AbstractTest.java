package com.workflow.bpmn.model;

import com.workflow.bpmn.model.error.BPMNTestException;
import com.workflow.bpmn.model.log.BuildLogger;
import com.workflow.bpmn.model.log.Slf4jBuildLogger;
import com.workflow.bpmn.model.serialization.BPMNReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static com.workflow.bpmn.model.serialization.BPMNSerializer.isBPMNFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public abstract class AbstractTest {
    protected static final BuildLogger LOGGER = new Slf4jBuildLogger(LoggerFactory.getLogger(AbstractTest.class));

    private final BPMNReader reader = new BPMNReader(LOGGER, false);

    protected URI resource(String path) {
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            if (url == null) {
                throw new BPMNTestException(String.format("Cannot find resource '%s'", path));
            }
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new BPMNTestException(e);
        }
    }

    protected String completePath(String pathFormat, String dmnFileName) {
        return String.format(pathFormat, dmnFileName);
    }

    protected String getInputPath() {
        return "bpmn/input/%s";
    }

    protected TDefinitions readBPMN(String inputPath) {
        File inputFile = new File(resource(inputPath));
        return readBPMN(inputFile);
    }

    protected TDefinitions readBPMN(File inputFile) {
        return this.reader.read(inputFile);
    }

    protected <T> Map<String, T> makeMap(Pair<String, T>... pairs) {
        Map<String, T> map = new LinkedHashMap<>();
        for (Pair<String, T> pair: pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    protected List<Map.Entry<String, Integer>> normalizeMap(Map<String, Integer> map) {
        if (map == null) {
            return null;
        }

        List<Map.Entry<String, Integer>> entries = new ArrayList(map.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        return entries;
    }

    protected void compareFile(File expectedOutputFile, File actualOutputFile) throws Exception {
        if (expectedOutputFile.isDirectory() && actualOutputFile.isDirectory()) {
            List<File> expectedChildren = Arrays.asList(expectedOutputFile.listFiles());
            List<File> actualChildren = Arrays.asList(actualOutputFile.listFiles());
            compareFileList(expectedOutputFile, expectedChildren, actualOutputFile, actualChildren);
        } else if (expectedOutputFile.isFile() && actualOutputFile.isFile()) {
            if (isJsonFile(expectedOutputFile) && isJsonFile(actualOutputFile)) {
                compareJsonFile(expectedOutputFile, actualOutputFile);
            } else if (isBPMNFile(expectedOutputFile) && isBPMNFile(actualOutputFile)) {
                compareXmlFile(expectedOutputFile, actualOutputFile);
            } else if (expectedOutputFile.getName().equals(actualOutputFile.getName())) {
                String expectedTypeContent = FileUtils.readFileToString(expectedOutputFile, "UTF-8").replace("    \r", "\r").replace("\r", "");
                String actualTypeContent = FileUtils.readFileToString(actualOutputFile, "UTF-8").replace("    \r", "\r").replace("\r", "");
                assertEquals(String.format("%s vs %s", expectedOutputFile.getCanonicalPath(), actualOutputFile.getCanonicalPath()), expectedTypeContent, actualTypeContent);
            } else {
                throw new RuntimeException(String.format("Files with different names '%s' '%s' ", expectedOutputFile.getCanonicalPath(), actualOutputFile.getCanonicalPath()));
            }
        } else {
            throw new RuntimeException(String.format("Cannot compare folder with file '%s' '%s' ", expectedOutputFile.getCanonicalPath(), actualOutputFile.getCanonicalPath()));
        }
    }

    private void compareJsonFile(File expectedOutputFile, File actualOutputFile) throws Exception {
        String expectedContent = FileUtils.readFileToString(expectedOutputFile, "UTF-8");
        String actualContent = FileUtils.readFileToString(actualOutputFile, "UTF-8");
        JSONAssert.assertEquals(expectedContent, actualContent, JSONCompareMode.STRICT);
    }

    private void compareXmlFile(File expectedOutputFile, File actualOutputFile) {
        Diff diff = DiffBuilder
                .compare(Input.fromFile(expectedOutputFile)).withTest(Input.fromFile(actualOutputFile))
                .checkForSimilar()
                .ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes))
                .build();
        if (diff.hasDifferences()) {
            for (Difference d: diff.getDifferences()) {
                LOGGER.error(d.toString());
            }
        }
        assertFalse(diff.hasDifferences(), String.format("%s vs %s", expectedOutputFile.getPath(), actualOutputFile.getPath()));
    }

    private boolean isJsonFile(File file) {
        return file.getName().endsWith(".json");
    }

    private void compareFileList(File expectedParent, List<File> expectedChildren, File actualParent, List<File> actualChildren) throws Exception {
        String message = String.format("Different number of children when comparing '%s' with '%s'", expectedParent.getCanonicalPath(), actualParent.getCanonicalPath());
        assertEquals(expectedChildren.size(), actualChildren.size(), message);
        expectedChildren.sort(new FileComparator());
        actualChildren.sort(new FileComparator());
        for (int i = 0; i < expectedChildren.size(); i++) {
            compareFile(expectedChildren.get(i), actualChildren.get(i));
        }
    }
}
