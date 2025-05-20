package bg.tu_varna.sit.b3.f23621743.nfa;

import bg.tu_varna.sit.b3.f23621743.Nfa;
import java.util.*;

public class RegexParser {
    private static int pos;
    private static String regex;

    public static Nfa parse(String input) {
        regex = input;
        pos = 0;
        return parseExpression();
    }

    private static Nfa parseExpression() {
        Nfa result = parseTerm();
        
        while (pos < regex.length() && regex.charAt(pos) == '|') {
            pos++; // Skip '|'
            Nfa term = parseTerm();
            result = AutomatonOperations.union(result, term);
        }
        
        return result;
    }

    private static Nfa parseTerm() {
        Nfa result = parseFactor();
        
        while (pos < regex.length() && isFactorStart(regex.charAt(pos))) {
            Nfa factor = parseFactor();
            result = AutomatonOperations.concat(result, factor);
        }
        
        return result;
    }

    private static Nfa parseFactor() {
        Nfa result = parseBase();
        
        while (pos < regex.length() && regex.charAt(pos) == '*') {
            pos++; // Skip '*'
            result = AutomatonOperations.positiveClosure(result);
        }
        
        return result;
    }

    private static Nfa parseBase() {
        if (pos >= regex.length()) {
            throw new IllegalArgumentException("Unexpected end of regex");
        }
        
        char c = regex.charAt(pos++);
        
        if (c == '(') {
            Nfa result = parseExpression();
            if (pos >= regex.length() || regex.charAt(pos) != ')') {
                throw new IllegalArgumentException("Expected closing parenthesis");
            }
            pos++; // Skip ')'
            return result;
        } else if (Character.isLetterOrDigit(c)) {
            return NfaBuilder.createBasicNfa(c).build();
        } else if (c == 'ε') {
            return NfaBuilder.createEpsilonNfa().build();
        } else {
            throw new IllegalArgumentException("Invalid character in regex: " + c);
        }
    }

    private static boolean isFactorStart(char c) {
        return Character.isLetterOrDigit(c) || c == '(' || c == 'ε';
    }
} 