package bg.tu_varna.sit.b3.f23621743.factory;

import bg.tu_varna.sit.b3.f23621743.Automaton;
import bg.tu_varna.sit.b3.f23621743.nfa.Nfa;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaBuilder;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaOperations;

public class AutomatonFactory {
    
    public static Automaton createEmptyNfa() {
        return new NfaBuilder()
            .addState(true)
            .setStartState(0)
            .build();
    }

    public static Automaton createSingleSymbolNfa(char symbol) {
        String symbolStr = String.valueOf(symbol);
        ValidationUtils.validateSymbol(symbolStr);
        return NfaBuilder.createBasicNfa(symbolStr).build();
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
            builder.addTransition(0, String.valueOf(c), 1);
        }

        return builder.setStartState(0).build();
    }

    public static Automaton createUnionNfa(Automaton a1, Automaton a2) {
        return NfaOperations.union((Nfa)a1, (Nfa)a2);
    }

    public static Automaton createConcatNfa(Automaton a1, Automaton a2) {
        return NfaOperations.concat((Nfa)a1, (Nfa)a2);
    }

    public static Automaton createKleeneStarNfa(Automaton a) {
        return NfaOperations.kleeneStar((Nfa)a);
    }
} 