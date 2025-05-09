package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.nfa.NfaOperations;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaSimulator;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaValidator;
import bg.tu_varna.sit.b3.f23621743.visitor.AutomatonVisitor;

import java.io.Serializable;
import java.util.*;

public class Nfa implements Automaton, Serializable {
    public static final Character EPSILON = null;

    private Set<Character> alphabet;
    private Set<State> states;
    private Map<State, Map<Character, Set<State>>> transitions;
    private State startState;

    public Nfa() {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    @Override
    public void addState(State state) {
        states.add(state);
        transitions.putIfAbsent(state, new HashMap<>());
    }

    @Override
    public void setStartState(State state) {
        this.startState = state;
    }

    @Override
    public State getStartState() {
        return startState;
    }

    @Override
    public Set<State> getStates() {
        return states;
    }

    @Override
    public Map<State, Map<Character, Set<State>>> getTransitions() {
        return transitions;
    }

    @Override
    public void addTransition(State from, Character symbol, State to) {
        if(symbol != EPSILON) {
            alphabet.add(symbol);
        }
        transitions.putIfAbsent(from, new HashMap<>());
        transitions.get(from).putIfAbsent(symbol, new HashSet<>());
        transitions.get(from).get(symbol).add(to);
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
        return NfaOperations.union(a1, a2);
    }

    public static Nfa concat(Nfa a1, Nfa a2) {
        return NfaOperations.concat(a1, a2);
    }

    public static Nfa kleeneStar(Nfa a) {
        return NfaOperations.kleeneStar(a);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NFA with ").append(states.size()).append(" states\n");
        sb.append("Alphabet: ").append(alphabet).append("\n");
        sb.append("Start state: ").append(startState).append("\n");
        sb.append("Transitions:\n");
        
        for (var entry : transitions.entrySet()) {
            State from = entry.getKey();
            for (var t : entry.getValue().entrySet()) {
                Character symbol = t.getKey();
                for (State to : t.getValue()) {
                    sb.append(String.format("  %s --%s--> %s\n", 
                        from, 
                        symbol == EPSILON ? "ε" : symbol, 
                        to));
                }
            }
        }
        
        return sb.toString();
    }
}


