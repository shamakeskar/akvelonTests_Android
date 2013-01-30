package com.linkedin.android.screens.settings;

import junit.framework.Assert;

import android.widget.CheckBox;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

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
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Settings Calendar");
    }

    @TestAction(value = "go_to_cal_sync")
    public static void go_to_cal_sync(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenExpose screenExpose  = screenUpdates.openExposeScreen();
        screenExpose.openSettingsScreen();
        cal_sync("go_to_cal_sync");
    }

    public static void cal_sync(String screenName) {
        new ScreenSettings().tapOnAddYourCalendar();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }
    
    public static void cal_sync() {
        new ScreenSettings().tapOnAddYourCalendar();
        TestUtils.delayAndCaptureScreenshot("cal_sync");
    }
    
    @TestAction(value = "cal_sync_tap_back")
    public static void cal_sync_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenSettings();
        TestUtils.delayAndCaptureScreenshot("cal_sync_tap_back");
    }
    
    @TestAction(value = "cal_sync_tap_back_reset")
    public static void cal_sync_tap_back_reset() {
        cal_sync("cal_sync_tap_back_reset");
    }
    
    @TestAction(value = "cal_sync_toggle_add_cal")
    public static void cal_sync_toggle_add_cal() {
        Assert.assertFalse("Check Box 'Add calendar' is not present", getSolo().getCurrentCheckBoxes().isEmpty());
        CheckBox addCall = getSolo().getCurrentCheckBoxes().get(0);
        boolean isChecked = addCall.isChecked();
        ViewUtils.tapOnView(getSolo().getCurrentCheckBoxes().get(0), "Check Box 'Add calendar'");
        Assert.assertFalse("Not to put a check mark", isChecked == addCall.isChecked());
        TestUtils.delayAndCaptureScreenshot("cal_sync_toggle_add_cal");
    }
}
