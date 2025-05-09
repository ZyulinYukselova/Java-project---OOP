package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.factory.AutomatonFactory;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaOperations;
import bg.tu_varna.sit.b3.f23621743.nfa.NfaValidator;
import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import bg.tu_varna.sit.b3.f23621743.visitor.DeterministicVisitor;
import bg.tu_varna.sit.b3.f23621743.visitor.EmptyLanguageVisitor;

import java.io.*;
import java.util.*;

public class AutomatonManager {
    private final Map<String, Automaton> automatons = new HashMap<>();
    private static int idCounter = 0;

    public String addAutomaton(Automaton automaton) {
        ValidationUtils.validateAutomaton(automaton);
        String id = "A" + idCounter++;
        automatons.put(id, automaton);
        return id;
    }

    public Set<String> listAutomata() {
        return new HashSet<>(automatons.keySet());
    }

    public Automaton getAutomaton(String id) {
        ValidationUtils.validateNotEmpty(id, "Automaton ID");
        Automaton automaton = automatons.get(id);
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

    public String fromRegex(String regex) {
        ValidationUtils.validateNotEmpty(regex, "Regular expression");
        Nfa nfa = RegexParser.parse(regex);
        return addAutomaton(nfa);
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

    public void save(String id, String filename) throws IOException {
        ValidationUtils.validateFileOperation(filename, "save");
        Automaton a = getAutomaton(id);
        if (a instanceof Serializable) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                out.writeObject(a);
                System.out.println("Automaton " + id + " saved to file: " + filename);
            }
        } else {
            throw new IllegalArgumentException("Automaton is not serializable");
        }
    }

    public String open(String filename) throws IOException, ClassNotFoundException {
        ValidationUtils.validateFileOperation(filename, "open");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof Automaton) {
                String id = addAutomaton((Automaton) obj);
                System.out.println("Automaton loaded with ID: " + id);
                return id;
            } else {
                throw new IOException("File does not contain a valid automaton");
            }
        }
    }

    public void saveAsText(String id, String filename) throws IOException {
        ValidationUtils.validateFileOperation(filename, "save as text");
        Automaton automaton = getAutomaton(id);
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(automaton.toString());
            System.out.println("Automaton " + id + " saved as readable text to: " + filename);
        }
    }
}
