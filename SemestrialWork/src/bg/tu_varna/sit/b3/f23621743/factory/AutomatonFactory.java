package bg.tu_varna.sit.b3.f23621743.factory;

import bg.tu_varna.sit.b3.f23621743.Automaton;
import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaBuilder;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;

public class AutomatonFactory {
    
    public static Automaton createEmptyNfa() {
        return new NfaBuilder()
            .addState(true)
            .setStartState(0)
            .build();
    }

    public static Automaton createSingleSymbolNfa(char symbol) {
        ValidationUtils.validateSymbol(symbol);
        return NfaBuilder.createBasicNfa(symbol).build();
    }

    public static Automaton createEpsilonNfa() {
        return NfaBuilder.createEpsilonNfa().build();
    }

    public static Automaton createRangeNfa(char start, char end) {
        ValidationUtils.validateRange(start, end);

        NfaBuilder builder = new NfaBuilder();
        builder.addState(false)  // Start state
               .addState(true);  // Final state

        for (char c = start; c <= end; c++) {
            builder.addTransition(0, c, 1);
        }

        return builder.setStartState(0).build();
    }

    public static Automaton createUnionNfa(Automaton a1, Automaton a2) {
        ValidationUtils.validateAutomaton(a1);
        ValidationUtils.validateAutomaton(a2);
        
        if (!(a1 instanceof Nfa) || !(a2 instanceof Nfa)) {
            throw new IllegalArgumentException("Both automata must be NFAs");
        }
        return Nfa.union((Nfa) a1, (Nfa) a2);
    }

    public static Automaton createConcatNfa(Automaton a1, Automaton a2) {
        ValidationUtils.validateAutomaton(a1);
        ValidationUtils.validateAutomaton(a2);
        
        if (!(a1 instanceof Nfa) || !(a2 instanceof Nfa)) {
            throw new IllegalArgumentException("Both automata must be NFAs");
        }
        return Nfa.concat((Nfa) a1, (Nfa) a2);
    }

    public static Automaton createKleeneStarNfa(Automaton a) {
        ValidationUtils.validateAutomaton(a);
        
        if (!(a instanceof Nfa)) {
            throw new IllegalArgumentException("Automaton must be an NFA");
        }
        return Nfa.kleeneStar((Nfa) a);
    }
} 