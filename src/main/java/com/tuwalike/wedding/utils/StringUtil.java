package com.tuwalike.wedding.utils;

public class StringUtil {

    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
