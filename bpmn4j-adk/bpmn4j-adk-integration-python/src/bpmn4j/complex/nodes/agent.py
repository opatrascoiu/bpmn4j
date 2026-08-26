from google.adk import Agent

from bpmn4j.utils.runner import create_runner, create_session, query_agent, GEMINI_FLASH_ID

# Define constants
APP_NAME = "spaceflight-news"
USER_ID = "user"
SESSION_ID = "session"

greeting_agent = Agent(
    name="greeting_agent",
    model=GEMINI_FLASH_ID,
    instruction="You are a helpful assistant. Greet the user warmly.",
)

# Define the agent runner
runner = create_runner(greeting_agent, APP_NAME)

# Create a session for our agent
session = create_session(runner, APP_NAME, USER_ID)
print(f"Session created: App='{APP_NAME}', User='{USER_ID}', Session='{SESSION_ID}'")

# Define sample prompts to use when the script is run
query_agent("Hello!", runner, session.id, USER_ID)
query_agent("What is the spaceflight news for today?", runner, session.id, USER_ID)
query_agent("What is the spaceflight news as of Jan 2022?", runner, session.id, USER_ID)
query_agent("What is the spaceflight news as of Jan 1900?", runner, session.id, USER_ID)
