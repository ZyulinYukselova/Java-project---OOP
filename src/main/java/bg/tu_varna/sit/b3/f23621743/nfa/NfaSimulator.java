package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import java.util.*;

public class NfaSimulator {
    public static boolean recognizes(Nfa nfa, String word) {
        Set<String> currentStates = epsilonClosure(nfa, Collections.singleton(nfa.getStartState()));
        
        for (char c : word.toCharArray()) {
            currentStates = step(nfa, currentStates, String.valueOf(c));
            if (currentStates.isEmpty()) {
                return false;
            }
        }
        
        return !Collections.disjoint(currentStates, nfa.getAcceptStates());
    }
    
    public static boolean isEmptyLanguage(Nfa nfa) {
        Set<String> reachableStates = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(nfa.getStartState());
        reachableStates.add(nfa.getStartState());
        
        while (!queue.isEmpty()) {
            String state = queue.poll();
            Map<String, Set<String>> stateTransitions = nfa.getTransitions().get(state);
            
            if (stateTransitions != null) {
                for (Set<String> targets : stateTransitions.values()) {
                    for (String target : targets) {
                        if (!reachableStates.contains(target)) {
                            reachableStates.add(target);
                            queue.add(target);
                        }
                    }
                }
            }
        }
        
        return Collections.disjoint(reachableStates, nfa.getAcceptStates());
    }
    
    private static Set<String> step(Nfa nfa, Set<String> states, String symbol) {
        Set<String> result = new HashSet<>();
        
        for (String state : states) {
            Map<String, Set<String>> stateTransitions = nfa.getTransitions().get(state);
            if (stateTransitions != null && stateTransitions.containsKey(symbol)) {
                result.addAll(stateTransitions.get(symbol));
            }
        }
        
        return epsilonClosure(nfa, result);
    }
    
    private static Set<String> epsilonClosure(Nfa nfa, Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Queue<String> queue = new LinkedList<>(states);
        
        while (!queue.isEmpty()) {
            String state = queue.poll();
            Map<String, Set<String>> stateTransitions = nfa.getTransitions().get(state);
            
            if (stateTransitions != null && stateTransitions.containsKey("ε")) {
                for (String target : stateTransitions.get("ε")) {
                    if (!closure.contains(target)) {
                        closure.add(target);
                        queue.add(target);
                    }
                }
            }
        }
        
        return closure;
    }
} 