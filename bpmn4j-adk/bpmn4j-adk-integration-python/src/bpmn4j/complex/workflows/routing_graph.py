# workflows/routing_graph.py
from google.adk import Workflow

from nodes import router
from nodes.classifier import classifier_agent
from nodes.router import router_function
from nodes.handlers import bug_handler, support_handler, logistics_handler
from bpmn4j.utils.logger import get_logger
from bpmn4j.utils.runner import create_runner, create_session, query_agent

logger = get_logger(__name__)
# We import the nodes we created.
# Note: In ADK 2.0, functions like router_function need to be wrapped
# as a Tool or FunctionNode to be used in a graph.
# For this example, we assume the WorkflowAgent accepts callables.
routing_workflow = Workflow(
    name="intent_routing_workflow",
    description="Routes user queries to the correct department based on intent.",
    nodes=[
        classifier_agent,
        # We wrap the function in a Node wrapper conceptually
        # For the sake of this tutorial's logic based on the transcript:
        router_function,
        bug_handler,
        support_handler,
        logistics_handler
    ],
    edges=[
        # 1. Start -> Classifier
        # 'START' is the reserved keyword for the entry point
        ("START", classifier_agent),
        # 2. Classifier -> Router
        # Unconditional edge. After classification, we always route.
        (classifier_agent, router),
        # 3. Router -> Handlers (Conditional)
        # Format: (Source, Destination, Condition_Tag)
        (router,
            {
                "bug": bug_handler,
                "customer_support": support_handler,
                "logistics": logistics_handler
            }
         )
    ]
)

# Define constants
APP_NAME = "spaceflight-news"
USER_ID = "user"
SESSION_ID = "session"

# Define the agent runner
runner = create_runner(routing_workflow, APP_NAME)

# Create a session for our agent
session = create_session(runner, APP_NAME, USER_ID)
print(f"Session created: App='{APP_NAME}', User='{USER_ID}', Session='{SESSION_ID}'")

# Define sample prompts to use when the script is run
query_agent("Hello!", runner, session.id, USER_ID)
