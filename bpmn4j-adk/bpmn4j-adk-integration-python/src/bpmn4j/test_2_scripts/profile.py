from line_profiler import profile

from bpmn4j.test_2_scripts.MainProcess import MainProcess


@profile
def mainProcess():
    # Execute
    process = MainProcess("spaceflight-news", "user", "session")
    context = {
        "path": 1
    }
    process.execute(context)

if __name__ == "__main__":
    for _ in range(100):
        mainProcess()