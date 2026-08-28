from timeit import Timer

from bpmn4j.test_2_scripts.MainProcess import MainProcess

import timeit

from bpmn4j.test_2_scripts.gateway import start, t1, gate11, gate12, t21, t3, end


def execute_workflow():
    # Execute
    mainProcess = MainProcess("spaceflight-news", "user", "session")
    context = {
        "path": 1
    }
    mainProcess.execute(context)


def execute_nodes():
    context = {
        "state": {
            "path": 1
        }
    }
    start(context)
    t1(context)
    gate11(context)
    t21(context)
    gate12(context)
    t3(context)
    end(context)


def print_nanos(t: Timer, message: str):
    num = int(t.timeit(1) * 1e9)
    print(f"{message} {num:,} ns")


def check_workflow():
    t1 = timeit.Timer(lambda: execute_workflow())
    print_nanos(t1, "Entire workflow")


def check_nodes():
    t2 = timeit.Timer(lambda:  execute_nodes())
    print_nanos(t2, "Just nodes")


# Check entire workflow
check_workflow()
# Check only nodes
check_nodes()
