from google.adk import Agent, Workflow

from bpmn4j.utils.runner import create_runner, create_session, query_agent

# Define agents
generate_fruit_agent = Agent(
    name="generate_fruit_agent",
    instruction="Return the name of a random fruit. Return only the name.",
)

generate_benefit_agent = Agent(
    name="generate_benefit_agent",
    instruction="Tell me a health benefit about the specified fruit.",
)

root_agent = Workflow(
    name="root_agent",
    edges=[("START", generate_fruit_agent, generate_benefit_agent)],
)

# Define constants
APP_NAME = "spaceflight-news"
USER_ID = "user"
SESSION_ID = "session"

# Define the agent runner
runner = create_runner(root_agent, APP_NAME)

# Create a session for our agent
session = create_session(runner, APP_NAME, USER_ID)
print(f"Session created: App='{APP_NAME}', User='{USER_ID}', Session='{SESSION_ID}'")

# Define sample prompts to use when the script is run
query_agent("Hello!", runner, session.id, USER_ID)
