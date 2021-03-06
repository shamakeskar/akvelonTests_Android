package com.linkedin.android.tests;

import junit.framework.Assert;

import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.Logger;

/**
 * Tests for You screen.
 * 
 * @author alexander.makarov
 * @created Aug 9, 2012 11:03:34 AM
 */
public class YouScreenTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public YouScreenTests() {
        super(YouScreenTests.class.getSimpleName());
        Logger.i("Start LinkedIn tests");
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 33848279: 'You. Verify data and scroll down.'
     * 
     * Open You screen. Verify You screen. Scroll down. Verify new data is
     * shown.
     */
    public void test33848279() {
        startFixture("33848279");
        startTest("33848279", "You. Verify data and scroll down.");

        // Login and open Inbox screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open You screen.
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        ScreenYou screenYou = screenExpose.openYouScreen();
        
        // Verify You screen.
        screenYou.assertAllDataForTestUser();
        Logger.i("Done: Verify all data on You screen");

        // Check that 'Websites' section not shown.
        Assert.assertTrue("Nowhere to scroll",
                !getSolo().searchText("Websites", 1, false, true));

        // 2x Scroll down.
        Logger.i("Scroll screen down");
        Assert.assertTrue("Scroll down not performed", getSolo().scrollDown());
        Assert.assertTrue("Scroll down not performed", getSolo().scrollDown());

        // Verify new data is shown.
        Assert.assertTrue("Websites section is not shown after scroll",
                getSolo().searchText("Websites", 1, false, false));
        Logger.i("Done: Verify new data is shown after scroll down.");

        passTest();
    }
    
    /**
     * Test case 35974365: 'You - Expose.'
     * 
     * Open You screen. Verify You screen. Tap on IN button. Verify Expose screen.
     * Tap on You. Verify You screen.
     */
    public void test35974365() {
        startFixture("35974365");
        startTest("35974365", "YOU - Expose.");

        // Login and open You screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        ScreenYou screenYou = screenExpose.openYouScreen();
        Logger.i(DONE + "You screen loads properly");

        // Tap on IN. Verify Expose.
        screenExpose = screenYou.openExposeScreen();
        Logger.i(DONE + "Expose screen loads properly");

        // Open You screen. Verify.
        screenExpose.openYouScreen();
        Logger.i(DONE + "Data loads properly when reaching Expose and going back to the YOU page");

        passTest();
    }
}
