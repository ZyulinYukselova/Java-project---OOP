package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.State;

import java.util.Map;
import java.util.Set;

public class NfaValidator {
    
    public static boolean isDeterministic(Nfa nfa) {
        for (Map<Character, Set<State>> map : nfa.getTransitions().values()) {
            for (Set<State> targets : map.values()) {
                if (targets.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValid(Nfa nfa) {
        if (nfa == null) return false;
        if (nfa.getStartState() == null) return false;
        if (nfa.getStates().isEmpty()) return false;
        
        // Check if all states in transitions exist in the state set
        for (State state : nfa.getTransitions().keySet()) {
            if (!nfa.getStates().contains(state)) {
                return false;
            }
        }
        
        // Check if all target states in transitions exist in the state set
        for (Map<Character, Set<State>> transitions : nfa.getTransitions().values()) {
            for (Set<State> targets : transitions.values()) {
                for (State target : targets) {
                    if (!nfa.getStates().contains(target)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    public static boolean hasEpsilonTransitions(Nfa nfa) {
        for (Map<Character, Set<State>> transitions : nfa.getTransitions().values()) {
            if (transitions.containsKey(Nfa.EPSILON)) {
                return true;
            }
        }
        return false;
    }
} 