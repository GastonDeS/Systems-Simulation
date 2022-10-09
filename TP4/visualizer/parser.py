import json


def parse_config(filename):
    with open(filename) as f:
        return json.load(f)


def parse_ssv(filename):
    with open(filename) as f:
        return f.read().split('\n')


def parse_xyz(path):
    with open(path, 'r') as f:
        # Skip xyz headers
        for i in range(2):
            next(f)
        token_list = [line.rstrip() for line in f]
    return token_list
