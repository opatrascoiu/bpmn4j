# nodes/router.py
from bpmn4j.utils.logger import get_logger
logger = get_logger(__name__)


def router_function(context):
    """
    This function acts as the decision switch.
    It receives the output from the classifier.
    It must return a specific tag that matches the Edge definitions.
    """
    # The context contains the output from the previous node
    intent = context.get("output", "").strip().lower()
    logger.info(f"Router received intent: {intent}")
    # We map the LLM's text output to our internal routing tags
    # In a real app, you might handle synonyms here
    if "bug" in intent:
        return {"next_tag": "bug", "original_intent": intent}
    elif "support" in intent:
        return {"next_tag": "customer_support", "original_intent": intent}
    elif "logistics" in intent:
        return {"next_tag": "logistics", "original_intent": intent}
    else:
        # Default fallback
        return {"next_tag": "customer_support", "original_intent": intent}
