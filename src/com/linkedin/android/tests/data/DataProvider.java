package com.linkedin.android.tests.data;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.utils.Registry;
import com.linkedin.android.tests.utils.Tag;

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
    // Logcat tag for find logs from this app.
    private static final String LOGCAT_TAG = "LinkedIn_Android_Tests";
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
    public static final float WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED = 10.0f;
    // Default timeout for wait until progress bar disappear.
    public static final int WAIT_PROGRESSBAR_DISAPPEAR = 4000;
    // Delay in end each action before take screenshot and start next action.
    public static final int WAIT_DELAY_FOR_SCREENSHOTS = 5000;
    // Delay for waiting progress bar
    public static final int WAIT_DELAY_FOR_PROGRESS_BAR_STEP = 1000;

    // PROPERTIES -----------------------------------------------------------
    // Activity of apk to test.
    private Activity activity;
    // Registry object.
    private Registry registry;
    // Solo object.
    private Solo solo;
    // Current test ID.
    private String currentTestId;
    // Context object.
    private Context context;
    // Tags array.
    private ArrayList<Tag> tags;
    // Tags counter.
    private int tagsCounter;

    // Current test tag.
    private String currentTestTag;

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
     * Returns log tag.
     * 
     * @return log tag.
     */
    public String getLogTag() {
        if (currentTestTag != null)
            return LOGCAT_TAG + "#" +currentTestTag;
        else
            return LOGCAT_TAG;
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
     * @param currentTestTag
     *            current test tag
     */
    public void setCurrentTestIdAndTag(String currentTestId, String currentTestTag) {
        this.currentTestId = currentTestId;
        this.currentTestTag = currentTestTag;
    }

    /**
     * Returns Activity of app from JUnit.
     * 
     * @return Activity object
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Sets Activity for this app.
     * 
     * @param activity
     *            is Activity object.
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Sets Context for this app.
     * 
     * @param context
     *            is Context object.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Returns Context object.
     * 
     * @return Context object
     */
    public Context getContext() {
        return context;
    }

    /**
     * Returns tags array.
     * 
     * @return tags array
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Sets tags array.
     * 
     * @param tags
     *            is tags array
     */
    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Returns counter of tags.
     * 
     * @return counter of tags
     */
    public int getTagsCounter() {
        currentTestTag = tags.get(tagsCounter).getName();
        return tagsCounter;
    }

    /**
     * Sets counter of tags.
     * 
     * @param tagsCounter
     *            is counter of tags.
     */
    public void setTagsCounter(int tagsCounter) {
        this.tagsCounter = tagsCounter;
    }

    /**
     * Returns {@code Registry} object.
     * 
     * @return {@code Registry} object.
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * Sets {@code Registry} object.
     * 
     * @param registry
     *            the {@code Registry} object
     */
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
