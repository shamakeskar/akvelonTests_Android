package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.settings.ScreenSettingsAddConnections;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

public class ScreenYouConnections extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.connections.ConnectionListActivity";
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
        verifyCurrentActivity();
        Assert.assertTrue("'Connections' label is not present",
                getSolo().waitForText(CONNECTIONS_LABEL));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "You Connections");
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
     * Opens 'People You May Know' screen.
     * 
     * @return {@code ScreenPYMK}.
     */
    public ScreenPYMK openPYMKScreen() {
        TextView text = getSolo().getText("PEOPLE YOU MAY KNOW");
        Assert.assertNotNull("'PEOPLE YOU MAY KNOW' label not present", text);
        Assert.assertTrue("'PEOPLE YOU MAY KNOW' label not shown", text.isShown());

        Logger.i("Tapping on 'PEOPLE YOU MAY KNOW' label");
        getSolo().clickOnText("PEOPLE YOU MAY KNOW");
        return new ScreenPYMK();
    }

    // Tap 'Back' button and verify ScreenYouConnections
    public static void backInYouConnections(String screenName) {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenYouConnections();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }
    
    @TestAction(value = "go_to_connections")
    public static void go_to_connections(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenYou screenYou = new ScreenUpdates().openExposeScreen().openYouScreen();
        screenYou.openConnections();
        TestUtils.delayAndCaptureScreenshot("go_to_connections");
    }

    @TestAction(value = "connections_tap_expose")
    public static void connections_tap_expose() {
        new ScreenYouConnections().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("connections_tap_expose");
    }

    @TestAction(value = "connections_tap_expose_reset")
    public static void connections_tap_expose_reset() {
        tapOnINButton();
        new ScreenYouConnections();
        TestUtils.delayAndCaptureScreenshot("connections_tap_expose_reset");
    }
    
    @TestAction(value = "connections_tap_back")
    public static void connections_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        HardwareActions.scrollToTop();
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("connections_tap_back");
    }
    
    @TestAction(value = "connections_tap_back_reset")
    public static void connections_tap_back_reset() {
        new ScreenYou().openConnections();
        TestUtils.delayAndCaptureScreenshot("connections_tap_back_reset");
    }
    
    @TestAction(value = "connections_tap_add_con")
    public static void connections_tap_add_con() {
        new ScreenYouConnections().tapOnRightButtonInNavigationBar("Add connections");
        new ScreenSettingsAddConnections();
        TestUtils.delayAndCaptureScreenshot("connections_tap_add_con");
    }

    @TestAction(value = "connections_tap_add_con_reset")
    public static void connections_tap_add_con_reset() {
        backInYouConnections("connections_tap_add_con_reset");
    }
    
    @TestAction(value = "connections_tap_search")
    public static void connections_tap_search() {
        HardwareActions.pressSearch();
        new ScreenSearch();
        TestUtils.delayAndCaptureScreenshot("connections_tap_search");
    }

    @TestAction(value = "connections_tap_search_reset")
    public static void connections_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenYouConnections();
        TestUtils.delayAndCaptureScreenshot("connections_tap_search_reset");
    }
    
    @TestAction(value = "connections_tap_pymk")
    public static void connections_tap_pymk() {
        new ScreenYouConnections().openPYMKScreen();
        TestUtils.delayAndCaptureScreenshot("connections_tap_pymk");
    }
    
    @TestAction(value = "connections_tap_pymk_reset")
    public static void connections_tap_pymk_reset() {
        backInYouConnections("connections_tap_pymk_reset");
    }

    @TestAction(value = "connections_tap_profile")
    public static void connections_tap_profile() {
        new ScreenYouConnections().tapOnFirstVisibleConnectionProfileScreen();
        TestUtils.delayAndCaptureScreenshot("connections_tap_profile");
    }
    
    @TestAction(value = "connections_tap_profile_reset")
    public static void connections_tap_profile_reset() {
        backInYouConnections("connections_tap_profile_reset");
    }

    @TestAction(value = "connections_pull_refresh")
    public static void connections_pull_refresh() {
        new ScreenYouConnections().refreshScreen();
        TestUtils.delayAndCaptureScreenshot("connections_pull_refresh");
    }

    public static void connections_tap_scrollbar() {
        Logger.i("Screen scroll down");
        Assert.assertTrue("Scroll down not performed", getSolo().scrollDown());
        TestUtils.delayAndCaptureScreenshot("connections_tap_scrollbar");
    }
    
    public static void connections_tap_scrollbar_reset() {
        Logger.i("Screen scroll up");
        Assert.assertTrue("Scroll Up not performed", getSolo().scrollUp());
        TestUtils.delayAndCaptureScreenshot("connections_tap_scrollbar_reset");
    }
}
