package com.efrei.thdesgrphs.io;

import com.efrei.thdesgrphs.automaton.D1_Automaton;
import com.efrei.thdesgrphs.dates.D1_Date;
import com.efrei.thdesgrphs.dates.D1_DateType;
import com.efrei.thdesgrphs.operations.D1_Operations;
import com.efrei.thdesgrphs.operations.D1_Scheduling;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * The goal of the IHM is to simulate the console interface
 * for the user to perform operations on a graph
 * */
public class D1_IHM {

    /**
     * The automaton on which we will perform
     * the operations
     * */
    private static D1_Automaton automaton;
    private static Scanner scanner;

    public static void mainMenu() {
        scanner = new Scanner(System.in);
        var defaultPath = "src/main/java/com/efrei/thdesgrphs/files/";
        var choice = "";

        var file = new File(defaultPath + "tests/");

        // -1 because of the /traces directory
        var maxFiles = Objects.requireNonNull(file.listFiles()).length - 1;


        while (!choice.equalsIgnoreCase("q")) {
            System.out.println("\n".repeat(3));
            System.out.println(D1_Utils.title("Menu principal"));
            System.out.format("Veuillez choisir un automate : (entrez un nombre entre 1 et %d ou 'q' pour quitter): ", maxFiles);
            choice = scanner.next();

            // if the choice is an integer
            if (D1_Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);

                if (intChoice >= 1 && intChoice <= maxFiles) {
                    automaton = D1_FileReader.readFile(file.getPath() + "/table " + (intChoice) + ".txt");
                    D1_Operations.showValuesMatrix(automaton);
                    operationsMenu();
                }
            }

            else if (choice.equalsIgnoreCase("q")) {
                System.out.println("Merci d'avoir utilisé notre application :)");
            }

            else {
                System.out.println("Votre choix : '" + choice + "' n'est pas valide");
            }
        }
    }

    public static void operationsMenu() {
        var choice = "";

        while (!choice.equalsIgnoreCase("q")) {
            System.out.println("\n".repeat(2));
            System.out.println(D1_Utils.title("Menu des opérations"));

            System.out.print("""
                    1. Affichage de la matrice de d'adjacence
                    2. Affichage de la matrice de valeurs
                    3. Opérations d'ordonnancement
                    q. Retour en arrière
                    
                    Votre choix :\040
                              """);

            choice = scanner.next();

            if (D1_Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);

                switch (intChoice) {
                    case 1 -> D1_Operations.showAdjacencyMatrix(automaton);
                    case 2 -> D1_Operations.showValuesMatrix(automaton);
                    case 3 -> {

                        boolean scheduling = D1_Scheduling.isSchedulingGraph(automaton);

                        if(scheduling)
                            datesMenu();
                        else {
                            System.out.println("\nVoulez-vous changer de graphe ? (y/n) : ");
                            choice = scanner.next();

                            if (choice.equalsIgnoreCase("y")) {
                                choice = "q";
                            }
                        }
                    }
                }
            }

            else if (choice.equalsIgnoreCase("q")) {
                System.out.println("<= Retour à la page arrière");
            }

            else {
                System.out.println("Votre entrée : '" + choice + "' n'est pas valide");
            }
        }
    }

    public static void datesMenu() {
        var choice = "";

        while (!choice.equalsIgnoreCase("q")) {
            System.out.println("\n".repeat(2));
            System.out.println(D1_Utils.title("Calcul des dates"));

            System.out.print("""
                    1. Calcul des dates au plus tôt et au plus tard
                    q. Retour en arrière
                    
                    Votre choix :\040
                              """);

            choice = scanner.next();

            if (D1_Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);
                if (intChoice == 1) {

                    if (!automaton.getRanks().isEmpty())
                        System.out.println("Les rangs de ce graphe ont déjà été calculés : " + automaton.getRanks());
                    else
                        automaton.calculateRanks();

                    D1_Date date = new D1_Date(automaton);
                    date.buildDates();
                    date.printPrettyDates(D1_DateType.SOONEST);
                    date.printPrettyDates(D1_DateType.LATEST);
                    date.printPrettyDates(D1_DateType.MARGINS);
                    date.showCriticalPaths();
                }
            }

            else if (choice.equalsIgnoreCase("q")) {
                System.out.println("<= Retour à la page arrière");
            }

            else {
                System.out.println("Votre entrée : '" + choice + "' n'est pas valide");
            }
        }
    }
}
