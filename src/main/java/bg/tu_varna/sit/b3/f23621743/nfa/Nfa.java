package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Automaton;
import bg.tu_varna.sit.b3.f23621743.visitor.AutomatonVisitor;
import java.util.*;

public class Nfa implements Automaton {
    private final Set<String> states;
    private final String startState;
    private final Set<String> acceptStates;
    private final Map<String, Map<String, Set<String>>> transitions;

    public Nfa(Set<String> states, String startState, Set<String> acceptStates,
               Map<String, Map<String, Set<String>>> transitions) {
        this.states = new HashSet<>(states);
        this.startState = startState;
        this.acceptStates = new HashSet<>(acceptStates);
        this.transitions = new HashMap<>(transitions);
    }

    public Set<String> getStates() {
        return new HashSet<>(states);
    }

    public String getStartState() {
        return startState;
    }

    public Set<String> getAcceptStates() {
        return new HashSet<>(acceptStates);
    }

    public Map<String, Map<String, Set<String>>> getTransitions() {
        return new HashMap<>(transitions);
    }

    @Override
    public boolean isDeterministic() {
        return NfaValidator.isDeterministic(this);
    }

    @Override
    public boolean recognizes(String word) {
        return NfaSimulator.recognizes(this, word);
    }

    @Override
    public boolean isEmptyLanguage() {
        return NfaSimulator.isEmptyLanguage(this);
    }

    @Override
    public <T> T accept(AutomatonVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static Nfa union(Nfa a1, Nfa a2) {
        return AutomatonOperations.union(a1, a2);
    }

    public static Nfa concat(Nfa a1, Nfa a2) {
        return AutomatonOperations.concat(a1, a2);
    }

    public static Nfa kleeneStar(Nfa a) {
        return AutomatonOperations.kleeneStar(a);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("States: ").append(states).append("\n");
        sb.append("Start state: ").append(startState).append("\n");
        sb.append("Accept states: ").append(acceptStates).append("\n");
        sb.append("Transitions:\n");
        
        for (Map.Entry<String, Map<String, Set<String>>> stateEntry : transitions.entrySet()) {
            String fromState = stateEntry.getKey();
            for (Map.Entry<String, Set<String>> symbolEntry : stateEntry.getValue().entrySet()) {
                String symbol = symbolEntry.getKey();
                Set<String> toStates = symbolEntry.getValue();
                sb.append(String.format("  %s --%s--> %s\n", fromState, symbol, toStates));
            }
        }
        
        return sb.toString();
    }
} 