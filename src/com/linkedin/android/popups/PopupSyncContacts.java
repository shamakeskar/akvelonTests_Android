package com.linkedin.android.popups;

import junit.framework.Assert;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for popup 'SyncContacts'.
 * 
 * @author nikita.chehomov
 * @created Oct 14, 2012 6:43:38 PM
 */
public class PopupSyncContacts {
    // CONSTANTS ------------------------------------------------------------
    public static final String DO_NOT_SYNC_TEXT = "Do not sync LinkedIn contacts";
    public static final String SYNC_ALL_TEXT = "Sync all";
    public static final String SYNC_WITH_EXISTING_TEXT = "Sync with existing contacts";
    public static final String SYNC_LINKEDIN_CONTACTS_TEXT = "Sync LinkedIn Contacts";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for PopupSyncContacts.
     */
    public PopupSyncContacts() {
        verify();
    }

    // METHODS --------------------------------------------------------------
    public void verify() {
        Assert.assertTrue(
                "Popup with message '" + SYNC_LINKEDIN_CONTACTS_TEXT + "' is not present",
                getSolo().waitForText(SYNC_LINKEDIN_CONTACTS_TEXT, 1,
                        DataProvider.WAIT_DELAY_DEFAULT, false, true));

        Assert.assertTrue("Button '" + SYNC_ALL_TEXT + "'" + " is not present." + " popup",
                getSolo().searchText(SYNC_ALL_TEXT));
        Assert.assertTrue("Button '" + SYNC_WITH_EXISTING_TEXT + "'" + " is not present."
                + " popup", getSolo().searchText(SYNC_WITH_EXISTING_TEXT));
        Assert.assertTrue("Button '" + DO_NOT_SYNC_TEXT + "'" + " is not present." + " popup",
                getSolo().searchText(DO_NOT_SYNC_TEXT));
    }

    /**
     * Returns Solo object.
     * 
     * @return Solo object.
     */
    private static Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }

    /**
     * Taps on 'Do not sync LinkedIn contacts on popup.
     */
    public void tapOnDoNotSync() {
        TextView view = getSolo().getText(DO_NOT_SYNC_TEXT);

        ViewUtils.tapOnView(view, DO_NOT_SYNC_TEXT + " view");
    }

    /**
     * Taps on 'Sync all' on popup.
     */
    public void tapOnSyncAll() {
        TextView view = getSolo().getText(SYNC_ALL_TEXT);

        ViewUtils.tapOnView(view, SYNC_ALL_TEXT + " view");
    }

    /**
     * Taps on 'Sync with existing contacts' on popup.
     */
    public void tapOnSyncWithExistingContacts() {
        TextView view = getSolo().getText(SYNC_WITH_EXISTING_TEXT);

        ViewUtils.tapOnView(view, SYNC_WITH_EXISTING_TEXT + " view");
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_popup_abi_toast")
    public static void go_to_popup_abi_toast() {
        if (!ScreenLogin.isOnLoginScreen()) {
            LoginActions.logout();
        }
        ScreenLogin loginScreen = new ScreenLogin();
        loginScreen.typeEmail(StringData.test_email);
        loginScreen.typePassword(StringData.test_password);
        loginScreen.tapOnSignInButton();
        new PopupSyncContacts();
        TestUtils.delayAndCaptureScreenshot("go_to_popup_abi_toast");
    }
    
    @TestAction(value = "abi_tap_not_sync")
    public static void abi_tap_not_sync() {
        new PopupSyncContacts().tapOnDoNotSync();
        TestUtils.delayAndCaptureScreenshot("abi_tap_not_sync");
    }
}
