package com.linkedin.android.popups;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for popup 'MessageCancel'.
 * 
 * @author alexander.makarov
 * @created Oct 10, 2012 6:03:06 PM
 */
public class PopupMessageCancel {
    // CONSTANTS ------------------------------------------------------------
    // Strings with texts "Yes/No" or other.
    public static final String POPUP_TEXT = "Are you sure you want to cancel this message?";
    
    // Strings with texts "Yes/No" or other.
    public static final String YES_TEXT = "Yes";
    public static final String NO_TEXT = "No";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public PopupMessageCancel() {
        verify();
    }

    // METHODS --------------------------------------------------------------
    public static void verify() {
        Assert.assertTrue("Popup with message '" + POPUP_TEXT + "' is not present", getSolo()
                .waitForText(POPUP_TEXT, 1, DataProvider.WAIT_DELAY_SHORT, false, true));

        Assert.assertTrue("Button '" + YES_TEXT + "'" + " is not present." + " popup", getSolo()
                .searchText(YES_TEXT));
        Assert.assertTrue("Button '" + NO_TEXT + "'" + " is not present." + " popup", getSolo()
                .searchText(NO_TEXT));
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
     * Checks that on screen showed buttons "Yes" and "No"
     * 
     * @return <b>true</b> if buttons "Yes" and "No" showed on screen.
     */
    public static boolean isOnMessageCancelPopup() {
        return getSolo().waitForText(POPUP_TEXT, 1, DataProvider.WAIT_DELAY_SHORT, false, true);
    }

    /**
     * Taps on Yes button.
     */
    public void tapOnYesButton() {
        ViewUtils.tapOnView(getSolo().getButton(YES_TEXT), "Yes button on popup");
    }

    /**
     * Taps on No button.
     */
    public void tapOnNoButton() {
        ViewUtils.tapOnView(getSolo().getButton(NO_TEXT), "No button on popup");
    }
}
