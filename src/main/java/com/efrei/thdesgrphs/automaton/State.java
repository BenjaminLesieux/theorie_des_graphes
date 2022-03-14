package com.efrei.thdesgrphs.automaton;

import java.util.List;

public record State(int id, double cost, List<Integer> predecessors) {

    public void addPredecessor(Integer predecessor) {
        if (!this.predecessors.contains(predecessor)) {
            this.predecessors.add(predecessor);
        }
    }
}
