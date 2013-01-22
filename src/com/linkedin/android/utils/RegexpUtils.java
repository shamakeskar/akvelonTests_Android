package com.linkedin.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class contains constants and util methods for regular expressions.
 * 
 * @author vasily.gancharov
 * @created Sep 10, 2012 13:10:31 PM
 */
public class RegexpUtils {

    // CONSTANTS ------------------------------------------------------------
    public static final String HYPERLINK = "((http\\://|https\\://|ftp\\://)|(www.))+(([a-zA-Z0-9\\.-]+\\.[a-zA-Z]{2,4})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(/[a-zA-Z0-9%:/-_\\?\\.'~]*)?";

    public static final Pattern HYPERLINK_PATTERN = Pattern.compile(HYPERLINK);
    public static final Pattern TWITTER_HANDLE = Pattern.compile("(@[a-zA-Z0-9_]+)");
    public static final Pattern TWITTER_HASH_TAGS = Pattern.compile("(#[a-zA-Z0-9_-]+)");

    // METHODS --------------------------------------------------------------
    /**
     * Checks whether {@code pattern} can be found in {@code sourceString}
     * 
     * @param sourceString
     *            {@code String} to search pattern
     * @param pattern
     *            {@code Pattern} that must be found in {@code sourceString}
     * @return <b>true</b> if {@code pattern} can be found in
     *         {@code sourceString}, <b>false</b> otherwise
     */
    public static boolean isCanBeFound(String sourceString, Pattern pattern) {
        if (null == sourceString || null == pattern) {
            return false;
        }
        Matcher matcher = pattern.matcher(sourceString);
        return matcher.find();
    }

    /**
     * Gets value of found group if {@code sourceString} matches to
     * {@code pattern} and there is group with {@code groupNumber}.
     * 
     * @param sourceString
     *            {@code String} to search pattern
     * @param pattern
     *            {@code Pattern} that must be found in {@code sourceString}
     * @param groupNumber
     *            number of required group found in {@code sourceString}
     * @return value of found group if {@code sourceString} matches to
     *         {@code pattern} and there is group with {@code groupNumber},
     *         <b>null</b> otherwise.
     */
    public static String getFoundGroup(String sourceString, Pattern pattern, int groupNumber) {
        final int minPossibleGroupNumber = 0;
        if (null == sourceString || null == pattern || groupNumber < minPossibleGroupNumber) {
            return null;
        }

        Matcher matcher = pattern.matcher(sourceString);
        boolean isMatchesFound = matcher.find();
        if (!isMatchesFound) {
            return null;
        }
        int foundGroupsCount = matcher.groupCount();
        if (groupNumber > foundGroupsCount) {
            return null;
        }
        String foundGroupValue = matcher.group(groupNumber);
        return foundGroupValue;
    }

    /**
     * Gets value of first found group if {@code sourceString} matches to
     * {@code pattern}.
     * 
     * @param sourceString
     *            {@code String} to search pattern
     * @param pattern
     *            {@code Pattern} that must be found in {@code sourceString}
     * @return value of first found group if {@code sourceString} matches to
     *         {@code pattern}, <b>null</b> otherwise.
     */
    public static String getFirstFoundGroup(String sourceString, Pattern pattern) {
        final int firstFoundGroupNumber = 0;
        return getFoundGroup(sourceString, pattern, firstFoundGroupNumber);
    }
}
