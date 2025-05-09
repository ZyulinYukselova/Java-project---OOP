package bg.tu_varna.sit.b3.f23621743;

import java.io.*;
import java.util.*;

public class AutomatonManager {
    private final Map<String, Automaton> automatons = new HashMap<>();
    private static int idCounter = 0;

    public String addAutomaton(Automaton automaton) {
        String id = "A" + idCounter++;
        automatons.put(id, automaton);
        return id;
    }

    public Set<String> listAutomata() {
        return automatons.keySet();
    }

    public Automaton getAutomaton(String id) {
        return automatons.get(id);
    }

    //Операции
    public boolean recognizes(String id, String word) {
        Automaton a = getAutomaton(id);
        return a != null && a.recognizes(word);
    }

    public String fromRegex(String regex) {
        Nfa nfa = RegexParser.parse(regex);
        return addAutomaton(nfa);
    }

    public boolean isEmpty(String id) {
        Automaton a = getAutomaton(id);
        return a != null && a.isEmptyLanguage();
    }

    public boolean isDeterministic(String id) {
        Automaton a = getAutomaton(id);
        return a != null && a.isDeterministic();
    }

    public String union(String id1, String id2) {
        Automaton a1 = getAutomaton(id1);
        Automaton a2 = getAutomaton(id2);
        if (a1 instanceof Nfa && a2 instanceof Nfa) {
            Nfa result = Nfa.union((Nfa) a1, (Nfa) a2);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Union requires NFA automata.");
    }

    public String determinize(String id) {
        Automaton automaton = getAutomaton(id);
        if (automaton instanceof Nfa) {
            Nfa dfa = AutomatonOperations.determinize((Nfa) automaton);
            return addAutomaton(dfa);
        }
        throw new IllegalArgumentException("Only NFA can be determinized.");
    }

    public boolean isFinite(String id) {
        Automaton a = getAutomaton(id);
        if (a instanceof Nfa) {
            return AutomatonOperations.isFiniteLanguage((Nfa) a);
        }
        throw new IllegalArgumentException("Only NFA supported.");
    }

    public String concat(String id1, String id2) {
        Automaton a1 = getAutomaton(id1);
        Automaton a2 = getAutomaton(id2);
        if (a1 instanceof Nfa && a2 instanceof Nfa) {
            Nfa result = Nfa.concat((Nfa) a1, (Nfa) a2);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Concat requires NFA automata.");
    }

    public String un(String id) {
        Automaton a = getAutomaton(id);
        if (a instanceof Nfa) {
            Nfa result = Nfa.kleeneStar((Nfa) a);
            return addAutomaton(result);
        }
        throw new IllegalArgumentException("Un (Kleene star) requires NFA automaton.");
    }

    public void print(String id) {
        Automaton a = getAutomaton(id);
        if (a != null) {
            System.out.println(a);
        } else {
            System.out.println("No such automaton: " + id);
        }
    }

    //Сериализация(питай дали е удачно да ползваш Сериализация)
    public void save(String id, String filename) throws IOException {
        Automaton a = getAutomaton(id);
        if (a instanceof Serializable) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
                out.writeObject(a);
                System.out.println("Automaton " + id + " saved to file: " + filename);
            }
        } else {
            throw new IllegalArgumentException("Automaton is not serializable.");
        }
    }

    public String open(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof Automaton) {
                String id = addAutomaton((Automaton) obj);
                System.out.println("Automaton loaded with ID: " + id);
                return id;
            } else {
                throw new IOException("File does not contain a valid automaton.");
            }
        }
    }
    public void saveAsText(String id, String filename) throws IOException {
        Automaton automaton = getAutomaton(id);
        if (automaton != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println(automaton.toString());
                System.out.println("Automaton " + id + " saved as readable text to: " + filename);
            }
        } else {
            throw new IllegalArgumentException("No such automaton: " + id);
        }
    }


}
