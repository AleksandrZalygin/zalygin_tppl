@echo off
if "%1"=="" (
    echo Usage: run.bat ^<pascal-file^>
    echo Example: run.bat examples\example1.pas
    exit /b 1
)

java -cp target\classes com.pascal.PascalInterpreter %1
