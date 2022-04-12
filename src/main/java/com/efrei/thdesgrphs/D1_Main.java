package com.efrei.thdesgrphs;

import com.efrei.thdesgrphs.dates.D1_Date;
import com.efrei.thdesgrphs.dates.D1_DateType;
import com.efrei.thdesgrphs.io.D1_FileReader;
import com.efrei.thdesgrphs.io.D1_IHM;
import com.efrei.thdesgrphs.io.D1_Utils;
import com.efrei.thdesgrphs.operations.D1_Operations;
import com.efrei.thdesgrphs.operations.D1_Scheduling;

import java.io.*;
import java.util.Objects;

public class D1_Main {

    public static void main(String[] args) throws IOException {

        D1_IHM.mainMenu();

        //Ce code sert à exécuter puis à remplir les traces dans le fichier ci-dessous

        /*var defaultPath = "src/main/java/com/efrei/thdesgrphs/files/";

        var file = new File(defaultPath + "tests/");
        var maxFiles = Objects.requireNonNull(file.listFiles()).length;

        for (int i = 0; i < maxFiles-1; i++) {
            // Store current System.out before assigning a new value
            PrintStream console = System.out;

            var traceFile = new File(defaultPath + "/tests/traces/table " + (i+1) + ".txt");

            if (file.createNewFile()) {
                System.out.println("création de la trace");
            }

            var graphFile = new File(defaultPath + "/tests/table " + (i+1) + ".txt");
            var graph = D1_FileReader.readFile(graphFile.getPath());

            PrintStream fileOut = new PrintStream(new FileOutputStream(defaultPath + "/tests/traces/table " + (i+1) + ".txt"));
            System.setOut(fileOut);

            assert graph != null;

            D1_Operations.showAdjacencyMatrix(graph);
            D1_Operations.showValuesMatrix(graph);

            if (D1_Scheduling.isSchedulingGraph(graph)) {
                D1_Operations.showValuesMatrix(graph);
                graph.calculateRanks();
                D1_Date date = new D1_Date(graph);
                date.buildDates();
                date.printPrettyDates(D1_DateType.SOONEST);
                date.printPrettyDates(D1_DateType.LATEST);
                date.printPrettyDates(D1_DateType.MARGINS);
                date.showCriticalPaths();
            }

            System.out.println(D1_Utils.title("Fin du programme"));
        }*/
    }
}
