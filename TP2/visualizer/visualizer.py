import math
import matplotlib.pyplot as plt
from parser import parse


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


def main():
    va_list_0 = []
    va_list_1 = []

    va_list_0 = get_va(f'../position/positions_eta:0.0_', 500, 300)
    va_list_1 = get_va(f'../position/positions_eta:1.0_', 500, 300)

    x = list(range(0, 500))

    plt.plot(x, va_list_0)
    plt.plot(x, va_list_1)
    plt.show()
    print(x)


if __name__ == "__main__":
    main()
