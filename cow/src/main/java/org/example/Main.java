package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Использование: java -cp target/classes org.example.Main <файл.cow>");
            System.out.println("\nПримеры:");
            System.out.println("  java -cp target/classes org.example.Main hello.cow");
            System.out.println("  java -cp target/classes org.example.Main fib.cow");
            return;
        }

        String filename = args[0];
        Path path = Paths.get(filename);

        try {
            CowInterpreter interpreter = new CowInterpreter();
            interpreter.executeFromFile(path);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}