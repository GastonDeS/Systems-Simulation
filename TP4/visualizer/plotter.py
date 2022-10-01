import matplotlib.pyplot as plt
import numpy as np


def plot_exercise_1(delta_t, y):
    x = np.arange(start=0, step=delta_t, stop=5)
    plt.plot(x, y[:500])
    plt.show()

    print(x)
