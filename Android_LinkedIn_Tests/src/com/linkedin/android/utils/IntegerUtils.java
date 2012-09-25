package com.linkedin.android.utils;

/**
 * The class contains util methods for {@code Integer} and {@code int}.
 * 
 * @author vasily.gancharov
 * @created Sep 19, 2012 20:05:37 PM
 */
public class IntegerUtils {

    /**
     * Converts {@code stringToConvert} to {@code int} safely
     * 
     * @param stringToConvert
     *            {@code String} to convert
     * @param defaultValue
     *            default value
     * @return {@code stringToConvert} value converted to {@code int} or
     *         {@code defaultValue} if conversion impossible
     */
    public static int getValueOfSafely(String stringToConvert, int defaultValue) {
        if (null == stringToConvert) {
            return defaultValue;
        }

        int convertedValue = 0;
        try {
            convertedValue = Integer.valueOf(stringToConvert);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
        return convertedValue;
    }
}
