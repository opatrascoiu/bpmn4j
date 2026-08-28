# utils/logger.py
from pathlib import Path
import logging
import logging.config

PROJECT_ROOT = Path(__file__).resolve().parents[3]


def get_logger(name):
    logging.config.fileConfig(PROJECT_ROOT / 'logging.conf')
    logger = logging.getLogger(name)
    return logger
