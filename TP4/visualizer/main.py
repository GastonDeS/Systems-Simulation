from visualizer.parser import parse_config, parse_ssv
from visualizer.plotter import plot_exercise_1


def main():
    config = parse_config('../config.json')
    data = parse_ssv('../data/Analytic.ssv')
    y = []
    for d in data:
        if d != '':
            y.append(float(d.split(' ')[1]))
    print(y)
    print(config)
    plot_exercise_1(config['deltaT'], y)


if __name__ == "__main__":
    main()
