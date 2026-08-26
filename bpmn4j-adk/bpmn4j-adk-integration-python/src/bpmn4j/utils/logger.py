# utils/logger.py
import logging
import os


def get_logger(name):
    logger = logging.getLogger(name)
    level = os.getenv("LOG_LEVEL", "INFO")
    logger.setLevel(getattr(logging, level))
    # Create console handler
    ch = logging.StreamHandler()
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    ch.setFormatter(formatter)
    logger.addHandler(ch)
    return logger
