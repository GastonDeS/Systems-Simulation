from _csv import reader


def parse(path):
    data = []
    with open(path, 'r') as read_obj:
        csv_reader = reader(read_obj)
        for row in csv_reader:
            data.append(row[0])
    return data
# perform file operations
