package com.linkedin.android.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;

import com.linkedin.android.tests.BaseTestCase;

/**
 * The class contains util methods for tested application management
 * 
 * @author vasily.gancharov
 * @created Aug 24, 2012 15:55:37 PM
 */
public final class ApplicationUtils {

    /**
     * Moves tested application to stack background, wait for specified
     * {@code timeMs} and return the application to foreground then.
     * 
     * NOTE: you must recreate you 'screen' object (object of class extended
     * from {@code BaseScreen}) to do any actions with it after running this
     * method.
     * 
     * @param timeMs
     *            tested application stay in stack background during this time
     *            (in <b>milliseconds</b>). <b>Min possible value:</b> 5000
     *            milliseconds
     * @param testCase
     *            currently running test case
     * @return <b>true</b> if application was moved to stack background and back
     *         to foreground then <b>false</b> otherwise
     * 
     */
    public static boolean moveApplicationToBackgroundForTime(int timeMs, BaseTestCase testCase) {

        final int activityDefaultRestoreTimeMs = 2000;
        final int minPossibleTimeoutMs = 5000;

        if (timeMs < minPossibleTimeoutMs) {
            return false;
        }

        WeakReference<Activity> currentTestedApplicationActivity = getCurrentActivityWeakRefSafely(testCase);
        if (!moveActivityInStackToBackground(currentTestedApplicationActivity.get())) {
            return false;
        }

        ThreadUtils.Sleep(timeMs);

        boolean isActivityAtFrontInStack = moveActivityInStackToFront(
                currentTestedApplicationActivity.get(), testCase);

        ThreadUtils.Sleep(activityDefaultRestoreTimeMs);

        return isActivityAtFrontInStack;
    }

    /**
     * Move specified {@code activity} to stack background
     * 
     * @param activity
     *            {@code Activity} to move to stack background
     * @return <b>true</b> if {@code activity} was moved to stack background
     *         <b>false</b> otherwise
     */
    public static boolean moveActivityInStackToBackground(Activity activity) {
        if (null == activity) {
            return false;
        }

        HardwareActions.pressHome();
        return true;
    }

    /**
     * Move specified {@code activity} to stack foreground
     * 
     * @param activity
     *            {@code Activity} to move to stack foreground
     * @param testCase
     *            currently running test case
     * @return <b>true</b> if {@code activity} was moved to stack foreground
     *         <b>false</b> otherwise
     */
    public static boolean moveActivityInStackToFront(Activity activity, BaseTestCase testCase) {
        if (null == testCase) {
            return false;
        }

        Intent intent = prepareIntentToMoveActivityToFront(activity);
        if (null == intent) {
            return false;
        }

        Activity currentActivity = getCurrentActivitySafely(testCase);
        if (null == currentActivity) {
            return false;
        }
        currentActivity.startActivity(intent);
        return true;
    }

    /**
     * Gets current {@code Activity} from screen safely
     * 
     * @param testCase
     *            currently running test case
     * @return current {@code Activity} from screen or <b>null</b> if
     *         {@code testCase} is null
     */
    public static Activity getCurrentActivitySafely(BaseTestCase testCase) {
        if (null == testCase) {
            return null;
        }
        Activity currentActivity = testCase.getActivity();
        return currentActivity;
    }

    /**
     * Gets {@code WeakReference} to current {@code Activity} from screen safely
     * 
     * @param testCase
     *            currently running test case
     * @return {@code WeakReference} to current {@code Activity} from screen
     *         NOTE: it can be {@code WeakReference} to <b>null</b> if
     *         {@code testCase} is null
     */
    public static WeakReference<Activity> getCurrentActivityWeakRefSafely(BaseTestCase testCase) {
        WeakReference<Activity> currentActivityWeakRef = new WeakReference<Activity>(
                getCurrentActivitySafely(testCase));
        return currentActivityWeakRef;
    }

    /**
     * Prepares {@code Intent} to move {@code activity} to stack foreground
     * 
     * @param activity
     *            {@code Activity} that must be moved to stack foreground
     * @return {@code Intent} to move {@code activity} to stack foreground or
     *         <b>null</b> if {@code activity} is null
     */
    private static Intent prepareIntentToMoveActivityToFront(Activity activity) {
        if (null == activity) {
            return null;
        }

        Intent intent = activity.getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

}
