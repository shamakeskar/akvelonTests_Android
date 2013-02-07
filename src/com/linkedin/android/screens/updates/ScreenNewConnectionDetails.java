package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
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

    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "Details";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewConnectionDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("'Details' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView titleView = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        return ((titleView != null) && (titleView.getText().equals(TITLE)));
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "New Connection Details");
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
