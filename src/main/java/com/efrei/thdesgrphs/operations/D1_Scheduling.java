package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.D1_Automaton;
import com.efrei.thdesgrphs.automaton.D1_State;
import com.efrei.thdesgrphs.io.D1_Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Scheduling class has all the method required to check if a graph is a scheduling graph
 * */
public class D1_Scheduling {

    /**
     * Checks if a graph is a scheduling graph. To be one, a graph must :
     * <br>• have no negative costs
     * <br>• have no circuits
     * <br>• have all the vertices from one state have the same cost
     * <br>• arcs incident to the outside at the entry point have a zero value
     * */
    public static boolean isSchedulingGraph(D1_Automaton automaton) {
        if (hasCircuits(automaton) || hasNegativeCosts(automaton)) {
            // the last conditions aren't checked because the code is made so that it isn't required
            System.out.println("Votre graphe n'est pas un graphe d'ordonnancement");
            return false;
        }

        // if the above conditions are true, then we can add the 'alpha' and 'omega' states
        // to make the graph a scheduling graph
        D1_Operations.addAlphaState(automaton);
        D1_Operations.addOmegaState(automaton);

        System.out.println("Votre graphe est désormais bien un graphe d'ordonnancement !");

        return true;
    }

    /**
     * Checks if a graph has any negative costs
     * @param automaton the graph
     * @return true if there are, false if not
     * */
    private static boolean hasNegativeCosts(D1_Automaton automaton) {
        return automaton.getStates().stream().anyMatch(state -> state.cost() < 0);
    }

    /**
     * Checks if a graph has any circuits
     * @param automaton the graph
     * @return true if there are, false if not
     * */
    public static boolean hasCircuits(D1_Automaton automaton) {

        System.out.println(D1_Utils.title("Détection de circuit pour " + automaton.getName()));

        D1_Automaton copyAutomaton = automaton.clone();
        copyAutomaton.loadSuccessors();

        while (!copyAutomaton.getStates().isEmpty()) {

            List<D1_State> states = List.copyOf(copyAutomaton.getStates());

            if (states.stream().noneMatch(s -> s.predecessors().isEmpty())) {
                System.out.println("Les états "  + states + " n'ont pas pu être supprimés");
                System.out.println("L'" + states.get(0) + " est à l'origine du cycle");
                return true;
            }

            for (D1_State state : states) {
                if (state.predecessors().isEmpty()) {
                    for (D1_State successor : state.successors()) {
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


    /**
     * Returns the id's of all the states with no predecessors
     * @param automaton the graph
     * */
    public static List<Integer> getEntriesID(D1_Automaton automaton) {
        return automaton.getStates()
                .stream()
                .filter(state -> state.predecessors().isEmpty())
                .map(D1_State::id)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a graph has more than one 'entry'
     * @param automaton the graph
     * @return true if there is, false if not
     * */
    public static boolean hasMoreThanOneEntry(D1_Automaton automaton) {
        return getEntriesID(automaton).size() > 1;
    }

    /**
     * Returns the id's of all the states with no successors
     * @param automaton the graph
     * */
    public static List<Integer> getExitsID(D1_Automaton automaton) {
        return automaton.getStates()
                .stream()
                .filter(state -> state.successors().isEmpty())
                .map(D1_State::id)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a graph has more than one 'exit'
     * @param automaton the graph
     * @return true if there is, false if not
     * */
    public static boolean hasMoreThanOneExit(D1_Automaton automaton) {
        return getExitsID(automaton).size() > 1;
    }
}
