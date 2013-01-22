package com.linkedin.android.popups;

import java.util.ArrayList;

import junit.framework.Assert;

import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.utils.Logger;

/**
 * Class for popup 'Forward'.
 * 
 * @author alexander.makarov
 * @created Oct 9, 2012 5:23:38 PM
 */
public class PopupForward {
    // CONSTANTS ------------------------------------------------------------
    // Static ID of options on Forward popup.
    public static final int OPTION_ID_FORWARD_POPUP = 16908308;
    // Strings with texts for possible options.
    public static final String SEND_TO_CONNECTION_TEXT = "Send to Connection";
    public static final String REPLY_PRIVATELY_TEXT = "Reply Privately";
    public static final String SHARE_TEXT = "Share";
    public static final String CANCEL_TEXT = "Cancel";

    // PROPERTIES -----------------------------------------------------------
    // Array with options for current Forward popup.
    private String[] options;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for PopupForward.
     * 
     * @param options strings with options in this Forward popup.
     */
    public PopupForward(String... options) {
        this.options = options;
        verify();
    }

    // METHODS --------------------------------------------------------------
    public void verify() {
        for (String option : options) {
            Assert.assertTrue("Option '" + option + "' is not present", getSolo()
                    .searchText(option));
        }
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
     * Taps on specified option.
     * 
     * @param optionName
     *            is name of option for tap.
     */
    public void tapOnOption(String optionName) {
        for (String option : options) {
            if (option.equals(optionName)) {
                Logger.i("Tapping on option '" + optionName + "'");
                getSolo().clickOnText(optionName);
                return;
            }
        }
        Assert.fail("Wrong option name: " + optionName);
    }

    /**
     * Taps on Cancel button on popup.
     */
    public void tapOnCancelButtonOnPopup() {
        Assert.assertTrue("'Cancel' button is not present.", getSolo().searchText("Cancel"));
        Logger.i("Tapping on 'Cancel' button");
        getSolo().clickOnText("Cancel");
    }
    /**
     * Returns {@code ArrayList} of options (as {@code String}) in Forward popup including "Cancel"
     * 
     * @return {@code ArrayList} of options (as {@code String}) in Forward popup
     */
    public ArrayList<String> getCountOptionsForForwardPopup() {
        ArrayList<String> options = new ArrayList<String>();
        ArrayList<View> views = Id.getListOfViewById(OPTION_ID_FORWARD_POPUP);
        for (View view : views) {
            if (!(view instanceof TextView))
                Assert.fail("Wrong ID in Forward popup for options");
            options.add(((TextView) view).getText().toString());
        }
        if (getSolo().searchText("Cancel"))
            options.add("Cancel");
        return options;
    }
}
