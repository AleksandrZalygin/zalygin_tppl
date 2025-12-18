#!/bin/bash
echo "Building Pascal Interpreter..."

mkdir -p target/classes
mkdir -p target/test-classes

echo "Compiling main sources..."
javac -d target/classes -sourcepath src/main/java src/main/java/com/pascal/*.java src/main/java/com/pascal/ast/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Build successful!"
