/**
 * 
 */
package com.linkedin.android.screens.inbox;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenInvitationsAll extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.notifications.NewMessageListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NewMessageListActivity";

    private static final ViewIdName INVITATION_LAYOUT = new ViewIdName(
            "invite_reconnect_person_row");

    // Button indexes relatively "Invitation info" layout
    private static final int ACCEPT_INVITATION_BUTTON_INDEX = 4;
    private static final int DECLINE_INVITATION_BUTTON_INDEX = 5;
    private static final String ACCEPT_INVITATION_BUTTON_LABEL = "Accept invitation";
    private static final String DECLINE_INVITATION_BUTTON_LABEL = "Decline invitation";

    // Delay for wait completely load Invitations All webview.
    static final int WAIT_FOR_SCREEN_LOAD_COMPLETELY = 10;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenInvitationsAll() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Wait for screen load completely
        WaitActions.delay(WAIT_FOR_SCREEN_LOAD_COMPLETELY);

        // Verify presence Compose button
        verifyComposeButton();

        Assert.assertTrue("'All Invitations' screen is not present",
                getSolo().searchText("Invitations", 1, false, true));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Jobs");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Returns 'Compose' button.
     * 
     * @return if exist 'Compose' button {@code View}, else <b>null</b>
     */
    public ImageView getComposeButton() {
        return getRightButtonInNavigationBar();
    }

    /**
     * Verifies 'Compose' button.
     */
    public void verifyComposeButton() {
        Assert.assertNotNull("'Compose' button is not presented", getComposeButton());
    }

    // ACTIONS --------------------------------------------------------------
    public static void inbox_invitations_all(String screenshotName) {
        ScreenInbox.inbox_tap_all_invitations();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void inbox_invitations_all() {
        inbox_invitations_all("inbox_invitations_all");
    }

    public static void go_to_inbox_invitations_all() {
        ScreenInbox.go_to_inbox();
        inbox_invitations_all("go_to_inbox_invitations_all");
    }

    public static void inbox_invitations_all_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_back");
    }

    public static void inbox_invitations_all_tap_expose() {
        new ScreenInvitationsAll().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_expose");
    }

    public static void inbox_invitations_all_tap_expose_reset() {
        tapOnINButton();
        new ScreenInvitationsAll();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_expose_reset");
    }

    public static void inbox_invitations_all_tap_compose_new_invitation() {
        View button = new ScreenInvitationsAll().getRightButtonInNavigationBar();
        Assert.assertNotNull("'Compose button' is not present.", button);

        Logger.i("Tapping on 'Compose' button");
        getSolo().clickOnView(button);
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_compose_new_invitation");
    }

    public static void inbox_invitations_all_tap_compose_new_invitation_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_invitations_all_tap_compose_new_invitation_reset");
    }

    public static void inbox_invitations_all_tap_invitation() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        Assert.assertNotNull("'Invitation' row is not present on 'Inbox' screen", invitationLayout);
        ViewGroupUtils.tapFirstViewInLayout(invitationLayout, true, "'Invitation' row", null);

        new ScreenInvitationDetails();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_invitations");
    }

    public static void inbox_invitations_all_tap_invitation_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_invitation_reset");
    }

    public static void inbox_invitations_all_pull_refresh() {
        new ScreenInvitationsAll().refreshScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_pull_refresh");
    }

    public static void inbox_invitations_all_scroll_load_more() {
        new ScreenInvitationsAll().scrollDownForLoadMore();
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_scroll_load_more");
    }

    public static void inbox_invitations_all_tap_invitation_accept() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        Assert.assertNotNull("'Invitation' row is not present on 'Inbox' screen", invitationLayout);
        Assert.assertNotNull("'Invitation profile name' is not present on 'Inbox' screen",
                invitationLayout.getChildAt(1));
        final String displayName = ((TextView) invitationLayout.getChildAt(1)).getText().toString();

        ImageView acceptInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, ACCEPT_INVITATION_BUTTON_INDEX, ImageView.class);
        new ScreenInvitationsAll().tapOnButton(acceptInvitationButton,
                ACCEPT_INVITATION_BUTTON_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation' row is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return (getSolo().searchText(displayName, 1, false, true) != true);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_invitation_accept");
    }

    public static void inbox_invitations_all_tap_invitation_decline() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        Assert.assertNotNull("'Invitation' row is not present on 'Inbox' screen", invitationLayout);
        Assert.assertNotNull("'Invitation profile name' is not present on 'Inbox' screen",
                invitationLayout.getChildAt(1));
        final String displayName = ((TextView) invitationLayout.getChildAt(1)).getText().toString();

        ImageView declineInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, DECLINE_INVITATION_BUTTON_INDEX, ImageView.class);
        new ScreenInvitationsAll().tapOnButton(declineInvitationButton,
                DECLINE_INVITATION_BUTTON_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation' row is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return (getSolo().searchText(displayName, 1, false, true) != true);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("inbox_invitations_all_tap_invitation_decline");
    }
}
