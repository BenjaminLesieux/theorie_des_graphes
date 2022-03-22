package com.efrei.thdesgrphs.operations;

import com.efrei.thdesgrphs.automaton.Automaton;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Operations is a class composed with only static methods
 * The purpose of this class is to perform operations on
 * any graph we give.
 *
 * Therefore, any operations on a graph will be available in
 * this class.
 *
 * */
public class Operations {


    public static <T> void showValuesMatrix(int[][] matrix) {
        StringBuilder stringBuilder = new StringBuilder();

        // this is to determine the size of the biggest value in order to make a pretty screen
        IntSummaryStatistics stats = Arrays.stream(matrix).flatMapToInt(Arrays::stream).summaryStatistics();
        int maxLength = String.valueOf(stats.getMax()).length(); // if max = 12 then maxLength = 2

        System.out.println(Arrays.deepToString(matrix)
                .replace("], ", "]\n")
                .replace("[", "")
                .replace("]", "")
                .replace(",", "  | ")
                .replace("-1", "_")
        );
    }
}
