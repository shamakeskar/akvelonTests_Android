package com.linkedin.android.tests;

import junit.framework.Assert;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.utils.ContactInfoUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;

/**
 * Tests for Search screen.
 * 
 * @author vasily.gancharov
 * @created Sep 18, 2012 10:40:34 AM
 */
public class SearchScreenTests extends BaseTestCase {

    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public SearchScreenTests() {
        super(SearchScreenTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 35974341: 'YOU - Search / Send message Connection / Send invite
     * / go back YOU.'
     * 
     * From the NUS page tap on the Search box. Tap on any Connection name to
     * reach the profile page. Tap on the Send Message icon, Enter content and
     * hit Send (you are now back on the Profile Page). Hit back (you go back to
     * the Search page). Search for a name to find a 2nd degree contact. Tap on
     * found 2nd degree contact. Once the Profile page loads, tap on the Invite
     * button. Hit back (You reach the Search page). Hit Back (You reach the
     * Updates page).
     * 
     * Expected results: When reaching the Search page, the Connections are
     * loaded. Verify the Profile Page load properly. The Compose Message page
     * loads. Verify the toasts "Sending message..." and "Message sent" show up.
     * The Search page loads properly. Verify the data accuracy of the Search
     * results. The Profile Page loads. Verify the toasts
     * "Sending invitation..." and "Invitation sent" show up. The Search page
     * loads properly. The Updates page loads properly.
     */
    public void test35974341() {
        // TODO uncomment when fixtures will be recorded for the test
        // startFixture("35974341");
        Logger.i(START_TEST
                + "35974341: 'YOU - Search / Send message Connection / Send invite / go back YOU.'");

        // Login and open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open Search screen.
        ScreenSearch screenSearch = updatesScreen.openSearchScreen();
        Logger.i(DONE + "Search screen loads properly. The 'Connections' list was loaded");

        // Tap on first visible connection to open "Profile of connected user"
        // screen.
        screenSearch.tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfConnectedUser screenProfileOfConnectedUser = new ScreenProfileOfConnectedUser();
        Logger.i(DONE + "'Profile of connected user' screen loads properly.");

        // Open "New message" screen.
        ScreenNewMessage screenNewMessage = screenProfileOfConnectedUser.openNewMessageScreen();
        Logger.i(DONE + "'New message' screen loads properly.");

        // Type message.
        screenNewMessage.typeMessage(null);

        // Type subject.
        screenNewMessage.typeSubject(null);

        // Tap on Send button and back to "Profile of connected user" screen.
        screenNewMessage.sendMessage();
        screenProfileOfConnectedUser = new ScreenProfileOfConnectedUser();
        Logger.i(DONE + "'Profile of connected user' screen loads properly.");

        // Back to Search screen.
        HardwareActions.pressBack();
        screenSearch = new ScreenSearch();
        Logger.i(DONE + "Search screen loads properly.");

        // Search for contact with 2 degree.
        final String secondDegreeContactName = "evgeny agapov";
        TextView foundContact = screenSearch.searchForContact(secondDegreeContactName);
        ImageView foundContactDegree = screenSearch.getContactDegree(foundContact);
        int contactDegree = ContactInfoUtils.getContactDegree(foundContactDegree);

        // Verify found contact has 2 degree.
        final int expectedContactDegree = 2;
        Assert.assertTrue("Degree of found contact is not " + expectedContactDegree,
                contactDegree == expectedContactDegree);
        Logger.i(DONE + "Contact with 2 degree was found.");

        // Open "Profile of connected user" screen of found contact.
        screenSearch.tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfNotConnectedUser screenProfileOfNotConnectedUser = new ScreenProfileOfNotConnectedUser();

        // TODO verify toasts
        // Tap on Invite button.
        screenProfileOfNotConnectedUser.inviteByTappingOnInviteToConnectButton();

        // Back to Search screen.
        HardwareActions.pressBack();
        screenSearch = new ScreenSearch();

        // Back to Updates screen.
        HardwareActions.pressBack();
        updatesScreen = new ScreenUpdates();
        Logger.i(DONE + "Updates screen loads properly.");

        Logger.i(PASS + "35974341");
    }
}
