package bg.tu_varna.sit.b3.f23621743.validation;

import bg.tu_varna.sit.b3.f23621743.Automaton;
import bg.tu_varna.sit.b3.f23621743.nfa.Nfa;

import java.util.Map;
import java.util.Set;

public class ValidationUtils {
    
    public static void validateNotNull(Object obj, String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }

    public static void validateNotEmpty(String str, String paramName) {
        validateNotNull(str, paramName);
        if (str.isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be empty");
        }
    }

    public static void validateStateExists(String state, Set<String> states, String paramName) {
        validateNotNull(state, paramName);
        if (!states.contains(state)) {
            throw new IllegalArgumentException(paramName + " does not exist in the automaton");
        }
    }

    public static void validateSymbol(String symbol) {
        if (symbol != null && !symbol.isEmpty() && !Character.isLetterOrDigit(symbol.charAt(0))) {
            throw new IllegalArgumentException("Symbol must be a letter or digit, got: " + symbol);
        }
    }

    public static void validateWord(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word cannot be null");
        }
    }

    public static void validateAutomaton(Automaton automaton) {
        if (automaton == null) {
            throw new IllegalArgumentException("Automaton cannot be null");
        }
    }

    public static void validateNfa(Nfa nfa) {
        validateNotNull(nfa, "NFA");
        validateNotNull(nfa.getStartState(), "Start state");
        validateNotNull(nfa.getStates(), "States set");
        validateNotNull(nfa.getTransitions(), "Transitions map");

        if (nfa.getStates().isEmpty()) {
            throw new IllegalArgumentException("NFA must have at least one state");
        }

        for (String state : nfa.getTransitions().keySet()) {
            validateStateExists(state, nfa.getStates(), "Transition source state");
        }

        for (Map<String, Set<String>> transitions : nfa.getTransitions().values()) {
            for (Set<String> targets : transitions.values()) {
                for (String target : targets) {
                    validateStateExists(target, nfa.getStates(), "Transition target state");
                }
            }
        }

        for (Map<String, Set<String>> transitions : nfa.getTransitions().values()) {
            for (String symbol : transitions.keySet()) {
                validateSymbol(symbol);
            }
        }
    }

    public static void validateRange(char start, char end) {
        if (start > end) {
            throw new IllegalArgumentException(
                String.format("Invalid range: start (%c) must be less than or equal to end (%c)", start, end)
            );
        }
        validateSymbol(String.valueOf(start));
        validateSymbol(String.valueOf(end));
    }

    public static void validateFileOperation(String filename, String operation) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty for " + operation);
        }
    }
} 