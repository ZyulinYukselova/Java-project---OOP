package bg.tu_varna.sit.b3.f23621743.visitor;

import bg.tu_varna.sit.b3.f23621743.Automaton;
import bg.tu_varna.sit.b3.f23621743.Nfa;

public interface AutomatonVisitor<T> {
    T visit(Nfa nfa);
} 