package bg.tu_varna.sit.b3.f23621743;

import java.util.Objects;

public class State {
    private final int id;
    private boolean isFinal;

    public State(int id, boolean isFinal) {
        this.id = id;
        this.isFinal = isFinal;
    }

    public int getId() {
        return id;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return id == state.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "State " + id + (isFinal ? " (final)" : "");
    }
}

