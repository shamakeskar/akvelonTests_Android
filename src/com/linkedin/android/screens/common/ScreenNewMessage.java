package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreenMessage;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
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
    //private static final ViewIdName ID_NAME_CHECKBOX = new ViewIdName("checkbox");
    private static final ViewIdName ID_NAME_OF_TITLE = new ViewIdName("navigation_bar_title");

    private static int INDEX_OF_DONE_BUTTON = 0;

    // PROPERTIES -------------------------------------------------------------

    // CONSTRUCTORS -----------------------------------------------------------

    // METHODS -----------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        WaitActions.waitForTrueInFunction("New Message label is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView title = (TextView) Id.getViewByViewIdName(ID_NAME_OF_TITLE);
                        return (title.getText().toString().equalsIgnoreCase("New Message"));
                    }
                });
    }

    protected String getHeader() {
        return HEADER;
    }

    @TestAction(value = "go_to_message_compose")
    public static void go_to_message_compose(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenUpdates.updates_tap_expose();
        ScreenExpose.expose_tap_inbox();
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
    public static void message_compose_tap_send_precondition(String recipient, String message,
            String subject) {
        message_compose_tap_add_recipients();
        TextView connection = TextViewUtils.getTextViewByText(recipient);
        final RelativeLayout layout = (RelativeLayout) connection.getParent();
        CheckedTextView check = null;
        int indexOfCheckBox;
        for (indexOfCheckBox = 0; indexOfCheckBox < layout.getChildCount(); indexOfCheckBox++) {
            if (layout.getChildAt(indexOfCheckBox) instanceof CheckedTextView) {
                check = (CheckedTextView) layout.getChildAt(indexOfCheckBox);
                break;
            }
        }
        Assert.assertNotNull("There is no checkBox on screen", check);
        final boolean isChecked = check.isChecked();
        ViewUtils.tapOnView(check, "Checkbox of " + recipient);
        final int secondIndex = indexOfCheckBox;
        WaitActions.waitForTrueInFunction("CheckBox is not checked", new Callable<Boolean>() {
            public Boolean call() {
                if (!(layout.getChildAt(secondIndex) instanceof CheckedTextView)) return false;
                boolean cheked = ((CheckedTextView) layout.getChildAt(secondIndex)).isChecked();
                return (cheked != isChecked);
            }
        });

        Button done = getSolo().getButton(INDEX_OF_DONE_BUTTON);
        ViewUtils.tapOnView(done, "'Done' button");
        new ScreenNewMessage();
        TestUtils.typeTextInEditText1(TestUtils.verifyText(subject));
        TestUtils.typeTextInEditText2(TestUtils.verifyText(message));
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_send_precondition");
    }

    @TestAction(value = "message_compose_tap_send")
    public static void message_compose_tap_send() {
        Button send = getSolo().getButton(0);
        ViewUtils.tapOnView(send, "'Send' button");
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
