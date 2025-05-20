package bg.tu_varna.sit.b3.f23621743.nfa;

import java.util.*;

public class AutomatonOperations {
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
        // Deep copy transitions from a1
        for (Map.Entry<String, Map<String, Set<String>>> entry : a1.getTransitions().entrySet()) {
            transitions.put(entry.getKey(), new HashMap<>());
            for (Map.Entry<String, Set<String>> symbolEntry : entry.getValue().entrySet()) {
                transitions.get(entry.getKey()).put(symbolEntry.getKey(), new HashSet<>(symbolEntry.getValue()));
            }
        }
        // Deep copy transitions from a2
        for (Map.Entry<String, Map<String, Set<String>>> entry : a2.getTransitions().entrySet()) {
            transitions.computeIfAbsent(entry.getKey(), k -> new HashMap<>());
            for (Map.Entry<String, Set<String>> symbolEntry : entry.getValue().entrySet()) {
                transitions.get(entry.getKey())
                    .computeIfAbsent(symbolEntry.getKey(), k -> new HashSet<>())
                    .addAll(symbolEntry.getValue());
            }
        }
        
        // Add epsilon transitions from new start state
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
        // Deep copy transitions from a1
        for (Map.Entry<String, Map<String, Set<String>>> entry : a1.getTransitions().entrySet()) {
            transitions.put(entry.getKey(), new HashMap<>());
            for (Map.Entry<String, Set<String>> symbolEntry : entry.getValue().entrySet()) {
                transitions.get(entry.getKey()).put(symbolEntry.getKey(), new HashSet<>(symbolEntry.getValue()));
            }
        }
        // Deep copy transitions from a2
        for (Map.Entry<String, Map<String, Set<String>>> entry : a2.getTransitions().entrySet()) {
            transitions.computeIfAbsent(entry.getKey(), k -> new HashMap<>());
            for (Map.Entry<String, Set<String>> symbolEntry : entry.getValue().entrySet()) {
                transitions.get(entry.getKey())
                    .computeIfAbsent(symbolEntry.getKey(), k -> new HashSet<>())
                    .addAll(symbolEntry.getValue());
            }
        }
        
        // Add epsilon transitions from a1's accept states to a2's start state
        for (String acceptState : a1.getAcceptStates()) {
            transitions.computeIfAbsent(acceptState, k -> new HashMap<>())
                .computeIfAbsent("ε", k -> new HashSet<>())
                .add(a2.getStartState());
        }
        
        return new Nfa(states, startState, acceptStates, transitions);
    }

    public static Nfa kleeneStar(Nfa a) {
        Set<String> states = new HashSet<>(a.getStates());
        String startState = "q0";
        states.add(startState);
        
        Set<String> acceptStates = new HashSet<>(a.getAcceptStates());
        acceptStates.add(startState);
        
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
        // Deep copy original transitions
        for (Map.Entry<String, Map<String, Set<String>>> entry : a.getTransitions().entrySet()) {
            transitions.put(entry.getKey(), new HashMap<>());
            for (Map.Entry<String, Set<String>> symbolEntry : entry.getValue().entrySet()) {
                transitions.get(entry.getKey()).put(symbolEntry.getKey(), new HashSet<>(symbolEntry.getValue()));
            }
        }
        
        // Add new start state transitions
        Map<String, Set<String>> startTransitions = new HashMap<>();
        Set<String> epsilonTransitions = new HashSet<>();
        epsilonTransitions.add(a.getStartState());
        startTransitions.put("ε", epsilonTransitions);
        transitions.put(startState, startTransitions);
        
        // Add transitions from accept states back to start state
        for (String acceptState : a.getAcceptStates()) {
            transitions.computeIfAbsent(acceptState, k -> new HashMap<>())
                .computeIfAbsent("ε", k -> new HashSet<>())
                .add(a.getStartState());
        }
        
        return new Nfa(states, startState, acceptStates, transitions);
    }

    public static Nfa fromRegex(String regex) {
        return RegexParser.parse(regex);
    }
} 