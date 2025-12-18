package com.pascal;

import com.pascal.ast.Program;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class PascalInterpreter {
    public static Map<String, Double> execute(String code) {
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        Program program = parser.program();

        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);

        return interpreter.getVariables();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Pascal Interpreter");
            System.out.println("Usage:");
            System.out.println("  java -jar pascal-interpreter.jar <filename>");
            System.out.println("  java -jar pascal-interpreter.jar -i  (interactive mode)");
            System.out.println();
            System.out.println("Or run without arguments to enter code:");
            System.out.println("Enter Pascal code (enter empty line to execute):");
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            StringBuilder code = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty() && code.length() > 0) {
                    break;
                }
                code.append(line).append("\n");
            }

            try {
                Map<String, Double> variables = execute(code.toString());
                System.out.println("\nVariables:");
                for (Map.Entry<String, Double> entry : variables.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        } else if (args[0].equals("-i")) {
            System.out.println("Pascal Interpreter - Interactive Mode");
            System.out.println("Enter Pascal code (enter empty line to execute, 'exit' to quit):");
            System.out.println();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                StringBuilder code = new StringBuilder();
                System.out.print(">>> ");

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.trim().equalsIgnoreCase("exit")) {
                        return;
                    }
                    if (line.trim().isEmpty() && code.length() > 0) {
                        break;
                    }
                    code.append(line).append("\n");
                    if (!line.trim().isEmpty()) {
                        System.out.print("... ");
                    }
                }

                try {
                    Map<String, Double> variables = execute(code.toString());
                    System.out.println("Variables:");
                    for (Map.Entry<String, Double> entry : variables.entrySet()) {
                        System.out.println("  " + entry.getKey() + " = " + entry.getValue());
                    }
                    System.out.println();
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    System.out.println();
                }
            }
        } else {
            String filename = args[0];
            try {
                String code = new String(Files.readAllBytes(Paths.get(filename)));
                Map<String, Double> variables = execute(code);

                System.out.println("Variables:");
                for (Map.Entry<String, Double> entry : variables.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
