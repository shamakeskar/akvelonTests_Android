package com.linkedin.android.popups;

import junit.framework.Assert;
import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;

/**
 * Class for Compose popup
 * 
 * @author vladimir.belyakov
 * @created Oct 89, 2012 2:45:03 PM
 */
public class PopupCompose {
	// CONSTANTS ------------------------------------------------------------
	// Strings with texts "Compose/New Message" or other.
	public static final String COMPOSE_TEXT = "Compose";
    public static final String NEW_MESSAGE_TEXT = "New Message";
    public static final String NEW_INVITATION_TEXT = "New Invitation";
    
    // CONSTRUCTORS ---------------------------------------------------------
    public PopupCompose() {
    	verify();
	}

    // METHODS --------------------------------------------------------------
    public static void verify() {
        Assert.assertTrue("Popup with message '" + COMPOSE_TEXT + "' is not present", getSolo()
                .waitForText(COMPOSE_TEXT, 1, DataProvider.WAIT_DELAY_SHORT, false, true));

        Assert.assertTrue("Button '" + NEW_MESSAGE_TEXT + "'" + " is not present." + " popup", getSolo()
                .searchText(NEW_MESSAGE_TEXT));
        Assert.assertTrue("Button '" + NEW_INVITATION_TEXT + "'" + " is not present." + " popup", getSolo()
                .searchText(NEW_INVITATION_TEXT));
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
     * Taps on New Message on popup.
     */
    public void tapOnNewMessageOnPopup() {
        Assert.assertNotNull("'New Message' button is not present.",
                getSolo().searchText(NEW_MESSAGE_TEXT));

        Logger.i("Tapping on 'New Message' button");
        getSolo().clickOnText(NEW_MESSAGE_TEXT);
    }

    /**
     * Taps on New Invitation on popup.
     */
    public void tapOnNewInvitationOnPopup() {
        Assert.assertNotNull("'New Invitation' button is not present.",
                getSolo().searchText(NEW_INVITATION_TEXT));

        Logger.i("Tapping on 'New Invitation' button");
        getSolo().clickOnText(NEW_INVITATION_TEXT);
    }
    
    
}
