DOTS_QTY = int(open('../static.txt', 'r').readline().rsplit('=')[1].strip())

def parse_neighbors(path):
    neighbors = [[]] * DOTS_QTY
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
    particles = [[]] * DOTS_QTY
    with open(path, 'r') as f:
        while i < DOTS_QTY:
            line = f.readline()
            if not line:
                break
            positions = line.rsplit()[0].split(',')
            particles.insert(i, [eval(i) for i in positions])
            i += 1
    return particles
