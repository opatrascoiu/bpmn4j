
import json
from typing import Any, Dict

from google.adk import Workflow, Event, Runner, Context
from google.genai import types


def start(context: Context) -> Context:
    return context


def t1(context: Context) -> Context:
    path = get_state(context, "path")
    result = ""
    if path == 1:
        result = "branch1"
    elif path == 2:
        result = "branch2"
    elif path == 3:
        result = "branch3"
    set_state(context, "branch", result)
    return context


def gate11(context: Context) -> Context:
    branch = get_state(context, "branch")
    if branch == "branch1":
        return Event(route="branch1")
    elif branch == "branch2":
        return Event(route="branch2")
    elif branch == "branch3":
        return Event(route="branch3")
    return Event(route="branch1")


def t21(context: Context) -> Context:
    result = 21
    set_state(context, "result", result)
    return context


def t22(context: Context) -> Context:
    result = 22
    set_state(context, "result", result)
    return context


def t23(context: Context) -> Context:
    result = 23
    set_state(context, "result", result)
    return context


def gate12(context: Context) -> Context:
    return context


def t3(context: Context) -> Context:
    result = get_state(context, "result")
    set_state(context, "finalResult", result + 1)
    return context


def end(context: Context) -> str:
    return context


root_agent = Workflow(
    name="test_2_scripts",
    edges=[
        ("START", start),
        (start, t1),
        (t1, gate11),

        # gateway
        (
            gate11,
            {
                "branch1": t21,
                "branch2": t22,
                "branch3": t23,
            },
        ),
        (t21, gate12),
        (t22, gate12),
        (t23, gate12),

        (gate12, t3),
        (t3, end)
    ]
)


def get_state(context: Any, name: str):
    if isinstance(context, Context):
        return context.state.get(name)
    elif isinstance(context, Dict):
        return context.get("state").get(name)
    else:
        return None


def set_state(context: Any, name: str, value: Any):
    if isinstance(context, Context):
        context.state[name] = value
    elif isinstance(context, Dict):
        context.get("state")[name] = value


# Define a convenience function to query the agent
def query_agent(prompt: dict, runner: Runner, session_id: str, user_id: str):
    print("** User:", prompt)
    response = runner.run(
        user_id=user_id, session_id=session_id,
        new_message=types.Content(
            role="user",
            parts=[types.Part(text=json.dumps(prompt))]))
    for message in response:
        if message.content:
            if message.content.parts and message.content.parts[0].text:
                print(f'** {message.author}: {message.content.parts[0].text}')
                print()
        else:
            print(f'** {message.author}: {message.node_name}')

# # Define constants
# APP_NAME = "spaceflight-news"
# USER_ID = "user"
# SESSION_ID = "session"
#
# # Define the agent runner
# runner = create_runner(root_agent, APP_NAME)
#
# # Create a session for our agent
# session = create_session(runner, APP_NAME, USER_ID)
# print(f"Session created: App='{APP_NAME}', User='{USER_ID}', Session='{SESSION_ID}'")
#
# # Define sample prompts to use when the script is run
# prompt = {
#     "path": 2
# }
#
# content = types.Content(
#     role="user",
#     parts=[
#         types.Part(
#             text=json.dumps(prompt)
#         )
#     ]
# )
#
# for event in runner.run(
#     user_id=USER_ID,
#     session_id=session.id,
#     state_delta=prompt,
#     new_message=types.Content(
#             role="user",
#             parts=[types.Part(text="hello")])
# ):
#     if isinstance(event.output, Context):
#         print(f'** {event.author}: {event.node_name}: state:{event.output.session.state}')
#     else:
#         print(f'** {event.author}: {event.node_name}')
#     # elif isinstance(event.output, dict) and isinstance(event.output.session.state, dict):
#     #     print(f'** {event.author}: {event.node_name}: state:{event.output.session.state}')
#     # else:
#     #     print(f'** {event.author}: {event.node_name}: state:{event.output.session.state}')
#
# #query_agent(prompt, runner, session.id, USER_ID)
