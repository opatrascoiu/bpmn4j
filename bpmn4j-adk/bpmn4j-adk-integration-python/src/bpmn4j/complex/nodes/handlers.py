# nodes/handlers.py
from google.adk import Agent

from bpmn4j.utils.runner import GEMINI_FLASH_ID

bug_handler = Agent(
    name="bug_handler",
    model=GEMINI_FLASH_ID,
    instruction="You are a technical support specialist. Acknowledge the bug and create a ticket.",
    description="Handles bug reports."
)
support_handler = Agent(
    name="support_handler",
    model=GEMINI_FLASH_ID,
    instruction="You are a customer support agent. Answer general questions politely.",
    description="Handles general support queries."
)
logistics_handler = Agent(
    name="logistics_handler",
    model=GEMINI_FLASH_ID,
    instruction="You are a logistics coordinator. Provide tracking info or shipping policies.",
    description="Handles shipping and tracking."
)
