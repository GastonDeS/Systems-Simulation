import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv


def ej_1_1(path):
    n_h = [2, 10, 40, 80, 140, 200, 260, 320]
    iterations = 100
    for i in n_h:
        y = []
        e = []
        for j in range(0, iterations):
            data = parse_ssv(f'../{path}/ej{i}-{j}.ssv')
            print(f'N_h = {i}')
            y.append(np.mean(data))
            e.append(np.std(data))
        plt.plot(np.arange(0, iterations), y, e, label=f'N_h = {i}')
    plt.legend()
    plt.show()

