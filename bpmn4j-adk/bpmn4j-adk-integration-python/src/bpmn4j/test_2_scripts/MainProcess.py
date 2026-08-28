# Define constants
from typing import Dict, List

from google.adk import Event
from google.genai import types

from bpmn4j.test_2_scripts.gateway import root_agent
from bpmn4j.utils.logger import get_logger
from bpmn4j.utils.runner import create_runner, create_session


class MainProcess:
    LOGGER = get_logger(__name__)

    def __init__(self, appName: str, userId: str, sessionId: str):
        self.appName = appName
        self.userId = userId
        self.sessionId = sessionId

    #
    # Execute
    #
    def execute(self, context: Dict) -> List[Event]:
        try:
            # Define the agent runner
            runner = create_runner(root_agent, self.appName)

            # Create a session for our agent
            session = create_session(runner, self.appName, self.userId)
            self.LOGGER.debug(f"Session created: App='{self.appName}', User='{self.userId}', Session='{self.sessionId}'")

            result = runner.run(user_id=self.userId, session_id=session.id, state_delta=context,
                             new_message=types.Content(role="user", parts=[types.Part(text="hello")]))
            return [event for event in result]
        except Exception as e:
            message = f"execute({context})"
            self.LOGGER.error(message, e)
            return False
