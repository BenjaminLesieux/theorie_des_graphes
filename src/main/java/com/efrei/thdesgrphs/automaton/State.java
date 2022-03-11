package com.efrei.thdesgrphs.automaton;

public class State {

    private int id;
    private List<Transition> constraints;

    public int getId() {
        return id;
    }

    public List<Transition> getConstraints() {
        return constraints;
    }

    private static class Transition {
        double cost;
        State previous;
        State next;
    }
}
