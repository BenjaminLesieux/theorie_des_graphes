package com.efrei.thdesgrphs.automaton;

import java.util.List;

public record State(int id, int cost, List<Integer> predecessors) implements Comparable<State> {

    @Override
    public int compareTo(State o) {
        if (this.id > o.id()) return 1;
        else if (this.id < o.id()) return -1;
        return 0;
    }
}
