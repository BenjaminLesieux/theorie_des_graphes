package com.efrei.thdesgrphs.io;

import com.efrei.thdesgrphs.automaton.D1_Automaton;
import com.efrei.thdesgrphs.automaton.D1_State;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The FileReader class is just used to read a file and to create a graph from it
 * */
public class D1_FileReader {

    /**
     * Reads a file and creates a graph from it
     *
     * @param path the path of the file
     * @return {@link D1_Automaton}
     * */
    public static D1_Automaton readFile(String path) {

        File file = new File(path);
        Scanner scanner;
        D1_Automaton automaton = new D1_Automaton(file.getName());

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Couldn't find the given file");
            return null;
        }

        do {
            var line = scanner.nextLine();
            var values = line.split(" ");

            if (!D1_Utils.isAnInt(values[0]) || !D1_Utils.isAnInt(values[1])) continue;

            var id = Integer.parseInt(values[0]);
            var cost = Integer.parseInt(values[1]);

            var predecessors = new ArrayList<Integer>();

            if (values.length > 2) {
                for (int i = 2; i < values.length; i++) {
                    if (!D1_Utils.isAnInt(values[i])) continue;
                    predecessors.add(Integer.parseInt(values[i]));
                }
            }

            if (automaton.getByID(id) != null) System.out.println("L'état " + id + " existe déjà dans le graphe");
            else {
                var state = new D1_State(id, cost, predecessors, new ArrayList<>());
                automaton.addState(state);
            }
        } while (scanner.hasNextLine());

        automaton.loadSuccessors();
        automaton.calculateValuesMatrix();
        automaton.calculateAdjacencyMatrix();

        return automaton;
    }
}
