package com.linkedin.android.screens.inbox;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for All Messages screen.
 * 
 * @author nikita.chehomov
 * @created Jan 10, 2013
 */
public class ScreenAllMessages extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.notifications.NewMessageListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NewMessageListActivity";
    public static final ViewIdName ID_NAME_ALL_MESSAGES = new ViewIdName("chevron");
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

        Assert.assertTrue("'LinkedIn' label is not present on 'Inbox' screen", getSolo()
                .waitForText("LinkedIn", 1, 20));
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
        HardwareActions.scrollToTop();
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
        for (ImageView view : getSolo().getCurrentImageViews()) {
            if (LayoutUtils.isViewPlacedInLayout(view, LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT)) {
                Rect2DP viewRect = new Rect2DP(view);
                if (viewRect.isSizeEqual(RIGHT_NAV_BUTTON_RECT.width, RIGHT_NAV_BUTTON_RECT.height,
                        1.0f)) {
                    ViewUtils.tapOnView(view, "'Share' icon");
                    break;
                }
            }
        }
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
        ImageView view = getSolo().getImage(2);
        ViewUtils.tapOnView(view, "First message");
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }
}
