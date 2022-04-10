package com.efrei.thdesgrphs;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.automaton.State;
import com.efrei.thdesgrphs.dates.Date;
import com.efrei.thdesgrphs.dates.DateType;
import com.efrei.thdesgrphs.io.FileReader;
import com.efrei.thdesgrphs.io.IHM;
import com.efrei.thdesgrphs.io.Utils;
import com.efrei.thdesgrphs.operations.Operations;
import com.efrei.thdesgrphs.operations.Scheduling;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        var defaultPath = "src/main/java/com/efrei/thdesgrphs/files/";

        var file = new File(defaultPath + "tests/");
        var maxFiles = Objects.requireNonNull(file.listFiles()).length;

        for (int i = 0; i < maxFiles; i++) {
            // Store current System.out before assigning a new value
            PrintStream console = System.out;

            var traceFile = new File(defaultPath + "/tests/traces/table " + (i+1) + ".txt");

            if (file.createNewFile()) {
                System.out.println("création de la trace");
            }

            var graphFile = new File(defaultPath + "/tests/table " + (i+1) + ".txt");
            var graph = FileReader.readFile(graphFile.getPath());

            PrintStream fileOut = new PrintStream(new FileOutputStream(defaultPath + "/tests/traces/table " + (i+1) + ".txt"));
            System.setOut(fileOut);

            assert graph != null;

            Operations.showAdjacencyMatrix(graph);
            Operations.showValuesMatrix(graph);

            if (Scheduling.isSchedulingGraph(graph)) {
                Operations.showValuesMatrix(graph);
                graph.calculateRanks();
                Date date = new Date(graph);
                date.buildDates();
                date.printPrettyDates(DateType.SOONEST);
                date.printPrettyDates(DateType.LATEST);
                date.printPrettyDates(DateType.MARGINS);
            }

            System.out.println(Utils.title("Fin du programme"));
        }
    }
}
