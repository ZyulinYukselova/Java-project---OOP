package bg.tu_varna.sit.b3.f23621743;

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
            }
            // Добави и други команди тук при нужда
        }
    }
}


