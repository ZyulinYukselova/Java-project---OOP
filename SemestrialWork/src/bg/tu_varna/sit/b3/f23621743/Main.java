package bg.tu_varna.sit.b3.f23621743;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\"Welcome to the NFA program! Type 'help' for a list of commands. Type 'exit' to quit.");
        while (true) {
            System.out.print(">> ");
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] inputArgs = line.trim().split("\\s+");
            String command = inputArgs[0].toLowerCase();
            if (command.equals("exit")) {
                System.out.println("Exiting...");
                break;
            }
            try {
                switch (command) {
                    case "list":
                        handleList();
                        break;
                    case "print":
                        if (inputArgs.length < 2) throw new IllegalArgumentException("Missing automaton ID");
                        handlePrint(Integer.parseInt(inputArgs[1]));
                        break;
                    case "save":
                        if (inputArgs.length < 3) throw new IllegalArgumentException("Missing automaton ID or filename");
                        handleSave(Integer.parseInt(inputArgs[1]), inputArgs[2]);
                        break;
                    case "empty":
                        if (inputArgs.length < 2) throw new IllegalArgumentException("Missing automaton ID");
                        handleEmpty(Integer.parseInt(inputArgs[1]));
                        break;
                    case "deterministic":
                        if (inputArgs.length < 2) throw new IllegalArgumentException("Missing automaton ID");
                        handleDeterministic(Integer.parseInt(inputArgs[1]));
                        break;
                    case "recognize":
                        if (inputArgs.length < 3) throw new IllegalArgumentException("Missing automaton ID or word");
                        handleRecognize(Integer.parseInt(inputArgs[1]), inputArgs[2]);
                        break;
                    case "union":
                        if (inputArgs.length < 3) throw new IllegalArgumentException("Missing automaton IDs");
                        handleUnion(Integer.parseInt(inputArgs[1]), Integer.parseInt(inputArgs[2]));
                        break;
                    case "concat":
                        if (inputArgs.length < 3) throw new IllegalArgumentException("Missing automaton IDs");
                        handleConcat(Integer.parseInt(inputArgs[1]), Integer.parseInt(inputArgs[2]));
                        break;
                    case "un":
                        if (inputArgs.length < 2) throw new IllegalArgumentException("Missing automaton ID");
                        handleUn(Integer.parseInt(inputArgs[1]));
                        break;
                    case "reg":
                        if (inputArgs.length < 2) throw new IllegalArgumentException("Missing regular expression");
                        handleReg(inputArgs[1]);
                        break;
                    case "help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        printHelp();
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  list");
        System.out.println("  print <id>");
        System.out.println("  save <id> <filename>");
        System.out.println("  empty <id>");
        System.out.println("  deterministic <id>");
        System.out.println("  recognize <id> <word>");
        System.out.println("  union <id1> <id2>");
        System.out.println("  concat <id1> <id2>");
        System.out.println("  un <id>");
        System.out.println("  reg <regex>");
        System.out.println("  help");
    }

    private static void handleList() {
        List<Integer> automata = AutomatonManager.listAutomata();
        if (automata.isEmpty()) {
            System.out.println("No automata loaded");
        } else {
            System.out.println("Loaded automata:");
            for (int id : automata) {
                System.out.println("  " + id);
            }
        }
    }

    private static void handlePrint(int id) {
        Nfa automaton = AutomatonManager.getAutomaton(id);
        System.out.println(automaton);
    }

    private static void handleSave(int id, String filename) throws IOException {
        AutomatonManager.saveAutomaton(id, filename);
        System.out.println("Automaton saved to " + filename);
    }

    private static void handleEmpty(int id) {
        Nfa automaton = AutomatonManager.getAutomaton(id);
        boolean isEmpty = automaton.isEmptyLanguage();
        System.out.println("Language is " + (isEmpty ? "empty" : "not empty"));
    }

    private static void handleDeterministic(int id) {
        Nfa automaton = AutomatonManager.getAutomaton(id);
        boolean isDeterministic = automaton.isDeterministic();
        System.out.println("Automaton is " + (isDeterministic ? "deterministic" : "nondeterministic"));
    }

    private static void handleRecognize(int id, String word) {
        Nfa automaton = AutomatonManager.getAutomaton(id);
        boolean recognizes = automaton.recognizes(word);
        System.out.println("Word is " + (recognizes ? "accepted" : "rejected"));
    }

    private static void handleUnion(int id1, int id2) {
        Nfa a1 = AutomatonManager.getAutomaton(id1);
        Nfa a2 = AutomatonManager.getAutomaton(id2);
        Nfa result = AutomatonOperations.union(a1, a2);
        int newId = AutomatonManager.addAutomaton(result);
        System.out.println("New automaton created with ID: " + newId);
    }

    private static void handleConcat(int id1, int id2) {
        Nfa a1 = AutomatonManager.getAutomaton(id1);
        Nfa a2 = AutomatonManager.getAutomaton(id2);
        Nfa result = AutomatonOperations.concat(a1, a2);
        int newId = AutomatonManager.addAutomaton(result);
        System.out.println("New automaton created with ID: " + newId);
    }

    private static void handleUn(int id) {
        Nfa automaton = AutomatonManager.getAutomaton(id);
        Nfa result = AutomatonOperations.positiveClosure(automaton);
        int newId = AutomatonManager.addAutomaton(result);
        System.out.println("New automaton created with ID: " + newId);
    }

    private static void handleReg(String regex) {
        Nfa result = AutomatonOperations.fromRegex(regex);
        int newId = AutomatonManager.addAutomaton(result);
        System.out.println("New automaton created with ID: " + newId);
    }
}


