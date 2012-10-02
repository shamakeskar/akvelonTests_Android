package com.linkedin.android.tests.data;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class to store common data. Singleton.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 11:51:17 AM
 */
public class DataProvider {
    // CONSTANTS ------------------------------------------------------------
    // Instance of DataProvider.
    private static volatile DataProvider instance;
    // Target package.
    public static final String TARGET_PACKAGE_ID = "com.linkedin.android";
    // Launcher activity class names.
    public static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.linkedin.android.authenticator.LaunchActivity";
    public static final String LAUNCHACTIVITY_SHORT_CLASSNAME = "LaunchActivity";
    // Default time delay in ms.
    public static final int DEFAULT_DELAY_TIME = 2000;
    // Default delay step in ms.
    public static final int WAIT_DELAY_STEP = 200;
    // Short time delay in ms.
    public static final int WAIT_DELAY_SHORT = 10000;
    // Default time delay in ms.
    public static final int WAIT_DELAY_DEFAULT = 20000;
    // Long time delay in ms.
    public static final int WAIT_DELAY_LONG = 50000;
    // Delay for Save/Unsave button change in sec.
    public static final float WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED = 5.0f;
    // Default timeout for wait until progress bar disappear.
    public static final int WAIT_PROGRESSBAR_DISAPPEAR = 4000;

    // PROPERTIES -----------------------------------------------------------
    // Solo object.
    private Solo solo;
    // Current test tag.
    private String currentTestTag;
    // Current test ID.
    private String currentTestId;
    // Current test name.
    private String currentTestName;

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Return instance of singleton.
     * 
     * @return DataProvider singleton.
     */
    public static DataProvider getInstance() {
        if (instance == null)
            synchronized (DataProvider.class) {
                if (instance == null)
                    instance = new DataProvider();
            }
        return instance;
    }

    /**
     * Returns current test Tag (for logging).
     * 
     * @return currentTestTag
     */
    public String getCurrentTestTag() {
        return currentTestTag;
    }

    /**
     * Sets current test tag.
     * 
     * @param currentTestTag
     *            is test tag to set
     */
    public void setCurrentTestTag(String currentTestTag) {
        this.currentTestTag = currentTestTag;
    }

    /**
     * Returns Solo object.
     * 
     * @return Solo object
     */
    public Solo getSolo() {
        return solo;
    }

    /**
     * Sets solo object from BaseTestCase.
     * 
     * @param solo
     *            is solo object.
     */
    public void setSolo(Solo solo) {
        this.solo = solo;
    }

    /**
     * Returns current test ID.
     * 
     * @return current test ID
     */
    public String getCurrentTestId() {
        return currentTestId;
    }

    /**
     * Sets current test ID.
     * 
     * @param currentTestId
     *            current test ID
     * @param currentTestName
     *            current test name
     */
    public void setCurrentTestIdAndName(String currentTestId, String currentTestName) {
        this.currentTestId = currentTestId;
        this.currentTestName = currentTestName;
    }

    /**
     * Returns current test name.
     * 
     * @return current test name
     */
    public String getCurrentTestName() {
        return currentTestName;
    }
}
