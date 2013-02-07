package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import android.view.View;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreenMessage;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'New Message' screen.
 * 
 * @author nikita.chehomov
 * @created Aug 22, 2012 3:28:23 PM
 */
public class ScreenNewMessage extends BaseScreenMessage {
    // CONSTANTS ------------------------------------------------------------
    private static final String HEADER = "New Message";
    private static final ViewIdName SEND_BUTTON = new ViewIdName("share_button");

    // PROPERTIES -------------------------------------------------------------
    
    // CONSTRUCTORS -----------------------------------------------------------

    // METHODS -----------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("New Message label is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView sendButton = (TextView) Id.getViewByViewIdName(SEND_BUTTON);
                        return sendButton != null;
                    }
                });
    }
    
    protected String getHeader() {
        return HEADER;
    }

    @TestAction(value = "go_to_message_compose")
    public static void go_to_message_compose(String email, String password) {
        ScreenInbox.go_to_inbox(email, password);
        message_compose("go_to_message_compose");
    }

    public static void message_compose(String screenshotName) {
        new ScreenInbox().openPopupCompose();
        TextView message = TextViewUtils.getTextViewByText("New Message");
        ViewUtils.tapOnView(message, "New Message button");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "message_compose")
    public static void message_compose() {
        message_compose("message_compose");
    }

    @TestAction(value = "message_compose_tap_cancel")
    public static void message_compose_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_cancel");
    }

    @TestAction(value = "message_compose_tap_add_recipients")
    public static void message_compose_tap_add_recipients() {
        View view = Id.getViewByViewIdName(new ViewIdName("add_users_button"));
        ViewUtils.tapOnView(view, "Add Recipients button");
        new ScreenAddConnections();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_add_recipients");
    }

    @TestAction(value = "message_compose_tap_send_precondition")
    public static void message_compose_tap_send_precondition(String subject, String body) {
        TestUtils.typeTextInEditText1(subject);
        TestUtils.typeTextInEditText2(body);
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_send_precondition");
    }
    
    @TestAction(value = "message_compose_tap_send_regression_precondition")
    public static void message_compose_tap_send_regression_precondition(String subject, String body, String recipient) {
        ScreenNewMessage.message_compose_tap_add_recipients();
        ScreenAddConnections.select_recipients_tap_select(recipient);
        ScreenAddConnections.select_recipients_tap_done();
        TestUtils.typeTextInEditText1(subject);
        TestUtils.typeTextInEditText2(body);
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_send_regression_precondition");
    }

    @TestAction(value = "message_compose_tap_send")
    public static void message_compose_tap_send() {
        TextView sendButton = (TextView) Id.getViewByViewIdName(SEND_BUTTON);
        ViewUtils.tapOnView(sendButton, "'Send' button");
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_send");
    }

    @TestAction(value = "message_compose_tap_add_recipients_reset")
    public static void message_compose_tap_add_recipients_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_add_recipients_reset");
    }
}
