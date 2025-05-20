package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.nfa.AutomatonOperations;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class CommandProcessor {
    private boolean running;
    private final Scanner scanner;
    private final Map<Command, Consumer<String[]>> commandMap;

    public CommandProcessor() {
        this.running = true;
        this.scanner = new Scanner(System.in);
        this.commandMap = new HashMap<>();
        initializeCommands();
    }

    private void initializeCommands() {
        commandMap.put(Command.LIST, args -> {
            List<Integer> automata = AutomatonManager.listAutomata();
            if (automata.isEmpty()) {
                System.out.println("No automata loaded");
            } else {
                System.out.println("Loaded automata: " + automata);
            }
        });

        commandMap.put(Command.PRINT, args -> {
            if (args.length < 2) {
                System.out.println("Usage: print <id>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            Nfa automaton = AutomatonManager.getAutomaton(id);
            System.out.println(automaton);
        });

        commandMap.put(Command.SAVE, args -> {
            if (args.length < 3) {
                System.out.println("Usage: save <id> <filename>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            String filename = args[2];
            try {
                FileOperations.saveAutomaton(id, filename);
                System.out.println("Automaton saved successfully");
            } catch (IOException e) {
                System.out.println("Error saving automaton: " + e.getMessage());
            }
        });

        commandMap.put(Command.SAVE_JSON, args -> {
            if (args.length < 3) {
                System.out.println("Usage: savejson <id> <filename>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            String filename = args[2];
            try {
                FileOperations.saveToJson(id, filename);
                System.out.println("Automaton saved to JSON successfully");
            } catch (IOException e) {
                System.out.println("Error saving automaton to JSON: " + e.getMessage());
            }
        });

        commandMap.put(Command.LOAD_JSON, args -> {
            if (args.length < 2) {
                System.out.println("Usage: loadjson <filename>");
                return;
            }
            String filename = args[1];
            try {
                int id = FileOperations.loadFromJson(filename);
                System.out.println("Automaton loaded with ID: " + id);
            } catch (IOException e) {
                System.out.println("Error loading automaton from JSON: " + e.getMessage());
            }
        });

        commandMap.put(Command.EMPTY, args -> {
            if (args.length < 2) {
                System.out.println("Usage: empty <id>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            Nfa automaton = AutomatonManager.getAutomaton(id);
            System.out.println("Empty language: " + automaton.isEmptyLanguage());
        });

        commandMap.put(Command.DETERMINISTIC, args -> {
            if (args.length < 2) {
                System.out.println("Usage: deterministic <id>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            Nfa automaton = AutomatonManager.getAutomaton(id);
            System.out.println("Is deterministic: " + automaton.isDeterministic());
        });

        commandMap.put(Command.RECOGNIZE, args -> {
            if (args.length < 3) {
                System.out.println("Usage: recognize <id> <word>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            String word = args[2];
            Nfa automaton = AutomatonManager.getAutomaton(id);
            System.out.println("Word recognized: " + automaton.recognizes(word));
        });

        commandMap.put(Command.UNION, args -> {
            if (args.length < 3) {
                System.out.println("Usage: union <id1> <id2>");
                return;
            }
            int id1 = Integer.parseInt(args[1]);
            int id2 = Integer.parseInt(args[2]);
            Nfa a1 = AutomatonManager.getAutomaton(id1);
            Nfa a2 = AutomatonManager.getAutomaton(id2);
            Nfa result = AutomatonOperations.union(a1, a2);
            int newId = AutomatonManager.addAutomaton(result);
            System.out.println("Union created with ID: " + newId);
        });

        commandMap.put(Command.CONCAT, args -> {
            if (args.length < 3) {
                System.out.println("Usage: concat <id1> <id2>");
                return;
            }
            int id1 = Integer.parseInt(args[1]);
            int id2 = Integer.parseInt(args[2]);
            Nfa a1 = AutomatonManager.getAutomaton(id1);
            Nfa a2 = AutomatonManager.getAutomaton(id2);
            Nfa result = AutomatonOperations.concat(a1, a2);
            int newId = AutomatonManager.addAutomaton(result);
            System.out.println("Concatenation created with ID: " + newId);
        });

        commandMap.put(Command.UN, args -> {
            if (args.length < 2) {
                System.out.println("Usage: un <id>");
                return;
            }
            int id = Integer.parseInt(args[1]);
            Nfa automaton = AutomatonManager.getAutomaton(id);
            Nfa result = AutomatonOperations.positiveClosure(automaton);
            int newId = AutomatonManager.addAutomaton(result);
            System.out.println("Positive closure created with ID: " + newId);
        });

        commandMap.put(Command.REG, args -> {
            if (args.length < 2) {
                System.out.println("Usage: reg <regex>");
                return;
            }
            String regex = args[1];
            Nfa result = AutomatonOperations.fromRegex(regex);
            int newId = AutomatonManager.addAutomaton(result);
            System.out.println("Automaton from regex created with ID: " + newId);
        });

        commandMap.put(Command.HELP, args -> {
            System.out.println("Available commands:");
            System.out.println("  list - List all loaded automata");
            System.out.println("  print <id> - Print automaton details");
            System.out.println("  save <id> <filename> - Save automaton to file");
            System.out.println("  savejson <id> <filename> - Save automaton to JSON file");
            System.out.println("  loadjson <filename> - Load automaton from JSON file");
            System.out.println("  empty <id> - Check if language is empty");
            System.out.println("  deterministic <id> - Check if automaton is deterministic");
            System.out.println("  recognize <id> <word> - Check if word is recognized");
            System.out.println("  union <id1> <id2> - Create union of two automata");
            System.out.println("  concat <id1> <id2> - Create concatenation of two automata");
            System.out.println("  un <id> - Create positive closure");
            System.out.println("  reg <regex> - Create automaton from regex");
            System.out.println("  exit - Exit program");
        });
    }

    public void processInput() {
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            String[] inputArgs = input.split("\\s+");
            Optional<Command> commandOpt = Command.fromString(inputArgs[0]);
            
            if (!commandOpt.isPresent()) {
                System.out.println("Unknown command. Type 'help' for available commands.");
                continue;
            }
            
            Command command = commandOpt.get();
            if (command == Command.EXIT) {
                running = false;
                continue;
            }
            
            try {
                Consumer<String[]> handler = commandMap.get(command);
                if (handler != null) {
                    handler.accept(inputArgs);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
} 