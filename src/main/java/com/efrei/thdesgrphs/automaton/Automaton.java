package com.efrei.thdesgrphs.automaton;

import java.util.ArrayList;
import java.util.List;

public class Automaton {

    private double[][] valuesMatrix;
    private double[][] adjacencyMatrix;

    private List<State> states;

    public Automaton() {
        this.states = new ArrayList<>();
        this.valuesMatrix = new double[][]{};
        this.adjacencyMatrix = new double[][]{};
    }

    public List<State> getStates() {
        return states;
    }

    public void addState(State state) {
        if (!this.states.contains(state)) {
            this.states.add(state);
        }
    }

    public double[][] getValuesMatrix() {
        return valuesMatrix;
    }

    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(State s : this.states) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
