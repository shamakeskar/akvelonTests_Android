/**
 * 
 */
package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.Button;

import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Calendar Splash Screen.
 * 
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenCalSplash extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.spotlight.CalendarOptInActivity";

    public static final String ACTIVITY_SHORT_CLASSNAME = "CalendarOptInActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCalSplash() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        Assert.assertTrue("'Add Your Calendar' label is not present on 'Calendar Splash' Screen",
                getSolo().searchText("Add Your Calendar", 1, false));
    }

    @Override
    public void waitForMe() {
        Assert.assertTrue("Cannot wait to launch activity '" + ACTIVITY_SHORT_CLASSNAME + "'",
                getSolo()
                        .waitForActivity(ACTIVITY_SHORT_CLASSNAME, DataProvider.WAIT_DELAY_DEFAULT));
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Checks that currently open Calendar splash screen.
     * 
     * @return <b>true</b> if opened Calendar splash screen.
     */
    public static boolean isCalSplashOpened() {
        return getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME);
    }

    /**
     * Taps on 'Close' button.
     */
    public void tapLaterButton() {
        Assert.assertNotNull("'Close' button is not present.", getSolo().getImageButton(0));
        Logger.i("Tapping on 'Close' button on 'Calendar Splash' Screen");
        getSolo().clickOnImageButton(0);
    }

    /**
     * Taps on 'Close' button.
     */
    public void tapAddCalendarButton() {
        Button addCalendarButton = getSolo().getButton("Add Calendar");
        Assert.assertNotNull("'Add Calendar' button is not present.", addCalendarButton);
        Logger.i("Tapping on 'Add Calendar' button on 'Calendar Splash' Screen");
        ViewUtils.tapOnView(addCalendarButton, "'Add Calendar' button");
    }

    /**
     * Taps on 'Learn More' button.
     */
    public void tapLearnMoreButton() {
        Assert.assertNotNull("'Learn More' text is not present.",
                getSolo().searchText("Learn More"));
        Logger.i("Tapping on 'Learn More' text");
        getSolo().clickOnText("Learn More");
    }

    /**
     * Verifies Learn More splash screen.
     */
    public void verifyLearnMoreSplashScreen() {
        Assert.assertTrue("'How LinkedIn uses your calendar data' text is not present", getSolo()
                .waitForText("How LinkedIn uses your calendar data"));

        Assert.assertTrue("'Learn More' text is not present", getSolo().searchText("Learn More"));
        Assert.assertNotNull(
                "'This feature accesses your phone's calendar...' text is not present",
                TextViewUtils.searchTextViewInLayout("This feature accesses your phone's calendar",
                        null, false));
    }

    /**
     * Taps on back to close blue window.
     */
    public void handleInButtonHint() {
        if (getSolo().waitForText("Welcome to LinkedIn", 1, DataProvider.WAIT_DELAY_DEFAULT, false)) {
            HardwareActions.pressBack();
        }
    }

    // ACTIONS --------------------------------------------------------------

    @TestAction(value = "cal_splash")
    public static void cal_splash(String email, String password) {
        ScreenLogin loginScreen = new ScreenLogin();
        loginScreen.typeEmail(email);
        loginScreen.typePassword(password);
        loginScreen.tapOnSignInButton();
        new PopupSyncContacts().tapOnDoNotSync();
        loginScreen.waitForCalendarSplashScreen();
        TestUtils.delayAndCaptureScreenshot("cal_splash");
    }

    @TestAction(value = "cal_splash_tap_later")
    public static void cal_splash_tap_later() {
        ScreenCalSplash calSplash = new ScreenCalSplash();
        calSplash.tapLaterButton();
        calSplash.handleInButtonHint();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_later");
    }

    @TestAction(value = "cal_splash_tap_later_reset")
    public static void cal_splash_tap_later_reset(String email, String password) {
        LoginActions.logout();
        cal_splash(email, password);
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_later_reset");
    }

    @TestAction(value = "cal_splash_tap_sync")
    public static void cal_splash_tap_sync() {
        ScreenCalSplash calSplash = new ScreenCalSplash();
        calSplash.tapAddCalendarButton();
        calSplash.handleInButtonHint();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_sync");
    }

    @TestAction(value = "cal_splash_tap_sync_reset")
    public static void cal_splash_tap_sync_reset(String email, String password) {
        LoginActions.logout();
        cal_splash(email, password);
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_sync_reset");
    }

    @TestAction(value = "cal_splash_tap_learn_more")
    public static void cal_splash_tap_learn_more() {
        ScreenCalSplash callSplash = new ScreenCalSplash();
        callSplash.tapLearnMoreButton();
        callSplash.verifyLearnMoreSplashScreen();
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_learn_more");
    }

    @TestAction(value = "cal_splash_tap_learn_more_reset")
    public static void cal_splash_tap_learn_more_reset() {
        HardwareActions.pressBack();
        new ScreenCalSplash();
        TestUtils.delayAndCaptureScreenshot("cal_splash_tap_learn_more_reset");
    }
}
