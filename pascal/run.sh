#!/bin/bash
if [ -z "$1" ]; then
    echo "Usage: ./run.sh <pascal-file>"
    echo "Example: ./run.sh examples/example1.pas"
    exit 1
fi

java -cp target/classes com.pascal.PascalInterpreter "$1"
