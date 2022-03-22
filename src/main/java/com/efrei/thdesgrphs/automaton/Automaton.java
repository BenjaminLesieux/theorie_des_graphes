package com.efrei.thdesgrphs.automaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Automaton implements Cloneable {

    private List<State> states;
    private Map<State, Integer> ranks;

    private int[][] valuesMatrix;

    public Automaton() {
        this.states = new ArrayList<>();
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

    public int[][] getValuesMatrix() {
        return valuesMatrix;
    }

    public void calculateRanks() {
        if (this.valuesMatrix == null || this.valuesMatrix.length == 0) {
            System.err.println("You must calculate the values matrix before calculating the ranks of your states");
            System.exit(1);
        }

        /**
         * TODO: Perform algorithm
         * */
    }

    public Map<State, Integer> getRanks() {
        return ranks;
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

    @Override
    public Automaton clone() {
        Automaton a = new Automaton();
        for (State s : this.states) {
            a.addState(new State(s.id(), s.cost(), s.predecessors()));
        }

        return a;
    }
}
