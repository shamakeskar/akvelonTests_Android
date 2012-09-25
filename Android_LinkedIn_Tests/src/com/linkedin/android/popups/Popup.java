/**
 * 
 */
package com.linkedin.android.popups;

import junit.framework.Assert;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;

/**
 * Class for popup views.
 * 
 * @author Dmitry.Somov
 * 
 * @created Aug 21, 2012 2:15:30 PM
 */
// TODO please add in code checking buttons (if exists on 'popup') and describe precisely why there is a class. (alexander makarov)
public class Popup {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    String title;
    String text;

    // CONSTRUCTORS ---------------------------------------------------------
    public Popup(String title, String text) {
        this.title = title;
        this.text = text;
        verify();
    }

    // METHODS --------------------------------------------------------------
    private void verify() {
        Assert.assertTrue("Popup with title '" + title + "' is not present",
                getSolo().waitForText(title, 1, DataProvider.WAIT_DELAY_SHORT, false, false));

        Assert.assertTrue("The text '" + text + "'" + " is not present on '" + title + "'"
                + " popup", getSolo().searchText(text));
    }

    /**
     * Method for reduce amount of text for get Solo object.
     * 
     * @return Solo object.
     */
    private static Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }

    /**
     * Taps on a button.
     * 
     * @param name
     *            is button name that should be tapped.
     */
    public void tapOnButton(String name) {
        Button button = getSolo().getButton(name);

        Assert.assertNotNull("'" + name + "'" + " button is not present on '" + title + "'"
                + " popup", button);

        Logger.i("Tap on '" + name + "' button on popup");
        getSolo().clickOnView(button);
    }
}
