package com.efrei.thdesgrphs.automaton;

import java.util.List;
import java.util.Objects;

/**
 * A State holds the data for a task such as its id, its cost. <br><br>
 * A State has a list of predecessors which contains the id of said predecessors <br><br>
 * A state has a list of successors which points to each successor
 * */
public record D1_State(int id, int cost, List<Integer> predecessors, List<D1_State> successors) implements Comparable<D1_State> {

    @Override
    public int compareTo(D1_State o) {
        if (this.id > o.id()) return 1;
        else if (this.id < o.id()) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "État " + this.id();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null)
            return false;

        if (getClass() != o.getClass())
            return false;

        D1_State s = (D1_State) o;
        return Objects.equals(id(), s.id());
    }
}
