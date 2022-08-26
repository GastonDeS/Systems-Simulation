def parse(path):
    with open(path, 'r') as f:
        # Skip xyz headers
        for i in range(2):
            next(f)
        token_list = [line.rstrip() for line in f]
    return token_list
# perform file operations
