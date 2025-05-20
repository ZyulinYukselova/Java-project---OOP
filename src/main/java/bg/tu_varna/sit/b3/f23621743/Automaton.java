package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.visitor.AutomatonVisitor;

public interface Automaton {
    boolean isDeterministic();
    boolean recognizes(String word);
    boolean isEmptyLanguage();
    <T> T accept(AutomatonVisitor<T> visitor);
}
