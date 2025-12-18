# Pascal Interpreter

Интерпретатор для упрощенной версии языка Pascal, написанный на Java.

## Описание

Интерпретатор поддерживает следующие возможности:
- Объявление и присваивание переменных
- Арифметические операции: `+`, `-`, `*`, `/`
- Унарные операции: `+`, `-`
- Использование скобок для изменения приоритета операций
- Вложенные блоки `BEGIN...END`
- Множественные операторы с разделителем `;`

## Требования

- Java 11 или выше
- Maven 3.6 или выше (для запуска тестов и автоматической сборки)

**Примечание:** Для запуска интерпретатора Maven не обязателен - можно использовать скрипты `build.sh`/`build.bat` и `run.sh`/`run.bat`.

## Сборка проекта

### С помощью Maven

**Windows:**
```cmd
mvn clean package
```

**Linux/macOS:**
```bash
mvn clean package
```

### Без Maven (только компиляция основного кода)

**Windows:**
```cmd
build.bat
```

**Linux/macOS:**
```bash
chmod +x build.sh
./build.sh
```

## Запуск интерпретатора

### Вариант 1: Запуск с файлом (с Maven)

**Windows:**
```cmd
mvn exec:java -Dexec.args="examples\example1.pas"
```

**Linux/macOS:**
```bash
mvn exec:java -Dexec.args="examples/example1.pas"
```

### Вариант 1A: Запуск с файлом (без Maven)

**Windows:**
```cmd
run.bat examples\example1.pas
```

**Linux/macOS:**
```bash
chmod +x run.sh
./run.sh examples/example1.pas
```

### Вариант 2: Интерактивный режим

**Windows:**
```cmd
mvn exec:java -Dexec.args="-i"
```

**Linux/macOS:**
```bash
mvn exec:java -Dexec.args="-i"
```

В интерактивном режиме введите код Pascal, затем пустую строку для выполнения. Введите `exit` для выхода.

### Вариант 3: Запуск через JAR файл

**Сборка JAR:**

**Windows:**
```cmd
mvn clean package
```

**Linux/macOS:**
```bash
mvn clean package
```

**Запуск с файлом:**

**Windows:**
```cmd
java -cp target\pascal-interpreter-1.0.0.jar com.pascal.PascalInterpreter examples\example1.pas
```

**Linux/macOS:**
```bash
java -cp target/pascal-interpreter-1.0.0.jar com.pascal.PascalInterpreter examples/example1.pas
```

**Запуск в интерактивном режиме:**

**Windows:**
```cmd
java -cp target\pascal-interpreter-1.0.0.jar com.pascal.PascalInterpreter -i
```

**Linux/macOS:**
```bash
java -cp target/pascal-interpreter-1.0.0.jar com.pascal.PascalInterpreter -i
```

## Запуск тестов

### Запуск всех тестов

**Windows:**
```cmd
mvn test
```

**Linux/macOS:**
```bash
mvn test
```

### Запуск тестов с отчетом о покрытии

**Windows:**
```cmd
mvn clean test jacoco:report
```

**Linux/macOS:**
```bash
mvn clean test jacoco:report
```

Отчет о покрытии будет доступен в файле `target/site/jacoco/index.html`

### Запуск конкретного теста

**Windows:**
```cmd
mvn test -Dtest=LexerTest
```

**Linux/macOS:**
```bash
mvn test -Dtest=LexerTest
```

## Примеры использования

### Пример 1: Пустая программа

**Файл:** `examples/example1.pas`

```pascal
BEGIN
END.
```

**Запуск (Windows):**
```cmd
mvn exec:java -Dexec.args="examples\example1.pas"
```

**Запуск (Linux/macOS):**
```bash
mvn exec:java -Dexec.args="examples/example1.pas"
```

**Результат:**
```
Variables:
```

### Пример 2: Арифметические выражения

**Файл:** `examples/example2.pas`

```pascal
BEGIN
    x:= 2 + 3 * (2 + 3);
        y:= 2 / 2 - 2 + 3 * ((1 + 1) + (1 + 1))
END.
```

**Запуск (Windows):**
```cmd
mvn exec:java -Dexec.args="examples\example2.pas"
```

**Запуск (Linux/macOS):**
```bash
mvn exec:java -Dexec.args="examples/example2.pas"
```

**Результат:**
```
Variables:
x = 17.0
y = 11.0
```

### Пример 3: Вложенные блоки и использование переменных

**Файл:** `examples/example3.pas`

```pascal
BEGIN
    y: = 2;
    BEGIN
        a := 3;
        a := a;
        b := 10 + a + 10 * y / 4;
        c := a - b
    END;
    x := 11;
END.
```

**Запуск (Windows):**
```cmd
mvn exec:java -Dexec.args="examples\example3.pas"
```

**Запуск (Linux/macOS):**
```bash
mvn exec:java -Dexec.args="examples/example3.pas"
```

**Результат:**
```
Variables:
a = 3.0
b = 18.0
c = -15.0
x = 11.0
y = 2.0
```

## Структура проекта

```
pascal/
├── pom.xml                          # Maven конфигурация
├── README.md                        # Этот файл
├── examples/                        # Примеры программ
│   ├── example1.pas
│   ├── example2.pas
│   └── example3.pas
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── pascal/
    │               ├── TokenType.java          # Типы токенов
    │               ├── Token.java              # Класс токена
    │               ├── Lexer.java              # Лексический анализатор
    │               ├── Parser.java             # Синтаксический анализатор
    │               ├── Interpreter.java        # Интерпретатор
    │               ├── PascalInterpreter.java  # Главный класс
    │               └── ast/                    # AST узлы
    │                   ├── ASTNode.java
    │                   ├── Program.java
    │                   ├── ComplexStatement.java
    │                   ├── Assignment.java
    │                   ├── BinOp.java
    │                   ├── UnaryOp.java
    │                   ├── Num.java
    │                   ├── Var.java
    │                   └── NoOp.java
    └── test/
        └── java/
            └── com/
                └── pascal/
                    ├── TokenTest.java              # Тесты для Token
                    ├── LexerTest.java              # Тесты для Lexer
                    ├── ParserTest.java             # Тесты для Parser
                    ├── InterpreterTest.java        # Тесты для Interpreter
                    └── PascalInterpreterTest.java  # Тесты для главного класса
```

## Архитектура

Интерпретатор состоит из трех основных компонентов:

1. **Lexer (Лексический анализатор)**: Преобразует исходный код в последовательность токенов
2. **Parser (Синтаксический анализатор)**: Строит абстрактное синтаксическое дерево (AST) из токенов
3. **Interpreter (Интерпретатор)**: Выполняет AST и вычисляет значения переменных

### Грамматика

```
program ::= complex_statement dot
complex_statement ::= BEGIN statement_list END
statement_list ::= statement | statement SEMI statement_list
statement ::= compound_statement | assignment | empty
assignment ::= variable ASSIGN expr
variable ::= ID
empty ::= ''
expr ::= term ( ( '+' | '-' ) term )*
term ::= factor ( ( '*' | '/' ) factor )*
factor ::= ( '+' | '-' ) factor | INTEGER | LPAREN expr RPAREN | variable
```

## Тестирование

Проект имеет полное покрытие тестами:

- **TokenTest**: Тесты для класса Token
- **LexerTest**: Тесты для лексического анализатора
- **ParserTest**: Тесты для синтаксического анализатора
- **InterpreterTest**: Тесты для интерпретатора
- **PascalInterpreterTest**: Интеграционные тесты

Все тесты можно запустить командой:

**Windows/Linux/macOS:**
```bash
mvn test
```

Для просмотра отчета о покрытии кода:

**Windows/Linux/macOS:**
```bash
mvn clean test jacoco:report
```

Затем откройте файл `target/site/jacoco/index.html` в браузере.

## Устранение проблем

### Проблема: "mvn: command not found"

**Решение:** Установите Maven и добавьте его в PATH.

**Windows:**
1. Скачайте Maven с https://maven.apache.org/download.cgi
2. Распакуйте архив
3. Добавьте `bin` директорию Maven в системную переменную PATH

**Linux:**
```bash
sudo apt-get install maven
```

**macOS:**
```bash
brew install maven
```

### Проблема: "JAVA_HOME not set"

**Решение:** Установите переменную окружения JAVA_HOME.

**Windows:**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-11
```

**Linux/macOS:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
```

### Проблема: Тесты не запускаются

**Решение:** Убедитесь, что используется Java 11 или выше:

**Windows/Linux/macOS:**
```bash
java -version
```

Если версия ниже 11, установите более новую версию Java.

### Проблема: UnsupportedClassVersionError

**Ошибка:**
```
java.lang.UnsupportedClassVersionError: com/pascal/PascalInterpreter has been compiled by a more recent version of the Java Runtime
```

**Решение:** Пересоберите проект с нужной версией Java:

**Windows/Linux/macOS:**
```bash
mvn clean compile
# или
./build.sh
```

Убедитесь, что версия Java совпадает при компиляции и запуске:
```bash
java -version
javac -version
```

## Лицензия

Этот проект создан в образовательных целях.
