# utils/runner
import asyncio

from google.adk.agents import BaseAgent
from google.adk.runners import InMemoryRunner, Runner
from google.genai import types

GEMINI_FLASH_ID = "gemini-3.6-flash"


def create_runner(root_agent: BaseAgent, app_name: str):
    return InMemoryRunner(
        agent=root_agent,
        app_name=app_name,
    )


def create_session(runner: Runner, app_name: str, user_agent: str):
    session = asyncio.run(
        runner.session_service.create_session(
            app_name=app_name,
            user_id=user_agent,
        ))
    return session


# Define a convenience function to query the agent
def query_agent(prompt: str, runner: Runner, session_id: str, user_id: str):
    print("** User:", prompt)
    response = runner.run(
        user_id=user_id, session_id=session_id,
        new_message=types.Content(
            role="user",
            parts=[types.Part(text=prompt)]))
    for message in response:
        if message.content:
            if message.content.parts and message.content.parts[0].text:
                print(f'** {message.author}: {message.content.parts[0].text}')
                print()
        else:
            print(f'** {message.author}: {message.node_name}')
