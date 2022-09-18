import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse

fig = plt.gcf()
fig.set_size_inches(9.5, 5.5)
plt.rc('font', size=12)
def ej_1():
    fig = plt.gcf()
    fig.set_size_inches(9.5, 5.5)
    for i in [5, 10, 15, 20, 25, 30]:
        data = parse(f'../../../events/events1{i:02d}.csv')
        events_qty = len(data)
        deltas = []
        bin_size = 0.0005
        for idx, d in enumerate(data):
            if idx == 0:
                deltas.append(float(d))
            else:
                deltas.append(float(d) - float(data[idx - 1]))

        max_time = max(deltas)
        intervals = np.arange(0, max_time, bin_size)
        hits = np.zeros(len(intervals))
        for d in deltas:
            print(d)
            print(int(d / bin_size))
            hits[int(d / bin_size)] += 1
        no_match_cells = []
        for j, hit in enumerate(hits):
            if hit == 0:
                no_match_cells.append(j)
        intervals = np.delete(intervals, no_match_cells)
        hits = np.delete(hits, no_match_cells)
        plt.plot(intervals, hits / events_qty, marker="o", label=f"N={100 + i}")
    plt.yscale('log')
    plt.legend(loc='upper right', borderaxespad=0.)
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Distribución de probabilidad de tiempos de colisiones')
    plt.show()


def ej_2():
    bin_size = 0.2
    plt.xlabel('Velocidades (m/s)')
    plt.ylabel('Distribucion de probabilidad de velocidades')
    plt.show()
