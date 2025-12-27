package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CowInterpreter {
    private List<Integer> memory;
    private int pointer;
    private Integer register;
    private List<String> instructions;
    private Scanner scanner;

    public CowInterpreter() {
        this.memory = new ArrayList<>();
        this.memory.add(0);
        this.pointer = 0;
        this.register = null;
        this.instructions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void loadProgram(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        parseInstructions(content);
    }

    private void parseInstructions(String content) {
        instructions.clear();
        int i = 0;
        while (i < content.length()) {
            if (i + 2 < content.length()) {
                String candidate = content.substring(i, i + 3);
                if (isValidInstruction(candidate)) {
                    instructions.add(candidate);
                    i += 3;
                    continue;
                }
            }
            i++;
        }
    }

    private boolean isValidInstruction(String s) {
        return s.equals("MoO") || s.equals("MOo") || s.equals("moO") ||
               s.equals("mOo") || s.equals("moo") || s.equals("MOO") ||
               s.equals("OOM") || s.equals("oom") || s.equals("mOO") ||
               s.equals("Moo") || s.equals("OOO") || s.equals("MMM");
    }

    public void execute() {
        memory.clear();
        memory.add(0);
        pointer = 0;
        register = null;

        int pc = 0;

        while (pc < instructions.size()) {
            String instr = instructions.get(pc);

            switch (instr) {
                case "MoO":
                    ensureMemorySize();
                    memory.set(pointer, memory.get(pointer) + 1);
                    break;

                case "MOo":
                    ensureMemorySize();
                    memory.set(pointer, memory.get(pointer) - 1);
                    break;

                case "moO":
                    pointer++;
                    ensureMemorySize();
                    break;

                case "mOo":
                    if (pointer > 0) {
                        pointer--;
                    }
                    break;

                case "MOO":
                    ensureMemorySize();
                    if (memory.get(pointer) == 0) {
                        int depth = 1;
                        int searchPc = pc + 1;
                        while (searchPc < instructions.size() && depth > 0) {
                            if (instructions.get(searchPc).equals("MOO")) {
                                depth++;
                            } else if (instructions.get(searchPc).equals("moo")) {
                                depth--;
                            }
                            searchPc++;
                        }
                        pc = searchPc - 1;
                    }
                    break;

                case "moo":
                    int depth = 1;
                    int searchPc = pc - 1;
                    while (searchPc >= 0 && depth > 0) {
                        if (instructions.get(searchPc).equals("moo")) {
                            depth++;
                        } else if (instructions.get(searchPc).equals("MOO")) {
                            depth--;
                        }
                        searchPc--;
                    }
                    pc = searchPc;
                    break;

                case "OOM":
                    ensureMemorySize();
                    System.out.print(memory.get(pointer));
                    break;

                case "oom":
                    ensureMemorySize();
                    try {
                        if (scanner.hasNextInt()) {
                            memory.set(pointer, scanner.nextInt());
                        } else {
                            memory.set(pointer, 0);
                        }
                    } catch (Exception e) {
                        memory.set(pointer, 0);
                    }
                    break;

                case "mOO":
                    ensureMemorySize();
                    int targetIndex = memory.get(pointer);
                    if (targetIndex >= 0 && targetIndex < instructions.size()) {
                        executeInstruction(instructions.get(targetIndex));
                    }
                    break;

                case "Moo":
                    ensureMemorySize();
                    if (memory.get(pointer) == 0) {
                        try {
                            int input = System.in.read();
                            memory.set(pointer, input);
                        } catch (IOException e) {
                            memory.set(pointer, 0);
                        }
                    } else {
                        System.out.print((char) (int) memory.get(pointer));
                    }
                    break;

                case "OOO":
                    ensureMemorySize();
                    memory.set(pointer, 0);
                    break;

                case "MMM":
                    ensureMemorySize();
                    if (register == null) {
                        register = memory.get(pointer);
                    } else {
                        memory.set(pointer, register);
                        register = null;
                    }
                    break;
            }

            pc++;
        }
    }

    private void executeInstruction(String instr) {
        switch (instr) {
            case "MoO":
                ensureMemorySize();
                memory.set(pointer, memory.get(pointer) + 1);
                break;
            case "MOo":
                ensureMemorySize();
                memory.set(pointer, memory.get(pointer) - 1);
                break;
            case "moO":
                pointer++;
                ensureMemorySize();
                break;
            case "mOo":
                if (pointer > 0) {
                    pointer--;
                }
                break;
            case "OOM":
                ensureMemorySize();
                System.out.print(memory.get(pointer));
                break;
            case "oom":
                ensureMemorySize();
                try {
                    if (scanner.hasNextInt()) {
                        memory.set(pointer, scanner.nextInt());
                    } else {
                        memory.set(pointer, 0);
                    }
                } catch (Exception e) {
                    memory.set(pointer, 0);
                }
                break;
            case "Moo":
                ensureMemorySize();
                if (memory.get(pointer) == 0) {
                    try {
                        int input = System.in.read();
                        memory.set(pointer, input);
                    } catch (IOException e) {
                        memory.set(pointer, 0);
                    }
                } else {
                    System.out.print((char) (int) memory.get(pointer));
                }
                break;
            case "OOO":
                ensureMemorySize();
                memory.set(pointer, 0);
                break;
            case "MMM":
                ensureMemorySize();
                if (register == null) {
                    register = memory.get(pointer);
                } else {
                    memory.set(pointer, register);
                    register = null;
                }
                break;
        }
    }

    private void ensureMemorySize() {
        while (pointer >= memory.size()) {
            memory.add(0);
        }
    }

    public void close() {
        scanner.close();
    }
}
