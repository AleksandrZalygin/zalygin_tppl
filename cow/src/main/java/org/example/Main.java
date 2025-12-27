package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar cow-interpreter.jar <filename.cow>");
            System.exit(1);
        }

        String filename = args[0];
        CowInterpreter interpreter = new CowInterpreter();

        try {
            interpreter.loadProgram(filename);
            interpreter.execute();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error executing program: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            interpreter.close();
        }
    }
}
