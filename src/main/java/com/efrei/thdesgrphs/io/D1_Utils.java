package com.efrei.thdesgrphs.io;

/**
 * Utils is a utility class with some useful methods
 * */
public class D1_Utils {

    public static String title(String message) {
        int length = message.length();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("-".repeat(length * 3)).append("\n");
        stringBuilder.append(" ".repeat(length));
        stringBuilder.append(message);
        stringBuilder.append(" ".repeat(length)).append("\n");
        stringBuilder.append("-".repeat(length * 3)).append("\n");

        return stringBuilder.toString();
    }

    public static String subTitle(String message) {
        int length = message.length();

        return """
                |==>\040""" + message + "\n";
    }

    public static boolean isAnInt(String message) {
        try {
            Integer.valueOf(message);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
