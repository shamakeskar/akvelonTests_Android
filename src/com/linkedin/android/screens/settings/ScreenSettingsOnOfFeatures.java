package com.linkedin.android.screens.settings;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

@SuppressWarnings("rawtypes")
public class ScreenSettingsOnOfFeatures extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.debug.ConfigFeaturesActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ConfigFeaturesActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettingsOnOfFeatures() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        Assert.assertTrue("Checbox's are not found.", getSolo().getCurrentCheckBoxes().size() > 0);

        // Scroll down.
        Logger.i("Scroll screen down");
        Assert.assertTrue("Scroll down not performed", getSolo().scrollDown());

        AssertButton("Launch feature");

        HardwareActions.takeCurrentActivityScreenshot("Config Features screen.");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Settings On Of Feature");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Assert button on the Settings.
     * 
     * @param name
     *            is name of button
     */
    public void AssertButton(String name) {
        Assert.assertNotNull("'" + name + "' button is not present. on Settings screen.", getSolo()
                .getButton(name));
    }
}
