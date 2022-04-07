package com.efrei.thdesgrphs.automaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record State(int id, int cost, List<Integer> predecessors, List<State> successors) implements Comparable<State> {

    @Override
    public int compareTo(State o) {
        if (this.id > o.id()) return 1;
        else if (this.id < o.id()) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "État " + String.valueOf(this.id());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        State s = (State) o;
        return Objects.equals(id(), s.id());
    }
}
