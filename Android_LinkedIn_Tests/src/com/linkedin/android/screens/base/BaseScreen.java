package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.ThreadUtils;

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

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for screen class. Need {@code Solo} object and activity class
     * name.
     * 
     * Standard implement is: {@code super(solo, ACTIVITY_SHORT_CLASSNAME);}
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
            e.printStackTrace();
            Logger.logAllOpenedActivities();
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
     * Assert current activity.
     */
    public void assertCurrentActivity() {
        for (Activity a : getSolo().getAllOpenedActivities()) {
            Logger.d(a.getClass().getName());
        }
        Assert.assertEquals(clazz, getSolo().getCurrentActivity().getClass());
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
    public void verifyToast(String toastText) {
        boolean isToastAppears = getSolo().waitForText(toastText, 1,
                DataProvider.WAIT_DELAY_DEFAULT, false, false);
        Assert.assertTrue("'" + toastText + "'" + " toast is not present.", isToastAppears);
        Logger.i("Following toast appears: " + toastText);
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
        AsyncToastsVerifier asyncToastVerifier = new AsyncToastsVerifier(firstToastText,
                secondToastText, lockObjectForTwoToastsVerification);
        Thread newThread = new Thread(asyncToastVerifier);
        newThread.start();
    }

    /**
     * Verifies two toast in series. Call it <b>after invocation</b> of method
     * that triggers toasts appearance. <b>NOTE:</b> don't miss to call
     * 'verifyTwoToastsStart' method <b>before invocation</b> of method that
     * triggers toasts appearance.
     */
    public void verifyTwoToastsEnd() {
        ThreadUtils.wait(lockObjectForTwoToastsVerification, lockObjectForTwoToastsVerification);
    }

    /***
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

    /***
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
     * Verifies current {@code Activity}.
     */
    protected void verifyCurrentActivity() {
        Assert.assertTrue("Wrong activity " + getActivityShortClassName(), getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(getActivityShortClassName()));
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
         *            text that must be displayed in the toast.
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
            verifyToast(firstToastText);
            verifyToast(secondToastText);
            synchronized (lock) {
                lock.notify();
            }

        }

    }

}