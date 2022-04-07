package com.efrei.thdesgrphs.io;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.dates.Date;
import com.efrei.thdesgrphs.dates.DateType;
import com.efrei.thdesgrphs.operations.Operations;
import com.efrei.thdesgrphs.operations.Scheduling;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * The goal of the IHM is to simulate the console interface
 * for the user to perform operations on a graph
 * */
public class IHM {

    /**
     * The automaton on which we will perform
     * the operations
     * */
    private static Automaton automaton;
    private static Scanner scanner;

    public static void mainMenu() {
        scanner = new Scanner(System.in);
        var defaultPath = "src/main/java/com/efrei/thdesgrphs/files/";
        var choice = "";

        var file = new File(defaultPath + "tests/");
        var maxFiles = Objects.requireNonNull(file.listFiles()).length;


        while (!Objects.equals(choice, "q")) {
            System.out.println("\n".repeat(3));
            System.out.println(Utils.title("Menu principal"));
            System.out.format("Veuillez choisir un automate : (entrez un nombre entre 1 et %d): ", maxFiles);
            choice = scanner.next();

            // if the choice is an integer
            if (Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);

                if (intChoice >= 1 && intChoice <= maxFiles) {
                    automaton = FileReader.readFile(file.getPath() + "/table " + (intChoice) + ".txt");
                    Operations.showValuesMatrix(automaton);
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

        while (!Objects.equals(choice, "q")) {
            System.out.println("\n".repeat(2));
            System.out.println(Utils.title("Menu des opérations"));

            System.out.print("""
                    1. Affichage de la matrice de d'adjacence
                    2. Affichage de la matrice de valeurs
                    3. Opérations d'ordonnancement
                    q. Retour en arrière
                    
                    Votre choix :\040
                              """);

            choice = scanner.next();

            if (Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);

                switch (intChoice) {
                    case 1 -> Operations.showAdjacencyMatrix(automaton);
                    case 2 -> Operations.showValuesMatrix(automaton);
                    case 3 -> {
                        if(Scheduling.isSchedulingGraph(automaton))
                            datesMenu();
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

        while (!Objects.equals(choice, "q")) {
            System.out.println("\n".repeat(2));
            System.out.println(Utils.title("Calcul des dates"));

            System.out.print("""
                    1. Calcul des dates au plus tôt et au plus tard
                    q. Retour en arrière
                    
                    Votre choix :\040
                              """);

            choice = scanner.next();

            if (Utils.isAnInt(choice)) {
                var intChoice = Integer.parseInt(choice);
                if (intChoice == 1) {

                    if (!automaton.getRanks().isEmpty())
                        System.out.println("Les rangs de ce graphe ont déjà été calculés : " + automaton.getRanks());
                    else
                        automaton.calculateRanks();

                    Date date = new Date(automaton);
                    date.buildDates();
                    date.printPrettyDates(DateType.SOONEST);
                    date.printPrettyDates(DateType.LATEST);
                    date.printPrettyDates(DateType.MARGINS);
                    System.out.println(date);
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
