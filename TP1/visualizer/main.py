import matplotlib.pyplot as plt
from parser import *

LABEL_COORDINATES_FIX = 0.01
DOT_RADIUS_MULTIPLIER = 30

particles = parse_particles('../../positions.csv')
neighbors = parse_neighbors('../../neighbors.csv')
plt.figure(figsize=(9, 8), dpi=80)

exit_loop = False
# Is there a way to make this iterator to reach only to size - 1?
for idx, elem in enumerate(particles):
    if idx == DOTS_QTY:
        break
    plt.plot(elem[0], elem[1], 'o', color='#FFAAAF', markersize=elem[2] * DOT_RADIUS_MULTIPLIER)
    # plt.text(elem[0] - LABEL_COORDINATES_FIX, elem[1] - LABEL_COORDINATES_FIX, str(idx), color="black", fontsize=15)
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
        if idx == DOTS_QTY:
            break
        if idx == inp or idx in neighbors[inp]:
            if idx == inp:
                plt.plot(elem[0], elem[1], 'ro', markersize=elem[2] * DOT_RADIUS_MULTIPLIER)
            else:
                plt.plot(elem[0], elem[1], 'go', markersize=elem[2] * DOT_RADIUS_MULTIPLIER)
            # plt.text(elem[0] - LABEL_COORDINATES_FIX, elem[1] - LABEL_COORDINATES_FIX, str(idx), color="black",
            #          fontsize=15)
        else:
            plt.plot(elem[0], elem[1], 'o', color='#FFAAAF', markersize=elem[2] * DOT_RADIUS_MULTIPLIER)
            # plt.text(elem[0] - LABEL_COORDINATES_FIX, elem[1] - LABEL_COORDINATES_FIX, str(idx), color="black",
            #          fontsize=15)
    plt.show()
