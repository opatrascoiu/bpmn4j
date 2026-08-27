package com.gs.bpmn.jmh.test_2_scripts;

import com.gs.bpmn.generated.test_2_scripts.MainProcess;
import com.gs.bpmn.jmh.PerformanceUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class MainProcessBenchmarkTest {

    @Benchmark
    @BenchmarkMode(Mode.All)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testCompiler() {
        executeCompiled(System.currentTimeMillis());
    }

    private void executeCompiled(long startTime) {
        testCase001();

        long endTime = System.currentTimeMillis();
//        System.out.println(String.format("Compiled version took %s ms", endTime - startTime));
    }

    private void testCase001() {
        try {
            MainProcess process = new MainProcess();

            Map<String, Object> initialState = Map.of("path", 1);
            process.execute(initialState);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MainProcessBenchmarkTest.class.getSimpleName())
                .warmupIterations(PerformanceUtils.WARMUP_ITERATIONS)
                .measurementIterations(PerformanceUtils.MEASUREMENT_ITERATIONS)
                .forks(1)
                .build();

        System.out.println("Print performance results for langgraph4j");
        Collection<RunResult> runResults = new Runner(opt).run();
        PerformanceUtils.printReport(runResults, "langgraph4j-benchmark.txt");
        System.out.println("Done");
    }
}
