import math
from parser import parse

def main():
    va_list = []
    for i in range(500):
        token_list = parse(f'../position/positions{i}.xyz')
        va = 0
        for token in token_list:
            v = token.split(' ')
            va += math.sqrt(float(v[0]) ** 2 + float(v[1]) ** 2)
        va_list.append(va/300)

    for v in va_list:
        print(v)


if __name__ == "__main__":
    main()
