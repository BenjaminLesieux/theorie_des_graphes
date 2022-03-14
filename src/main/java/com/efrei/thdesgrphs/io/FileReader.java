package com.efrei.thdesgrphs.io;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    public static Automaton readFile(String path) {

        Automaton automaton = new Automaton();

        File file = new File(path);
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Couldn't find the given file");
        }

        do {
            assert scanner != null;
            var line = scanner.nextLine();
            var values = line.split(" ");

            var id = Integer.parseInt(values[0]);
            var cost = Double.parseDouble(values[1]);

            var predecessors = new ArrayList<Integer>();

            if (values.length > 2) {
                for (int i = 2; i < values.length; i++) {
                    predecessors.add(Integer.valueOf(values[i]));
                }
            }

            var state = new State(id, cost, predecessors);
            automaton.addState(state);

        } while (scanner.hasNextLine());


        return automaton;
    }
}
