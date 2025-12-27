# Интерпретатор языка COW

Интерпретатор эзотерического языка программирования COW, разработанный на Java.

## Требования

- Java 11 или выше

## Структура проекта

```
cow/
├── pom.xml
├── README.md
├── cow-interpreter.jar
├── hello.cow
├── fib.cow
├── lib/
│   ├── junit-4.13.2.jar
│   └── hamcrest-core-1.3.jar
└── src/
    ├── main/
    │   └── java/
    │       └── org/
    │           └── example/
    │               ├── Main.java
    │               └── CowInterpreter.java
    └── test/
        └── java/
            └── CowInterpreterTest.java
```

## Компиляция

### Способ 1: Используя javac (работает на Windows и Linux)

```bash
javac --release 11 -d target/classes -sourcepath src/main/java src/main/java/org/example/*.java
```

### Способ 2: Используя Maven (если установлен)

```bash
mvn clean compile
```

## Создание JAR файла

```bash
javac --release 11 -d target/classes -sourcepath src/main/java src/main/java/org/example/*.java
cd target/classes
jar cfe ../../cow-interpreter.jar org.example.Main org/example/*.class
cd ../..
```

## Запуск

### Вариант 1: Используя JAR файл (рекомендуется)

```bash
java -jar cow-interpreter.jar <файл.cow>
```

Примеры:

```bash
java -jar cow-interpreter.jar hello.cow
java -jar cow-interpreter.jar fib.cow
```

### Вариант 2: Используя скомпилированные классы

```bash
java -cp target/classes org.example.Main <файл.cow>
```

Примеры:

```bash
java -cp target/classes org.example.Main hello.cow
java -cp target/classes org.example.Main fib.cow
```

## Примеры программ

### hello.cow
Выводит "Hello, World!"

Запуск:
```bash
java -jar cow-interpreter.jar hello.cow
```

Ожидаемый вывод:
```
Hello, World!
```

### fib.cow
Выводит первые числа Фибоначчи

Запуск:
```bash
java -jar cow-interpreter.jar fib.cow
```

Ожидаемый вывод:
```
1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, ...
```

## Описание операторов COW

| Оператор | Описание |
|----------|----------|
| MoO | Увеличить значение текущей ячейки на 1 |
| MOo | Уменьшить значение текущей ячейки на 1 |
| moO | Переместить указатель на следующую ячейку |
| mOo | Переместить указатель на предыдущую ячейку |
| MOO | Начало цикла (пропустить до moo, если ячейка = 0) |
| moo | Конец цикла (вернуться к MOO) |
| OOM | Вывести значение текущей ячейки как целое число |
| oom | Ввести целое число в текущую ячейку |
| mOO | Выполнить инструкцию с номером из текущей ячейки |
| Moo | Если ячейка = 0, ввести символ; иначе вывести ASCII символ |
| OOO | Обнулить значение в ячейке |
| MMM | Копировать значение между ячейкой и регистром |

## Тестирование

Проект включает набор юнит-тестов для проверки всех функций интерпретатора.

### Компиляция тестов

**Способ 1: Используя javac (работает на Windows и Linux)**

Сначала скачайте JUnit библиотеки (если они еще не скачаны):

```bash
mkdir -p lib
curl -L -o lib/junit-4.13.2.jar https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
curl -L -o lib/hamcrest-core-1.3.jar https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
```

Затем скомпилируйте основные классы и тесты:

**На Windows (Command Prompt, PowerShell, Git Bash):**
```bash
javac --release 11 -d target/classes -sourcepath src/main/java src/main/java/org/example/*.java
javac --release 11 -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;target/classes" -d target/test-classes src/test/java/CowInterpreterTest.java
```

**На Linux/Mac:**
```bash
javac --release 11 -d target/classes -sourcepath src/main/java src/main/java/org/example/*.java
javac --release 11 -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:target/classes" -d target/test-classes src/test/java/CowInterpreterTest.java
```

**Способ 2: Используя Maven (если установлен)**

```bash
mvn clean test
```

### Запуск тестов

**Способ 1: Используя javac**

**На Windows (Command Prompt, PowerShell, Git Bash):**
```bash
java -cp "lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;target/classes;target/test-classes" org.junit.runner.JUnitCore CowInterpreterTest
```

**На Linux/Mac:**
```bash
java -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:target/classes:target/test-classes" org.junit.runner.JUnitCore CowInterpreterTest
```

**Способ 2: Используя Maven (если установлен)**

```bash
mvn test
```

### Покрытие тестами

Тесты покрывают:
- Все 12 операторов языка COW
- Работу с памятью и указателем
- Циклы (включая вложенные)
- Граничные случаи (пустые файлы, невалидные команды)
- Ввод/вывод данных
- Работу с регистром
- Выполнение инструкций по индексу (mOO)

Всего: 26 тестов

Ожидаемый результат при успешном выполнении:
```
JUnit version 4.13.2
..........................
Time: 0.1XX

OK (26 tests)
```

## Устранение неполадок

### Ошибка "command not found: java"

Установите Java Development Kit (JDK) версии 11 или выше:

- **Windows**: Скачайте с https://adoptium.net/ или https://www.oracle.com/java/technologies/downloads/
- **Linux**: `sudo apt install openjdk-11-jdk` (Ubuntu/Debian) или `sudo yum install java-11-openjdk` (CentOS/RHEL)

### Ошибка "Error reading file"

Убедитесь, что файл .cow существует и находится в текущей директории, или укажите полный путь к файлу.
