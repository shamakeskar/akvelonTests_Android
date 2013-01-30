package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import android.view.ViewGroup;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;

/**
 * Class for 'Comments List' screen.
 * 
 * @author evgeny.agapov
 * @created Jan 15, 2013 5:00:54 PM
 */
public class ScreenCommentsList extends BaseListScreen {

    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.CommentsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "CommentsListActivity";

    private static final ViewIdName COMMENT_LAYOUT = new ViewIdName("comments_row");
    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "Comments";

    public ScreenCommentsList() {
        super(ACTIVITY_CLASSNAME);
    }

    @Override
    public void verify() {
        WaitActions.waitForTrueInFunction("'Comments' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView titleView = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        return ((titleView != null) && (titleView.getText().equals(TITLE)));
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Comments List");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @TestAction(value = "go_to_feed_detail_comments_list")
    public static void go_to_feed_detail_comments_list(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        screenUpdates.openNews(ScreenUpdates.COMMENTS_COUNT_FOR_SPINNER_APPEARING, 0);

        feed_detail_comments_list("go_to_feed_detail_comments_list");
    }

    @TestAction(value = "feed_detail_comments_list")
    public static void feed_detail_comments_list() {
        feed_detail_comments_list("feed_detail_comments_list");
    }

    public static void feed_detail_comments_list(String screenshotName) {
        new ScreenUpdate().openCommentsList();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "feed_detail_comments_list_tap_back")
    public static void feed_detail_comments_list_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdate();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_back");
    }

    @TestAction(value = "feed_detail_comments_list_tap_expose")
    public static void feed_detail_comments_list_tap_expose() {
        new ScreenCommentsList().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_expose");
    }

    @TestAction(value = "feed_detail_comments_list_tap_expose_reset")
    public static void feed_detail_comments_list_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenCommentsList();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_expose_reset");
    }

    @TestAction(value = "feed_detail_comments_list_tap_profile_author")
    public static void feed_detail_comments_list_tap_profile_author() {
        ViewGroup commentLayout = (ViewGroup) Id.getViewByViewIdName(COMMENT_LAYOUT);
        ViewGroupUtils.tapFirstViewInLayout(commentLayout, true, "first comment author", null);
        new ScreenProfile();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_profile_author");
    }

    @TestAction(value = "feed_detail_comments_list_tap_profile_author_reset")
    public static void feed_detail_comments_list_tap_profile_author_reset() {
        HardwareActions.pressBack();
        new ScreenCommentsList();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_profile_author_reset");
    }

    @TestAction(value = "feed_detail_comments_list_scroll_load_more")
    public static void feed_detail_comments_list_scroll_load_more() {
        new ScreenCommentsList().scrollDownForLoadMore();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_scroll_load_more");
    }
}
