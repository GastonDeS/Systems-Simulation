def parse_neighbors(path):
    neighbors = [[]] * 20
    with open(path, 'r') as f:
        while True:
            line = f.readline()
            if not line:
                break
            data = line.rsplit()[0].split(',')
            neighbors.insert(int(data[0]), [eval(i) for i in data[1:]])
    return neighbors


def parse_particles(path):
    i = 0
    particles = [[]] * 20
    with open(path, 'r') as f:
        while i < 20:
            line = f.readline()
            if not line:
                break
            positions = line.rsplit()[0].split(',')
            particles.insert(i, [eval(i) for i in positions])
            i += 1
    return particles
