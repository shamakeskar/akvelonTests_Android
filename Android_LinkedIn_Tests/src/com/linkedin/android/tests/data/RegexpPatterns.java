package com.linkedin.android.tests.data;

import java.util.regex.Pattern;

/**
 * Class for store regexp {@code Pattern}-s.
 * For get {@code String} for solo.searchText use PATTERN.pattern() method.
 * 
 * @author alexander.makarov
 * @created Sep 27, 2012 3:41:55 PM
 */
public final class RegexpPatterns {
    // CONSTANTS ------------------------------------------------------------
	// RegExp for news date (like '10 hours ago').
    public final static Pattern DATE_LIKE_10_HOURS_AGO = Pattern.compile("^(\\d{1,}) (\\D{1,}) ago$");
    // RegExp for news date (like 'October 1').
    public final static Pattern DATE_LIKE_OCTOBER_1 = Pattern.compile("^(\\D{1,}) (\\d{1,2})$");
    // RegExp for "Like" or "Comment".
    public final static Pattern LIKE_OR_COMMIT = Pattern.compile(".*Like.*|.*Comment.*");
    // RegExp for string like "10 followers".
    public final static Pattern LIKE_10_FOLLOWERS = Pattern.compile("^followers$");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
}
