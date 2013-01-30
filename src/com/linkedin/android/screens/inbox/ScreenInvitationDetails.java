package com.linkedin.android.screens.inbox;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Invitation Detail screen.
 * 
 * @author Aleksey.Chichagov
 * @created Sep 11, 2012 3:27:37 PM
 */
public class ScreenInvitationDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.invitations.ViewInvitationActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewInvitationActivity";

    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    private static final ViewIdName HEADER_LAYOUT = new ViewIdName("navigation_bar_title");
    private static final ViewIdName UP_LAYOUT = new ViewIdName("nav_inbox_previous");
    private static final ViewIdName DOWN_LAYOUT = new ViewIdName("nav_inbox_next");
    private static final ViewIdName INVITATION_LAYOUT = new ViewIdName("profile_template_2");
    private static final ViewIdName INVITATION_ALL_INVITATION_LAYOUT = new ViewIdName(
            "invite_reconnect_person_row");

    private static final int INVITATION_DISLAY_NAME_INDEX = 2;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenInvitationDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        WaitActions.waitForTrueInFunction("Title '.. of ..' is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        Assert.assertTrue("Header of 'Invitation' is not present",
                                (TextView) Id.getViewByViewIdName(HEADER_LAYOUT) != null);
                        return ((String) ((TextView) Id.getViewByViewIdName(HEADER_LAYOUT))
                                .getText()).indexOf(" of") > -1;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Invitation Details");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets 'Up Arrow' button {@code ImageView}
     * 
     * @return 'Up Arrow' button {@code ImageView}
     */
    public ImageView getUpArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return upArrowButton;
    }

    /**
     * Gets 'Down Arrow' button {@code ImageView}.
     * 
     * @return 'Down Arrow' button {@code ImageView}.
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return downArrowButton;
    }

    /**
     * Taps on 'Ok' button.
     */
    public void tapOnOkButton() {
        ImageButton okButton = getSolo().getImageButton(0);
        ViewUtils.tapOnView(okButton, "Ok");
    }

    /**
     * Taps on 'Cancel' button.
     */
    public void tapOnCancelButton() {
        ImageButton cancelButton = getSolo().getImageButton(1);
        ViewUtils.tapOnView(cancelButton, "Cancel");
    }

    /**
     * Taps on 'Replay' button.
     */
    public void tapOnReplayButton() {
        ImageButton replayButton = getSolo().getImageButton(2);
        ViewUtils.tapOnView(replayButton, "Replay");
    }

    /**
     * Taps on 'Up Arrow' button.
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = getUpArrowButton();
        ViewUtils.tapOnView(upArrowButton, "Up arrow button");
    }

    /**
     * Taps on 'Down Arrow' button.
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = getDownArrowButton();
        ViewUtils.tapOnView(downArrowButton, "Down arrow button");
    }

    /**
     * Taps on Connection, who send invite.
     */
    public void tapOnConnectionProfile() {
        TextView connection = getSolo().getText(1);
        ViewUtils.tapOnView(connection, "Connection profile");
    }

    /**
     * Opens {@code ScreenProfileOfNotConnectedUser} of first user in invitation
     * list.
     * 
     * @return {@code ScreenProfileOfNotConnectedUser}.
     */
    public ScreenProfileOfNotConnectedUser openConnectionProfile() {
        tapOnConnectionProfile();
        return new ScreenProfileOfNotConnectedUser();
    }

    public static String getInvitationUserName() {
        ArrayList<View> view = Id.getListOfViewByViewIdName(INVITATION_LAYOUT);
        Assert.assertTrue("'profile layout' is not present", view.size() > 1);
        RelativeLayout invitationLayout = (RelativeLayout) view.get(1);
        Assert.assertTrue("Profile Name is not present",
                (TextView) invitationLayout.getChildAt(INVITATION_DISLAY_NAME_INDEX) != null);
        return ((TextView) invitationLayout.getChildAt(INVITATION_DISLAY_NAME_INDEX)).getText()
                .toString();
    }

    public static void verifyThatInvitationScreenChanged() {
        final String userName = getInvitationUserName();
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation Detail' screen is not changed", new Callable<Boolean>() {
                    public Boolean call() {
                        return (TextViewUtils.getTextViewByText(userName) == null);
                    }
                });
    }

    // ACTIONS --------------------------------------------------------------
    public static void inbox_invitation_detail(String screenshotName) {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_ALL_INVITATION_LAYOUT);
        Assert.assertNotNull("'Invitation' row is not present on 'Inbox' screen", invitationLayout);
        ViewGroupUtils.tapFirstViewInLayout(invitationLayout, true, "'Invitation' row", null);

        new ScreenInvitationDetails();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "inbox_invitation_detail")
    public static void inbox_invitation_detail() {
        inbox_invitation_detail("inbox_invitation_detail");
    }

    @TestAction(value = "go_to_inbox_invitation_detail")
    public static void go_to_inbox_invitation_detail(String email, String password) {
        ScreenInbox.go_to_inbox(email, password);
        ScreenInbox.inbox_tap_all_invitations();
        inbox_invitation_detail("go_to_inbox_invitation_detail");
    }

    @TestAction(value = "inbox_invitation_detail_tap_back")
    public static void inbox_invitation_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_back");
    }

    @TestAction(value = "inbox_invitation_detail_tap_invitation_up")
    public static void inbox_invitation_detail_tap_invitation_up() {
        ImageView upButton = (ImageView) Id.getViewByViewIdName(UP_LAYOUT);
        Assert.assertTrue("'Up' button is not enabled", upButton.isEnabled());
        Logger.i("Tapping on 'Up' button");
        getSolo().clickOnView(upButton);
        verifyThatInvitationScreenChanged();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_invitation_up");
    }

    @TestAction(value = "inbox_invitation_detail_tap_invitation_down")
    public static void inbox_invitation_detail_tap_invitation_down() {
        ImageView downButton = (ImageView) Id.getViewByViewIdName(DOWN_LAYOUT);
        Assert.assertTrue("'Down' button is not enabled", downButton.isEnabled());
        Logger.i("Tapping on 'Down' button");
        getSolo().clickOnView(downButton);
        verifyThatInvitationScreenChanged();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_invitation_down");
    }

    @TestAction(value = "inbox_invitation_detail_tap_profile")
    public static void inbox_invitation_detail_tap_profile() {
        new ScreenInvitationDetails().tapOnConnectionProfile();
        new ScreenProfileOfNotConnectedUser();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_profile");
    }

    @TestAction(value = "inbox_invitation_detail_tap_profile_reset")
    public static void inbox_invitation_detail_tap_profile_reset() {
        HardwareActions.pressBack();
        new ScreenInvitationDetails();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_profile_reset");
    }

    @TestAction(value = "inbox_invitation_detail_tap_reply")
    public static void inbox_invitation_detail_tap_reply() {
        new ScreenInvitationDetails().tapOnReplayButton();
        new ScreenReplyMessage();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_reply");
    }

    @TestAction(value = "inbox_invitation_detail_tap_reply_reset")
    public static void inbox_invitation_detail_tap_reply_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_reply_reset");
    }

    @TestAction(value = "inbox_invitation_detail_tap_expose")
    public static void inbox_invitation_detail_tap_expose() {
        new ScreenInvitationDetails().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_expose");
    }

    @TestAction(value = "inbox_invitation_detail_tap_expose_reset")
    public static void inbox_invitation_detail_tap_expose_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_expose_reset");
    }

    @TestAction(value = "inbox_invitation_detail_tap_accept")
    public static void inbox_invitation_detail_tap_accept() {
        ImageButton acceptButton = getSolo().getImageButton(0);
        ViewUtils.tapOnView(acceptButton, "'Accept' button");
        new ScreenInvitationsAll();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_accept");
    }

    @TestAction(value = "inbox_invitation_detail_tap_decline")
    public static void inbox_invitation_detail_tap_decline() {
        ImageButton declineButton = getSolo().getImageButton(1);
        ViewUtils.tapOnView(declineButton, "'Decline' button");
        new ScreenInvitationsAll();
        TestUtils.delayAndCaptureScreenshot("inbox_invitation_detail_tap_accept");
    }
}
