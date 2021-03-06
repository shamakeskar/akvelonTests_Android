package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;

/**
 * Screen to switch between main screens
 * 
 * @author vasily.gancharov
 * @created Aug 14, 2012 16:35:35 PM
 */
public class ScreenExpose {

    // CONSTANTS ------------------------------------------------------------

    private static final ViewIdName UPDATES_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_updates");
    private static final ViewIdName YOU_PANEL_RESOURCE_NAME = new ViewIdName("global_nav_you");
    private static final ViewIdName MESSAGES_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_inbox");
    private static final ViewIdName GROUPS_AND_MORE_PANEL_RESOURCE_NAME = new ViewIdName(
            "global_nav_network");

    private static final String UPDATES_LABEL = "Updates";
    private static final String YOU_LABEL = "You";
    private static final String MESSAGES_LABEL = "Messages";
    private static final String GROUPS_AND_MORE_LABEL = "Groups & More";

    private static final Rect2DP UPDATES_LAYOUT_RECT = new Rect2DP(0.0f, 84.0f, 160.0f, 228.0f);
    private static final Rect2DP YOU_LAYOUT_RECT = new Rect2DP(160.0f, 84.0f, 160.0f, 228.0f);
    private static final Rect2DP MESSAGES_LAYOUT_RECT = new Rect2DP(0.0f, 308.0f, 160.0f, 228.0f);
    private static final Rect2DP GROUPS_AND_MORE_LAYOUT_RECT = new Rect2DP(160.0f, 308.0f, 160.0f,
            228.0f);

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
        this.screenFromWhichExposeOpened = screenFromWhichExposeOpened;

        verify();
    }

    /**
     * Verifies 'Expose' screen
     */
    public void verify() {
        Assert.assertNotNull(
                "Error: there is null pointer to BaseINScreen during creation of 'Expose' screen",
                screenFromWhichExposeOpened);

        WaitActions.waitForText(UPDATES_LABEL, "Cannot wait to open 'Expose'");
        WaitActions.waitForText(GROUPS_AND_MORE_LABEL, "Cannot wait to open 'Expose'");
        
        assertExposeMenuTextView(UPDATES_LABEL);
        ViewAssertUtils.assertViewIsPlacedInLayout("'Updates' layout is not present",
                getUpdatesLayout(), UPDATES_LAYOUT_RECT);

        assertExposeMenuTextView(YOU_LABEL);
        ViewAssertUtils.assertViewIsPlacedInLayout("'You' layout is not present", getYouLayout(),
                YOU_LAYOUT_RECT);

        assertExposeMenuTextView(MESSAGES_LABEL);
        ViewAssertUtils.assertViewIsPlacedInLayout("'Messages' layout is not present",
                getMessagesLayout(), MESSAGES_LAYOUT_RECT);

        assertExposeMenuTextView(GROUPS_AND_MORE_LABEL);
        ViewAssertUtils.assertViewIsPlacedInLayout("'Groups & More' layout is not present",
                getGroupsAndMoreLayout(), GROUPS_AND_MORE_LAYOUT_RECT);

        screenFromWhichExposeOpened.verifyINButton();
        
        HardwareActions.takeCurrentActivityScreenshot("Expose");
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
        Logger.i("Tapping on '" + textViewText + "' TextView");
        solo.clickOnView(textView);
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
        Assert.assertTrue("'" + textViewText + "' TextView not shown", textView.getVisibility() == View.VISIBLE);
    }

    /**
     * Gets layout (as {@code View}) on which Updates section placed
     * 
     * @return layout (as {@code View}) on which Updates section placed
     */
    private View getUpdatesLayout() {
        View updatesLayout = Id.getViewByViewIdName(UPDATES_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which You section placed
     * 
     * @return layout (as {@code View}) on which You section placed
     */
    private View getYouLayout() {
        View updatesLayout = Id.getViewByViewIdName(YOU_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which Messages section placed
     * 
     * @return layout (as {@code View}) on which Messages section placed
     */
    private View getMessagesLayout() {
        View updatesLayout = Id.getViewByViewIdName(MESSAGES_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

    /**
     * Gets layout (as {@code View}) on which 'Groups & More' section placed
     * 
     * @return layout (as {@code View}) on which 'Groups & More' section placed
     */
    private View getGroupsAndMoreLayout() {
        View updatesLayout = Id.getViewByViewIdName(GROUPS_AND_MORE_PANEL_RESOURCE_NAME);
        return updatesLayout;
    }

}
