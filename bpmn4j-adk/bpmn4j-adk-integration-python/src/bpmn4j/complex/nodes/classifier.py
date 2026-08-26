# nodes/classifier.py
from google.adk import Agent
from bpmn4j.utils.logger import get_logger
from bpmn4j.utils.runner import GEMINI_FLASH_ID

logger = get_logger(__name__)
# We define a specific system prompt that forces structured output
CLASSIFIER_PROMPT = """
You are an intent classification engine.
Do not answer the user's question.
Your only job is to categorize the input into one of three tags:
1. 'bug' - If the user reports a technical error or crash.
2. 'support' - If the user has a general account or service question.
3. 'logistics' - If the user asks about shipping, tracking, or delivery.
Output ONLY the tag name in lowercase.
"""
classifier_agent = Agent(
    name="classifier",
    model=GEMINI_FLASH_ID,
    instruction=CLASSIFIER_PROMPT,
    description="Classifies user intent into bug, support, or logistics."
)
