@echo off
echo Building Pascal Interpreter...

if not exist "target\classes" mkdir target\classes
if not exist "target\test-classes" mkdir target\test-classes

echo Compiling main sources...
javac -d target\classes -sourcepath src\main\java src\main\java\com\pascal\*.java src\main\java\com\pascal\ast\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    exit /b 1
)

echo Build successful!
