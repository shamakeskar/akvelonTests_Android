package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.widget.CheckBox;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;

public class ScreenSettingsRichStreamSettings extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.debug.LIConfigRichStreamActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "LIConfigRichStreamActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettingsRichStreamSettings() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        assertChecBox();

        HardwareActions.takeCurrentActivityScreenshot("Rich Stream Settings screen.");
    }

    /**
     * Assert chechbox 'Enable'
     * 
     */
    public void assertChecBox() {
        final CheckBox enableCheckBox = getSolo().getCurrentCheckBoxes().get(0);
        Assert.assertNotNull("Can not find checkbox 'Enable'", enableCheckBox);
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Enables Rich Stream feature.
     */
    public void enableRichStream() {
        if (!getSolo().isCheckBoxChecked(0)) {
            getSolo().clickOnCheckBox(0);
        }
    }

    /**
     * Disables Rich Stream feature.
     */
    public void disableRichStream() {
        if (getSolo().isCheckBoxChecked(0)) {
            getSolo().clickOnCheckBox(0);
        }
    }
}
