package com.linkedin.android.screens.inbox;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'New Invitation' screen.
 * 
 * @author nikita.chehomov
 * @created Sep 13, 2012 9:13:23 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenNewInvitation extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.invitations.InviteByEmailActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "InviteByEmailActivity";
    private static final String SEND_BUTTON = "Send";
    private static final String EMAIL_FOR_INVITATION = "test@asdasd.ru";
    private static final ViewIdName SEND_BUTTON_LAYOUT = new ViewIdName("share_button");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewInvitation() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("New Message label is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView sendButton = (TextView) Id.getViewByViewIdName(SEND_BUTTON_LAYOUT);
                        return sendButton != null;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "New Inviatation");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Add Connections' button.
     */
    public void tapOnAddConnectionsButton() {
        ImageView addConnection = getSolo().getImage(0);
        ViewUtils.tapOnView(addConnection, "'Add Connection' button");
    }

    @TestAction(value = "go_to_invitation_compose")
    public static void go_to_invitation_compose(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenExpose.go_to_expose(email, password);
        ScreenExpose.expose_tap_inbox();
        new ScreenInbox().openPopupCompose().tapOnNewInvitationOnPopup();
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("go_to_invitation_compose");
    }

    @TestAction(value = "invitation_compose_tap_cancel")
    public static void invitation_compose_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_cancel");
    }

    @TestAction(value = "invitation_compose_tap_cancel_reset")
    public static void invitation_compose_tap_cancel_reset() {
        new ScreenInbox().openPopupCompose().tapOnNewInvitationOnPopup();
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_cancel_reset");
    }

    @TestAction(value = "invitation_compose_tap_send_precondition")
    public static void invitation_compose_tap_send_precondition() {
        TestUtils.typeTextInEditText0(EMAIL_FOR_INVITATION);
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_send_precondition");
    }

    @TestAction(value = "invitation_compose_tap_send")
    public static void invitation_compose_tap_send() {
        Button button = getSolo().getButton(SEND_BUTTON);

        Assert.assertNotNull("'" + SEND_BUTTON + "'" + " button is not present", button);
        ViewUtils.tapOnView(button, SEND_BUTTON);
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_send");
    }
}
