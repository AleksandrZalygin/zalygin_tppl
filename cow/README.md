# COW Programming Language Interpreter

Интерпретатор эзотерического языка программирования COW, написанный на Java.

## Описание

COW (Cattle Oriented Windows) - эзотерический язык программирования, использующий только комбинации букв M, o и O для создания команд. Этот проект представляет собой полнофункциональный интерпретатор COW с поддержкой всех 12 команд языка.

## Возможности

- ✅ Полная поддержка всех 12 инструкций COW
- ✅ 30,000 ячеек памяти
- ✅ Поддержка вложенных циклов
- ✅ Регистр для временного хранения значений
- ✅ Ввод/вывод данных
- ✅ 67 юнит-тестов с 100% покрытием кода

## Требования

- Java 17 или выше
- Maven 3.6+ (опционально)

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone <URL_репозитория>
cd cow
```

### 2. Компиляция проекта

**Через Maven (рекомендуется):**
```bash
mvn clean compile
```

**Вручную:**
```bash
# Windows
mkdir target\classes
javac -d target\classes src\main\java\org\example\*.java

# Linux/macOS
mkdir -p target/classes
javac -d target/classes src/main/java/org/example/*.java
```

### 3. Запуск программы

```bash
# Windows
java -cp target\classes org.example.Main hello.cow

# Linux/macOS
java -cp target/classes org.example.Main hello.cow
```

## Примеры

В проекте включены два примера программ на COW:

- **hello.cow** - выводит "Hello, World!"
- **fib.cow** - вычисляет числа Фибоначчи

## Язык программирования COW

### Инструкции

| Команда | Описание |
|---------|----------|
| `MoO` | Увеличить значение текущей ячейки на 1 |
| `MOo` | Уменьшить значение текущей ячейки на 1 |
| `moO` | Перейти к следующей ячейке памяти |
| `mOo` | Перейти к предыдущей ячейке памяти |
| `MOO` | Начало цикла (если текущая ячейка = 0, перейти к соответствующей `moo`) |
| `moo` | Конец цикла (вернуться к соответствующей `MOO`) |
| `OOM` | Вывести значение текущей ячейки как число |
| `oom` | Ввести значение в текущую ячейку |
| `mOO` | Выполнить инструкцию с номером из текущей ячейки |
| `Moo` | Если ячейка = 0: ввод, иначе: вывод символа (ASCII) |
| `OOO` | Обнулить текущую ячейку |
| `MMM` | Работа с регистром (копировать в/из регистра) |

### Пример программы

Программа, выводящая число 5:

```cow
MoO MoO MoO MoO MoO OOM
```

**Объяснение:**
- `MoO` × 5 — увеличить значение ячейки до 5
- `OOM` — вывести значение как число

## Тестирование

Проект включает 67 юнит-тестов с полным покрытием кода.

### Запуск тестов

```bash
mvn test
```

### Покрытие

Тесты покрывают:
- Все конструкторы и методы
- Все 12 инструкций COW
- Парсинг токенов
- Построение карты циклов
- Граничные случаи (циклический буфер, вложенные циклы)
- Файловые операции
- Ввод/вывод данных

## Структура проекта

```
cow/
├── src/
│   ├── main/java/org/example/
│   │   ├── Main.java              # Точка входа
│   │   └── CowInterpreter.java    # Интерпретатор
│   └── test/java/
│       └── CowInterpreterTest.java # Юнит-тесты
├── hello.cow                       # Пример: Hello World
├── fib.cow                         # Пример: Fibonacci
├── pom.xml                         # Maven конфигурация
└── README.md                       # Документация
```

## API

### CowInterpreter

**Конструкторы:**
```java
// Конструктор по умолчанию (System.in/System.out)
CowInterpreter interpreter = new CowInterpreter();

// С кастомными потоками ввода/вывода
CowInterpreter interpreter = new CowInterpreter(inputStream, outputStream);
```

**Основные методы:**
```java
// Выполнить код из строки
interpreter.execute("MoO MoO MoO OOM");

// Выполнить код из файла
interpreter.executeFromFile(Paths.get("program.cow"));

// Загрузить исходный код из файла
String source = CowInterpreter.loadSource(Paths.get("program.cow"));

// Парсинг токенов
String[] tokens = interpreter.parseTokens(source);

// Построить карту циклов
Map<Integer, Integer> loops = interpreter.buildLoopMap(tokens);
```

**Методы для тестирования:**
```java
// Получить состояние памяти
int[] memory = interpreter.getMemory();

// Получить позицию указателя
int pointer = interpreter.getPointer();

// Получить значение регистра
Integer register = interpreter.getRegister();

// Установить значение ячейки памяти
interpreter.setMemoryCell(0, 42);

// Установить позицию указателя
interpreter.setPointer(10);

// Сбросить состояние интерпретатора
interpreter.reset();
```

## Решение проблем

### "java: command not found"

Установите JDK 17 или выше:
- **Windows**: https://adoptium.net/
- **Linux**: `sudo apt install openjdk-17-jdk`
- **macOS**: `brew install openjdk@17`

### "Error: Could not find or load main class"

Убедитесь, что проект скомпилирован:
```bash
mvn clean compile
```

### "package org.example does not exist"

Проверьте структуру папок и перекомпилируйте:
```bash
mvn clean compile
```

## Лицензия

Этот проект создан в образовательных целях.

## Автор

Создано как учебный проект для изучения эзотерических языков программирования и практики разработки интерпретаторов.

---

**Версия:** 1.0
**Дата:** 2025-11-24
**Покрытие тестами:** 100%
