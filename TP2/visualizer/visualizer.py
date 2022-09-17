import math
import matplotlib.pyplot as plt
from parser import parse
import numpy as np

SAME_ETA_ITERATIONS = 25

def get_va(path, iterations, particles_qty):
    va = []
    for i in range(iterations):
        token_list = parse(f'{path}{i}.xyz')
        accum_x = 0
        accum_y = 0
        for token in token_list:
            theta = token.split(' ')[2]
            accum_x += math.cos(float(theta)) * 0.03
            accum_y += math.sin(float(theta)) * 0.03
        va.append(math.sqrt(accum_x ** 2 + accum_y ** 2) / (particles_qty * 0.03))
    return va

def get_va_bulk(path, iterations, particles_qty, density):
    va = []
#     for j in range(SAME_ETA_ITERATIONS):
    for i in range(iterations):
        accum_x = 0
        accum_y = 0
        token_list = parse(f'{path}0_{i}_{density}.0.xyz')
        for token in token_list:
            theta = token.split(' ')[2]
            accum_x += math.cos(float(theta)) * 0.5
            accum_y += math.sin(float(theta)) * 0.5
        va.append(math.sqrt(accum_x ** 2 + accum_y ** 2) / (particles_qty * 0.5))
    return va


def main():

    # polarizacion vs eta
    va_list = list(range(6))
    va_mean = list(range(6))
    va_std = list(range(6))

    agentsAmount = [0.1, 1.0, 2.0, 3.0, 4.0, 5.0]

    for i in range(6):
        va_list[i] = get_va_bulk(f'../position/positions_eta:{agentsAmount[i]}_', 1500, 1024, 1024)
    x = list(range(0, 1500))

    fig = plt.figure()
    fig.set_size_inches(9.5, 6.5)

    for i in range(6):
        plt.plot(x, va_list[i], linewidth=1, label=f'η = {agentsAmount[i]}')
    plt.ylabel('Polarización', fontsize=16)
    plt.xlabel('Iteraciones', fontsize=16, labelpad=12)

    plt.legend(loc='upper right', bbox_to_anchor=(1.12, 1), fontsize=12)
    plt.show()

    # polarizacion vs eta
#     va_list = list(range(6))
#     va_mean = list(range(6))
#     va_std = list(range(6))
#
#     etas = [0.0, 1.0, 2.0,3.0,4.0,5.0]
#
#     for i in range(6):
#         va_list[i] = get_va_bulk(f'../position/positions_eta:{etas[i]}_', 1500, 1024, 1024)
#         va_mean[i] = np.mean(va_list[i])
#         va_std[i] = np.std(va_list[i])
#
#     x = etas
#
#     fig = plt.figure()
#     fig.set_size_inches(7.0, 6.5)
#     plt.ylabel('Polarización', fontsize=16)
#     plt.xlabel('Amplitud de ruido', fontsize=16, labelpad=6)
#     plt.errorbar(x, va_mean, yerr=va_std, linestyle='none', fmt='-o', capsize=8)
#
#     plt.show()

    # rho vs iterations
#     va_list = list(range(3))
#     va_mean = list(range(3))
#     va_std = list(range(3))
#
#     agentsAmount = [40, 800, 2000]
#
#     for i in range(3):
#         va_list[i] = get_va_bulk(f'../position/positions_eta:2.0_', 1500, agentsAmount[i], agentsAmount[i] / (20 **2))
#     x = list(range(0, 1500))
#
#     fig = plt.figure()
#     fig.set_size_inches(9.5, 6.5)
#
#     for i in range(3):
#         plt.plot(x, va_list[i], linewidth=1, label=f'\u03C1 = {agentsAmount[i] / (20 **2)}')
#     plt.ylabel('polarizacion', fontsize=16)
#     plt.xlabel('Iteraciones', fontsize=16, labelpad=12)
#
#     plt.legend(loc='upper right', bbox_to_anchor=(1.12, 1), fontsize=12)
#     plt.show()

#     print(x)

    #PLOT MEAN W/DEVIATION VS ETA
#     va_list = list(range(6))
#     va_mean = list(range(6))
#     va_std = list(range(6))
#     fig = plt.figure()
#
#     agentsAmount = [0.1, 1.0, 2.0, 3.0, 4.0, 5.0]
#     etas = [0.1, 2.0, 5.0]
#
#     for i in range(3):
#         print(i)
#         for j in range(6):
#             print(j)
#             va_list[j] = get_va_bulk(f'../position/positions_eta:{etas[i]}_', 1500, 400 * agentsAmount[j], agentsAmount[j])
#             va_mean[j] = np.mean(va_list[j])
#             va_std[j] = np.std(va_list[j])
#         x = list(range(6))
#
#         fig.set_size_inches(7.0, 6.5)
#         plt.ylabel('Polarizacion', fontsize=16)
#         plt.xlabel('Densidad', fontsize=16, labelpad=6)
#         plt.errorbar(x, va_mean, yerr=va_std, linestyle='none', fmt='-o', capsize=8, label=f'η = {etas[i]}')
#         plt.legend(loc='upper right', bbox_to_anchor=(1.12, 1), fontsize=12)
#     plt.show()


    # PLOT ETA VS ITER
    # va_list = list(range(6))
    # for i in range(6):
    #     va_list[i] = get_va(f'../position/positions_eta:{i}.0_', 1000, 350)
    # x = list(range(0, 1000))
    #
    # fig = plt.figure()
    # fig.set_size_inches(9.5, 6.5)
    #
    # for i in range(6):
    #     plt.plot(x, va_list[i], linewidth=1, label=f'η = {i}')
    # plt.ylabel('Va', fontsize=16)
    # plt.xlabel('Iteraciones', fontsize=16, labelpad=12)
    #
    # plt.legend(loc='upper right', bbox_to_anchor=(1.12, 1), fontsize=12)
    # plt.show()
    #
    # print(x)


if __name__ == "__main__":
    main()
