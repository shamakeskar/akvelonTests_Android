package com.linkedin.android.tests.utils;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.ScreenLogin;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenSettings;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;

/**
 * Class for actions with login/logout.
 * 
 * @author alexander.makarov
 * @created Aug 7, 2012 11:59:48 AM
 */
public final class LoginActions {
    // CONSTANTS ------------------------------------------------------------
    private static final String START_VIDEO_ACTIVITY_SHORT_CLASSNAME = "LiMediaPlayerVideo";

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
    public static ScreenUpdates openUpdatesScreenOnStart(String email, String password) {
        Logger.i("Try open Updates screen");
        if (!isWeAreOnINScreen()) {
            // Wait until start video disappear if present.
            handleStartVideo();

            // Find expected screens.
            if (ScreenLogin.isOnLoginScreen()) {
                // If on Login screen then login as test user.
                ScreenLogin loginScreen = new ScreenLogin();
                loginScreen.login(email, password);
            }
        }
        ScreenUpdates screenUpdates = new ScreenUpdates();
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
                HardwareActions.waitForScreenUpdate();
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
     * Opens Login screen.
     */
    public static void logout() {
        // If on Login screen, then return.
        if (ScreenLogin.isOnLoginScreen())
            return;
        // If on handle Start Video screen, then return.
        if (handleStartVideo())
            return;

        Solo solo = DataProvider.getInstance().getSolo();
        // Go to IN screen.
        while (!isWeAreOnINScreen()) {
            HardwareActions.pressBack();
        }
        // Open You screen.
        BaseINScreen.tapOnINButton();
        ScreenExpose.tapOnYouButton();
        ScreenYou screenYou = new ScreenYou();

        // Open Settings Screen.
        ScreenSettings screenSettings = screenYou.openSettingsScreen();

        // Scroll to bottom.
        solo.scrollToBottom();

        // Tap on 'Sign Out' button.
        screenSettings.tapOnSignOutButton();

        // Wait for Sign Out happens.
        HardwareActions.waitForScreenUpdate();
    }

}