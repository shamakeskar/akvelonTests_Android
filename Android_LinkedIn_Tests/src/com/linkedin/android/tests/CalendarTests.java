package com.linkedin.android.tests;

import junit.framework.Assert;

import com.linkedin.android.screens.ScreenLogin;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.updates.ScreenCalendar;
import com.linkedin.android.screens.updates.ScreenCalendarEventDetail;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Tests for Calendar screen.
 * 
 * @author alexey.makhalov
 * @created Aug 30, 2012 6:29:03 PM
 */
public class CalendarTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public CalendarTests() {
        super(CalendarTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 34632799: 'NUS - Calendar'
     * 
     * Prereq: Set up the following gmail account on your device and sync the
     * google calendar -> qa.homer@gmail.com / cctest234
     * 
     * Steps: 1- From the NUS page tap on the Calendar Section (see screen shot)
     * 2- Tap on any meeting 3- Tap on any attendee profile (2nd degree or
     * further) 4- Tap on the Send Invitation button 5- Hit back and find an
     * event with 1st degree connection 6- Tap on the Send message button, enter
     * content and hit Send 7- Hit back and tap on "View all attendees" 8- Hit
     * back twice (you are back to the Calendar events list) 9- Scroll down
     * until you load more data, then hit the "Today" button 10- Scroll up until
     * you load more data, then hit the "Today" button
     * 
     * Result: 4- "Sending Invitation..." & "Invitation sent" toasts show up 6-
     * "Sending message..." & "Message sent" toasts show up 9- You go back to
     * Today's first event 10- You go back to Today's first event
     */
    public void test34632799() {
        startFixture("34632799");

        Logger.i(START_TEST + "34632799: 'NUS - Calendar.'");

        // Verify Login screen.
        ScreenLogin loginScreen = new ScreenLogin();

        // Login as test_200 user.
        loginScreen.typeEmail(StringData.test_200_email);
        loginScreen.typePassword(StringData.test_200_password);
        loginScreen.tapOnSignInButton();

        // Tap 'Sync all'.
        loginScreen.handleSyncContactsDialog("Sync all");
        WaitActions.waitForScreenUpdate();

        // Tap on 'Add Calendar' button.
        loginScreen.handleCalendarSplashAddCalendar();

        // Handle blue hint for IN button.
        loginScreen.handleInButtonHint();

        // Verify Updates screen contains calendar.
        ScreenUpdates screenUpdates = new ScreenUpdates();

        // Check the Hero Slot doesn't display the Calendar
        Assert.assertTrue("Calendar is not present in Hero Slot",
                screenUpdates.isCalendarPresentInHeroSlot());
        Logger.i(DONE + "Verify the Hero Slot does display the Calendar.");

        // Open Calendar screen.
        ScreenCalendar screenCalendar = screenUpdates.openCalendarScreen();

        // Open any meeting.
        ScreenCalendarEventDetail screenEvent = screenCalendar.openAnyMeeting();

        screenEvent.tapOnViewAllAttendees();

        // Tap on any attendee profile.
        ScreenProfileOfNotConnectedUser notConnectedProfile = screenEvent
                .findAnyNotConnectedUserAndOpenScreen();
        Assert.assertNotNull("Not connected profile not found on meeting", notConnectedProfile);

        // Tap on the Send Invitation button.
        notConnectedProfile.inviteByTappingOnInviteToConnectButton();
        Logger.i(DONE + "Verify 'Sending invitation' and 'Invitation sent' toasts are shown.");
        WaitActions.waitForScreenUpdate();

        // Go back on Event Screen (All Attendees).
        HardwareActions.pressBack();

        // Find an event with 1st degree connection.
        ScreenProfileOfConnectedUser connectedProfile = screenEvent
                .findFirstConnectedUserAndOpenScreen();
        Assert.assertNotNull("Connected profile not found on meeting", connectedProfile);

        // Open "New Message" screen.
        ScreenNewMessage newMessageScreen = connectedProfile.openNewMessageScreen();

        // Type subject.
        String testSubject = "testSubject";
        newMessageScreen.typeSubject(testSubject);
        // Type message.
        String testMessage = "testMessage";
        newMessageScreen.typeMessage(testMessage);

        // Tap on send button.
        newMessageScreen.sendMessage();

        Logger.i(DONE + "Verify 'Sending message' and 'Message sent' toasts are shown.");
        WaitActions.waitForScreenUpdate();

        // Go back on Event Screen.
        HardwareActions.pressBack();
        WaitActions.waitForScreenUpdate();
        HardwareActions.pressBack();
        screenEvent = new ScreenCalendarEventDetail();
        screenEvent.tapOnViewAllAttendees();

        // Go back on Calendar Screen.
        HardwareActions.pressBack();
        WaitActions.waitForScreenUpdate();
        HardwareActions.pressBack();
        WaitActions.waitForScreenUpdate();

        // Scroll down to load more data. Then hit Today button.
        getSolo().scrollToBottom();
        screenCalendar.tapOnTodayButton();
        Assert.assertTrue("'Today' button doesn't work on 'Calendar' screen",
                screenCalendar.isOnToday());
        Logger.i(DONE + "Verify go back to Today's first event on 'Calendar'.");

        // Scroll up to load more data. Then hit Today button.
        getSolo().scrollToTop();
        screenCalendar.tapOnTodayButton();
        Assert.assertTrue("'Today' button doesn't work on 'Calendar' screen",
                screenCalendar.isOnToday());
        Logger.i(DONE + "Verify go back to Today's first event on 'Calendar'.");

        Logger.i(PASS + "34632799");
    }
}
