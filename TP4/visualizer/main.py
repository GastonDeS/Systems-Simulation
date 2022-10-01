from visualizer.parser import parse_config, parse_ssv
from visualizer.plotter import plot_exercise_1


def main():
    config = parse_config('../config.json')
    plot_exercise_1(config['deltaT'])


if __name__ == "__main__":
    main()
