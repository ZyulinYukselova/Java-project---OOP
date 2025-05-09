package bg.tu_varna.sit.b3.f23621743.visitor;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaSimulator;

public class EmptyLanguageVisitor implements AutomatonVisitor<Boolean> {
    @Override
    public Boolean visit(Nfa nfa) {
        return NfaSimulator.isEmptyLanguage(nfa);
    }
} 