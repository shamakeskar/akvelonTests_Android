package com.linkedin.android.screens.updates;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseScreenSharedNewsDetails;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.utils.HardwareActions;

/**
 * Class for "Single updated profile details" screen.
 * 
 * @author vasily.gancharov
 * @created Sep 21, 2012 18:14:31 PM
 */
public class ScreenSingleUpdatedProfileDetails extends BaseScreenSharedNewsDetails {

    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        super.verify();

        HardwareActions.takeCurrentActivityScreenshot("Single updated profile details Screen");
    }

    /**
     * Opens Connection Profile screen.
     * 
     * @return {@code ScreenProfileOfConnectedUser} with just opened 'Connection
     *         Profile' screen.
     */
    public ScreenProfileOfConnectedUser openConnectionProfile() {
        tapOnConnectionProfile();
        return new ScreenProfileOfConnectedUser();
    }

    /**
     * Verifies that label 'n people liked this' is present.
     */
    public void verifyPeopleLikedLabel() {
        boolean condition = getSolo().searchText("people liked this", 1, true, false);

        if (!condition) {
            HardwareActions.scrollUp();
            condition = getSolo().searchText("person likes this", 1, true, false);
        }

        Assert.assertTrue("Label 'people like this' or 'person likes this' is not present",
                condition);
    }
}
