package bg.tu_varna.sit.b3.f23621743;

import java.util.Objects;
import java.io.Serializable;


public class State  implements  Serializable{
    private final int id;
    private final boolean isFinal;

    public State(int id, boolean isFinal) {
        if (id < 0) throw new IllegalArgumentException("State ID must be non-negative");
        this.id = id;
        this.isFinal = isFinal;
    }

    public int getId() {
        return id;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "q" + id + (isFinal ? "(F)" : "");
    }
}

