package com.tuwalike.wedding.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MD5Generator {

    public static String generateHash(String username) {
        try {
            // Get current date in the format "dd-MM-yyyy"
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(formatter);

            // Concatenate username and formatted current date with a pipe
            String input = username + "|" + formattedDate;

            // Create a MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Digest the input byte array, and then convert the hash into a hexadecimal
            // string
            byte[] digest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // MD5 algorithm should always be available, but fall back to null for error
                         // handling
        }
    }

}
