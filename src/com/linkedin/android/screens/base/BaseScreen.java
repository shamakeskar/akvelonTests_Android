package com.linkedin.android.screens.base;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.StringDefaultValues;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.ThreadUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;

/**
 * Base screen class. Abstract.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 4:19:55 PM
 */
public abstract class BaseScreen<T extends Activity> {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    protected String screenName;
    private Class<T> clazz;

    private final Object lockObjectForTwoToastsVerification = new Object();
    private static String exceptionFromVerifyTwoToasts;
    private static boolean isStopThreadFromVerifyTwoToasts;
    public static final String MENU_ITEM_REFRESH = "Refresh";

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for screen class. Need {@code Solo} object and activity class
     * name.
     * 
     * Standard implement is: {@code super(solo, ACTIVITY_SHORT_CLASSNAME);}
     * 
     * @param activityName
     *            is activity class name
     */
    @SuppressWarnings("unchecked")
    public BaseScreen(String activityName) {
        waitForMe();

        try {
            this.clazz = (Class<T>) Class.forName(activityName);
        } catch (ClassNotFoundException e) {
            Logger.e("Wrong activity name '" + activityName + "'", e);
            Logger.logAllOpenedActivities();
            Assert.fail("Wrong activity name '" + activityName + "'");
        }
        verify();
    }

    // ABSTRACT METHODS -----------------------------------------------------
    /**
     * Verify screen.
     */
    abstract public void verify();

    /**
     * Method for wait launch screen activity.
     * 
     * Standard implement is:
     * {@code Assert.assertTrue(getSolo().waitForActivity(ACTIVITY_SHORT_CLASSNAME,
     *         DataProvider.WAIT_DELAY_DEFAULT));}
     */
    abstract public void waitForMe();

    /**
     * Returns screen {@code Activity} short class name.
     * 
     * Standard implement is: {@code return ACTIVITY_SHORT_CLASSNAME;}
     * 
     * @return screen {@code Activity} short class name.
     */
    abstract public String getActivityShortClassName();

    // METHODS --------------------------------------------------------------
    /**
     * Method for reduce amount of text for get Solo object.
     * 
     * @return Solo object.
     */
    protected static Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }

    /**
     * Method for reduce amount of text for get current test ta.
     * 
     * @return current test tag.
     */
    protected static String getTag() {
        return DataProvider.getInstance().getCurrentTestTag();
    }

    /**
     * Checks that opened current activity.
     * 
     * @return <b>true</b> if opened current activity.
     */
    public boolean isCurrentActivityOpened() {
        if (clazz.equals(getSolo().getCurrentActivity().getClass())) {
            return true;
        }
        return false;
    }

    // TODO move 'verifyToast', 'verifyTwoToasts' methods and
    // 'AsyncToastVerifier' class to separated util class
    /**
     * Verifies toast.
     * 
     * @param toastText
     *            text that must be displayed in the toast.
     */
    public void verifyToast(final String toastText) {
        WaitActions.waitForTrueInFunction("'" + toastText + "'" + " toast is not present.",
                new Callable<Boolean>() {
                    public Boolean call() {
                        if (TextViewUtils.getTextViewByText(toastText) != null){
                            Logger.i("Following toast appears: " + toastText);
                            return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * Verifies two toast in series. Call it <b>before invocation</b> of method
     * that triggers toasts appearance. <b>NOTE:</b> don't miss to call
     * 'verifyTwoToastsEnd' method <b>after invocation</b> of method that
     * triggers toasts appearance.
     * 
     * @param firstToastText
     *            text that must be displayed in first toast.
     * @param secondToastText
     *            text that must be displayed in second toast.
     */
    public void verifyTwoToastsStart(String firstToastText, String secondToastText) {
        exceptionFromVerifyTwoToasts = StringDefaultValues.EMPTY_STRING;
        AsyncToastsVerifier asyncToastVerifier = new AsyncToastsVerifier(firstToastText,
                secondToastText, lockObjectForTwoToastsVerification);
        isStopThreadFromVerifyTwoToasts = false;
        Thread thread = new Thread(asyncToastVerifier);
        thread.start();
    }

    /**
     * Verifies two toast in series. Call it <b>after invocation</b> of method
     * that triggers toasts appearance. <b>NOTE:</b> don't miss to call
     * 'verifyTwoToastsStart' method <b>before invocation</b> of method that
     * triggers toasts appearance.
     */
    public void verifyTwoToastsEnd() {
        ThreadUtils.wait(lockObjectForTwoToastsVerification, lockObjectForTwoToastsVerification);
        isStopThreadFromVerifyTwoToasts = true;

        if (!StringUtils.isNullOrEmpty(exceptionFromVerifyTwoToasts)) {
            Assert.fail(exceptionFromVerifyTwoToasts);
        }
    }

    /**
     * Types text to specified text field
     * 
     * @param fieldIndex
     *            index of field to type text
     * @param fieldName
     *            name of field to type text
     * @param text2Type
     *            text to type to specified field
     */
    protected void typeTextToField(int fieldIndex, String fieldName, String text2Type) {
        EditText editText = getSolo().getEditText(fieldIndex);
        Assert.assertNotNull(fieldName + " field is not found", editText);
        Logger.i("Enter '" + text2Type + "' in " + fieldName + " field");
        getSolo().enterText(fieldIndex, text2Type);
    }

    /**
     * Taps on specified {@code button}
     * 
     * @param button
     *            button to tap
     * @param buttonName
     *            the button name
     */
    protected void tapOnButton(ImageView button, String buttonName) {
        Assert.assertNotNull("'" + buttonName + "' button not present", button);

        Logger.i("Tapping on '" + buttonName + "' button");
        getSolo().clickOnView(button);
    }

    /**
     * Verifies current {@code Activity} (returned by
     * getActivityShortClassName()).
     */
    protected void verifyCurrentActivity() {
        String currentActivity = getSolo().getCurrentActivity().getClass().getSimpleName();
        Assert.assertTrue("Wrong activity: expected '" + getActivityShortClassName()
                + "' but get '" + currentActivity + "'",
                currentActivity.equals(getActivityShortClassName()));
    }

    /**
     * The class to verify two toasts asynchronously.
     * 
     * @author vasily.gancharov
     * @created Aug 29, 2012 15:47:31 AM
     */
    private class AsyncToastsVerifier implements Runnable {
        // VARIABLES _----------------------------------
        private String firstToastText;
        private String secondToastText;
        private Object lock;

        // CONSTRUCTORS --------------------------------
        /**
         * Constructor for AsyncToastVerifier class
         * 
         * @param firstToastText
         *            text that must be displayed in the first toast.
         * @param secondToast
         *            text that must be displayed in the second toast.
         * @param lock
         *            object to lock thread
         */
        public AsyncToastsVerifier(String firstToastText, String secondToast, Object lock) {
            this.firstToastText = firstToastText;
            this.secondToastText = secondToast;
            this.lock = lock;
        }

        // METHODS ----------------------------
        /**
         * Verifies toast specified in constructor
         */
        public void run() {
            if (null == lock) {
                return;
            }
            try {
                // Check toasts.
                verifyToast(firstToastText);
                verifyToast(secondToastText);
            } catch (final AssertionFailedError e) {
                // If receive exception then save it
                exceptionFromVerifyTwoToasts = e.getMessage();
            }
            // Call notify to break wait for "lock"
            while (!isStopThreadFromVerifyTwoToasts)
                synchronized (lock) {
                    lock.notify();
                }
        }

    }

}