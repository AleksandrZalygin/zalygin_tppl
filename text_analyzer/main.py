import sys
from collections import Counter


def analyze_file(filename):
    try:
        with open(filename, 'r', encoding='utf-8') as file:
            lines = file.readlines()

        line_count = len(lines)

        char_count = sum(len(line) for line in lines)

        empty_line_count = sum(1 for line in lines if line.strip() == '')

        char_freq = Counter(''.join(lines))

        return line_count, char_count, empty_line_count, char_freq

    except FileNotFoundError:
        print(f"Ошибка: Файл '{filename}' не найден.")
        sys.exit(1)
    except Exception as e:
        print(f"Произошла ошибка: {e}")
        sys.exit(1)


def print_results(line_count, char_count, empty_line_count, char_freq, options):
    if '1' in options:
        print(f"строки: {line_count}")
    if '2' in options:
        print(f"символы: {char_count}")
    if '3' in options:
        print(f"пустыхе  строки: {empty_line_count}")
    if '4' in options:
        print("словарь:")
        for char, freq in sorted(char_freq.items()):
            if char.isspace():
                char_display = repr(char)[1:-1]
            else:
                char_display = char
            print(f"'{char_display}': {freq}")


def main():
    if len(sys.argv) < 2:
        print("Ошибка: Укажите имя файла.")
        print("Пример: python text_analyzer.py input.txt")
        sys.exit(1)

    filename = sys.argv[1]
    print("выберите, что вывести:")
    print("1 - строки")
    print("2 - символы")
    print("3 - пустые строки")
    print("4 - словарь")
    print("Введите номера через пробел (например, '1 2 3') или '0' для всех:")

    options = input().strip()
    if options == '0':
        options = '1 2 3 4'

    valid_options = {'1', '2', '3', '4'}
    selected_options = set(options.split())
    if not selected_options.issubset(valid_options):
        print("неправильный выбор")
        sys.exit(1)

    line_count, char_count, empty_line_count, char_freq = analyze_file(filename)
    print_results(line_count, char_count, empty_line_count, char_freq, selected_options)


if __name__ == "__main__":
    main()
