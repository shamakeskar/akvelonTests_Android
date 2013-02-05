package com.linkedin.android.screens.common;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.settings.ScreenSettings;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Screen to switch between main screens
 * 
 * @author vasily.gancharov
 * @created Aug 14, 2012 16:35:35 PM
 */
public class ScreenExpose extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.common.ExposeActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ExposeActivity";

    private static final ViewIdName UPDATES_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_updates");
    private static final ViewIdName YOU_PANEL_RESOURCE_NAME = new ViewIdName("global_nav_you");
    private static final ViewIdName MESSAGES_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_inbox");
    private static final ViewIdName GROUPS_AND_MORE_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_network");
    private static final ViewIdName SETTINGS_RESOURCE_NAME = new ViewIdName("menu_settings");
    private static final ViewIdName SEARCH_RESOURCE_NAME = new ViewIdName("menu_search");
    private static final ViewIdName ITEM_LAYOUT_ID = new ViewIdName("expose_widget_text");

    private static final String UPDATES_LABEL = "Updates";
    private static final String YOU_LABEL = "You";
    private static final String MESSAGES_LABEL = "Messages";
    private static final String GROUPS_AND_MORE_LABEL = "Groups & More";

    /*
     * private static final Rect2DP UPDATES_LAYOUT_RECT = new Rect2DP(0.0f,
     * 84.0f, 160.0f, 228.0f); private static final Rect2DP YOU_LAYOUT_RECT =
     * new Rect2DP(160.0f, 84.0f, 160.0f, 228.0f); private static final Rect2DP
     * MESSAGES_LAYOUT_RECT = new Rect2DP(0.0f, 308.0f, 160.0f, 228.0f); private
     * static final Rect2DP GROUPS_AND_MORE_LAYOUT_RECT = new Rect2DP(160.0f,
     * 308.0f, 160.0f, 228.0f);
     */

    // PROPERTIES -----------------------------------------------------------
    private BaseINScreen screenFromWhichExposeOpened;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for 'Expose' screen class.
     * 
     * @param screenFromWhichExposeOpened
     *            {@code BaseINScreen} from which expose opened
     */
    public ScreenExpose(BaseINScreen screenFromWhichExposeOpened) {
        super(ACTIVITY_CLASSNAME);
        this.screenFromWhichExposeOpened = screenFromWhichExposeOpened;
    }

    /**
     * Verifies 'Expose' screen
     */
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG,
                "'Groups and More' screen is not loaded", new Callable<Boolean>() {
                    public Boolean call() {
                        ArrayList<View> views = Id.getListOfViewByViewIdName(ITEM_LAYOUT_ID);
                        if (views.size() >= 4) {
                            for (int i = 0; i < views.size(); i++) {
                                if (!views.get(i).isShown())
                                    return false;
                            }
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Expose");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Checks that we are on Expose screen.
     * 
     * @return <b>true</b> if we are on Expose screen
     */
    public static boolean isOnExposeScreen() {
        return (DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                .getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));
    }

    /**
     * Backs to screen from which current 'Exposed' screen was opened.
     * 
     * @return {@code BaseINScreen} from which current 'Exposed' screen was
     *         opened.
     */
    @SuppressWarnings("static-access")
    public BaseINScreen backToScreenFromWhichExposeOpened() {
        screenFromWhichExposeOpened.tapOnINButton();
        return screenFromWhichExposeOpened;
    }

    /**
     * Opens 'Updates' screen.
     * 
     * @return {@code ScreenUpdates} with just opened 'Updates' screen.
     */
    public ScreenUpdates openUpdatesScreen() {
        tapOnUpdatesButton();
        return new ScreenUpdates();
    }

    /**
     * Opens 'You' screen.
     * 
     * @return {@code ScreenYou} with just opened 'You' screen.
     */
    public ScreenYou openYouScreen() {
        tapOnYouButton();
        return new ScreenYou();
    }

    /**
     * Opens 'Inbox' screen.
     * 
     * @return {@code ScreenInbox} with just opened 'Messages' screen.
     */
    public ScreenInbox openInboxScreen() {
        tapOnMessagesButton();
        return new ScreenInbox();
    }

    /**
     * Opens 'Groups & More' screen.
     * 
     * @return {@code ScreenGroupsAndMore} with just opened 'Groups & More'
     *         screen.
     */
    public ScreenGroupsAndMore openGroupsAndMoreScreen() {
        tapOnGroupsAndMoreButton();
        return new ScreenGroupsAndMore();
    }

    /**
     * Taps on 'Updates' {@code TextView}.
     */
    public void tapOnUpdatesButton() {
        tapOnExposeMenuTextView(UPDATES_LABEL);
    }

    /**
     * Taps on 'You' {@code TextView}.
     */
    public static void tapOnYouButton() {
        tapOnExposeMenuTextView(YOU_LABEL);
    }

    /**
     * Taps on 'Messages' {@code TextView}.
     */
    public void tapOnMessagesButton() {
        tapOnExposeMenuTextView(MESSAGES_LABEL);
    }

    /**
     * Taps on 'Groups & More' {@code TextView}.
     */
    public void tapOnGroupsAndMoreButton() {
        tapOnExposeMenuTextView(GROUPS_AND_MORE_LABEL);
    }

    /**
     * Taps on {@code TextView} that has specified {@code textViewText}.
     * 
     * @param textViewText
     *            {@code TextView} text
     */
    private static void tapOnExposeMenuTextView(String textViewText) {
        assertExposeMenuTextView(textViewText);
        Solo solo = DataProvider.getInstance().getSolo();
        TextView textView = solo.getText(textViewText);
        ViewUtils.tapOnView(textView, "'" + textViewText + "' label");
    }

    /**
     * Verifies that {@code TextView} with specified {@code textViewText} is
     * presented on 'Expose' screen.
     * 
     * @param textViewText
     *            {@code TextView} text
     */
    private static void assertExposeMenuTextView(String textViewText) {
        Solo solo = DataProvider.getInstance().getSolo();
        TextView textView = solo.getText(textViewText);
        Assert.assertNotNull("'" + textViewText + "' TextView not present", textView);
        Assert.assertTrue("'" + textViewText + "' TextView not shown",
                textView.getVisibility() == View.VISIBLE);
    }

    /**
     * Opens ScreenSettings by tapping on button in right corner of navigation
     * bar.
     * 
     * @return ScreenSettings object.
     */
    public ScreenSettings openSettingsScreen() {
        View settingsButton = (View) Id.getViewByViewIdName(SETTINGS_RESOURCE_NAME);
        Logger.i("Tapping on 'Settings' button in top right corner");
        getSolo().clickOnView(settingsButton);
        return new ScreenSettings();
    }

    /**
     * Gets layout (as {@code View}) on which Updates section placed
     * 
     * @return layout (as {@code View}) on which Updates section placed
     */
    @SuppressWarnings("unused")
    private View getUpdatesLayout() {
        View updatesLayout = Id.getViewByViewIdName(UPDATES_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which You section placed
     * 
     * @return layout (as {@code View}) on which You section placed
     */
    @SuppressWarnings("unused")
    private View getYouLayout() {
        View updatesLayout = Id.getViewByViewIdName(YOU_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which Messages section placed
     * 
     * @return layout (as {@code View}) on which Messages section placed
     */
    @SuppressWarnings("unused")
    private View getMessagesLayout() {
        View updatesLayout = Id.getViewByViewIdName(MESSAGES_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which 'Groups & More' section placed
     * 
     * @return layout (as {@code View}) on which 'Groups & More' section placed
     */
    @SuppressWarnings("unused")
    private View getGroupsAndMoreLayout() {
        View updatesLayout = Id.getViewByViewIdName(GROUPS_AND_MORE_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Opens SEARCH screen.
     * 
     * @return {@code SearchScreen} with just opened 'SEARCH' screen.
     */
    public ScreenSearch openSearchScreen() {
        View searchButton = (View) Id.getViewByViewIdName(SEARCH_RESOURCE_NAME);
        Logger.i("Tapping on 'Search' button.");
        getSolo().clickOnView(searchButton);
        return new ScreenSearch();
    }

    /**
     * Verify that 'Expose' screen dismissed.
     */
    public static void verifyThatExposeScreenDismissed(int timeout) {
        if (timeout == 0)
            timeout = DataProvider.WAIT_DELAY_DEFAULT;
        int stepCounter = timeout / DataProvider.WAIT_DELAY_STEP;
        for (int i = 0; i <= stepCounter; i++) {
            if (!DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                    .getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME)) {
                return;
            }
            DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_STEP);
        }
        Assert.fail("'Expose' screen is not disappear");
    }

    // ACTIONS --------------------------------------------------------------
    public static void expose(String screenshotName) {
        new ScreenUpdates().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "expose")
    public static void expose() {
        expose("expose");
    }

    @TestAction(value = "go_to_expose")
    public static void go_to_expose(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);
        expose("go_to_expose");
    }

    @TestAction(value = "expose_tap_expose")
    public static void expose_tap_expose() {
        tapOnINButton();
        verifyThatExposeScreenDismissed(DataProvider.WAIT_DELAY_DEFAULT);
        TestUtils.delayAndCaptureScreenshot("expose_tap_expose");
    }

    @TestAction(value = "expose_tap_expose_reset")
    public static void expose_tap_expose_reset() {
        tapOnINButton();
        new ScreenExpose(null);
        TestUtils.delayAndCaptureScreenshot("expose_tap_expose_reset");
    }

    @TestAction(value = "expose_tap_settings")
    public static void expose_tap_settings() {
        new ScreenExpose(null).openSettingsScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_settings");
    }

    @TestAction(value = "expose_tap_settings_reset")
    public static void expose_tap_settings_reset() {
        HardwareActions.pressBack();
        new ScreenExpose(null);
        TestUtils.delayAndCaptureScreenshot("expose_tap_settings_reset");
    }

    @TestAction(value = "expose_tap_you")
    public static void expose_tap_you() {
        new ScreenExpose(null).openYouScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_you");
    }

    @TestAction(value = "expose_tap_you_reset")
    public static void expose_tap_you_reset() {
        new ScreenYou().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_you_reset");
    }

    @TestAction(value = "expose_tap_updates")
    public static void expose_tap_updates() {
        new ScreenExpose(null).openUpdatesScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_updates");
    }

    @TestAction(value = "expose_tap_updates_reset")
    public static void expose_tap_updates_reset() {
        new ScreenUpdates().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_updates_reset");
    }

    @TestAction(value = "expose_tap_inbox")
    public static void expose_tap_inbox() {
        new ScreenExpose(null).openInboxScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_inbox");
    }

    @TestAction(value = "expose_tap_inbox_reset")
    public static void expose_tap_inbox_reset() {
        new ScreenInbox().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_inbox_reset");
    }

    @TestAction(value = "expose_tap_groups_and_more")
    public static void expose_tap_groups_and_more() {
        new ScreenExpose(null).openGroupsAndMoreScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_groups_and_more");
    }

    @TestAction(value = "expose_tap_groups_and_more_reset")
    public static void expose_tap_groups_and_more_reset() {
        new ScreenGroupsAndMore().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_groups_and_more_reset");
    }

    @TestAction(value = "expose_tap_search")
    public static void expose_tap_search() {
        new ScreenExpose(null).openSearchScreen();
        TestUtils.delayAndCaptureScreenshot("expose_tap_search");
    }

    @TestAction(value = "expose_tap_search_reset")
    public static void expose_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenExpose(null);
        TestUtils.delayAndCaptureScreenshot("expose_tap_search_reset");
    }
}
