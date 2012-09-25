package com.linkedin.android.utils.asserts;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;

import android.app.Activity;

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
     *              expected activity short class name
     */
    public static void assertValidActivity(String expectedActivityShortClassName) {
        Solo solo = DataProvider.getInstance().getSolo();
        Activity currentActivity = solo.getCurrentActivity();
        Assert.assertNotNull("There is no current activity.", currentActivity);
        String currentActivityClassName = currentActivity.getClass().getName();
        solo.assertCurrentActivity("Wrong activity (expected '" + expectedActivityShortClassName
                + "', get '" + currentActivityClassName + "')", expectedActivityShortClassName);
    }
}
