import matplotlib.pyplot as plt

MARKER_SIZE = 18
DOTS_QTY = 4
TEXT_COORD_FIX = 0.035
particles = [[1, 1], [1.4, 1.6], [1.2, 4], [3.1, 2.2]]

print("Hello")

exit_loop = False
for idx, elem in enumerate(particles):
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
    for idx, elem in enumerate(particles):
        print(elem, idx)
        if idx == inp:
            plt.plot(elem[0], elem[1], 'go', markersize=MARKER_SIZE)
            plt.text(elem[0] - TEXT_COORD_FIX, elem[1] - TEXT_COORD_FIX, str(idx), color="black", fontsize=12)
        else:
            plt.plot(elem[0], elem[1], 'bo', markersize=MARKER_SIZE)
            plt.text(elem[0] - TEXT_COORD_FIX, elem[1] - TEXT_COORD_FIX, str(idx), color="white", fontsize=12)
    plt.show()


