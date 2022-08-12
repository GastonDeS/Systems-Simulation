import matplotlib.pyplot as plt
from parser import *

MARKER_SIZE = 36
DOTS_QTY = 200
TEXT_COORD_FIX = 0.01
particles = parse_particles('../../positions.csv')
neighbors = parse_neighbors('../../neighbors.csv')
plt.figure(figsize=(9, 8), dpi=80)

exit_loop = False
for idx, elem in enumerate(particles):
    if idx == 20:
        break
    plt.plot(elem[0], elem[1], 'bo', markersize=MARKER_SIZE)
    plt.text(elem[0] - TEXT_COORD_FIX, elem[1] - TEXT_COORD_FIX, str(idx), color="white", fontsize=12)
plt.show()
while not exit_loop:
    inp = input("Chose dot or type exit to quit\n")
    if inp == 'exit':
        exit_loop = True
        break
    inp = int(inp)

    while inp < 0 or inp > DOTS_QTY - 1:
        inp = input("Particle not found, try again: ")
        inp = int(inp)
    plt.clf()
    plt.figure(figsize=(9, 8), dpi=80)
    for idx, elem in enumerate(particles):
        if idx == 20:
            break
        print(inp, idx)
        if idx == inp or idx in neighbors[inp]:
            plt.plot(elem[0], elem[1], 'go', markersize=elem[2]*500)
            plt.text(elem[0] - TEXT_COORD_FIX, elem[1] - TEXT_COORD_FIX, str(idx), color="black", fontsize=12)
        else:
            plt.plot(elem[0], elem[1], 'bo', markersize=elem[2]*500)
            plt.text(elem[0] - TEXT_COORD_FIX, elem[1] - TEXT_COORD_FIX, str(idx), color="white", fontsize=12)
    plt.show()
