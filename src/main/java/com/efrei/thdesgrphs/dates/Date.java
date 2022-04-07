package com.efrei.thdesgrphs.dates;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;
import com.efrei.thdesgrphs.io.PrettyPrinter;
import com.efrei.thdesgrphs.io.Utils;

import java.util.*;

public class Date {

    private final Automaton correspondingAutomaton;

    private final List<Integer> ranks;
    private final List<State> states;

    private final Map<State, Integer> earliestDate;
    private final Map<State, Integer> latestDate;
    private final Map<State, Integer> margins;

    public Date(Automaton correspondingAutomaton) {
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
    }

    public int getEarliestDate(State state) {

        if (!earliestDate.isEmpty() && earliestDate.containsKey(state)) return earliestDate.get(state);
        else if (state.predecessors().isEmpty()) return 0;

        List<Integer> predecessors = state.predecessors();
        List<Integer> dates = new ArrayList<>();

        for (Integer predecessor : predecessors) {
            State predecessorState = this.correspondingAutomaton.getByID(predecessor);
            dates.add(getEarliestDate(predecessorState) + predecessorState.cost());
        }

        return dates.stream().max(Integer::compareTo).orElse(0);
    }

    public int getLatestDate(State state) {

        if (!latestDate.isEmpty() && latestDate.containsKey(state)) return latestDate.get(state);

        if (state.successors().isEmpty()) {
            return getEarliestDate(state);
        }

        List<State> successors = state.successors();
        List<Integer> dates = new ArrayList<>();

        for (State successor : successors) {
            dates.add(getLatestDate(successor) - state.cost());
        }

        return dates.stream().min(Integer::compareTo).orElse(0);
    }

    public int calculateMargin(State state) {
        return getLatestDate(state) - getEarliestDate(state);
    }

    public void buildDates() {
        for (State state : states) {
            this.earliestDate.put(state, getEarliestDate(state));
            this.latestDate.put(state, getLatestDate(state));
            this.margins.put(state, calculateMargin(state));
        }
    }

    public void printPrettyDates(DateType type) {
        System.out.println(Utils.title(type.getStringValue()));

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

                for (State state : states) {
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

                for (State state : states) {
                    List<Integer> dates = new ArrayList<>();
                    state.successors().forEach(s -> dates.add(getLatestDate(s)-state.cost()));
                    listToPrint.get(2).add(state.successors().stream().map(State::id).toList().toString());
                    listToPrint.get(3).add(dates.toString());
                    listToPrint.get(4).add(String.valueOf(getLatestDate(state)));
                }
            }

            case MARGINS -> {
                listToPrint.add(new ArrayList<>());
                listToPrint.get(2).add("Marge");

                for (State state : states) {
                    listToPrint.get(2).add(String.valueOf(calculateMargin(state)));
                }
            }
        }

        String[][] toPrint = listToPrint.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, "");
        prettyPrinter.print(toPrint);
    }

    @Override
    public String toString() {
        return "Dates au plus tôt:" + earliestDate + "\nDates au plus tard:" + latestDate + "\nMarges:" + margins;
    }
}
