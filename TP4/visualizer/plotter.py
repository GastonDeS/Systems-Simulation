import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv

ex1_files = ['verlet', 'beeman', 'analitica', 'gear-predictor']

font = {
    'weight': 'normal',
    'size': 14}

plt.rc('font', **font)


def plot_exercise_1(delta_t):
    f = plt.figure()
    f.set_size_inches(13.5, 5.5)
    plt.ylim([-1.5, 1.5])

    for idx in range(len(ex1_files)):
        data = parse_ssv(f'../data/{ex1_files[idx]}.ssv')
        y = []
        for d in data:
            if d != '':
                y.append(float(d.split(' ')[1]))
        print(y)
        x = np.arange(start=0, step=delta_t, stop=5)
        if ex1_files[idx] == 'analitica':
            plt.plot(x, y[:500], label='Analítica', linestyle='dashed', linewidth=2)
        else:
            plt.plot(x, y[:500], label=ex1_files[idx], linewidth=0.5)
    plt.legend()
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Posición (m)')
    plt.show()

    print(x)
