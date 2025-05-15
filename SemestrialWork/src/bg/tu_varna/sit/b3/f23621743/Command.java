package bg.tu_varna.sit.b3.f23621743;

import java.util.Arrays;
import java.util.Optional;

public enum Command {
    LIST("list"),
    PRINT("print"),
    SAVE("save"),
    EMPTY("empty"),
    DETERMINISTIC("deterministic"),
    RECOGNIZE("recognize"),
    UNION("union"),
    CONCAT("concat"),
    UN("un"),
    REG("reg"),
    HELP("help"),
    EXIT("exit");

    private final String text;

    Command(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Optional<Command> fromString(String text) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.text.equalsIgnoreCase(text))
                .findFirst();
    }
} 