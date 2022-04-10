package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;
import com.efrei.thdesgrphs.io.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Scheduling {

    public static boolean isSchedulingGraph(Automaton automaton) {
        if (hasCircuits(automaton) || hasNegativeCosts(automaton)) {
            System.out.println("Votre graphe n'est pas un graphe d'ordonnancement");
            return false;
        }

        Operations.addAlphaState(automaton);
        Operations.addOmegaState(automaton);

        System.out.println("Votre graphe est désormais bien un graphe d'ordonnancement !");

        return true;
    }

    private static boolean hasNegativeCosts(Automaton automaton) {
        return automaton.getStates().stream().anyMatch(state -> state.cost() < 0);
    }

    public static boolean hasCircuits(Automaton automaton) {

        System.out.println(Utils.title("Détection de circuit pour " + automaton.getName()));

        Automaton copyAutomaton = automaton.clone();
        copyAutomaton.loadSuccessors();

        while (!copyAutomaton.getStates().isEmpty()) {

            List<State> states = List.copyOf(copyAutomaton.getStates());

            if (states.stream().noneMatch(s -> s.predecessors().isEmpty())) {
                System.out.println("Les états "  + states + " n'ont pas pu être supprimés");
                System.out.println("L'" + states.get(0) + " est à l'origine du cycle");
                return true;
            }

            for (State state : states) {
                if (state.predecessors().isEmpty()) {
                    for (State successor : state.successors()) {
                        // Removal of the transition between the state and its successor
                        successor.predecessors().remove((Integer) state.id());
                    }

                    state.successors().clear();
                    copyAutomaton.getStates().remove(state);

                    System.out.println("Suppression du point d'entrée suivant : " + state);
                    System.out.println("États restants : " + copyAutomaton.getStates() + "\n");
                }
            }
        }

        System.out.println("L'automate " + automaton.getName() + " ne comporte aucun cycle");

        return false;
    }


    public static List<Integer> getEntriesID(Automaton automaton) {
        return automaton.getStates()
                .stream()
                .filter(state -> state.predecessors().size() == 0)
                .map(State::id)
                .collect(Collectors.toList());
    }

    public static boolean hasMoreThanOneEntry(Automaton automaton) {
        return getEntriesID(automaton).size() > 1;
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

    public static boolean hasMoreThanOneExit(Automaton automaton) {
        return getExitsID(automaton).size() > 1;
    }
}
