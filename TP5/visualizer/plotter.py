import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv

fig = plt.gcf()
fig.set_size_inches(9.5, 5.5)
plt.rc('font', size=12)

def ej_1_1(path):
    f = plt.figure()
    f.set_size_inches(10.5, 6.5)

    # n_h = {2, 10, 40, 80, 140, 200, 260, 320, 380};
    n_h = [2, 10, 40, 80, 140, 200, 260, 320, 380]
    vdz = np.arange(1, 5.5, 0.5)
    t = np.arange(0, 300, 0.0125*5)
    for i in vdz:
        y = []
        e = []
        tf = []
        for j in range(len(t)):
            accum = []
            if j % 100 == 0:
                for n in range(10):
                    data = parse_ssv(f'{path}{i}_{n}.txt')
                    if len(data) > j and len(data[j].split(' ')) > 1:
                        accum.append(float(data[j].split(' ')[1]))
                if len(accum) > 0:
                    tf.append(t[j])
                    y.append(np.mean(accum))
                    e.append(np.std(accum))
        plt.errorbar(tf, y, yerr=e, label=f'Vdz={i}', marker='o', capsize=3, markersize=4.3, linewidth=1)
    plt.legend()
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Fracción de zombies')
    plt.show()


def human_qty_vs_time():
    f = plt.figure()
    f.set_size_inches(11.5, 5.5)

    # n_h = {2, 10, 40, 80, 140, 200, 260, 320, 380};
    n_h = np.arange(30, 270, 30)
    vdz = np.arange(1, 5.5, 0.5)
    for i in n_h:
        accum = []
        y = 0
        e = 0
        data = parse_ssv(f'../times/timeNh{i}.txt')
        for j in range(len(data)):
            if data[j] != '':
                accum.append(float(data[j]))
        if len(accum) > 0:
            y = np.mean(accum)
            e = np.std(accum)
        plt.errorbar(i, y, yerr=e, label=f'Vdz={i}', marker='o', markersize=4, linewidth=1, capsize=3)
    plt.ylabel('Tiempo hasta contagio total (s)')
    plt.xlabel('Cantidad de humanos')
    plt.show()
