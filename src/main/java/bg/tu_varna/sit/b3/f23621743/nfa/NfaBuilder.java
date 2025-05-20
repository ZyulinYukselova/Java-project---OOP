package bg.tu_varna.sit.b3.f23621743.nfa;

import java.util.*;

public class NfaBuilder {
    private final Set<String> states;
    private String startState;
    private final Set<String> acceptStates;
    private final Map<String, Map<String, Set<String>>> transitions;
    private int stateCounter;

    public NfaBuilder() {
        this.states = new HashSet<>();
        this.acceptStates = new HashSet<>();
        this.transitions = new HashMap<>();
        this.stateCounter = 0;
    }

    public NfaBuilder addState(boolean isFinal) {
        String state = "q" + stateCounter++;
        states.add(state);
        if (isFinal) {
            acceptStates.add(state);
        }
        return this;
    }

    public NfaBuilder setStartState(int stateId) {
        this.startState = "q" + stateId;
        return this;
    }

    public NfaBuilder addTransition(int fromId, String symbol, int toId) {
        String from = "q" + fromId;
        String to = "q" + toId;
        
        transitions.computeIfAbsent(from, k -> new HashMap<>())
                  .computeIfAbsent(symbol, k -> new HashSet<>())
                  .add(to);
        return this;
    }

    public NfaBuilder addEpsilonTransition(int fromId, int toId) {
        return addTransition(fromId, "ε", toId);
    }

    public static NfaBuilder createBasicNfa(String symbol) {
        return new NfaBuilder()
            .addState(false)  // q0
            .addState(true)   // q1
            .setStartState(0)
            .addTransition(0, symbol, 1);
    }

    public static NfaBuilder createEpsilonNfa() {
        return new NfaBuilder()
            .addState(false)  // q0
            .addState(true)   // q1
            .setStartState(0)
            .addEpsilonTransition(0, 1);
    }

    public Nfa build() {
        return new Nfa(states, startState, acceptStates, transitions);
    }
} 