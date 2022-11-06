import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv


def ej_1_1(path):
    f = plt.figure()
    f.set_size_inches(10.5, 5.5)

    # n_h = {2, 10, 40, 80, 140, 200, 260, 320, 380};
    n_h = [2, 10, 40, 80, 140, 200, 260]
    vdz = np.arange(0, 5, 0.5)
    t = np.arange(0, 300, 0.0125*5)
    for i in n_h:
        y = []
        e = []
        accum = []
        for n in range(10):
            data = parse_ssv(f'{path}{i}-{n}.txt')
            if data.split(' ').length > 1:
                accum.append(float(data.split(' ')[1]))
        y.append(np.mean(accum))
        e.append(np.std(accum))
        plt.errorbar(t[:len(y)], y, yerr=e , label=f'Nh={i}', marker='o', markersize=2, linewidth=0.2)
    plt.legend()
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Fracción de zombies')
    plt.show()

def human_qty_vs_time():
    f = plt.figure()
    f.set_size_inches(10.5, 5.5)

    # n_h = {2, 10, 40, 80, 140, 200, 260, 320, 380};
    n_h = [2, 10, 40, 80, 140, 200, 260]
    vdz = np.arange(0, 5, 0.5)
    for i in n_h:
        y = 0
        e = 0
        accum = []
        for n in range(10):
            data = parse_ssv(f'../ratios/ratioNh{i}-{n}.txt')
            if data.split(' ').length > 1:
                accum.append(float(data.split(' ')[1]))
        y.append(np.mean(accum))
        e.append(np.std(accum))
        plt.errorbar(i, y, yerr=e, label=f'Nh={i}', marker='o', markersize=2, linewidth=0.2)
    plt.legend()
    plt.xlabel('Tiempo hasta contagio total (s)')
    plt.ylabel('Cantidad de humanos')
    plt.show()
