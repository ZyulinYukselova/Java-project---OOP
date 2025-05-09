package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.State;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

public class NfaBuilder {
    private final Nfa nfa;
    private final Map<Integer, State> stateMap;
    private static int stateCounter = 0;

    public NfaBuilder() {
        this.nfa = new Nfa();
        this.stateMap = new HashMap<>();
    }

    public NfaBuilder addState(boolean isFinal) {
        State state = new State(stateCounter++, isFinal);
        stateMap.put(state.getId(), state);
        nfa.addState(state);
        return this;
    }

    public NfaBuilder setStartState(int stateId) {
        State state = stateMap.get(stateId);
        ValidationUtils.validateNotNull(state, "State with ID " + stateId);
        nfa.setStartState(state);
        return this;
    }

    public NfaBuilder addTransition(int fromId, Character symbol, int toId) {
        State from = stateMap.get(fromId);
        State to = stateMap.get(toId);
        ValidationUtils.validateNotNull(from, "Source state");
        ValidationUtils.validateNotNull(to, "Target state");
        ValidationUtils.validateSymbol(symbol);
        nfa.addTransition(from, symbol, to);
        return this;
    }

    public NfaBuilder addEpsilonTransition(int fromId, int toId) {
        return addTransition(fromId, Nfa.EPSILON, toId);
    }

    public Nfa build() {
        ValidationUtils.validateNotNull(nfa.getStartState(), "Start state");
        ValidationUtils.validateNfa(nfa);
        return nfa;
    }

    // Helper methods for common NFA patterns
    public static NfaBuilder createBasicNfa(char symbol) {
        ValidationUtils.validateSymbol(symbol);
        NfaBuilder builder = new NfaBuilder();
        builder.addState(false)  // Start state
               .addState(true)   // Final state
               .setStartState(0)
               .addTransition(0, symbol, 1);
        return builder;
    }

    public static NfaBuilder createEpsilonNfa() {
        NfaBuilder builder = new NfaBuilder();
        builder.addState(false)  // Start state
               .addState(true)   // Final state
               .setStartState(0)
               .addEpsilonTransition(0, 1);
        return builder;
    }
} 