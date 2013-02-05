package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for People you May Know screen.
 * 
 * @author Irina Gracheva
 * @created Aug 14, 2012
 */
public class ScreenPYMK extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.reconnect.ReconnectListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ReconnectListActivity";

    // Indexes of views relatively 'People you may know' list item
    private static final int CONNECTION_INFO_LAYOUT_INDEX = 1;
    // Indexes of views relatively Connection info layout
    private static final int ADD_CONNECTION_BUTTON_INDEX = 5;
    private static final int REMOVE_CONNECTION_BUTTON_INDEX = 6;

    private static final int SENDER_NAME_INDEX = 1;
    private static final int INVITATION_LIST_VIEW_INDEX = 1;
    private static final int ACCEPT_INVITATION_BUTTON_INDEX = 5;
    private static final int DECLINE_INVITATION_BUTTON_INDEX = 4;

    private static final String ADD_CONNECTION_BUTTON_LABEL = "Add connection";
    private static final String REMOVE_CONNECTION_BUTTON_LABEL = "Remove connection";
    private static final ViewIdName INVITATION_LAYOUT = new ViewIdName(
            "invite_reconnect_person_row");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenPYMK() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        Assert.assertTrue(
                "PYMK screen is not present ('People You May Know' label is not present)",
                getSolo().searchText("People You May Know", 1, false));
        verifyINButton();
        Assert.assertNotNull("PYMK screen is not present (cannot find ListView)",
                getPeopleYouMayKnowList());
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
        ViewUtils.tapOnView(firstVisibleConnectionFromList, "first connection");
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

    private static void tapOnSenderProfile() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        final String senderName = ((TextView) invitationLayout.getChildAt(1)).getText().toString();
        TextView acceptInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, SENDER_NAME_INDEX, TextView.class);
        ViewUtils.tapOnView(acceptInvitationButton, "senderName");
        ViewUtils.tapOnLink(senderName);
    }

    private static void tapOnNonFirstInvitation() {
        RelativeLayout layout = (RelativeLayout) Id.getListOfViewByViewIdName(INVITATION_LAYOUT)
                .get(INVITATION_LIST_VIEW_INDEX);
        TextView viewProfile = ViewGroupUtils.getChildViewByIndexSafely(layout, 1, TextView.class);
        ViewUtils.tapOnView(viewProfile, "receiverProfile");

        Button acceptButton = getSolo().getButton("Invite to Connect");
        Assert.assertNotNull("NO ACCEPT BUTTON", acceptButton);
        ViewUtils.tapOnView(acceptButton, "Accept Invitation");
    }

    // ACTIONS --------------------------------------------------------------
    public static void pymk(String screenshotName) {
        ScreenGroupsAndMore.groups_and_more_tap_pymk();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "pymk")
    public static void pymk() {
        pymk("pymk");
    }

    @TestAction(value = "go_to_pymk")
    public static void go_to_pymk(String email, String password) {
        ScreenGroupsAndMore.go_to_groups_and_more(email, password);
        pymk("go_to_pymk");
    }

    @TestAction(value = "pymk_tap_back")
    public static void pymk_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("pymk_back");
    }

    @TestAction(value = "pymk_tap_expose")
    public static void pymk_tap_expose() {
        new ScreenPYMK().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("pymk_tap_expose");
    }

    @TestAction(value = "pymk_tap_expose_reset")
    public static void pymk_tap_expose_reset() {
        tapOnINButton();
        new ScreenPYMK();
        TestUtils.delayAndCaptureScreenshot("pymk_tap_expose_reset");
    }

    @TestAction(value = "pymk_tap_profile")
    public static void pymk_tap_profile() {
        new ScreenPYMK().openFirstVisibleConnectionProfileScreen();
        TestUtils.delayAndCaptureScreenshot("pymk_tap_profile");
    }

    @TestAction(value = "pymk_tap_profile_reset")
    public static void pymk_tap_profile_reset() {
        HardwareActions.pressBack();
        new ScreenPYMK();
        TestUtils.delayAndCaptureScreenshot("pymk_tap_profile_reset");
    }

    @TestAction(value = "pymk_pull_refresh")
    public static void pymk_pull_refresh() {
        new ScreenPYMK().refreshScreen();
        TestUtils.delayAndCaptureScreenshot("pymk_pull_refresh");
    }

    @TestAction(value = "pymk_scroll_load_more")
    public static void pymk_scroll_load_more() {
        new ScreenPYMK().scrollDownForLoadMore();
        TestUtils.delayAndCaptureScreenshot("pymk_scroll_load_more");
    }

    @TestAction(value = "go_to_invite_accept_pymk")
    public static void go_to_invite_accept_pymk(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        new ScreenExpose(null).openGroupsAndMoreScreen();
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openPYMKScreen();
    }

    @TestAction(value = "invite_accept_pymk")
    public static void invite_accept_pymk() {
        tapOnINButton();
        ViewUtils.waitForToastDisappear();
        ScreenInbox inbox = new ScreenExpose(null).openInboxScreen();
        if (inbox.isInvitationOnScreen()) {
            tapOnSenderProfile();
            Button acceptButton = getSolo().getButton("Accept Invitation");
            Assert.assertNotNull("NO ACCEPT BUTTON", acceptButton);
            ViewUtils.tapOnView(acceptButton, "Accept Invitation");
        }

        tapOnINButton();
        ViewUtils.waitForToastDisappear();
        new ScreenExpose(null).openGroupsAndMoreScreen();
        ScreenGroupsAndMore.groups_and_more_tap_pymk();
        tapOnNonFirstInvitation();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk");
    }

    @TestAction(value = "invite_accept_pymk_tap_expose")
    public static void invite_accept_pymk_tap_expose() {
        new ScreenPYMK().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_expose");
    }

    @TestAction(value = "invite_accept_pymk_tap_expose_reset")
    public static void invite_accept_pymk_tap_expose_reset() {
        new ScreenExpose(null).openGroupsAndMoreScreen();
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openPYMKScreen();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_expose_reset");
    }

    @TestAction(value = "invite_accept_pymk_tap_search")
    public static void invite_accept_pymk_tap_search() {
        HardwareActions.pressSearch();
        new ScreenSearch();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_search");
    }

    @TestAction(value = "invite_accept_pymk_tap_search_reset")
    public static void invite_accept_pymk_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        tapOnINButton();
        new ScreenExpose(null).openGroupsAndMoreScreen();
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openPYMKScreen();
    }

    @TestAction(value = "invite_accept_pymk_tap_profile")
    public static void invite_accept_pymk_tap_profile() {
        new ScreenPYMK().openFirstVisibleConnectionProfileScreen();
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_profile");
    }

    @TestAction(value = "invite_accept_pymk_tap_profile_reset")
    public static void invite_accept_pymk_tap_profile_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_profile_reset");
    }

    @TestAction(value = "pymk_tap_invite")
    public static void pymk_tap_invite(String profileName) {
        RelativeLayout layout;
        if (StringUtils.isNullOrEmpty(profileName)) {
            layout = (RelativeLayout) Id.getListOfViewByViewIdName(INVITATION_LAYOUT).get(0);
        } else {
            TextView text = TextViewUtils.getTextViewByText(profileName);
            Assert.assertNotNull("Cannot find profile '" + profileName + "' on PYMK screen", text);
            layout = (RelativeLayout) text.getParent();
        }

        ImageView acceptInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(layout,
                ACCEPT_INVITATION_BUTTON_INDEX, ImageView.class);
        String displayName = ((TextView) layout.getChildAt(SENDER_NAME_INDEX)).getText().toString();

        ViewUtils.tapOnView(acceptInvitationButton, "accept invitation button for " + displayName);
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_invite");
    }

    @TestAction(value = "pymk_tap_ignore")
    public static void pymk_tap_ignore() {
        RelativeLayout layout = (RelativeLayout) Id.getListOfViewByViewIdName(INVITATION_LAYOUT)
                .get(1);
        ImageView ignoreInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(layout,
                DECLINE_INVITATION_BUTTON_INDEX, ImageView.class);
        final String displayName = ((TextView) layout.getChildAt(SENDER_NAME_INDEX)).getText()
                .toString();
        ViewUtils.tapOnView(ignoreInvitationButton, "ignore invitation for " + displayName);

        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation' row is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return !getSolo().searchText(displayName, 1, false, true);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_ignore");
    }

    @TestAction(value = "invite_accept_pymk_tap_back")
    public static void invite_accept_pymk_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("invite_accept_pymk_tap_back");
    }
}
