package bg.tu_varna.sit.b3.f23621743;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class AutomatonOperations {
    private static int stateCounter = 100;

    public static Nfa union(Nfa a1, Nfa a2) {
        Nfa result = new Nfa();

        Map<State, State> mappingA1 = new HashMap<>();
        Map<State, State> mappingA2 = new HashMap<>();

        for (State s : a1.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA1.put(s, copy);
        }

        for (State s : a2.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA2.put(s, copy);
        }

        for (var entry : a1.getTransitions().entrySet()) {
            State from = mappingA1.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA1.get(to));
                }
            }
        }

        for (var entry : a2.getTransitions().entrySet()) {
            State from = mappingA2.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA2.get(to));
                }
            }
        }

        State newStart = new State(stateCounter++, false);
        result.addState(newStart);
        result.setStartState(newStart);
        result.addTransition(newStart, Nfa.EPSILON, mappingA1.get(a1.getStartState()));
        result.addTransition(newStart, Nfa.EPSILON, mappingA2.get(a2.getStartState()));

        return result;
    }

    public static Nfa concat(Nfa a1, Nfa a2) {
        Nfa result = new Nfa();

        Map<State, State> mappingA1 = new HashMap<>();
        Map<State, State> mappingA2 = new HashMap<>();

        for (State s : a1.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA1.put(s, copy);
        }

        for (State s : a2.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mappingA2.put(s, copy);
        }

        for (var entry : a1.getTransitions().entrySet()) {
            State from = mappingA1.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA1.get(to));
                }
            }
        }

        for (var entry : a2.getTransitions().entrySet()) {
            State from = mappingA2.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mappingA2.get(to));
                }
            }
        }

        result.setStartState(mappingA1.get(a1.getStartState()));

        for (State s : a1.getStates()) {
            if (s.isFinal()) {
                State mapped = mappingA1.get(s);
                mapped.setFinal(false);
                result.addTransition(mapped, Nfa.EPSILON, mappingA2.get(a2.getStartState()));
            }
        }

        return result;
    }

    public static Nfa positiveClosure(Nfa a) {
        Nfa result = new Nfa();

        Map<State, State> mapping = new HashMap<>();

        for (State s : a.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mapping.put(s, copy);
        }

        for (var entry : a.getTransitions().entrySet()) {
            State from = mapping.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mapping.get(to));
                }
            }
        }

        result.setStartState(mapping.get(a.getStartState()));

        for (State s : a.getStates()) {
            if (s.isFinal()) {
                result.addTransition(mapping.get(s), Nfa.EPSILON, mapping.get(a.getStartState()));
            }
        }

        return result;
    }


    public static Nfa fromRegex(String regex) {
        Stack<Nfa> automata = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processOperator(automata, operators.pop());
                }
                operators.pop();
            } else if (c == '|' || c == '*' || c == '+') {
                operators.push(c);
            } else {
                Nfa single = new Nfa();
                State start = new State(stateCounter++, false);
                State end = new State(stateCounter++, true);
                single.addState(start);
                single.addState(end);
                single.setStartState(start);
                single.addTransition(start, c, end);
                automata.push(single);
                if (i + 1 < regex.length()) {
                    char next = regex.charAt(i + 1);
                    if (next != '|' && next != ')' && next != '*' && next != '+') {
                        operators.push('.');
                    }
                }
            }
        }

        while (!operators.isEmpty()) {
            processOperator(automata, operators.pop());
        }

        return automata.pop();
    }

    private static void processOperator(Stack<Nfa> automata, char operator) {
        if (operator == '|') {
            Nfa second = automata.pop();
            Nfa first = automata.pop();
            automata.push(union(first, second));
        } else if (operator == '.') {
            Nfa second = automata.pop();
            Nfa first = automata.pop();
            automata.push(concat(first, second));
        } else if (operator == '*') {
            Nfa first = automata.pop();
            automata.push(kleeneClosure(first));
        } else if (operator == '+') {
            Nfa first = automata.pop();
            automata.push(positiveClosure(first));
        }
    }

    private static Nfa kleeneClosure(Nfa a) {
        Nfa positive = positiveClosure(a);

        State newStart = new State(stateCounter++, false);
        Nfa result = new Nfa();
        result.addState(newStart);
        result.setStartState(newStart);

        Map<State, State> mapping = new HashMap<>();

        for (State s : positive.getStates()) {
            State copy = new State(stateCounter++, s.isFinal());
            result.addState(copy);
            mapping.put(s, copy);
        }

        for (var entry : positive.getTransitions().entrySet()) {
            State from = mapping.get(entry.getKey());
            for (var t : entry.getValue().entrySet()) {
                for (State to : t.getValue()) {
                    result.addTransition(from, t.getKey(), mapping.get(to));
                }
            }
        }

        result.addTransition(newStart, Nfa.EPSILON, mapping.get(positive.getStartState()));
        result.getTransitions().get(mapping.get(positive.getStartState())).putIfAbsent(Nfa.EPSILON, Set.of(newStart));
        return result;
    }
}





