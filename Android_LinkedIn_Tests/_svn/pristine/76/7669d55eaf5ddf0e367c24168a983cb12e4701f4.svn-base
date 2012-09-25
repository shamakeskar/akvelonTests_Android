package com.linkedin.android.screens.you;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;

public class ScreenSettingsCalendar extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.spotlight.CalendarOptInActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "CalendarOptInActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettingsCalendar() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------

    @Override
    public void verify() {
        // TODO add more conditions?
        Assert.assertTrue("'CALENDAR' screen is not present",
                getSolo().waitForText("Calendar", 1, DataProvider.WAIT_DELAY_SHORT, false, false));

        HardwareActions.takeCurrentActivityScreenshot("CALENDAR screen");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }
}
