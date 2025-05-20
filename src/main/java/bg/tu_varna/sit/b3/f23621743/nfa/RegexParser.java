package bg.tu_varna.sit.b3.f23621743.nfa;

import java.util.Stack;

public class RegexParser {

    public static Nfa parse(String regex) {
        Stack<Nfa> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        //оператори за конкатенация
        regex = insertExplicitConcatOperator(regex);

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);
            switch (c) {
                case '(':
                    operators.push(c);
                    break;
                case ')':
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        processOperator(operands, operators.pop());
                    }
                    if (!operators.isEmpty()) {
                        operators.pop(); // махаме '('
                    }
                    break;
                case '*':
                    processOperator(operands, c);
                    break;
                case '|':
                case '.': // използвам '.' за конкатенация
                    while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                        processOperator(operands, operators.pop());
                    }
                    operators.push(c);
                    break;
                default:
                    operands.push(singleCharNfa(c));
            }
        }

        while (!operators.isEmpty()) {
            processOperator(operands, operators.pop());
        }

        return operands.pop();
    }

    private static void processOperator(Stack<Nfa> operands, char operator) {
        switch (operator) {
            case '*':
                Nfa a = operands.pop();
                operands.push(Nfa.kleeneStar(a));
                break;
            case '|':
                Nfa b = operands.pop();
                Nfa a2 = operands.pop();
                operands.push(Nfa.union(a2, b));
                break;
            case '.':
                Nfa right = operands.pop();
                Nfa left = operands.pop();
                operands.push(Nfa.concat(left, right));
                break;
        }
    }

    private static int precedence(char op) {
        return switch (op) {
            case '*' -> 3;
            case '.' -> 2;
            case '|' -> 1;
            default -> 0;
        };
    }

    private static String insertExplicitConcatOperator(String regex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            sb.append(c1);
            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);
                if ((Character.isLetterOrDigit(c1) || c1 == ')' || c1 == '*') &&
                        (Character.isLetterOrDigit(c2) || c2 == '(')) {
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }

    private static Nfa singleCharNfa(char c) {
        NfaBuilder builder = NfaBuilder.createBasicNfa(String.valueOf(c));
        return builder.build();
    }
} 