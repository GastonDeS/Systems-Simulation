import matplotlib.pyplot as plt
import numpy as np

from visualizer.parser import parse_ssv, parse_xyz

delta_t_list = np.arange(start=0.00001, stop=0.01, step=0.0001, dtype=np.float64)
ex1_files = ['verlet', 'beeman', 'analitica', 'gear-predictor']
ex1_names = ['Verlet', 'Beeman', 'Analitica', 'Gear Predictor']
ex1_b_files = ['verlet', 'beeman', 'gear-predictor']
ex1_b_names = ['Verlet', 'Beeman', 'Gear Predictor']




# font = {
#     'weight': 'normal',
#     'size': 14}
#
# plt.rc('font', **font)


def plot_exercise_1(delta_t):
    f = plt.figure()
    #plt.ylim([0.39, .5])
    real_data = parse_ssv('../data/analitica.ssv')

    for idx in range(len(ex1_files)):
        error = 0
        data = parse_ssv(f'../data/{ex1_files[idx]}.ssv')
        y = []
        for id, d in enumerate(data):
            if d != '':
                value = float(d.split(' ')[1])
                real_value = float(real_data[id].split(' ')[1])
                error += (value - real_value)**2
                y.append(value)
        x = np.arange(start=0, step=delta_t, stop=5)
        if ex1_files[idx] == 'analitica':
            plt.plot(x, y[:500], label='Analítica', linestyle='dashed', linewidth=1.2)
        else:
            print(ex1_files[idx])
            plt.plot(x, y[:500], label=ex1_names[idx], linewidth=0.88)
            print(f'Error: {error / 500}')
    plt.legend()
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Posición (m)')
    plt.show()


def plot_exercise_1b():
    f = plt.figure()
    f.set_size_inches(10.5, 5.5)
    plt.yscale('log')
    for idx, d in enumerate(ex1_b_files):
        ecm = []
        for delta_t in delta_t_list:
            real_data = parse_ssv(f'../data/analitica-{np.round(delta_t*1e5)/1e5}.ssv')
            data = parse_ssv(f'../data/{d}-{np.round(delta_t*1e5)/1e5}.ssv')
            error = 0
            for idd, dd in enumerate(data):
                if dd != '':
                    value = float(dd.split(' ')[1])
                    real_value = float(real_data[idd].split(' ')[1])
                    error += (value - real_value)**2
            ecm.append(error / 500)
        x = np.arange(start=0.00001, step=0.0001, stop=0.01)
        plt.plot(x, ecm, label=ex1_b_names[idx], linewidth=1)
    plt.legend()
    plt.xlabel('dt (s)')
    plt.ylabel('Error cuadrático medio (m²)')
    plt.show()


def plot_earth():
    iter = np.arange(start=0, stop=109440, step=72)
    x = []
    y = []
    for i in iter:
        data = parse_xyz(f'../position/positions{i}.xyz')
        x.append(get_pos(data[1])[0])
        y.append(get_pos(data[1])[1])
    plt.plot(x, y)
    plt.show()

def get_pos(data):
    return data.split(' ')[0:2]