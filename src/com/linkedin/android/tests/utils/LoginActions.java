package com.linkedin.android.tests.utils;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.popups.PopupMessageCancel;
import com.linkedin.android.popups.PopupMessageExit;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenCalSplash;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.screens.settings.ScreenSettings;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Class for actions with login/logout.
 * 
 * @author alexander.makarov
 * @created Aug 7, 2012 11:59:48 AM
 */
public final class LoginActions {
    // CONSTANTS ------------------------------------------------------------
    private static final String START_VIDEO_ACTIVITY_SHORT_CLASSNAME = "LiMediaPlayerVideo";
    private static final int COUNT_TRIES_TAP_BACK = 50;
    private static final String START_SIGN_IN_ACTIVITY_SHORT_CLASSNAME = "LaunchActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Opens Updates screen on start of test. Login as test user if need.
     * 
     * @return {@code ScreenUpdates} object.
     */
    public static ScreenUpdates openUpdatesScreenOnStart() {
        return openUpdatesScreenOnStart(StringData.test_email, StringData.test_password);
    }

    /**
     * Opens Updates screen on start of test. Login with specified credentials,
     * if need.
     * 
     * @param email
     *            - email to login
     * @param password
     *            - password to login
     * @return {@code ScreenUpdates} object.
     */
    @TestAction(value = "open_updates_screen_on_start")
    public static ScreenUpdates openUpdatesScreenOnStart(String email, String password) {
        Logger.i("Try open Updates screen");
        ScreenUpdates screenUpdates = null;

        // Not on IN screen.
        if (ScreenUpdates.isOnUpdatesScreen()) {
            screenUpdates = new ScreenUpdates();
        } else if (!isWeAreOnINScreen()) {
            // Wait until start video disappear if present.
            handleStartVideo();

            // Login.
            if (ScreenLogin.isOnLoginScreen()) {
                // If on Login screen then login as test user.
                ScreenLogin loginScreen = new ScreenLogin();
                screenUpdates = loginScreen.login(email, password);
            } else if (ScreenCalSplash.isCalSplashOpened()) {
                new ScreenCalSplash().tapLaterButton();
            } else {
                Assert.fail("Not implemented case: It is not IN, Login or CalSplash screen."
                        + " Current screen is "
                        + DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                                .getSimpleName());
            }
        } else {
            BaseINScreen.tapOnINButton();
            ScreenExpose screenExpose = new ScreenExpose(null);
            screenUpdates = screenExpose.openUpdatesScreen();
        }
        Logger.i("We are on Updates screen.");
        return screenUpdates;
    }

    /**
     * Checks that we are on screen with IN button.
     * 
     * @return <b>true</b> if we are on screen with IN button.
     */
    public static boolean isWeAreOnINScreen() {
        return BaseINScreen.getINButton() != null;
    }

    /**
     * If start video present, then wait until it disappear.
     */
    private static boolean handleStartVideo() {
        for (int i = 0; i < 30; i++) {
            boolean isExit = true;
            if (DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                    .getSimpleName().equals(START_VIDEO_ACTIVITY_SHORT_CLASSNAME)) {
                isExit = false;
                WaitActions.waitForScreenUpdate();
            }
            if (isExit) {
                if (i == 0)
                    return false;
                else
                    return true;
            }
        }
        Assert.fail("Cannot wait start video");
        return false;
    }

    /**
     * Opens Login screen from any screen.
     */
    @TestAction(value = "logout")
    public static void logout() {
        Logger.i("Start log out");
        if (DataProvider.getInstance().getSolo().getCurrentActivity().getClass().getSimpleName()
                .equalsIgnoreCase(START_SIGN_IN_ACTIVITY_SHORT_CLASSNAME)) {
            WaitActions.waitSingleActivity(ScreenLogin.ACTIVITY_SHORT_CLASSNAME, "Login screen");
        }
        // If on Login screen, then return.
        if (ScreenLogin.isOnLoginScreen())
            return;
        // If on handle Start Video screen, then return.
        if (handleStartVideo())
            return;

        Solo solo = DataProvider.getInstance().getSolo();
        // Go to IN screen.
        int counterTries = COUNT_TRIES_TAP_BACK;
        while (!isWeAreOnINScreen() && counterTries > 0) {
            HardwareActions.pressBack();
            // Handle MessageExit popup if present.
            WaitActions.waitForScreenUpdate();// Wait new screen.
            if (PopupMessageExit.isOnMessageExitPopup()) {
                new PopupMessageExit().tapOnYesButton();
                WaitActions.waitForScreenUpdate();// Wait previous screen.
            } else if (PopupMessageCancel.isOnMessageCancelPopup()) {
                new PopupMessageCancel().tapOnYesButton();
                WaitActions.waitForScreenUpdate();// Wait previous screen.
            }
            counterTries--;
        }

        // Open Expose screen.
        if (!ScreenExpose.isOnExposeScreen()) {
            BaseINScreen.tapOnINButton();
        }
        ScreenExpose screenExpose = new ScreenExpose(null);

        // Open Settings Screen.
        ScreenSettings screenSettings = screenExpose.openSettingsScreen();

        // Scroll to bottom.
        solo.scrollToBottom();

        // Tap on 'Sign Out' button.
        screenSettings.tapOnSignOutButton();

        // Wait for Sign Out happens.
        WaitActions.waitSingleActivity(ScreenLogin.ACTIVITY_SHORT_CLASSNAME, "Login",
                DataProvider.WAIT_DELAY_LONG);
        Logger.i("Log out complete");
    }
}
