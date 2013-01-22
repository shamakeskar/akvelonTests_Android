package com.linkedin.android.screens.inbox;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
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
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.invitations.InviteByEmailActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "InviteByEmailActivity";
    private static final String SEND_BUTTON = "Send";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewInvitation() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Invite' label is not presented", getSolo().waitForText("Invite"));

        Button sendButton = getSolo().getButton(0);
        ImageView addConnectionButton = getSolo().getImage(0);

        Assert.assertNotNull("'Send' button is not presented", sendButton);
        Assert.assertNotNull("'Add' button is not presented", addConnectionButton);
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

    /**
     * Types random subject of your message.
     * 
     * @return subject
     */
    public String typeRandomSubject() {
        String subject = "Subject " + Math.random();
        Assert.assertNotNull("Subject field is not present.", getSolo().getEditText(0));

        Logger.i("Typing random subject: '" + subject + "'");
        getSolo().enterText(0, subject);

        return subject;
    }

    /**
     * Types random message.
     * 
     * @return message
     */
    public String typeRandomMessage() {
        String message = "Test message " + Math.random();
        Assert.assertNotNull("Message field is not present.", getSolo().getEditText(1));

        Logger.i("Typing random message: '" + message + "'");
        getSolo().enterText(1, message);

        return message;
    }

    /**
     * Types message.
     * 
     * @param message
     *            is text which you want to sent.
     * @return message body.
     */
    public String typeMessageBody(String message) {
        Assert.assertNotNull("Message field is not present.", getSolo().getEditText(1));

        Logger.i("Typing message: '" + message + "'");
        getSolo().enterText(1, message);
        return message;
    }

    /**
     * Types subject of message.
     * 
     * @param subject
     *            is subject of your message.
     * @return subject of message.
     */
    public String typeMessageTitle(String subject) {
        Assert.assertNotNull("Subject field is not present.", getSolo().getEditText(0));

        Logger.i("Typing subject: '" + subject + "'");
        getSolo().enterText(0, subject);
        return subject;
    }

    public static void go_to_invitation_compose() {
        LoginActions.openUpdatesScreenOnStart();
        ScreenExpose.go_to_expose();
        ScreenExpose.expose_tap_inbox();
        new ScreenInbox().openPopupCompose().tapOnNewInvitationOnPopup();
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("go_to_invitation_compose");
    }

    public static void invitation_compose_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_cancel");
    }

    public static void invitation_compose_tap_cancel_reset() {
        new ScreenInbox().openPopupCompose().tapOnNewInvitationOnPopup();
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_cancel_reset");
    }

    public static void invitation_compose_tap_send_precondition() {
        new ScreenNewInvitation().typeMessageTitle(StringData.test_email);
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_send_precondition");
    }

    public static void invitation_compose_tap_send() {
        Button button = getSolo().getButton(SEND_BUTTON);

        Assert.assertNotNull("'" + SEND_BUTTON + "'" + " button is not present", button);
        ViewUtils.tapOnView(button, SEND_BUTTON);
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("invitation_compose_tap_send");
    }
}
