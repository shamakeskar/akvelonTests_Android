package com.linkedin.android.screens.common;

import junit.framework.Assert;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;

/**
 * Class for People you May Know screen.
 * 
 * @author Irina Gracheva
 * @created Aug 14, 2012
 */
public class ScreenPYMK extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.reconnect.ReconnectListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ReconnectListActivity";

    // Indexes of views relatively 'People you may know' list item
    private static final int CONNECTION_INFO_LAYOUT_INDEX = 1;
    // Indexes of views relatively Connection info layout
    private static final int ADD_CONNECTION_BUTTON_INDEX = 5;
    private static final int REMOVE_CONNECTION_BUTTON_INDEX = 6;

    private static final String ADD_CONNECTION_BUTTON_LABEL = "Add connection";
    private static final String REMOVE_CONNECTION_BUTTON_LABEL = "Remove connection";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenPYMK() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        Assert.assertTrue("'You may know' header is not present on PYMK screen", getSolo()
                .searchText("You may know", 1, false));

        Assert.assertTrue("'People You May Know' label is not present on PYMK screen", getSolo()
                .searchText("People You May Know", 1, false));

        verifyINButton();

        verifyPeopleYouMayKnowList();

        HardwareActions.takeCurrentActivityScreenshot("PYMK screen");
    }

    @Override
    public void waitForMe() {
        Assert.assertTrue("Cannot wait to launch activity '" + ACTIVITY_SHORT_CLASSNAME + "'",
                getSolo()
                        .waitForActivity(ACTIVITY_SHORT_CLASSNAME, DataProvider.WAIT_DELAY_DEFAULT));
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets 'People you may know' {@code ListView}
     * 
     * @return 'People you may know' {@code ListView}
     */
    public ListView getPeopleYouMayKnowList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Gets layout of first visible item from 'People you may know' list
     * 
     * @return {@code LinearLayout} first item from Connections list
     */
    public LinearLayout getFirstVisibleConnectionFromList() {
        final int firstVisibleItemFromListIndex = 1;

        ListView peopleYouMayKnowList = getPeopleYouMayKnowList();
        return ViewGroupUtils.getChildViewByIndexSafely(peopleYouMayKnowList,
                firstVisibleItemFromListIndex, LinearLayout.class);
    }

    /**
     * Taps on first visible item from 'People you may know' list
     */
    public void tapOnFirstVisibleConnectionProfileScreen() {
        LinearLayout firstVisibleConnectionFromList = getFirstVisibleConnectionFromList();
        Assert.assertNotNull("There is no connections in 'People you may know' list",
                firstVisibleConnectionFromList);
        getSolo().clickOnView(firstVisibleConnectionFromList);
    }

    /**
     * Opens {@code ScreenProfileOfNotConnectedUser} of first visible user from
     * 'People you may know' list
     * 
     * @return {@code ScreenProfileOfNotConnectedUser} of first visible user
     *         from 'People you may know' list
     */
    public ScreenProfileOfNotConnectedUser openFirstVisibleConnectionProfileScreen() {
        tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfNotConnectedUser screenProfileOfNotConnectedUser = new ScreenProfileOfNotConnectedUser();
        return screenProfileOfNotConnectedUser;
    }

    /**
     * Gets "Add connection" button ({@code ImageView}) from specified
     * {@code peopleYouMayKnowListItemLayout}
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     * @return "Add connection" button ({@code ImageView}) from specified
     *         {@code peopleYouMayKnowListItemLayout} or <b>null</b> if there is
     *         no such button
     */
    public ImageView getAddConnectionButton(LinearLayout peopleYouMayKnowListItemLayout) {
        return getButtonFromConnectionInfoLayout(peopleYouMayKnowListItemLayout,
                ADD_CONNECTION_BUTTON_INDEX);
    }

    /**
     * Gets "Remove connection" button ({@code ImageView}) from specified
     * {@code peopleYouMayKnowListItemLayout}
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     * @return "Remove connection" button ({@code ImageView}) from specified
     *         {@code peopleYouMayKnowListItemLayout} or <b>null</b> if there is
     *         no such button
     */
    public ImageView getRemoveConnectionButton(LinearLayout peopleYouMayKnowListItemLayout) {
        return getButtonFromConnectionInfoLayout(peopleYouMayKnowListItemLayout,
                REMOVE_CONNECTION_BUTTON_INDEX);
    }

    /**
     * Taps on "Add connection" button from specified
     * {@code peopleYouMayKnowListItemLayout}
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     */
    public void tapOnAddConnectionButton(LinearLayout peopleYouMayKnowListItemLayout) {
        ImageView addConnectionButton = getAddConnectionButton(peopleYouMayKnowListItemLayout);
        tapOnButton(addConnectionButton, ADD_CONNECTION_BUTTON_LABEL);
    }

    /**
     * Taps on "Remove connection" button from specified
     * {@code peopleYouMayKnowListItemLayout}
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     */
    public void tapOnRemoveConnectionButton(LinearLayout peopleYouMayKnowListItemLayout) {
        ImageView addConnectionButton = getRemoveConnectionButton(peopleYouMayKnowListItemLayout);
        tapOnButton(addConnectionButton, REMOVE_CONNECTION_BUTTON_LABEL);
    }

    /**
     * Gets button from {@code peopleYouMayKnowListItemLayout} with specified
     * {@code buttonIndex}
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     * @param buttonIndex
     *            index of required button
     * @return button from {@code peopleYouMayKnowListItemLayout} with specified
     *         {@code buttonIndex} or <b>null</b> if there is no such button
     */
    private ImageView getButtonFromConnectionInfoLayout(
            LinearLayout peopleYouMayKnowListItemLayout, int buttonIndex) {
        RelativeLayout connectionInfoLayout = getConnectionInfoLayout(peopleYouMayKnowListItemLayout);
        ImageView button = ViewGroupUtils.getChildViewByIndexSafely(connectionInfoLayout,
                buttonIndex, ImageView.class);
        return button;
    }

    /**
     * Gets {@code RelativeLayout} of connection with specified
     * {@code peopleYouMayKnowListItemLayout} contained info about connection
     * 
     * @param peopleYouMayKnowListItemLayout
     *            {@code LinearLayout} of 'People you may know' list item
     * @return {@code RelativeLayout} of connection with specified
     *         {@code peopleYouMayKnowListItemLayout} contained info about
     *         connection or <b>null</b> if there is no such layout
     */
    private RelativeLayout getConnectionInfoLayout(LinearLayout peopleYouMayKnowListItemLayout) {
        RelativeLayout connectionInfoLayout = ViewGroupUtils.getChildViewByIndexSafely(
                peopleYouMayKnowListItemLayout, CONNECTION_INFO_LAYOUT_INDEX, RelativeLayout.class);
        return connectionInfoLayout;
    }

    /**
     * Verifies 'People you may know' list
     */
    private void verifyPeopleYouMayKnowList() {
        ListView peopleYouMayKnowList = getPeopleYouMayKnowList();
        Assert.assertNotNull("'People you may know' list is not presented", peopleYouMayKnowList);
        Assert.assertTrue(
                "Width of 'People you may know' list is not equal width of device screen.",
                ListViewUtils.isListViewWidthEqualToScreenWidth(peopleYouMayKnowList));
        Assert.assertTrue("'People you may know' list is empty.",
                ListViewUtils.isListViewNotEmpty(peopleYouMayKnowList));
    }
}