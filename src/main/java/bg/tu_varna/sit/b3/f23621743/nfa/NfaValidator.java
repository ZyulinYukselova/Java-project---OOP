package bg.tu_varna.sit.b3.f23621743.nfa;

import java.util.*;

public class NfaValidator {
    public static boolean isDeterministic(Nfa nfa) {
        // Check for epsilon transitions
        for (Map<String, Set<String>> stateTransitions : nfa.getTransitions().values()) {
            if (stateTransitions.containsKey("ε")) {
                return false;
            }
            
            // Check for multiple transitions on same symbol
            for (Set<String> targets : stateTransitions.values()) {
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
        for (String state : nfa.getTransitions().keySet()) {
            if (!nfa.getStates().contains(state)) {
                return false;
            }
        }
        
        // Check if all target states in transitions exist in the state set
        for (Map<String, Set<String>> transitions : nfa.getTransitions().values()) {
            for (Set<String> targets : transitions.values()) {
                for (String target : targets) {
                    if (!nfa.getStates().contains(target)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    public static boolean hasEpsilonTransitions(Nfa nfa) {
        for (Map<String, Set<String>> transitions : nfa.getTransitions().values()) {
            if (transitions.containsKey("ε")) {
                return true;
            }
        }
        return false;
    }
} 