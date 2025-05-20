package bg.tu_varna.sit.b3.f23621743.nfa;

import java.util.*;

public class NfaOperations {
    public static Nfa union(Nfa a1, Nfa a2) {
        Set<String> states = new HashSet<>();
        states.addAll(a1.getStates());
        states.addAll(a2.getStates());
        
        String startState = "q0";
        states.add(startState);
        
        Set<String> acceptStates = new HashSet<>();
        acceptStates.addAll(a1.getAcceptStates());
        acceptStates.addAll(a2.getAcceptStates());
        
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
        transitions.putAll(a1.getTransitions());
        transitions.putAll(a2.getTransitions());
        
        Map<String, Set<String>> startTransitions = new HashMap<>();
        Set<String> epsilonTransitions = new HashSet<>();
        epsilonTransitions.add(a1.getStartState());
        epsilonTransitions.add(a2.getStartState());
        startTransitions.put("ε", epsilonTransitions);
        transitions.put(startState, startTransitions);
        
        return new Nfa(states, startState, acceptStates, transitions);
    }

    public static Nfa concat(Nfa a1, Nfa a2) {
        Set<String> states = new HashSet<>();
        states.addAll(a1.getStates());
        states.addAll(a2.getStates());
        
        String startState = a1.getStartState();
        Set<String> acceptStates = new HashSet<>(a2.getAcceptStates());
        
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
        transitions.putAll(a1.getTransitions());
        transitions.putAll(a2.getTransitions());
        
        for (String acceptState : a1.getAcceptStates()) {
            Map<String, Set<String>> stateTransitions = transitions.computeIfAbsent(acceptState, k -> new HashMap<>());
            Set<String> epsilonTransitions = stateTransitions.computeIfAbsent("ε", k -> new HashSet<>());
            epsilonTransitions.add(a2.getStartState());
        }
        
        return new Nfa(states, startState, acceptStates, transitions);
    }

    public static Nfa kleeneStar(Nfa a) {
        Set<String> states = new HashSet<>(a.getStates());
        String startState = "q0";
        states.add(startState);
        
        Set<String> acceptStates = new HashSet<>(a.getAcceptStates());
        acceptStates.add(startState);
        
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>(a.getTransitions());
        
        Map<String, Set<String>> startTransitions = new HashMap<>();
        Set<String> epsilonTransitions = new HashSet<>();
        epsilonTransitions.add(a.getStartState());
        startTransitions.put("ε", epsilonTransitions);
        transitions.put(startState, startTransitions);
        
        for (String acceptState : a.getAcceptStates()) {
            Map<String, Set<String>> stateTransitions = transitions.computeIfAbsent(acceptState, k -> new HashMap<>());
            Set<String> acceptEpsilonTransitions = stateTransitions.computeIfAbsent("ε", k -> new HashSet<>());
            acceptEpsilonTransitions.add(a.getStartState());
        }
        
        return new Nfa(states, startState, acceptStates, transitions);
    }
} 