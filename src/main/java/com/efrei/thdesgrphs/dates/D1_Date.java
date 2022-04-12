package com.efrei.thdesgrphs.dates;

import com.efrei.thdesgrphs.automaton.D1_Automaton;
import com.efrei.thdesgrphs.automaton.D1_State;
import com.efrei.thdesgrphs.io.D1_PrettyPrinter;
import com.efrei.thdesgrphs.io.D1_Utils;
import com.efrei.thdesgrphs.operations.D1_Scheduling;

import java.util.*;

/**
 * Date is a class that deals with all the computions concerning the dates. <br><br>
 * 
 * It can calculate the earliest dates, the latest dates as well as the margins and the critical paths
 * */
public class D1_Date {

    private final D1_Automaton correspondingAutomaton;

    private final List<Integer> ranks;
    private final List<D1_State> states;

    private final List<List<D1_State>> criticalPaths;

    private final Map<D1_State, Integer> earliestDate;
    private final Map<D1_State, Integer> latestDate;
    private final Map<D1_State, Integer> margins;

    public D1_Date(D1_Automaton correspondingAutomaton) {
        this.correspondingAutomaton = correspondingAutomaton;

        this.ranks = correspondingAutomaton.getRanks().values()
                .stream()
                .sorted(Integer::compareTo)
                .toList();

        this.states = new ArrayList<>(correspondingAutomaton.getStates())
                .stream()
                .sorted(Comparator.comparing(s -> correspondingAutomaton.getRanks().get(s.id())))
                .toList();

        this.earliestDate = new LinkedHashMap<>();
        this.latestDate = new LinkedHashMap<>();
        this.margins = new LinkedHashMap<>();
        this.criticalPaths = new ArrayList<>();
    }

    /**
     * Returns the earliest date of a state 
     * @param state the state 
     * @return an integer that corresponds to the earliest date 
     * */
    public int getEarliestDate(D1_State state) {

        if (!earliestDate.isEmpty() && earliestDate.containsKey(state)) return earliestDate.get(state);
        else if (state.predecessors().isEmpty()) return 0;

        List<Integer> predecessors = state.predecessors();
        List<Integer> dates = new ArrayList<>();

        for (Integer predecessor : predecessors) {
            D1_State predecessorState = this.correspondingAutomaton.getByID(predecessor);
            dates.add(getEarliestDate(predecessorState) + predecessorState.cost());
        }

        return dates.stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * Returns the latest date of a state 
     * @param state the state 
     * @return an integer that corresponds to the latest date 
     * */
    public int getLatestDate(D1_State state) {

        if (!latestDate.isEmpty() && latestDate.containsKey(state)) return latestDate.get(state);

        if (state.successors().isEmpty()) {
            return getEarliestDate(state);
        }

        List<D1_State> successors = state.successors();
        List<Integer> dates = new ArrayList<>();

        for (D1_State successor : successors) {
            dates.add(getLatestDate(successor) - state.cost());
        }

        return dates.stream().min(Integer::compareTo).orElse(0);
    }

    /**
     * Calculates the margins by computing {@link D1_Date#getLatestDate(D1_State)} - {@link D1_Date#getEarliestDate(D1_State)}
     * */
    public int calculateMargin(D1_State state) {
        return getLatestDate(state) - getEarliestDate(state);
    }

    /**
     * This function can be called so that once you try to compute {@link #getEarliestDate(D1_State)} or any
     * other such method it will take its value from the corresponding map
     * */
    public void buildDates() {
        for (D1_State state : states) {
            this.earliestDate.put(state, getEarliestDate(state));
            this.latestDate.put(state, getLatestDate(state));
            this.margins.put(state, calculateMargin(state));
        }
    }

    /**
     * This function print the result in a 'very' pretty way kinda similar
     * to the way we've been writing dates in class
     *
     * @param type the type of the date (earliest, soonest, margins)
     * */
    public void printPrettyDates(D1_DateType type) {
        System.out.println(D1_Utils.title(type.getStringValue()));

        List<List<String>> listToPrint = new ArrayList<>();

        listToPrint.add(new ArrayList<>());
        listToPrint.get(0).add("Rangs");
        ranks.forEach(r -> listToPrint.get(0).add(String.valueOf(r.intValue())));

        listToPrint.add(new ArrayList<>());
        listToPrint.get(1).add("T & L");
        states.forEach(s -> listToPrint.get(1).add(s.id() + "(" + s.cost() + ")"));

        switch (type) {
            case SOONEST -> {
                listToPrint.add(new ArrayList<>());
                listToPrint.get(2).add("Predecesseurs");

                listToPrint.add(new ArrayList<>());
                listToPrint.get(3).add("Dates prédecesseurs");

                listToPrint.add(new ArrayList<>());
                listToPrint.get(4).add("Dates au plus tôt");

                for (D1_State state : states) {
                    List<Integer> dates = new ArrayList<>();
                    state.predecessors().forEach(p -> dates.add(
                            getEarliestDate(correspondingAutomaton.getByID(p))
                            + correspondingAutomaton.getByID(p).cost()
                    ));
                    listToPrint.get(2).add(state.predecessors().toString());
                    listToPrint.get(3).add(dates.toString());
                    listToPrint.get(4).add(String.valueOf(getEarliestDate(state)));
                }
            }

            case LATEST -> {
                listToPrint.add(new ArrayList<>());
                listToPrint.get(2).add("Successeurs");

                listToPrint.add(new ArrayList<>());
                listToPrint.get(3).add("Dates successeurs");

                listToPrint.add(new ArrayList<>());
                listToPrint.get(4).add("Dates au plus tard");

                for (D1_State state : states) {
                    List<Integer> dates = new ArrayList<>();
                    state.successors().forEach(s -> dates.add(getLatestDate(s)-state.cost()));
                    listToPrint.get(2).add(state.successors().stream().map(D1_State::id).toList().toString());
                    listToPrint.get(3).add(dates.toString());
                    listToPrint.get(4).add(String.valueOf(getLatestDate(state)));
                }
            }

            case MARGINS -> {
                listToPrint.add(new ArrayList<>());
                listToPrint.get(2).add("Marge");

                for (D1_State state : states) {
                    listToPrint.get(2).add(String.valueOf(calculateMargin(state)));
                }
            }
        }

        String[][] toPrint = listToPrint.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
        D1_PrettyPrinter prettyPrinter = new D1_PrettyPrinter(System.out, "");
        prettyPrinter.print(toPrint);
    }

    /**
     * Print all the critical paths
     * */
    public void showCriticalPaths() {
        System.out.println();
        System.out.println(D1_Utils.title("Chemin(s) critique(s)"));

        showCriticalPath(correspondingAutomaton.getByID(0), new ArrayList<>());

        for (List<D1_State> criticalPath : this.criticalPaths) {
            Integer sum = criticalPath.stream().map(D1_State::cost).reduce(Integer::sum).orElse(0);

            int maxOmegaValue = getLatestDate(
                    correspondingAutomaton.getByID(D1_Scheduling.getExitsID(correspondingAutomaton).get(0))
            );

            if (sum == maxOmegaValue)
                System.out.println(criticalPath + " = " + sum);
        }

        System.out.println();
    }

    /**
     * Calculates a critical path <br>
     * WARNING: the path may not be a real critical path in the case of a graph having many
     * therefore using {@link #showCriticalPaths()} is the right way to print all the 'real' critical paths
     * */
    public void showCriticalPath(D1_State current, List<D1_State> visited) {
        if (!visited.contains(current))
            visited.add(current);

        if (current.successors().isEmpty()) {
            this.criticalPaths.add(visited);
        }

        else {
            for (D1_State successor : current.successors()) {
                if (margins.get(successor) == 0) {
                    showCriticalPath(successor, new ArrayList<>(visited));
                }
            }
        }
    }


    @Override
    public String toString() {
        return "Dates au plus tôt:" + earliestDate + "\nDates au plus tard:" + latestDate + "\nMarges:" + margins;
    }
}
