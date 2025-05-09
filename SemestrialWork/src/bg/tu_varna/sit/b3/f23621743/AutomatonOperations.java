package bg.tu_varna.sit.b3.f23621743;

import java.util.*;

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

    public static Nfa determinize(Nfa nfa) {
        Nfa dfa = new Nfa();
        Map<Set<State>, State> stateMap = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();

        Set<State> startClosure = epsilonClosure(nfa, Set.of(nfa.getStartState()));
        State startState = new State(NfaStateCounter.next(), containsFinal(startClosure));
        dfa.addState(startState);
        dfa.setStartState(startState);
        stateMap.put(startClosure, startState);
        queue.add(startClosure);

        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State currentDfaState = stateMap.get(currentSet);

            for (Character symbol : getUsedAlphabet(nfa)) {
                if (symbol == Nfa.EPSILON) continue;

                Set<State> moveResult = move(nfa, currentSet, symbol);
                Set<State> closure = epsilonClosure(nfa, moveResult);

                if (closure.isEmpty()) continue;

                State targetState = stateMap.get(closure);
                if (targetState == null) {
                    targetState = new State(NfaStateCounter.next(), containsFinal(closure));
                    dfa.addState(targetState);
                    stateMap.put(closure, targetState);
                    queue.add(closure);
                }

                dfa.addTransition(currentDfaState, symbol, targetState);
            }
        }

        return dfa;
    }

    private static Set<State> epsilonClosure(Nfa nfa, Set<State> states) {
        Set<State> closure = new HashSet<>(states);
        Stack<State> stack = new Stack<>();
        stack.addAll(states);


        while (!stack.isEmpty()) {
            State s = stack.pop();
            Set<State> eps = nfa.getTransitions().getOrDefault(s, Map.of()).getOrDefault(Nfa.EPSILON, Set.of());
            for (State t : eps) {
                if (closure.add(t)) stack.push(t);
            }
        }
        return closure;
    }

    private static Set<State> move(Nfa nfa, Set<State> states, Character symbol) {
        Set<State> result = new HashSet<>();
        for (State s : states) {
            Set<State> targets = nfa.getTransitions().getOrDefault(s, Map.of()).getOrDefault(symbol, Set.of());
            result.addAll(targets);
        }
        return result;
    }

    private static boolean containsFinal(Set<State> states) {
        for (State s : states) {
            if (s.isFinal()) return true;
        }
        return false;
    }

    private static Set<Character> getUsedAlphabet(Nfa nfa) {
        Set<Character> result = new HashSet<>();
        for (Map<Character, Set<State>> map : nfa.getTransitions().values()) {
            result.addAll(map.keySet());
        }
        return result;
    }
    public static boolean isFiniteLanguage(Nfa nfa) {
        Set<State> visited = new HashSet<>();
        Set<State> stack = new HashSet<>();
        return !hasCycle(nfa, nfa.getStartState(), visited, stack);
    }

    private static boolean hasCycle(Nfa nfa, State current, Set<State> visited, Set<State> stack) {
        if (stack.contains(current)) return true;
        if (!visited.add(current)) return false;

        stack.add(current);

        Map<Character, Set<State>> next = nfa.getTransitions().getOrDefault(current, Map.of());
        for (Set<State> targets : next.values()) {
            for (State nextState : targets) {
                if (hasCycle(nfa, nextState, visited, stack)) return true;
            }
        }

        stack.remove(current);
        return false;
    }



}





