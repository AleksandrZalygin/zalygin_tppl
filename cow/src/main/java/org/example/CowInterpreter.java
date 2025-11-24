package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class CowInterpreter {
    private static final int MEMORY_SIZE = 30000;

    private int[] memory;
    private int pointer;
    private Integer register;
    private InputStream input;
    private OutputStream output;


    public CowInterpreter(InputStream input, OutputStream output) {
        this.memory = new int[MEMORY_SIZE];
        this.pointer = 0;
        this.register = null;
        this.input = input;
        this.output = output;
    }

    public CowInterpreter() {
        this(System.in, System.out);
    }

    public static String loadSource(Path path) throws IOException {
        return Files.readString(path);
    }

    public String[] parseTokens(String source) {
        String cleaned = source.replaceAll("[^MmOo]", "");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cleaned.length(); i++) {
            sb.append(cleaned.charAt(i));
            if (sb.length() == 3) {
                String token = sb.toString();
                if (isValidToken(token)) {
                    sb = new StringBuilder();
                } else {
                    sb.deleteCharAt(0);
                }
            }
        }

        StringBuilder tokens = new StringBuilder();
        sb = new StringBuilder();
        for (int i = 0; i < cleaned.length(); i++) {
            sb.append(cleaned.charAt(i));
            if (sb.length() == 3) {
                String token = sb.toString();
                if (isValidToken(token)) {
                    tokens.append(token).append(" ");
                    sb = new StringBuilder();
                } else {
                    sb.deleteCharAt(0);
                }
            }
        }

        String result = tokens.toString().trim();
        return result.isEmpty() ? new String[0] : result.split(" ");
    }

    private boolean isValidToken(String token) {
        return token.equals("MoO") || token.equals("MOo") || token.equals("moO") ||
                token.equals("mOo") || token.equals("moo") || token.equals("MOO") ||
                token.equals("OOM") || token.equals("oom") || token.equals("mOO") ||
                token.equals("Moo") || token.equals("OOO") || token.equals("MMM");
    }

    public Map<Integer, Integer> buildLoopMap(String[] tokens) {
        Map<Integer, Integer> loops = new HashMap<>();
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("MOO")) {
                stack.push(i);
            } else if (tokens[i].equals("moo")) {
                if (!stack.isEmpty()) {
                    int start = stack.pop();
                    loops.put(start, i);
                    loops.put(i, start);
                }
            }
        }

        return loops;
    }

    public void execute(String source) throws IOException {
        String[] tokens = parseTokens(source);
        Map<Integer, Integer> loops = buildLoopMap(tokens);

        int pc = 0;

        while (pc < tokens.length) {
            String token = tokens[pc];

            switch (token) {
                case "MoO":
                    memory[pointer]++;
                    break;

                case "MOo":
                    memory[pointer]--;
                    break;

                case "moO":
                    pointer++;
                    if (pointer >= MEMORY_SIZE) {
                        pointer = 0;
                    }
                    break;

                case "mOo":
                    pointer--;
                    if (pointer < 0) {
                        pointer = MEMORY_SIZE - 1;
                    }
                    break;

                case "MOO":
                    if (memory[pointer] == 0) {
                        Integer target = loops.get(pc);
                        if (target != null) {
                            pc = target;
                        }
                    }
                    break;

                case "moo":
                    Integer target = loops.get(pc);
                    if (target != null) {
                        pc = target - 1;
                    }
                    break;

                case "OOM":
                    String numStr = String.valueOf(memory[pointer]);
                    output.write(numStr.getBytes());
                    output.flush();
                    break;

                case "oom":
                    int value = input.read();
                    if (value != -1) {
                        memory[pointer] = value;
                    }
                    break;

                case "mOO":
                    int targetPc = memory[pointer];
                    if (targetPc >= 0 && targetPc < tokens.length) {
                        pc = targetPc - 1;
                    }
                    break;

                case "Moo":
                    if (memory[pointer] == 0) {
                        int inputValue = input.read();
                        if (inputValue != -1) {
                            memory[pointer] = inputValue;
                        }
                    } else {
                        output.write(memory[pointer]);
                        output.flush();
                    }
                    break;

                case "OOO":
                    memory[pointer] = 0;
                    break;

                case "MMM":
                    if (register == null) {
                        register = memory[pointer];
                    } else {
                        memory[pointer] = register;
                        register = null;
                    }
                    break;
            }

            pc++;
        }
    }

    public void executeFromFile(Path path) throws IOException {
        String source = loadSource(path);
        execute(source);
    }

    public int[] getMemory() {
        return memory.clone();
    }

    public int getPointer() {
        return pointer;
    }

    public Integer getRegister() {
        return register;
    }

    public void setMemoryCell(int index, int value) {
        if (index >= 0 && index < MEMORY_SIZE) {
            memory[index] = value;
        }
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public void reset() {
        this.memory = new int[MEMORY_SIZE];
        this.pointer = 0;
        this.register = null;
    }
}