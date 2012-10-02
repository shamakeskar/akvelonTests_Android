package com.linkedin.android.utils;

import java.util.ArrayList;

import junit.framework.Assert;
import android.graphics.Point;
import android.view.View;
import android.widget.ProgressBar;

import com.linkedin.android.tests.data.DataProvider;

/**
 * Class for wait actions.
 * 
 * @author alexander.makarov
 * @created Aug 9, 2012 3:59:34 PM
 */
public final class WaitActions {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------

    /**
     * Default delay function.
     */
    public static void waitForScreenUpdate() {
        DataProvider.getInstance().getSolo().sleep(DataProvider.DEFAULT_DELAY_TIME);
    }

    /**
     * Custom delay function.
     * 
     * @param sec
     *            is time in seconds.
     */
    public static void delay(int sec) {
        DataProvider.getInstance().getSolo().sleep(sec * 1000);
    }

    /**
     * Custom delay function.
     * 
     * @param sec
     *            is time in seconds.
     */
    public static void delay(float sec) {
        sec *= 1000.0;
        int time = ((Float) sec).intValue();
        DataProvider.getInstance().getSolo().sleep(time);
    }

    /**
     * Waiting until the coordinates of "view" will not "location"
     * 
     * @param view
     *            is view for check coordinates
     * @param location
     *            is {@code Point} object of expected location
     * @param timeout
     *            is timeout. If 0 then will be
     *            {@code DataProvider.WAIT_DELAY_DEFAULT}
     * @param message
     *            is error message
     */
    public static void waitForViewLocation(View view, Point location, int timeout, String message) {
        if (timeout == 0)
            timeout = DataProvider.WAIT_DELAY_DEFAULT;
        int stepCounter = timeout / DataProvider.WAIT_DELAY_STEP;
        int[] xy = new int[2];
        for (int i = 0; i <= stepCounter; i++) {
            view.getLocationInWindow(xy);
            if (xy[0] == location.x && xy[1] == location.y) {
                return;
            }
            DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_STEP);
        }
        Assert.fail(message != null ? message : "Cannot wait while view location is " + location.x
                + "x" + location.y);
    }

    /**
     * Waiting until the coordinates and size of "view" will not equals
     * coordinates and size of "rect".
     * 
     * @param view
     *            is view for check coordinates
     * @param rect
     *            is {@code Rect2DP} object of expected location
     * @param timeout
     *            is timeout. If 0 then will be
     *                       {@DataProvider.WAIT_DELAY_DEFAULT
     * 
     * 
     * 
     * 
     * 
     * 
     * }
     * @param message
     *            is error message
     */
    public static void waitForViewRect(View view, Rect2DP rect, int timeout, String message) {
        if (timeout == 0)
            timeout = DataProvider.WAIT_DELAY_DEFAULT;
        int stepCounter = timeout / DataProvider.WAIT_DELAY_STEP;
        for (int i = 0; i <= stepCounter; i++) {
            if (rect.equals(new Rect2DP(view))) {
                return;
            }

            DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_STEP);
        }
        Assert.fail(message != null ? message : "Cannot wait while view location is " + rect.x
                + "x" + rect.y + "and size " + rect.width + "x" + rect.height);
    }

    /**
     * Method for wait multiply activities.
     * 
     * @param activitiesNames
     *            is array with {@code Activity} names
     * @param timeout
     *            is timeout for wait. If <b>0</b> then will
     *            {@code DataProvider.WAIT_DELAY_DEFAULT}
     * @param message
     *            error message. If <b>null</b> then generated automatically.
     */
    public static void waitMultiplyActivities(String[] activitiesNames, int timeout, String message) {
        if (timeout == 0)
            timeout = DataProvider.WAIT_DELAY_DEFAULT;
        int stepCounter = timeout / DataProvider.WAIT_DELAY_STEP;
        for (int i = 0; i <= stepCounter; i++) {
            for (int j = 0; j < activitiesNames.length; j++) {
                if (DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                        .getSimpleName().equals(activitiesNames[j])) {
                    return;
                }
            }
            DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_STEP);
        }
        if (message == null) {
            StringBuilder builder = new StringBuilder("Cannot wait to launch activities '");
            for (int j = 0; j < activitiesNames.length; j++) {
                if (j > 0)
                    builder.append(", ");
                builder.append(activitiesNames[j]).append("'");
            }
            message = builder.toString();
        }
        Assert.fail(message);
    }

    /**
     * Method for wait multiply activities.
     * 
     * @param activitiesNames
     *            is array with {@code Activity} names
     */
    public static void waitMultiplyActivities(String[] activitiesNames) {
        WaitActions.waitMultiplyActivities(activitiesNames, 0, null);
    }

    /**
     * Method for wait single activity.
     * 
     * @param activityShortName
     *            is name of Activity for wait
     * @param screenName
     *            is name of screen to log error message
     */
    public static void waitSingleActivity(String activityShortName, String screenName) {
        Assert.assertTrue(
                "Cannot wait to launch activity for " + screenName + " screen",
                DataProvider.getInstance().getSolo()
                        .waitForActivity(activityShortName, DataProvider.WAIT_DELAY_DEFAULT));
    }

    /**
     * Waits until {@code ProgressBar} with index <b>indexOfProgressBar</b> is
     * disappear or <b>timeoutInMs</b> is over.
     * 
     * @param indexOfProgressBar
     *            - index of {@code ProgressBar} for check in
     *            solo.getCurrentProgressBars().
     * @param timeoutInMs
     *            - timeout in milliseconds.
     */
    public static void waitForProgressBarDisappear(int indexOfProgressBar, int timeoutInMs) {
        // List of progress bars.
        ArrayList<ProgressBar> progressBars = DataProvider.getInstance().getSolo()
                .getCurrentProgressBars();
        // If list size less that index then wait is complete.
        if (progressBars.size() < indexOfProgressBar + 1)
            return;
        // ProgressBar for check.
        ProgressBar pb = progressBars.get(indexOfProgressBar);
        // Id of ProgressBar at start of helper.
        int idForCheck = pb.getId();
        
        int waitStepsCount = timeoutInMs / DataProvider.WAIT_DELAY_STEP;
        for (int i = 0; i < waitStepsCount; i++) {
            progressBars = DataProvider.getInstance().getSolo().getCurrentProgressBars();
            // If list size less that index then wait is complete.
            if (progressBars.size() < indexOfProgressBar + 1){
                return;
            }
            pb = progressBars.get(indexOfProgressBar);
            // If it other or hidden ProgressBar then wait is complete.
            if (pb.getId() != idForCheck || pb.getWidth() == 0 || pb.getHeight() == 0){
                return;
            }

            delay(DataProvider.WAIT_DELAY_STEP * 0.001f);
        }
        Assert.fail("Timeout error for progress bar");
    }

    /**
     * Waits until {@code ProgressBar} disappear from screen. If
     * {@code ProgressBar} does not disappear than shows error.
     */
    public static void waitForProgressBarDisappear() {
        waitForProgressBarDisappear(0, DataProvider.WAIT_PROGRESSBAR_DISAPPEAR);
    }

    /**
     * Wait appearance of {@code textToWait} on current screen for following
     * time: DataProvider.WAIT_DELAY_DEFAULT. If {@code textToWait} does not
     * appear then exception with {@code errorMesage} occurs.
     * 
     * @param textToWait
     *            text that must appear on current screen
     * @param errorMesage
     *            if {@code textToWait} does not appear then exception with this
     *            message occurs.
     */
    public static void waitForText(String textToWait, String errorMessage) {
        if (null == textToWait || null == errorMessage) {
            return;
        }
        boolean isTextPresented = DataProvider.getInstance().getSolo()
                .waitForText(textToWait, 1, DataProvider.WAIT_DELAY_DEFAULT);
        Assert.assertTrue(errorMessage, isTextPresented);
    }

    /**
     * Waits until <b>progressBarView</b> progress is <b>desiredProgress</b> or
     * <b>timeoutInMs</b> is over.
     * 
     * @param progressBarView
     *            - view of progress bar for check.
     * @param timeoutInMs
     *            - timeout in milliseconds.
     * @param desiredProgress
     *            - expected value of progress.
     */
    public static void waitForProgressBarFill(ProgressBar progressBarView, int timeoutInMs,
            int desiredProgress) {
        int waitStepsCount = timeoutInMs / DataProvider.WAIT_DELAY_STEP;
        for (int i = 0; i < waitStepsCount; i++) {
            if (progressBarView.getProgress() >= desiredProgress) {
                return;
            }
            delay(DataProvider.WAIT_DELAY_STEP * 0.001f);
        }
        Assert.fail("Timeout error for progress bar");
    }

    /**
     * Waits until <b>progressBarView</b> progress is 100 or <b>timeoutInMs</b>
     * is over.
     * 
     * @param progressBarView
     *            - view of progress bar for check
     * @param timeoutInMs
     *            - timeout in milliseconds.
     */
    public static void waitForProgressBarFill(ProgressBar progressBarView, int timeoutInMs) {
        waitForProgressBarFill(progressBarView, timeoutInMs, 100);
    }
}
