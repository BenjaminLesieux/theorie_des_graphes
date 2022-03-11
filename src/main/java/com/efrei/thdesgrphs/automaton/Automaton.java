package com.efrei.thdesgrphs.automaton;

import java.util.ArrayList;

public class Automaton {

    private List<State> states;

    public Automaton() {
        this.states = new ArrayList<>()
    }

    public List<State> getStates() {
        return states;
    }
}
