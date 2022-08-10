def parse_neighbors(path):
    particles = [[]] * 100
    with open(path, 'r') as f:
        while True:
            line = f.readline()
            if not line:
                break
            data = line.rsplit()[0].split(',')
            particles.insert(int(data[0]), [eval(i) for i in data[1:]])
    return particles
