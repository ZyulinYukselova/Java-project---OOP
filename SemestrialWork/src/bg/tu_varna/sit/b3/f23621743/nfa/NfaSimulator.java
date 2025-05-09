package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.State;

import java.util.*;

public class NfaSimulator {
    
    public static boolean recognizes(Nfa nfa, String word) {
        Set<State> currentStates = epsilonClosure(nfa, Set.of(nfa.getStartState()));
        
        for (char c : word.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                Set<State> targets = nfa.getTransitions()
                    .getOrDefault(state, new HashMap<>())
                    .get(c);
                if (targets != null) {
                    nextStates.addAll(targets);
                }
            }
            currentStates = epsilonClosure(nfa, nextStates);
        }
        
        return currentStates.stream().anyMatch(State::isFinal);
    }

    public static Set<State> epsilonClosure(Nfa nfa, Set<State> inputStates) {
        Set<State> closure = new HashSet<>(inputStates);
        Stack<State> stack = new Stack<>();
        stack.addAll(inputStates);

        while (!stack.isEmpty()) {
            State state = stack.pop();
            Set<State> epsilonStates = nfa.getTransitions()
                .getOrDefault(state, new HashMap<>())
                .getOrDefault(Nfa.EPSILON, new HashSet<>());
                
            for (State s : epsilonStates) {
                if (closure.add(s)) {
                    stack.push(s);
                }
            }
        }
        return closure;
    }

    public static boolean isEmptyLanguage(Nfa nfa) {
        Set<State> visited = new HashSet<>();
        Stack<State> stack = new Stack<>();
        stack.add(nfa.getStartState());

        while (!stack.isEmpty()) {
            State current = stack.pop();
            if (!visited.add(current)) continue;

            if (current.isFinal()) return false;

            Map<Character, Set<State>> nextMap = nfa.getTransitions()
                .getOrDefault(current, new HashMap<>());
            for (Set<State> neighbors : nextMap.values()) {
                stack.addAll(neighbors);
            }
        }

        return true;
    }
} 