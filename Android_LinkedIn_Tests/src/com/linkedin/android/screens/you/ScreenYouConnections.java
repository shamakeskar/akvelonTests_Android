package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

public class ScreenYouConnections extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.connections.ConnectionListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ConnectionListActivity";

    private static final String PEOPLE_YOU_MAY_KNOW_LABEL = "PEOPLE YOU MAY KNOW";
    private static final String CONNECTIONS_LABEL = "Connections";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenYouConnections() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        Assert.assertNotNull("'People you may know' text is not presented",
                getPeopleYouMayKnowLabel());

        Assert.assertTrue("'Connections' label is not present",
                getSolo().waitForText(CONNECTIONS_LABEL));

        verifyINButton();

        verifyConnectionsList();

        HardwareActions.takeCurrentActivityScreenshot("Config Features screen.");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    public ScreenProfile openConnection(String connectionName) {
        // Get TextView by text
        TextView item = getSolo().getText(connectionName);

        Assert.assertNotNull("'" + connectionName + "' connection is not presented", item);

        // Tap on TextView
        ViewUtils.tapOnView(item, connectionName);

        return new ScreenProfile();
    }

    /**
     * Gets 'PEOPLE YOU MAY KNOW' {@code TextView}
     * 
     * @return 'PEOPLE YOU MAY KNOW' {@code TextView}
     */
    public TextView getPeopleYouMayKnowLabel() {
        HardwareActions.scrollUp();
        return getSolo().getText(PEOPLE_YOU_MAY_KNOW_LABEL);
    }

    /**
     * Opens 'You may know' screen
     * 
     * @return 'You may know' screen ({@code ScreenPYMK})
     */
    public ScreenPYMK openYouMayKnowScreen() {
        Solo solo = getSolo();
        TextView peopleYouMayKnowLabel = getPeopleYouMayKnowLabel();
        solo.clickOnView(peopleYouMayKnowLabel);
        ScreenPYMK peopleYouMayKnowScreen = new ScreenPYMK();
        return peopleYouMayKnowScreen;
    }

    /**
     * Gets connections list
     * 
     * @return {@code ListView} connections list
     */
    public ListView getConnectionsList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Gets first item from Connections list
     * 
     * @return {@code View} first item from Connections list
     */
    public View getFirstVisibleConnectionFromList() {
        final int firstVisibleConnectionFromListIndex = 1;

        ListView connectionsList = getConnectionsList();
        return ListViewUtils.getChildAtSafely(connectionsList, firstVisibleConnectionFromListIndex);
    }

    /**
     * Taps on first visible item from Connection list
     */
    public void tapOnFirstVisibleConnectionProfileScreen() {
        View firstVisibleConnectionFromList = getFirstVisibleConnectionFromList();
        if (null == firstVisibleConnectionFromList) {
            return;
        }

        getSolo().clickOnView(firstVisibleConnectionFromList);
    }

    /**
     * Opens {@code ScreenProfileOfConnectedUser} of first visible user from
     * 'Connection' list
     * 
     * @return {@code ScreenProfileOfConnectedUser} of first visible user from
     *         'Connection' list
     */
    public ScreenProfileOfConnectedUser openFirstVisibleConnectionProfileScreen() {
        tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfConnectedUser screenProfileOfConnectedUser = new ScreenProfileOfConnectedUser();
        return screenProfileOfConnectedUser;
    }

    /**
     * Verifies 'Connections' screen
     */
    private void verifyConnectionsList() {
        ListView connectionsList = getConnectionsList();
        Assert.assertNotNull("'Connections' list is not presented", connectionsList);
        Assert.assertTrue("Width of 'Connections' list is not equal width of device screen.",
                ListViewUtils.isListViewWidthEqualToScreenWidth(connectionsList));
        Assert.assertTrue("'Connections' list is empty.",
                ListViewUtils.isListViewNotEmpty(connectionsList));
    }
}
