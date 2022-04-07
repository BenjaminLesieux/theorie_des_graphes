package com.efrei.thdesgrphs.automaton;

import com.efrei.thdesgrphs.io.Utils;
import com.efrei.thdesgrphs.operations.Scheduling;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Automaton implements Cloneable {

    private List<State> states;
    private Map<Integer, Integer> ranks;
    private String name;

    private int[][] valuesMatrix;
    private int[][] adjacencyMatrix;

    public Automaton(String name) {
        this.states = new ArrayList<>();
        this.ranks = new LinkedHashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<State> getStates() {
        return states;
    }

    public void addState(State state) {
        if (!this.states.contains(state)) {
            this.states.add(state);
            Collections.sort(this.states);
        }
    }

    public State getByID(int id) {
        for (State state : this.states) {
            if (state.id() == id) return state;
        }

        return null;
    }

    public void loadSuccessors() {
        for (State state : states) {
            List<Integer> predecessors = state.predecessors();
            List<State> predecessorsStates = predecessors.stream().map(this::getByID).filter(Objects::nonNull).toList();

            for (State predecessor : predecessorsStates) {
                predecessor.successors().add(state);
            }
        }
    }

    public void calculateValuesMatrix() {
        int[][] valMatrix = new int[states.size()][states.size()];

        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < states.size(); j++) {

                if (states.get(j).predecessors().contains(i+1)) {
                    valMatrix[i][j] = states.get(i).cost();
                }

                else {
                    valMatrix[i][j] = -1;
                }
            }
        }

        this.valuesMatrix = valMatrix;
    }

    public void calculateAdjacencyMatrix() {
        int[][] adjacencyMatrix = new int[states.size()][states.size()];

        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < states.size(); j++) {

                if (states.get(j).predecessors().contains(i+1)) {
                    adjacencyMatrix[i][j] = 1;
                }

                else {
                    adjacencyMatrix[i][j] = -1;
                }
            }
        }

        this.adjacencyMatrix = adjacencyMatrix;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public int[][] getValuesMatrix() {

        if (this.valuesMatrix.length == 0) this.calculateValuesMatrix();

        return valuesMatrix;
    }

    public void calculateRanks() {
        System.out.println(Utils.title("Calcule des rangs des états de : " + name));

        Queue<Integer> waitingStates = new ArrayBlockingQueue<>(states.size());
        waitingStates.addAll(states.stream().map(State::id).toList());

        while (!waitingStates.isEmpty()) {
            Integer currentStateID = waitingStates.remove();
            State currentState = getByID(currentStateID);

            if (currentState.predecessors().isEmpty()) {
                ranks.put(currentStateID, 0);
                System.out.println(currentStateID + " est un état d'entrée, il possède donc le rang 0");
            }
            else {
                List<Integer> predecessors = currentState.predecessors();
                List<Integer> unknownRanks = predecessors.stream().filter(state -> !ranks.containsKey(state)).toList();

                if (!unknownRanks.isEmpty()) {
                    for (Integer unknown : unknownRanks) {
                        if (!waitingStates.contains(unknown)) {
                            waitingStates.offer(unknown);
                        }
                    }
                    waitingStates.offer(currentStateID);
                }

                else {
                    int maxRank = ranks.entrySet()
                            .stream()
                            .filter(entry -> predecessors.contains(entry.getKey()))
                            .map(Map.Entry::getValue)
                            .max(Integer::compareTo)
                            .orElse(0);

                    ranks.put(currentStateID, maxRank + 1);
                    System.out.println("Le rang maximal des prédecesseurs " + currentState.predecessors() + " de " + currentState +  " est = " + maxRank);
                    System.out.println(" => Rang(" + currentState + ") = " + (maxRank + 1) + "\n");
                }
            }
        }
    }

    public Map<Integer, Integer> getRanks() {
        return ranks;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Utils.title("Automate:" + name));

        for(State s : this.states) {
            stringBuilder.append(s.predecessors()).append(" -> ").append(s).append(" -> ").append(s.successors());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public Automaton clone() {
        Automaton a = new Automaton(this.name);

        for (State s : List.copyOf(this.states)) {
            State copiedState = new State(s.id(), s.cost(), new ArrayList<>(), new ArrayList<>());

            for (Integer pred : s.predecessors()) {
                copiedState.predecessors().add(pred);
            }

            a.addState(copiedState);
        }

        a.loadSuccessors();

        return a;
    }
}
