package bg.tu_varna.sit.b3.f23621743;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AutomatonManager manager = new AutomatonManager();

        while (true) {
            System.out.print(">> ");
            String[] tokens = scanner.nextLine().split("\\s+");
            if (tokens.length == 0) continue;

            String command = tokens[0];

            if (command.equals("reg")) {
                String regex = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
                String id = manager.fromRegex(regex);
                System.out.println("NFA for regex '" + regex + "' created with ID: " + id);
            } else if (command.equals("list")) {
                for (String id : manager.listAutomata()) {
                    System.out.println(id);
                }
            } else if (command.equals("print")) {
                manager.print(tokens[1]);
            }else if (command.equals("determinize")) {
                if (tokens.length < 2) {
                    System.out.println("Usage: determinize <id>");
                    continue;
                }
                String newId = manager.determinize(tokens[1]);
                System.out.println("Deterministic automaton created with ID: " + newId);
            }else if (command.equals("finite")) {
                if (tokens.length < 2) {
                    System.out.println("Usage: finite <id>");
                    continue;
                }
                boolean finite = manager.isFinite(tokens[1]);
                System.out.println("Is finite: " + finite);
            }else if (command.equals("save")) {
                if (tokens.length < 3) {
                    System.out.println("Usage: save <id> <filename>");
                    continue;
                }
                try {
                    manager.save(tokens[1], tokens[2]);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            } else if (command.equals("open")) {
                if (tokens.length < 2) {
                    System.out.println("Usage: open <filename>");
                    continue;
                }
                try {
                    String id = manager.open(tokens[1]);
                    System.out.println("Automaton opened with ID: " + id);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error opening file: " + e.getMessage());
                }
            }else if (command.equals("saveAsText")) {
                if (tokens.length < 3) {
                    System.out.println("Usage: saveAsText <id> <filename>");
                    continue;
                }
                try {
                    manager.saveAsText(tokens[1], tokens[2]);
                } catch (IOException e) {
                    System.out.println("Error writing text file: " + e.getMessage());
                }
            }
            // Добавяш и други команди при нужда
        }
    }
}


