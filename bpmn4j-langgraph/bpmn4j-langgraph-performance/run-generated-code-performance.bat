call mvn clean install -Pjmh
java -cp target/benchmarks.jar com.gs.bpmn.jmh.test_2_scripts.MainProcessBenchmarkTest
