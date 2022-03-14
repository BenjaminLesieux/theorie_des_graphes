package com.efrei.thdesgrphs;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.io.FileReader;


public class Main {

    public static void main(String[] args) {
        Automaton a = FileReader.readFile("/Users/benjaminlesieux/Desktop/Théorie des graphes/src/main/java/com/efrei/thdesgrphs/files/test.txt");
        System.out.println(a);
    }
}
