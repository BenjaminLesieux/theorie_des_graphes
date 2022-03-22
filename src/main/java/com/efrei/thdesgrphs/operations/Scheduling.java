package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Scheduling {

    public static boolean isSchedulingGraph(Automaton automaton) {
        return hasOneEntry(automaton) && hasOneExit(automaton) && !hasCircuits(automaton) && !hasNegativeCosts(automaton);
    }

    private static boolean hasNegativeCosts(Automaton automaton) {
        return automaton.getStates().stream().anyMatch(state -> state.cost() < 0);
    }

    public static boolean hasCircuits(Automaton automaton) {

        addOmegaState(automaton);

        Automaton copyAutomaton = automaton.clone();
        State exitState = copyAutomaton.getByID(getExitsID(copyAutomaton).get(0));

        if (!isALeaf(copyAutomaton, exitState, true)) {
            return true;
        }

        return false;
    }

    private static boolean isALeaf(Automaton automaton, State leaf, boolean init) {

        if (!init) return false;

        if (automaton.getStates()
                .stream()
                .noneMatch(state -> state.predecessors().contains(leaf.id()))) {

            automaton.getStates().remove(leaf);

            for (Integer nextLeaf : leaf.predecessors()) {
                if (automaton.getByID(nextLeaf) == null) continue;
                State leafState = automaton.getByID(nextLeaf);
                init = isALeaf(automaton, leafState, true);
            }

            return init;
        }

        return false;
    }


    public static List<Integer> getEntriesID(Automaton automaton) {
        return automaton.getStates()
                .stream()
                .filter(state -> state.predecessors().size() == 0)
                .map(State::id)
                .collect(Collectors.toList());
    }

    private static boolean hasOneEntry(Automaton automaton) {
        return getEntriesID(automaton).size() == 1;
    }

    public static List<Integer> getExitsID(Automaton automaton) {
        var exits = new ArrayList<Integer>();

        automaton.getStates().forEach(state -> {
            if (automaton.getStates().stream().noneMatch(otherState -> otherState.predecessors().contains(state.id()))) {
               exits.add(state.id());
            }
        });

        return exits;
    }

    private static boolean hasOneExit(Automaton automaton) {
        return getExitsID(automaton).size() == 1;
    }

    private static void addOmegaState(Automaton automaton) {
        if (!hasOneExit(automaton)) {
            List<Integer> exits = getExitsID(automaton);
            State state = new State(automaton.getStates().size() + 1, 0, exits);
            automaton.addState(state);
        }
    }

    public static void addAlphaState(Automaton automaton) {
        if (!hasOneEntry(automaton)) {
            List<Integer> entries = getEntriesID(automaton);
            State state = new State(0, 0, new ArrayList<>());
            automaton.addState(state);

            for (Integer entry : entries) {
                automaton.getByID(entry).predecessors().add(state.id());
            }
        }
    }
}
