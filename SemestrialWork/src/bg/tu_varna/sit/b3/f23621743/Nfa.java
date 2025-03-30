package bg.tu_varna.sit.b3.f23621743;

import java.util.*;

public class Nfa {
    private Set<Character> alphabet;
    private Set<State> states;
    private Map<State, Map<Character, Set<State>>> transitions;
    private State startState;

    public Nfa() {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public void addState(State state) {
        states.add(state);
        transitions.putIfAbsent(state, new HashMap<>());
    }

    public void setStartState(State state) {
        this.startState = state;
    }

    public void addTransition(State from, char symbol, State to) {
        alphabet.add(symbol);
        transitions.putIfAbsent(from, new HashMap<>());
        transitions.get(from).putIfAbsent(symbol, new HashSet<>());
        transitions.get(from).get(symbol).add(to);
    }

    public boolean isDeterministic() {
        for (Map<Character, Set<State>> map : transitions.values()) {
            for (Set<State> targets : map.values()) {
                if (targets.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean recognizes(String word) {
        Set<State> currentStates = new HashSet<>();
        currentStates.add(startState);
        for (char c : word.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                if (transitions.containsKey(state) && transitions.get(state).containsKey(c)) {
                    nextStates.addAll(transitions.get(state).get(c));
                }
            }
            if (nextStates.isEmpty()) return false;
            currentStates = nextStates;
        }
        for (State state : currentStates) {
            if (state.isFinal()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("States: ").append(states).append("\n");
        sb.append("Alphabet: ").append(alphabet).append("\n");
        sb.append("Transitions:\n");
        for (Map.Entry<State, Map<Character, Set<State>>> entry : transitions.entrySet()) {
            for (Map.Entry<Character, Set<State>> innerEntry : entry.getValue().entrySet()) {
                sb.append(entry.getKey()).append(" --").append(innerEntry.getKey()).append("--> ").append(innerEntry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}

