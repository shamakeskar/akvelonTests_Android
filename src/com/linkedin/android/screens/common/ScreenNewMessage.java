package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreenMessage;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
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

    // PROPERTIES -------------------------------------------------------------
    
    // CONSTRUCTORS -----------------------------------------------------------

    // METHODS -----------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        WaitActions.waitForTrueInFunction("New Message label is not present", new Callable<Boolean>() {
                    public Boolean call() {
                        TextView title = (TextView) Id.getViewByViewIdName(new ViewIdName("navigation_bar_title"));
                        return (title.getText().toString().equalsIgnoreCase("New Message"));
                    }
                });
    }
    
    protected String getHeader() {
        return HEADER;
    }
    
    public static void go_to_message_compose() {
        LoginActions.openUpdatesScreenOnStart();
        ScreenUpdates.updates_tap_expose();
        ScreenExpose.expose_tap_inbox();
        message_compose("go_to_message_compose");
    }
    
    public static void message_compose(String screenshotName){
        if (screenshotName == null) screenshotName = "message_compose";
        new ScreenInbox().openPopupCompose();
        TextView message = TextViewUtils.getTextViewByText("New Message");
        ViewUtils.tapOnView(message, "New Message button");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }
    
    public static void message_compose() {
        message_compose("message_compose");
    }
    
    public static void message_compose_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_cancel");
    }
    
    public static void message_compose_tap_add_recipients() {
        View view = Id.getViewByViewIdName(new ViewIdName("add_users_button"));
        ViewUtils.tapOnView(view, "Add Recipients button");
        //TODO:add verify than screen Recipients will be implement.
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_add_recipients");
    }
    
    public static void message_compose_tap_send_precondition() {
        message_compose_tap_add_recipients();
        CheckedTextView checkView = (CheckedTextView) Id.getViewByViewIdName(new ViewIdName("check_box"));
        ViewUtils.tapOnView(checkView, "Check box");
        Button done = getSolo().getButton(0);
        ViewUtils.tapOnView(done, "'Done' button");
        new ScreenNewMessage();
        getSolo().enterText(1, "Test subject");
        getSolo().enterText(2, "Test message");
    }
    
    public static void message_compose_tap_send() {
        Button send = getSolo().getButton(0);
        ViewUtils.tapOnView(send, "'Send' button");
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_send");
    }
    
    public static void message_compose_tap_add_recipients_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("message_compose_tap_add_recipients_reset");
    }
}
