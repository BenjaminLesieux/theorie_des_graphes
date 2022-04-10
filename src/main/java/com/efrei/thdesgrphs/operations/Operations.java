package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;
import com.efrei.thdesgrphs.io.PrettyPrinter;
import com.efrei.thdesgrphs.io.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Operations is a class composed with only static methods
 * The purpose of this class is to perform operations on
 * any graph we give.
 *
 * Therefore, any operations on a graph will be available in
 * this class.
 *
 * */
public class Operations {


    public static <T> void showValuesMatrix(Automaton automaton) {
        int[][] matrix = automaton.getValuesMatrix();
        System.out.println(Utils.title("Affichage de la matrice de valeur de l'automate : " + automaton.getName()));
        helperPrinterMatrix(automaton, matrix);
    }

    public static <T> void showAdjacencyMatrix(Automaton automaton) {
        int[][] matrix = automaton.getAdjacencyMatrix();
        System.out.println(Utils.title("Affichage de la matrice d'adjacence de l'automate : " + automaton.getName()));
        helperPrinterMatrix(automaton, matrix);
    }

    private static void helperPrinterMatrix(Automaton automaton, int[][] matrix) {
        List<String> titleValues = new ArrayList<>();
        List<String> rowValues;
        List<List<String>> allRows = new ArrayList<>();

        titleValues.add(" État ");

        allRows.add(titleValues);

        for (int i = 0; i < matrix.length; i++) {
            titleValues.add("  " + automaton.getStates().get(i).id() + "  ");
        }

        for (int i = 0; i < matrix.length; i++) {
            rowValues = new ArrayList<>();
            rowValues.add(String.valueOf(automaton.getStates().get(i).id()));
            for (int j = 0; j < matrix.length; j++) {
                rowValues.add(String.valueOf(matrix[i][j]));
            }

            allRows.add(rowValues);
        }

        String[][] toPrint = allRows.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);

        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, "");
        prettyPrinter.print(toPrint);
    }

    public static void addOmegaState(Automaton automaton) {
        System.out.println(Utils.subTitle("Ajout de l'état omega (id:" + (automaton.getStates().size()+1) + ") car votre graphe a "
                + Scheduling.getExitsID(automaton).size()
                + " sorties !"
        ));

        List<Integer> exits = Scheduling.getExitsID(automaton);
        State state = new State(automaton.getStates().size(), 0, exits, new ArrayList<>());

        for (Integer exit : exits) {
            State exitState = automaton.getByID(exit);
            exitState.successors().add(state);
        }

        automaton.addState(state);
        automaton.calculateAdjacencyMatrix();
        automaton.calculateValuesMatrix();
    }

    public static void addAlphaState(Automaton automaton) {
        System.out.println(Utils.subTitle("Ajout de l'état alpha (id:0) car votre graphe a "
                + Scheduling.getEntriesID(automaton).size()
                + " entrées !"
        ));

        List<Integer> entries = Scheduling.getEntriesID(automaton);
        List<State> entriesState = entries.stream().map(automaton::getByID).toList();
        State state = new State(0, 0, new ArrayList<>(), entriesState);

        automaton.addState(state);

        for (State entry : entriesState) {
            entry.predecessors().add(state.id());
        }

        automaton.calculateAdjacencyMatrix();
        automaton.calculateValuesMatrix();
    }
}
