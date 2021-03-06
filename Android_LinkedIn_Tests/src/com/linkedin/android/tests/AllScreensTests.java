package com.linkedin.android.tests;

import junit.framework.Assert;

import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.inbox.ScreenMessageDetail;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.ApplicationUtils;
import com.linkedin.android.utils.ConversionUnits;

/**
 * Complex tests for all screens.
 * 
 * @author vasily.gancharov
 * @created Aug 27, 2012 17:34:03 PM
 */
public class AllScreensTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public AllScreensTests() {
        super(AllScreensTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 33848425: 'Check correct work after background.'
     * 
     * Login as test user. Put app to background, sleep for 5mins. Relaunch app.
     * assertScreens (Updates, You, Inbox, More). Last You screen. Put app to
     * background, sleep for 45sec. assertScreens. Last Recent Updates screen.
     * Put app to background, sleep for 45sec. assertScreens. Last More screen.
     * Put app to background, sleep for 45sec. assertScreens. Last Inbox screen.
     * Put app to background, sleep for 45sec. assertScreens. Last Inbox screen.
     * Open first message. Put app to background, sleep for 45sec.
     * assertScreens.
     * 
     * Verify that all 'assertScreens' passed (screens not blank and have
     * content).
     */
    public void test33848425() {
        startTest("33848425", "Check correct work after background.");

        // Login and open Updates screen.
        ScreenUpdates updates = LoginActions.openUpdatesScreenOnStart();

        // Specify time during which application will be in background
        // (5 minutes)
        int backgroundTimeoutMs = 5 * ConversionUnits.SECONDS_IN_MINUTE
                * ConversionUnits.MILLISECONDS_IN_SECOND;

        // Put application to background for 5 minutes
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets Updates screen object and verify if current screen is "Updates"
        updates = new ScreenUpdates();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Open Expose screen
        ScreenExpose screenExpose = updates.openExposeScreen();
        // Open "Inbox" screen
        ScreenInbox screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Open Expose screen
        screenExpose = screenInbox.openExposeScreen();
        // Open "More" screen and verify it
        ScreenGroupsAndMore moreScreen = screenExpose.openGroupsAndMoreScreen();

        // Open Expose screen
        screenExpose = moreScreen.openExposeScreen();
        // Open "You" screen
        ScreenYou screenYou = screenExpose.openYouScreen();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Specify time during which application will be in background
        // (45 seconds)
        backgroundTimeoutMs = 45 * ConversionUnits.MILLISECONDS_IN_SECOND;

        // Put application to background. Sleep for 45 seconds
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets You screen object and verify if current screen is "You"
        screenYou = new ScreenYou();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Open Expose screen
        screenExpose = screenYou.openExposeScreen();
        // Open "Inbox" screen
        screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Open Expose screen
        screenExpose = screenInbox.openExposeScreen();
        // Open "More" screen and verify it
        moreScreen = screenExpose.openGroupsAndMoreScreen();

        // Open Expose screen
        screenExpose = moreScreen.openExposeScreen();
        // Open "Updates" screen
        updates = screenExpose.openUpdatesScreen();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Put application to background. Sleep for 45 seconds
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets Updates screen object and verify if current screen is "Updates"
        updates = new ScreenUpdates();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Open Expose screen
        screenExpose = updates.openExposeScreen();
        // Open "You" screen
        screenYou = screenExpose.openYouScreen();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Open Expose screen
        screenExpose = screenYou.openExposeScreen();
        // Open "Inbox" screen
        screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Open Expose screen
        screenExpose = screenInbox.openExposeScreen();
        // Open "More" screen and verify it
        moreScreen = screenExpose.openGroupsAndMoreScreen();

        // Put application to background. Sleep for 45 seconds
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets More screen object and verify if current screen is "More"
        moreScreen = new ScreenGroupsAndMore();

        // Open Expose screen
        screenExpose = moreScreen.openExposeScreen();
        // Open "Updates" screen
        updates = screenExpose.openUpdatesScreen();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Open Expose screen
        screenExpose = updates.openExposeScreen();
        // Open "You" screen
        screenYou = screenExpose.openYouScreen();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Open Expose screen
        screenExpose = screenYou.openExposeScreen();
        // Open "Inbox" screen
        screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Put application to background. Sleep for 45 seconds
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets Inbox screen object and verify if current screen is "Inbox"
        screenInbox = new ScreenInbox();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Open Expose screen
        screenExpose = screenInbox.openExposeScreen();
        // Open "More" screen and verify it
        moreScreen = screenExpose.openGroupsAndMoreScreen();

        // Open Expose screen
        screenExpose = moreScreen.openExposeScreen();
        // Open "Updates" screen
        updates = screenExpose.openUpdatesScreen();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Open Expose screen
        screenExpose = updates.openExposeScreen();
        // Open "You" screen
        screenYou = screenExpose.openYouScreen();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Open Expose screen
        screenExpose = screenYou.openExposeScreen();
        // Open "Inbox" screen
        screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Open "Inbox message details" screen and verify it
        ScreenMessageDetail screenMessageDetail = screenInbox.openFirstMessage();

        // Put application to background. Sleep for 45 seconds
        ApplicationUtils.moveApplicationToBackgroundForTime(backgroundTimeoutMs, this);

        // Gets "Inbox message details" screen object and verify if current
        // screen is "Inbox message details"
        screenMessageDetail = new ScreenMessageDetail();

        // Open Expose screen
        screenExpose = screenMessageDetail.openExposeScreen();
        // Open "More" screen and verify it
        moreScreen = screenExpose.openGroupsAndMoreScreen();

        // Open Expose screen
        screenExpose = moreScreen.openExposeScreen();
        // Open "Updates" screen
        updates = screenExpose.openUpdatesScreen();

        // Verify test data present in "Updates" screen
        assertScreenUpdatesForTest33848425(updates);

        // Open Expose screen
        screenExpose = updates.openExposeScreen();
        // Open "You" screen
        screenYou = screenExpose.openYouScreen();

        // Verify test data present in "You" screen
        assertScreenYouForTest33848425(screenYou);

        // Open Expose screen
        screenExpose = screenYou.openExposeScreen();
        // Open "Inbox" screen
        screenInbox = screenExpose.openInboxScreen();

        // Verify test data present in "Inbox" screen
        assertScreenInboxForTest33848425(screenInbox);

        // Logout
        LoginActions.logout();

        passTest();
    }

    // TEST 33848425 HELPER METHODS
    // --------------------------------------------------------
    /**
     * Verifies test data on "Updates" screen for test 33848425
     * 
     * @param screenUpdates
     *            {@code ScreenUpdates} to verify
     */
    private static void assertScreenUpdatesForTest33848425(ScreenUpdates screenUpdates) {
        boolean isRecentUpdatesListNotEmpty = screenUpdates.isRecentUpdatesListNotEmpty();
        Assert.assertTrue("'Recent updates' list is empty.", isRecentUpdatesListNotEmpty);
    }

    /**
     * Verifies test data on "You" screen for test 33848425
     * 
     * @param screenYou
     *            {@code ScreenYou} to verify
     */
    private static void assertScreenYouForTest33848425(ScreenYou screenYou) {
        screenYou.assertAllDataForTestUser();
    }

    /**
     * Verifies test data on "Inbox" screen for test 33848425
     * 
     * @param screenInbox
     *            {@code ScreenInbox} to verify
     */
    private static void assertScreenInboxForTest33848425(ScreenInbox screenInbox) {
        boolean isMessagesListNotEmpty = screenInbox.isMessagesListNotEmpty();
        Assert.assertTrue("'Messages' list is empty.", isMessagesListNotEmpty);
    }

}
