package com.efrei.thdesgrphs.automaton;

import com.efrei.thdesgrphs.io.D1_Utils;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Represents a graph. A graph is composed of a list of {@link D1_State}.
 * */
public class D1_Automaton implements Cloneable {

    private final List<D1_State> states;
    private final Map<Integer, Integer> ranks;
    private final String name;

    private int[][] valuesMatrix;
    private int[][] adjacencyMatrix;

    public D1_Automaton(String name) {
        this.states = new ArrayList<>();
        this.ranks = new LinkedHashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<D1_State> getStates() {
        return states;
    }

    /**
     * Adds a new state to the graph
     * */
    public void addState(D1_State state) {
        if (!this.states.contains(state)) {
            this.states.add(state);
            Collections.sort(this.states);
        }
    }

    /**
     * Find a state by its id
     *
     * @param id the id of the state
     * @return {@link D1_State} if found
     * */
    public D1_State getByID(int id) {
        for (D1_State state : this.states) {
            if (state.id() == id) return state;
        }

        return null;
    }

    /**
     * At first {@link D1_State} has an empty successors list
     * therefore we need to fill it with this function
     * before doing any scheduling algorithm
     * */
    public void loadSuccessors() {
        for (D1_State state : states) {
            List<Integer> predecessors = state.predecessors();
            List<D1_State> predecessorsStates = predecessors.stream().map(this::getByID).filter(Objects::nonNull).toList();

            for (D1_State predecessor : predecessorsStates) {
                predecessor.successors().add(state);
            }
        }
    }

    /**
     * Calculates the values matrix of the graph
     * */
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

    /**
     * Calculates the adjacency matrix of the graph
     * */
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

    /**
     * @return the adjacency matrix of the graph
     * */
    public int[][] getAdjacencyMatrix() {

        if (this.adjacencyMatrix.length == 0) this.calculateAdjacencyMatrix();

        return adjacencyMatrix;
    }

    /**
     * @return the values matrix of the graph
     * */
    public int[][] getValuesMatrix() {

        if (this.valuesMatrix.length == 0) this.calculateValuesMatrix();

        return valuesMatrix;
    }

    /**
     * This method calculates the rank of the graph
     * It works by using a queue and by calculating the rank of each state in the rising order.
     * Once computed, the rank is put in a map : {@code ranks}
     * */
    public void calculateRanks() {
        System.out.println(D1_Utils.title("Calcule des rangs des états de : " + name));

        Queue<Integer> waitingStates = new ArrayBlockingQueue<>(states.size());
        waitingStates.addAll(states.stream().map(D1_State::id).toList());

        while (!waitingStates.isEmpty()) {
            Integer currentStateID = waitingStates.remove();
            D1_State currentState = getByID(currentStateID);

            // If the state has no predecessors (i.e - it's the alpha state)
            // then the rank is obviously 0
            if (currentState.predecessors().isEmpty()) {
                ranks.put(currentStateID, 0);
                System.out.println(currentStateID + " est un état d'entrée, il possède donc le rang 0");
            }
            else {
                List<Integer> predecessors = currentState.predecessors();
                List<Integer> unknownRanks = predecessors.stream().filter(state -> !ranks.containsKey(state)).toList();

                /*
                * If there are some unknown ranks we need to calculate before the state
                * we are going to put state back in the queue after those unknown states
                * */
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

    /**
     * @return the map of the ranks of all the states
     * */
    public Map<Integer, Integer> getRanks() {
        return ranks;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(D1_Utils.title("Automate:" + name));

        for(D1_State s : this.states) {
            stringBuilder.append(s.predecessors()).append(" -> ").append(s).append(" -> ").append(s.successors());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Make a deep copy of the graph
     * @return a deep copy of the graph
     * */
    @Override
    public D1_Automaton clone() {
        D1_Automaton a = new D1_Automaton(this.name);

        for (D1_State s : List.copyOf(this.states)) {
            D1_State copiedState = new D1_State(s.id(), s.cost(), new ArrayList<>(), new ArrayList<>());

            for (Integer pred : s.predecessors()) {
                copiedState.predecessors().add(pred);
            }

            a.addState(copiedState);
        }

        a.loadSuccessors();

        return a;
    }
}
