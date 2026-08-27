set PYTHONPATH=%CD%\src
python.exe -m pyperf system tune
python src/bpmn4j/test_2_scripts/benchmark.py -o benchmark.json
python -m pyperf stats benchmark.json