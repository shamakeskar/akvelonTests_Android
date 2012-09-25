package com.linkedin.android.utils;

import java.util.Random;

/**
 * The class contains util methods for String.
 * 
 * @author vasily.gancharov
 * @created Aug 17, 2012 10:07:31 AM
 */
public final class StringUtils {

    /**
     * Generates string of specified length randomly
     * 
     * @param stringLength
     *            required string length
     * @return {@code String} random string of specified length
     */
    public static String generateStringRandomly(int stringLength) {
        final char[] possibleChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ "
                .toCharArray();
        final int lastPossibleCharIndex = possibleChars.length;
        final int mixPossibleGeneratedStringLength = 1;

        if (stringLength < mixPossibleGeneratedStringLength) {
            return StringDefaultValues.EMPTY_STRING;
        }

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            int randomCharIndex = random.nextInt(lastPossibleCharIndex);
            char randomChar = possibleChars[randomCharIndex];
            stringBuilder.append(randomChar);
        }
        String randomString = stringBuilder.toString();
        return randomString;
    }

    /**
     * Checks if strings equal
     * 
     * @param str1
     *            first string
     * @param str2
     *            second string
     * @param isStrictEqual
     *            checks if strings strict equals if <b>true</b> checks if
     *            {@code str1} contains {@code str2} otherwise
     * @return <b>true</b> if {@code str1} and {@code str2} are equal
     *         <b>false</b> otherwise.
     */
    public static boolean isStringsEquals(String str1, String str2, boolean isStrictEqual) {
        if (null == str1) {
            return false;
        }
        if (isStrictEqual) {
            return str1.equals(str2);
        }
        return str1.indexOf(str2) != -1;
    }

    /**
     * Indicates whether the specified string is null or an Empty string.
     * 
     * @param The
     *            string to test.
     * @return true if the value parameter is null or an empty string ("");
     *         otherwise, false.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * Indicates whether a specified string is null, empty, or consists only of
     * white-space characters.
     * 
     * @param The
     *            string to test.
     * @return true if the value parameter is null or empty string (""), or if
     *         value consists exclusively of white-space characters.
     */
    public static boolean isNullOrWhitespace(String s) {
        return s == null || isWhitespace(s);
    }

    /**
     * Indicates whether the characters in a specified string is categorized as
     * white space.
     * 
     * @param s
     *            string.
     * @return true if the characters in s is white space; otherwise, false.
     */
    public static boolean isWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
