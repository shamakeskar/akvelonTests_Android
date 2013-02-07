package com.linkedin.android.screens.inbox;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for All Messages screen.
 * 
 * @author nikita.chehomov
 * @created Jan 10, 2013
 */
public class ScreenAllMessages extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.notifications.NewMessageListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NewMessageListActivity";
    public static final ViewIdName ID_NAME_ALL_MESSAGES = new ViewIdName("chevron");
    public static final ViewIdName NAV_LABEL = new ViewIdName("abs__action_bar_title");
    public static final ViewIdName COMPOSE_BUTTON_ID = new ViewIdName("compose");
    public static final ViewIdName MESSAGE_ROW = new ViewIdName("message_row");
    static final Rect2DP RIGHT_NAV_BUTTON_RECT = new Rect2DP(265.4f, 28.0f, 54.6f, 49.3f);

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAllMessages() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("'All Messages' screen is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView navLabel = (TextView) Id.getViewByViewIdName(NAV_LABEL);
                        Assert.assertNotNull("Navigation title is not present", navLabel);
                        return navLabel.getText() != "Mail";
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "All Messages");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    // Actions --------------------------------------------------------------

    public static void inbox_mail_all(String screenshotName) {
        ScreenInbox.inbox_tap_all_mail("inbox_mail_all");
    }

    @TestAction(value = "inbox_mail_all")
    public static void inbox_mail_all() {
        ScreenAllMessages.inbox_mail_all("inbox_mail_all");
    }

    @TestAction(value = "go_to_inbox_mail_all")
    public static void go_to_inbox_mail_all(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        ScreenExpose.expose_tap_inbox();
        inbox_mail_all("go_to_inbox_mail_all");
    }

    @TestAction(value = "inbox_mail_all_tap_back")
    public static void inbox_mail_all_tap_back() {
        HardwareActions.pressBack();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_tap_back");
    }

    @TestAction(value = "inbox_mail_all_pull_refresh")
    public static void inbox_mail_all_pull_refresh() {
        new ScreenAllMessages().refreshScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_pull_refresh");
    }

    @TestAction(value = "inbox_mail_all_scroll_load_more")
    public static void inbox_mail_all_scroll_load_more() {
        final int countOfMessages = ListViewUtils.getFirstListView().getCount();
        HardwareActions.scrollToBottomNTimes(1, 0);
        WaitActions.waitForProgressBarDisappear();
        WaitActions.waitForTrueInFunction("New messages are not loaded", new Callable<Boolean>() {
            public Boolean call() {
                if (ListViewUtils.getFirstListView() != null)
                    return (ListViewUtils.getFirstListView().getCount() > countOfMessages);
                else
                    return false;
            }
        });
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_scroll_load_more");
    }

    @TestAction(value = "inbox_mail_all_tap_compose_new_message")
    public static void inbox_mail_all_tap_compose_new_message() {
        View compose = (View) Id.getViewByViewIdName(COMPOSE_BUTTON_ID);
        ViewUtils.tapOnView(compose, "'Compose' button");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_tap_compose_new_message");
    }

    @TestAction(value = "inbox_mail_all_tap_compose_new_message_reset")
    public static void inbox_mail_all_tap_compose_new_message_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_tap_compose_new_message_reset");
    }

    @TestAction(value = "inbox_mail_all_tap_message")
    public static void inbox_mail_all_tap_message() {
        inbox_mail_all_tap_message("inbox_mail_all_tap_message");
    }

    public static void inbox_mail_all_tap_message(String screenName) {
        RelativeLayout message = (RelativeLayout) Id.getViewByViewIdName(MESSAGE_ROW);
        Assert.assertNotNull("Message is not present on 'Inbox Mail All' screen", message);

        ViewGroupUtils.tapFirstViewInLayout(message, true, "first message", null);
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }
    
    @TestAction(value = "inbox_mail_all_tap_message_reset")
    public static void inbox_mail_all_tap_message_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_mail_all_tap_message_reset");
    }
}
