package com.efrei.thdesgrphs.io;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static Automaton readFile(String path) {

        File file = new File(path);
        Scanner scanner = null;
        Automaton automaton = new Automaton(file.getName());

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

            if (!Utils.isAnInt(values[0])) continue;

            var id = Integer.parseInt(values[0]);
            var cost = Integer.parseInt(values[1]);

            var predecessors = new ArrayList<Integer>();

            if (values.length > 2) {
                for (int i = 2; i < values.length; i++) {
                    if (!Utils.isAnInt(values[i])) continue;
                    predecessors.add(Integer.parseInt(values[i]));
                }
            }

            if (automaton.getByID(id) != null) System.out.println("L'état " + id + " existe déjà dans le graphe");
            else {
                var state = new State(id, cost, predecessors, new ArrayList<>());
                automaton.addState(state);
            }
        } while (scanner.hasNextLine());

        automaton.loadSuccessors();
        automaton.calculateValuesMatrix();
        automaton.calculateAdjacencyMatrix();

        return automaton;
    }
}
