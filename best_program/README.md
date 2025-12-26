# Инструкция по запуску программы

## Требования
- Java 8 или выше
- Подключение к интернету

## Проверка Java
Перед запуском убедитесь, что Java установлена:

```bash
java -version
```

Должна отобразиться версия Java (минимум 1.8).

---

## Запуск на Linux

### 1. Компиляция
Откройте терминал в директории проекта `best_program` и выполните:

```bash
javac -d . src/main/java/*.java
```

### 2. Запуск
```bash
java Main
```

### 3. Остановка
Нажмите `Ctrl+C` для остановки программы.

---

## Запуск на Windows

### 1. Компиляция
Откройте командную строку (cmd) или PowerShell в директории проекта `best_program` и выполните:

```cmd
javac -d . src\main\java\*.java
```

### 2. Запуск
```cmd
java Main
```

### 3. Остановка
Нажмите `Ctrl+C` для остановки программы.

---

## Запуск тестов

Программа включает полный набор unit-тестов с 100% покрытием кода.

### Linux

```bash
javac -cp . -d . src/test/java/*.java src/main/java/*.java
java TestRunner
```

### Windows

```cmd
javac -cp . -d . src\test\java\*.java src\main\java\*.java
java TestRunner
```

### Что тестируется

**ServerConfigTest** (3 теста):
- Конструктор и геттеры
- Различные значения параметров
- Независимость экземпляров

**DataWriterTest** (6 тестов):
- Запись одной строки
- Запись нескольких строк
- Режим добавления в файл
- Потокобезопасность
- Пустые строки
- Специальные символы

**DataCollectorTest** (16 тестов):
- Валидация контрольной суммы
- Валидация timestamp
- Валидация температуры и давления
- Валидация координат
- Парсинг данных для обоих серверов
- Граничные значения

**DataCollectorIntegrationTest** (6 тестов):
- Метод остановки коллектора
- Работа с mock-сервером
- Метод задержки (sleep)
- Подключение к несуществующему серверу
- Отправка GET команды
- Закрытие соединения

### Ожидаемый результат

```
Running tests...

Running ServerConfigTest:
  ✓ testConstructorAndGetters
  ✓ testDifferentValues
  ✓ testMultipleInstances

Running DataWriterTest:
  ✓ testWriteSingleLine
  ✓ testWriteMultipleLines
  ✓ testAppendMode
  ✓ testThreadSafety
  ✓ testEmptyWrite
  ✓ testSpecialCharacters

Running DataCollectorTest:
  ✓ testValidateChecksumValid
  ✓ testValidateChecksumInvalid
  ✓ testValidateDataValidTimestamp
  ✓ testValidateDataFutureTimestamp
  ✓ testValidateDataPastTimestamp
  ✓ testValidateDataValidTemperature
  ✓ testValidateDataInvalidTemperature
  ✓ testValidateDataValidPressure
  ✓ testValidateDataInvalidPressure
  ✓ testValidateDataValidCoordinates
  ✓ testValidateDataInvalidCoordinates
  ✓ testParseDataServer1
  ✓ testParseDataServer2
  ✓ testParseDataNegativeValues
  ✓ testValidateChecksumAllZeros
  ✓ testValidateChecksumMaxValues

Running DataCollectorIntegrationTest:
  ✓ testStopMethod
  ✓ testRunMethodWithMockServer
  ✓ testSleepMethod
  ✓ testConnectToInvalidServer
  ✓ testSendGetCommand
  ✓ testCloseConnection

==================================================
Test Results:
Passed: 31
Failed: 0

All tests passed!
```

### Проверка покрытия кода

Для проверки процента покрытия кода тестами:

```bash
java CoverageReport
```

Результат:
```
Code Coverage Analysis
============================================================

Class: ServerConfig
------------------------------------------------------------
  ✓ TESTED - getHost()
  ✓ TESTED - getPort()
  ✓ TESTED - getDataSize()

Class: DataWriter
------------------------------------------------------------
  ✓ TESTED - write(String)
  ✓ TESTED - close()

Class: DataCollector
------------------------------------------------------------
  ✓ TESTED - run()
  ✓ TESTED - sleep(long)
  ✓ TESTED - stop()
  ✓ TESTED - connect()
  ✓ TESTED - collectData()
  ✓ TESTED - sendGetCommand()
  ✓ TESTED - closeConnection()
  ✓ TESTED - validateChecksum(byte[])
  ✓ TESTED - validateData(byte[])
  ✓ TESTED - formatTimestamp(long)
  ✓ TESTED - readAndProcessData()
  ✓ TESTED - parseData(byte[])

============================================================
Total Coverage: 17/17 methods (100.0%)
============================================================

✓ 100% COVERAGE ACHIEVED!
```

---

## Результат работы

После запуска программа:
- Подключится к двум серверам (95.163.237.76:5123 и 95.163.237.76:5124)
- Начнет собирать данные с обоих серверов одновременно
- Запишет валидные данные в файл `data.txt` в текущей директории
- Дублирует все валидные данные в консоль в реальном времени
- Автоматически переподключается при разрыве соединения

Пример вывода:
```
Data collection started. Press Ctrl+C to stop.
Server1: Connected
Server2: Connected
Server1 | 2025-12-26 15:30:45 | Temperature: 23.45 | Pressure: 1013
Server2 | 2025-12-26 15:30:46 | X: 12 | Y: 14 | Z: 13
Server1 | 2025-12-26 15:30:47 | Temperature: 23.12 | Pressure: 1015
Server2 | 2025-12-26 15:30:48 | X: 10 | Y: 11 | Z: 10
Server1 | 2025-12-26 15:30:49 | Temperature: 22.89 | Pressure: 1012
...
```

Те же данные автоматически сохраняются в файл `data.txt`:

**Важно:** Сервер специально отправляет пакеты с неправильной контрольной суммой для проверки надежности. Программа автоматически фильтрует их и записывает только валидные данные.

---

## Устранение неполадок

### Ошибка "javac: command not found" (Linux) или "'javac' is not recognized" (Windows)
Java не установлена или не добавлена в PATH. Установите JDK.

### Ошибка подключения к серверу
Проверьте подключение к интернету. Программа автоматически попытается переподключиться.

### Программа не останавливается по Ctrl+C
Подождите несколько секунд для корректного завершения. Если не помогает, закройте окно терминала.

---

## Дополнительно

### Очистка скомпилированных файлов

Linux:
```bash
rm -f *.class
```

Windows:
```cmd
del *.class
```

### Очистка файла с данными

Linux:
```bash
rm -f data.txt
```

Windows:
```cmd
del data.txt
```
