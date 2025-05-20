package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.factory.AutomatonFactory;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaOperations;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaValidator;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import bg.tu_varna.sit.b3.f23621743.visitor.DeterministicVisitor;
import bg.tu_varna.sit.b3.f23621743.visitor.EmptyLanguageVisitor;

import java.util.*;
import java.util.stream.Collectors;

public class AutomatonManager {
    private static final Map<Integer, Nfa> loadedAutomata = new HashMap<>();
    private static int nextId = 1;

    public static List<Integer> listAutomata() {
        return new ArrayList<>(loadedAutomata.keySet());
    }

    public static Nfa getAutomaton(int id) {
        Nfa automaton = loadedAutomata.get(id);
        if (automaton == null) {
            throw new IllegalArgumentException("Automaton with ID " + id + " not found");
        }
        return automaton;
    }

    public static void closeAutomaton(int id) {
        if (!loadedAutomata.containsKey(id)) {
            throw new IllegalArgumentException("Automaton with ID " + id + " not found");
        }
        loadedAutomata.remove(id);
    }

    public static int addAutomaton(Nfa automaton) {
        int id = nextId++;
        loadedAutomata.put(id, automaton);
        return id;
    }

    public static int fromRegex(String regex) {
        Nfa nfa = RegexParser.parse(regex);
        return addAutomaton(nfa);
    }

    public String addAutomaton(Automaton automaton) {
        ValidationUtils.validateAutomaton(automaton);
        String id = "A" + nextId++;
        loadedAutomata.put(nextId - 1, (Nfa) automaton);
        return id;
    }

    public Automaton getAutomaton(String id) {
        ValidationUtils.validateNotEmpty(id, "Automaton ID");
        Integer integerId = Integer.parseInt(id.substring(1));
        Nfa automaton = loadedAutomata.get(integerId);
        if (automaton == null) {
            throw new IllegalArgumentException("No automaton found with ID: " + id);
        }
        return automaton;
    }

    public boolean recognizes(String id, String word) {
        ValidationUtils.validateWord(word);
        Automaton a = getAutomaton(id);
        return a.recognizes(word);
    }

    public boolean isEmpty(String id) {
        Automaton a = getAutomaton(id);
        return a.accept(new EmptyLanguageVisitor());
    }

    public boolean isDeterministic(String id) {
        Automaton a = getAutomaton(id);
        return a.accept(new DeterministicVisitor());
    }

    public String union(String id1, String id2) {
        Automaton a1 = getAutomaton(id1);
        Automaton a2 = getAutomaton(id2);
        if (a1 instanceof Nfa && a2 instanceof Nfa) {
            Automaton result = AutomatonFactory.createUnionNfa(a1, a2);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Union requires NFA automata");
    }

    public String concat(String id1, String id2) {
        Automaton a1 = getAutomaton(id1);
        Automaton a2 = getAutomaton(id2);
        if (a1 instanceof Nfa && a2 instanceof Nfa) {
            Automaton result = AutomatonFactory.createConcatNfa(a1, a2);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Concat requires NFA automata");
    }

    public String un(String id) {
        Automaton a = getAutomaton(id);
        if (a instanceof Nfa) {
            Automaton result = AutomatonFactory.createKleeneStarNfa(a);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Un (Kleene star) requires NFA automaton");
    }

    public void print(String id) {
        Automaton a = getAutomaton(id);
        System.out.println(a);
    }
}
