from visualizer.parser import parse_config, parse_ssv
from visualizer.plotter import plot_exercise_1, plot_exercise_1b, plot_earth, plot_delta_E, plot_time_vs_speed


def main():
    config = parse_config('../config.json')
    #plot_exercise_1(config['deltaT'])
    #plot_exercise_1b()
    #plot_delta_E()
    plot_time_vs_speed()


if __name__ == "__main__":
    main()
