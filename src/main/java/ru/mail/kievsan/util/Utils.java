package ru.mail.kievsan.util;

public class Utils {

    public static String capitalize(String name) {
        String first = name.substring(0, 1);
        return name.replaceFirst(first, first.toUpperCase());
    }

}
