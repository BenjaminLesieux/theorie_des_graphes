package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.D1_Automaton;
import com.efrei.thdesgrphs.automaton.D1_State;
import com.efrei.thdesgrphs.io.D1_PrettyPrinter;
import com.efrei.thdesgrphs.io.D1_Utils;

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
public class D1_Operations {


    /**
     * Shows the values matrix with {@link #helperPrinterMatrix(D1_Automaton, int[][])}
     * */
    public static <T> void showValuesMatrix(D1_Automaton automaton) {
        int[][] matrix = automaton.getValuesMatrix();
        System.out.println(D1_Utils.title("Affichage de la matrice de valeur de l'automate : " + automaton.getName()));
        helperPrinterMatrix(automaton, matrix);
    }

    /**
     * Shows the adjacency matrix with {@link #helperPrinterMatrix(D1_Automaton, int[][])}
     * */
    public static <T> void showAdjacencyMatrix(D1_Automaton automaton) {
        int[][] matrix = automaton.getAdjacencyMatrix();
        System.out.println(D1_Utils.title("Affichage de la matrice d'adjacence de l'automate : " + automaton.getName()));
        helperPrinterMatrix(automaton, matrix);
    }

    /**
     * Prints a matrix from a graph with {@link D1_PrettyPrinter}
     * */
    private static void helperPrinterMatrix(D1_Automaton automaton, int[][] matrix) {
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

        D1_PrettyPrinter prettyPrinter = new D1_PrettyPrinter(System.out, "");
        prettyPrinter.print(toPrint);
    }

    /**
     * Adds an 'omega' state to the graph
     * @param automaton the graph
     * */
    public static void addOmegaState(D1_Automaton automaton) {
        System.out.println(D1_Utils.subTitle("Ajout de l'état omega (id:" + (automaton.getStates().size()+1) + ") car votre graphe a "
                + D1_Scheduling.getExitsID(automaton).size()
                + " sorties !"
        ));

        List<Integer> exits = D1_Scheduling.getExitsID(automaton);
        D1_State state = new D1_State(automaton.getStates().size(), 0, exits, new ArrayList<>());

        for (Integer exit : exits) {
            D1_State exitState = automaton.getByID(exit);
            exitState.successors().add(state);
        }

        automaton.addState(state);
        automaton.calculateAdjacencyMatrix();
        automaton.calculateValuesMatrix();
    }

    /**
     * Adds an 'alpha' state to the graph
     * @param automaton the graph
     * */
    public static void addAlphaState(D1_Automaton automaton) {
        System.out.println(D1_Utils.subTitle("Ajout de l'état alpha (id:0) car votre graphe a "
                + D1_Scheduling.getEntriesID(automaton).size()
                + " entrées !"
        ));

        List<Integer> entries = D1_Scheduling.getEntriesID(automaton);
        List<D1_State> entriesState = entries.stream().map(automaton::getByID).toList();
        D1_State state = new D1_State(0, 0, new ArrayList<>(), entriesState);

        automaton.addState(state);

        for (D1_State entry : entriesState) {
            entry.predecessors().add(state.id());
        }

        automaton.calculateAdjacencyMatrix();
        automaton.calculateValuesMatrix();
    }
}
