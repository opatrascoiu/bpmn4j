from bpmn4j.test_2_scripts.MainProcess import MainProcess

import time

# Start time
start = time.time_ns()

# Execute
mainProcess = MainProcess("spaceflight-news", "user", "session")
context = {
    "path": 1
}
mainProcess.execute(context)

# End time
end = time.time_ns()
num = end - start
print(f"{num:,}", "ns")
