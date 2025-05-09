package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.visitor.AutomatonVisitor;

import java.util.Map;
import java.util.Set;

public interface Automaton {
    void addState(State state);
    void setStartState(State state);
    void addTransition(State from, Character symbol, State to);
    boolean recognizes(String word);
    boolean isDeterministic();
    boolean isEmptyLanguage();
    Set<State> getStates();
    State getStartState();
    Map<State, Map<Character, Set<State>>> getTransitions();
    
    // Visitor pattern support
    <T> T accept(AutomatonVisitor<T> visitor);
}
