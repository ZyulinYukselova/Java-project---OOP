package bg.tu_varna.sit.b3.f23621743;

import bg.tu_varna.sit.b3.f23621743.validation.ValidationUtils;
import bg.tu_varna.sit.b3.f23621743.nfa.Nfa;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileOperations {
    public static void saveToJson(String id, String filename) throws IOException {
        Nfa nfa = AutomatonManager.getAutomaton(id);
        
        JSONObject json = new JSONObject();
        json.put("startState", nfa.getStartState());
        
        // Save states
        JSONObject statesJson = new JSONObject();
        for (String state : nfa.getStates()) {
            JSONObject stateJson = new JSONObject();
            stateJson.put("isFinal", nfa.getAcceptStates().contains(state));
            statesJson.put(state, stateJson);
        }
        json.put("states", statesJson);
        
        // Save transitions
        JSONObject transitionsJson = new JSONObject();
        for (Map.Entry<String, Map<String, Set<String>>> stateEntry : nfa.getTransitions().entrySet()) {
            String fromState = stateEntry.getKey();
            JSONObject stateTransitions = new JSONObject();
            
            for (Map.Entry<String, Set<String>> symbolEntry : stateEntry.getValue().entrySet()) {
                String symbol = symbolEntry.getKey();
                JSONArray targetStates = new JSONArray();
                for (String targetState : symbolEntry.getValue()) {
                    targetStates.put(targetState);
                }
                stateTransitions.put(symbol, targetStates);
            }
            
            transitionsJson.put(fromState, stateTransitions);
        }
        json.put("transitions", transitionsJson);
        
        Files.writeString(Paths.get(filename), json.toString(2));
    }
    
    public static String loadFromJson(String filename) throws IOException {
        String jsonContent = Files.readString(Paths.get(filename));
        JSONObject json = new JSONObject(jsonContent);
        
        Set<String> states = new HashSet<>();
        Set<String> acceptStates = new HashSet<>();
        String startState = json.getString("startState");
        
        // Load states
        JSONObject statesJson = json.getJSONObject("states");
        for (String stateId : statesJson.keySet()) {
            JSONObject stateJson = statesJson.getJSONObject(stateId);
            states.add(stateId);
            if (stateJson.getBoolean("isFinal")) {
                acceptStates.add(stateId);
            }
        }
        
        // Load transitions
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
        JSONObject transitionsJson = json.getJSONObject("transitions");
        
        for (String fromStateId : transitionsJson.keySet()) {
            JSONObject stateTransitions = transitionsJson.getJSONObject(fromStateId);
            Map<String, Set<String>> stateTransitionMap = new HashMap<>();
            
            for (String symbol : stateTransitions.keySet()) {
                JSONArray targetStateIds = stateTransitions.getJSONArray(symbol);
                Set<String> targetStates = new HashSet<>();
                for (int i = 0; i < targetStateIds.length(); i++) {
                    targetStates.add(targetStateIds.getString(i));
                }
                stateTransitionMap.put(symbol, targetStates);
            }
            
            transitions.put(fromStateId, stateTransitionMap);
        }
        
        Nfa nfa = new Nfa(states, startState, acceptStates, transitions);
        return AutomatonManager.addAutomaton(nfa);
    }
    
    public static void saveAutomaton(String id, String filename) throws IOException {
        ValidationUtils.validateFileOperation(filename, "save");
        Nfa nfa = AutomatonManager.getAutomaton(id);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(nfa);
        }
    }
    
    public static String loadAutomaton(String filename) throws IOException, ClassNotFoundException {
        ValidationUtils.validateFileOperation(filename, "open");
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Nfa nfa = (Nfa) ois.readObject();
            return AutomatonManager.addAutomaton(nfa);
        }
    }
    
    public static void saveAsText(String id, String filename) throws IOException {
        ValidationUtils.validateFileOperation(filename, "save as text");
        Nfa nfa = AutomatonManager.getAutomaton(id);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(nfa.toString());
        }
    }
} 