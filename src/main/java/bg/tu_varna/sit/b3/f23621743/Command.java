package bg.tu_varna.sit.b3.f23621743;

import java.util.Optional;

public enum Command {
    LIST("list"),
    PRINT("print"),
    SAVE("save"),
    SAVE_JSON("savejson"),
    LOAD_JSON("loadjson"),
    EMPTY("empty"),
    DETERMINISTIC("deterministic"),
    RECOGNIZE("recognize"),
    UNION("union"),
    CONCAT("concat"),
    UN("un"),
    REG("reg"),
    HELP("help"),
    EXIT("exit");

    private final String commandString;

    Command(String commandString) {
        this.commandString = commandString;
    }

    public static Optional<Command> fromString(String str) {
        for (Command cmd : Command.values()) {
            if (cmd.commandString.equalsIgnoreCase(str)) {
                return Optional.of(cmd);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return commandString;
    }
} 