package bg.tu_varna.sit.b3.f23621743.visitor;

import bg.tu_varna.sit.b3.f23621743.Automaton;

public class DeterministicVisitor implements AutomatonVisitor<Boolean> {
    @Override
    public Boolean visit(Automaton automaton) {
        return automaton.isDeterministic();
    }
} 