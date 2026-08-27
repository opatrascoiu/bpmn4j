package com.gs.bpmn.jmh;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormat;
import org.openjdk.jmh.results.format.ResultFormatFactory;
import org.openjdk.jmh.results.format.ResultFormatType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;

public class PerformanceUtils {
    public static final int WARMUP_ITERATIONS = 10;
    public static final int MEASUREMENT_ITERATIONS = 20;

    public static void printReport(Collection<RunResult> runResults, String fileName) {
        File outputFile = new File(fileName);
        PrintStream out;
        try {
            out = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        ResultFormat resultFormat = ResultFormatFactory.getInstance(ResultFormatType.TEXT, out);
        resultFormat.writeOut(runResults);
    }
}
