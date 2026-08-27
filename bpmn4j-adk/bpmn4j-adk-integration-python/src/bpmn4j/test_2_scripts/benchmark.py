import pyperf

from bpmn4j.test_2_scripts.MainProcess import MainProcess


def operation():
    # Execute
    process = MainProcess("spaceflight-news", "user", "session")
    context = {
        "path": 1
    }
    process.execute(context)

if __name__ == "__main__":
    runner = pyperf.Runner(
        warmups=10,
        values=20,
        processes=10,
    )

    runner.bench_func("operation", operation)

