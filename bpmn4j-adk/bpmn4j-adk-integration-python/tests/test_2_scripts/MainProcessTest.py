from unittest import TestCase

from bpmn4j.test_2_scripts.MainProcess import MainProcess


class MainProcessTest(TestCase):
    mainProcess = MainProcess("spaceflight-news", "user", "session")

    #
    # Start and check status of MainProcess
    #
    def testNoPath(self):
        context = {
        }
        events = self.mainProcess.execute(context)

        self.assertEqual(7, len(events))
        expectedEvents = ['start', 't1', 'gate11', 't21', 'gate12', 't3', 'end']
        self.assertEqual(expectedEvents, [event.node_name for event in events])

    def testPath1(self):
        context = {
            "path": 1
        }
        events = self.mainProcess.execute(context)

        self.assertEqual(7, len(events))
        expectedEvents = ['start', 't1', 'gate11', 't21', 'gate12', 't3', 'end']
        self.assertEqual(expectedEvents, [event.node_name for event in events])
