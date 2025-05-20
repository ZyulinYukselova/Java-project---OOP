package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.factory.AutomatonFactory;
import bg.tu_varna.sit.b3.f23621743.nfa.Nfa;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaOperations;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaValidator;
import bg.tu_varna.sit.b3.f23621743.nfa.RegexParser;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import bg.tu_varna.sit.b3.f23621743.visitor.DeterministicVisitor;
import bg.tu_varna.sit.b3.f23621743.visitor.EmptyLanguageVisitor;

import java.util.*;
import java.util.stream.Collectors;

public class AutomatonManager {
    private static final Map<String, Nfa> loadedAutomata = new HashMap<>();
    private static int nextId = 1;

    public static List<String> listAutomata() {
        return new ArrayList<>(loadedAutomata.keySet());
    }

    public static Nfa getAutomaton(String id) {
        Nfa automaton = loadedAutomata.get(id);
        if (automaton == null) {
            throw new IllegalArgumentException("Automaton with ID " + id + " not found");
        }
        return automaton;
    }

    public static void closeAutomaton(String id) {
        if (!loadedAutomata.containsKey(id)) {
            throw new IllegalArgumentException("Automaton with ID " + id + " not found");
        }
        loadedAutomata.remove(id);
    }

    public static String addAutomaton(Nfa automaton) {
        String id = "A" + nextId++;
        loadedAutomata.put(id, automaton);
        return id;
    }

    public static String fromRegex(String regex) {
        Nfa nfa = RegexParser.parse(regex);
        return addAutomaton(nfa);
    }

    public String addAutomatonWithValidation(Automaton automaton) {
        ValidationUtils.validateAutomaton(automaton);
        if (!(automaton instanceof Nfa)) {
            throw new IllegalArgumentException("Only NFA automata are supported");
        }
        return addAutomaton((Nfa) automaton);
    }

    public Nfa getAutomatonById(String id) {
        ValidationUtils.validateNotEmpty(id, "Automaton ID");
        if (!id.startsWith("A")) {
            throw new IllegalArgumentException("Invalid automaton ID format: " + id);
        }
        Nfa automaton = loadedAutomata.get(id);
        if (automaton == null) {
            throw new IllegalArgumentException("No automaton found with ID: " + id);
        }
        return automaton;
    }

    public boolean recognizes(String id, String word) {
        ValidationUtils.validateWord(word);
        Nfa automaton = getAutomatonById(id);
        return automaton.recognizes(word);
    }

    public boolean isEmpty(String id) {
        Nfa automaton = getAutomatonById(id);
        return automaton.accept(new EmptyLanguageVisitor());
    }

    public boolean isDeterministic(String id) {
        Nfa automaton = getAutomatonById(id);
        return automaton.accept(new DeterministicVisitor());
    }

    public String union(String id1, String id2) {
        Nfa a1 = getAutomatonById(id1);
        Nfa a2 = getAutomatonById(id2);
        Nfa result = Nfa.union(a1, a2);
        return addAutomaton(result);
    }

    public String concat(String id1, String id2) {
        Nfa a1 = getAutomatonById(id1);
        Nfa a2 = getAutomatonById(id2);
        Nfa result = Nfa.concat(a1, a2);
        return addAutomaton(result);
    }

    public String kleeneStar(String id) {
        Nfa automaton = getAutomatonById(id);
        Nfa result = Nfa.kleeneStar(automaton);
        return addAutomaton(result);
    }

    public void print(String id) {
        Nfa automaton = getAutomatonById(id);
        System.out.println(automaton);
    }
}
