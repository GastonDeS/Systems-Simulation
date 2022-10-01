import json


def parse_config(filename):
    with open(filename) as f:
        return json.load(f)

def parse_ssv(filename):
    with open(filename) as f:
        return f.read().split('\n')