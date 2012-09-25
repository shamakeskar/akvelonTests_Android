package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for New Connection Details screen.
 * 
 * @author aleksey.chichagov
 * @created Sep 19, 2012 5:20:37 PM
 */
public class ScreenNewConnectionDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.DetailsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "DetailsListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewConnectionDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("Wrong activity (expected " + ACTIVITY_SHORT_CLASSNAME, getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));

        Assert.assertTrue("'Details' label not shown", getSolo().waitForText("Details"));

        Assert.assertNotNull("Connections list is empty", getConnectionsConnection());

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("New Connection Details screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets Connection's connection.
     * 
     * @return {@code TextView} connection roll up.
     */
    private TextView getConnectionsConnection() {
        int currentIndex = -2;
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            String text = view.getText().toString();
            if (text.indexOf("is now connected") > -1 || text.indexOf("new connections") > -1)
                break;
            currentIndex++;
        }
        return getSolo().getText(currentIndex);
    }

    /**
     * Taps on first connection`s connection.
     */
    public void tapOnFirstConnectionsConnection() {
        TextView rollUp = getConnectionsConnection();
        ViewUtils.tapOnView(rollUp, "'Connection' view");
    }

    /**
     * Taps on your connection profile view.
     */
    public void tapOnYourConectionProfile() {
        TextView profile = getSolo().getText(2);
        ViewUtils.tapOnView(profile, "'Profile' view");
    }

    /**
     * Opens connection`s connection profile.
     * 
     * @return {@code ScreenProfileOfNotConnectedUser} with just opened 'Profile
     *         Of Not Connected User' screen
     */
    public ScreenProfileOfNotConnectedUser openConnectionsConnectionProfile() {
        tapOnFirstConnectionsConnection();
        return new ScreenProfileOfNotConnectedUser();
    }

    /**
     * Opens your connection profile.
     * 
     * @return {@code ScreenProfileOftConnectedUser} with just opened 'Profile
     *         Of Connected User' screen
     */
    public ScreenProfileOfConnectedUser openYourConectionProfile() {
        tapOnYourConectionProfile();
        return new ScreenProfileOfConnectedUser();
    }
}
