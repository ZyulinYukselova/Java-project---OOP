package bg.tu_varna.sit.b3.f23621743;

import java.util.*;

public class Nfa implements Automaton {
    public static final Character EPSILON = null;

    private Set<Character> alphabet;
    private Set<State> states;
    private Map<State, Map<Character, Set<State>>> transitions;
    private State startState;
    private static int stateCounter = 0;

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
        for (Map<Character, Set<State>> map : transitions.values()) {
            for (Set<State> targets : map.values()) {
                if (targets.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<State> epsilonClosure(Set<State> inputStates) {
        Set<State> closure = new HashSet<>(inputStates);
        Stack<State> stack = new Stack<>();
        stack.addAll(inputStates);

        while (!stack.isEmpty()) {
            State state = stack.pop();
            Set<State> epsilonStates = transitions.getOrDefault(state, new HashMap<>())
                    .getOrDefault(EPSILON, new HashSet<>());
            for (State s : epsilonStates) {
                if (closure.add(s)) {
                    stack.push(s);
                }
            }
        }
        return closure;
    }

    @Override
    public boolean recognizes(String word) {
        Set<State> currentStates = epsilonClosure(Set.of(startState));
        for (char c : word.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                Set<State> targets = transitions.getOrDefault(state, new HashMap<>()).get(c);
                if (targets != null) {
                    nextStates.addAll(targets);
                }
            }
            currentStates = epsilonClosure(nextStates);
        }
        for (State state : currentStates) {
            if (state.isFinal()) return true;
        }
        return false;
    }

    @Override
    public boolean isEmptyLanguage() {
        Set<State> visited = new HashSet<>();
        Stack<State> stack = new Stack<>();
        stack.add(startState);

        while (!stack.isEmpty()) {
            State current = stack.pop();
            if (!visited.add(current)) continue;

            if (current.isFinal()) return false;

            Map<Character, Set<State>> nextMap = transitions.getOrDefault(current, new HashMap<>());
            for (Set<State> neighbors : nextMap.values()) {
                stack.addAll(neighbors);
            }
        }

        return true;
    }

    public static Nfa union(Nfa a1, Nfa a2) {
        Nfa result = new Nfa();

        Map<State, State> mappingA1 = new HashMap<>();
        Map<State, State> mappingA2 = new HashMap<>();

        for (State s : a1.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA1.put(s, copy);
        }

        for (State s : a2.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA2.put(s, copy);
        }

        for (var entry : a1.getTransitions().entrySet()) {
            State from = mappingA1.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA1.get(to));
                }
            }
        }

        for (var entry : a2.getTransitions().entrySet()) {
            State from = mappingA2.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA2.get(to));
                }
            }
        }

        State newStart = new State(stateCounter++, false);
        result.addState(newStart);
        result.setStartState(newStart);
        result.addTransition(newStart, EPSILON, mappingA1.get(a1.getStartState()));
        result.addTransition(newStart, EPSILON, mappingA2.get(a2.getStartState()));

        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("States: ").append(states).append("\n");
        sb.append("Alphabet: ").append(alphabet).append("\n");
        sb.append("Transitions:\n");
        for (Map.Entry<State, Map<Character, Set<State>>> entry : transitions.entrySet()) {
            for (Map.Entry<Character, Set<State>> innerEntry : entry.getValue().entrySet()) {
                String symbol = innerEntry.getKey() == null ? "ε" : innerEntry.getKey().toString();
                sb.append(entry.getKey()).append(" --").append(innerEntry.getKey()).append("--> ").append(innerEntry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}

