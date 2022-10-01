import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv

ex1_files = ['euler', 'euler-modificado', 'verlet', 'beeman', 'analitica', 'gear-predictor']

font = {
    'weight': 'normal',
    'size': 14}

plt.rc('font', **font)


def plot_exercise_1(delta_t):
    f = plt.figure()
    f.set_size_inches(9.5, 5.5)
    plt.ylim([-1.5, 1.5])
    idx = 4
    data = parse_ssv(f'../data/{ex1_files[idx]}.ssv')
    y = []
    for d in data:
        if d != '':
            y.append(float(d.split(' ')[1]))
    print(y)
    x = np.arange(start=0, step=delta_t, stop=5)
    plt.plot(x, y[:500], label=ex1_files[idx])
    plt.legend()
    plt.ylabel('Tiempo (s)')
    plt.xlabel('Posición (m)')
    plt.show()

    print(x)
