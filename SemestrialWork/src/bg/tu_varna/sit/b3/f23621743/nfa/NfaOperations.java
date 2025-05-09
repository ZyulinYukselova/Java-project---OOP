package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class    NfaOperations {
    private static int stateCounter = 0;

    public static Nfa union(Nfa a1, Nfa a2) {
        Nfa result = new Nfa();
        Map<State, State> mappingA1 = new HashMap<>();
        Map<State, State> mappingA2 = new HashMap<>();

        // Copy states from a1
        for (State s : a1.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA1.put(s, copy);
        }

        // Copy states from a2
        for (State s : a2.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA2.put(s, copy);
        }

        // Copy transitions from a1
        copyTransitions(a1, result, mappingA1);

        // Copy transitions from a2
        copyTransitions(a2, result, mappingA2);

        // Create new start state with epsilon transitions
        State newStart = new State(stateCounter++, false);
        result.addState(newStart);
        result.setStartState(newStart);
        result.addTransition(newStart, Nfa.EPSILON, mappingA1.get(a1.getStartState()));
        result.addTransition(newStart, Nfa.EPSILON, mappingA2.get(a2.getStartState()));

        return result;
    }

    public static Nfa concat(Nfa a1, Nfa a2) {
        Nfa result = new Nfa();
        Map<State, State> mappingA1 = new HashMap<>();
        Map<State, State> mappingA2 = new HashMap<>();

        // Copy states from a1 (non-final)
        for (State s : a1.getStates()) {
            State copy = new State(stateCounter++, false);
            result.addState(copy);
            mappingA1.put(s, copy);
        }

        // Copy states from a2
        for (State s : a2.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA2.put(s, copy);
        }

        // Copy transitions
        copyTransitions(a1, result, mappingA1);
        copyTransitions(a2, result, mappingA2);

        // Add epsilon transitions from final states of a1 to start of a2
        for (State s : a1.getStates()) {
            if (s.isFinal()) {
                result.addTransition(mappingA1.get(s), Nfa.EPSILON, mappingA2.get(a2.getStartState()));
            }
        }

        // Set start state
        result.setStartState(mappingA1.get(a1.getStartState()));

        return result;
    }

    public static Nfa kleeneStar(Nfa a) {
        Nfa result = new Nfa();
        Map<State, State> mapping = new HashMap<>();

        // Copy states
        for (State s : a.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mapping.put(s, copy);
        }

        // Copy transitions
        copyTransitions(a, result, mapping);

        // Create new start state (which is also final)
        State newStart = new State(stateCounter++, true);
        result.addState(newStart);
        result.setStartState(newStart);

        // Add epsilon transitions from new start to old start
        result.addTransition(newStart, Nfa.EPSILON, mapping.get(a.getStartState()));

        // Add epsilon transitions from final states to old start
        for (State s : a.getStates()) {
            if (s.isFinal()) {
                result.addTransition(mapping.get(s), Nfa.EPSILON, mapping.get(a.getStartState()));
            }
        }

        return result;
    }

    private static void copyTransitions(Nfa source, Nfa target, Map<State, State> mapping) {
        for (var entry : source.getTransitions().entrySet()) {
            State from = mapping.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    target.addTransition(from, t.getKey(), mapping.get(to));
                }
            }
        }
    }
} 