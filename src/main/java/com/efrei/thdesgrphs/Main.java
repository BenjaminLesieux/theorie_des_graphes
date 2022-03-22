package com.efrei.thdesgrphs;

import com.efrei.thdesgrphs.automaton.Automaton;
import com.efrei.thdesgrphs.io.FileReader;
import com.efrei.thdesgrphs.operations.Operations;
import com.efrei.thdesgrphs.operations.Scheduling;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        Automaton a = FileReader.readFile("/Users/benjaminlesieux/Desktop/Théorie des graphes/src/main/java/com/efrei/thdesgrphs/files/SiLuiPasseToutPasse.txt");
        System.out.println(a);
        System.out.println(Scheduling.hasCircuits(a));
        Scheduling.addAlphaState(a);
        System.out.println(a);
        a.calculateValuesMatrix();
        Operations.showValuesMatrix(a.getValuesMatrix());
    }
}
