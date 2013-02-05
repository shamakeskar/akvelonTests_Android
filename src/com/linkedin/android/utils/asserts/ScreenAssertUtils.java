package com.linkedin.android.utils.asserts;

import com.linkedin.android.tests.data.DataProvider;

/**
 * The class contains 'assert' methods for screen verification.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 17:20:31 PM
 */
public class ScreenAssertUtils {

    /**
     * Assert if there is expected activity on screen
     * 
     * @param expectedActivityShortClassName
     *            expected activity short class name
     */
    public static void assertValidActivity(String expectedActivityShortClassName) {
        DataProvider.getInstance().getSolo()
                .assertCurrentActivity("Wrong activity: ", expectedActivityShortClassName);
    }
}
