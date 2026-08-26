from google.adk import Workflow, Event

from bpmn4j.utils.runner import create_runner, create_session, query_agent


# ============================================================
# Node 1: Classify
# ============================================================

def classify(node_input: str):
    """
    Decide whether the request is simple or complex.
    """

    if len(node_input.split()) > 8:
        return Event(
            output=node_input,
            route="complex",
        )

    return Event(
        output=node_input,
        route="simple",
    )


# ============================================================
# Node 2: Answer simple requests
# ============================================================

def answer(node_input: str) -> str:
    """
    Handle simple requests directly.
    """

    return f"Simple answer: {node_input}"


# ============================================================
# Node 3: Generate complex answer
# ============================================================

def generate(node_input: str) -> dict:
    """
    Generate a draft answer.

    We deliberately use a counter so that the example can
    demonstrate a conditional loop.
    """

    if isinstance(node_input, dict):
        request = node_input["request"]
        attempt = node_input["attempt"] + 1
    else:
        request = node_input
        attempt = 1

    return {
        "request": request,
        "attempt": attempt,
        "answer": (
            f"Draft answer for: {request} "
            f"(generation attempt {attempt})"
        ),
    }


# ============================================================
# Node 4: Validate
# ============================================================

def validate(node_input: dict):
    """
    Validate the generated answer.

    On the first attempt we deliberately request a retry.
    On the second attempt we accept it.

    This creates:

        generate -> validate -> generate

    """

    attempt = node_input["attempt"]

    if attempt < 2:
        return Event(
            output=node_input,
            route="retry",
        )

    return Event(
        output=node_input,
        route="done",
    )


# ============================================================
# Node 5: Finalise
# ============================================================

def finalise(node_input: dict) -> str:
    """
    Produce the final response.
    """

    return (
        f"{node_input['answer']}\n"
        f"Validated after {node_input['attempt']} attempt(s)."
    )


# ============================================================
# GRAPH
# ============================================================

root_agent = Workflow(
    name="five_node_workflow",
    edges=[
        # START -> classify
        ("START", classify),

        # classify -> answer OR generate
        (
            classify,
            {
                "simple": answer,
                "complex": generate,
            },
        ),

        # Complex path:
        #
        # generate -> validate
        (generate, validate),

        # validate -> generate OR finalise
        #
        # "retry" creates the graph cycle:
        #
        # generate -> validate
        #       ^       |
        #       |       |
        #       +--retry+
        #
        (
            validate,
            {
                "retry": generate,
                "done": finalise,
            },
        ),
    ],
)


# ============================================================
# RUNNER
# ============================================================

# async def main():
#
#     app_name = "five_node_workflow"
#     user_id = "user_123"
#     session_id = "session_123"
#
#     session_service = InMemorySessionService()
#
#     # Create the ADK session
#     await session_service.create_session(
#         app_name=app_name,
#         user_id=user_id,
#         session_id=session_id,
#     )
#
#     # The Workflow is the root node of the Runner.
#     runner = Runner(
#         agent=root_agent,
#         app_name=app_name,
#         session_service=session_service,
#     )
#
#     prompt = (
#         "Explain how a graph-based workflow can use "
#         "conditional routing and retry validation."
#     )
#
#     message = types.Content(
#         role="user",
#         parts=[
#             types.Part.from_text(text=prompt)
#         ],
#     )
#
#     print("Starting workflow...\n")
#
#     async for event in runner.run_async(
#         user_id=user_id,
#         session_id=session_id,
#         new_message=message,
#     ):
#
#         print(
#             f"event author={event.author}"
#         )
#
#         if event.content:
#             for part in event.content.parts:
#                 if part.text:
#                     print(part.text)
#
#         print("---")
#
#     print("\nWorkflow finished.")
#
#
# if __name__ == "__main__":
#     asyncio.run(main())

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
prompt = "Explain how a graph-based workflow can use conditional routing and retry validation."
query_agent(prompt, runner, session.id, USER_ID)
