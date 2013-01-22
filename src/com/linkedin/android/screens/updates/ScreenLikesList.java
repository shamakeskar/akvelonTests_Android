package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import android.view.ViewGroup;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;

/**
 * Class for 'Likes list' screen.
 *
 * @author evgeny.agapov
 * @created Jan 17, 2013 5:17:02 PM
 */
public class ScreenLikesList extends BaseListScreen {

    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.LikesListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "LikesListActivity";

    private static final ViewIdName LIKE_LAYOUT = new ViewIdName("likes_row");
    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "Likes";
    
    public ScreenLikesList() {
        super(ACTIVITY_CLASSNAME);
    }

    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        
        WaitActions.waitForTrueInFunction("'Likes' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView titleView = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        return ((titleView != null) && (titleView.getText().equals(TITLE)));
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Likes List");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
    
    public static void go_to_feed_detail_likes_list() {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        screenUpdates.openNews(0, ScreenUpdates.LIKES_COUNT_FOR_SPINNER_APPEARING);
        
        feed_detail_likes_list("go_to_feed_detail_likes_list");
    }
    
    public static void feed_detail_likes_list(String screenshotName) {
        new ScreenUpdate().openLikesList();
        
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }
    
    public static void feed_detail_likes_list() {
        feed_detail_likes_list("feed_detail_likes_list");
    }
    
    public static void feed_detail_likes_list_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdate();

        TestUtils.delayAndCaptureScreenshot("feed_detail_likes_list_tap_back");
    }
    
    public static void feed_detail_likes_list_tap_expose() {
        new ScreenLikesList().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("feed_detail_comments_list_tap_expose");
    }
    
    public static void feed_detail_likes_list_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenLikesList();

        TestUtils.delayAndCaptureScreenshot("feed_detail_likes_list_tap_expose_reset");
    }
    
    public static void feed_detail_likes_list_tap_profile() {
        ViewGroup likesLayout = (ViewGroup) Id.getViewByViewIdName(LIKE_LAYOUT);
        ViewGroupUtils.tapFirstViewInLayout(likesLayout, true, "first like author", null);
        new ScreenProfile();

        TestUtils.delayAndCaptureScreenshot("feed_detail_likes_list_tap_profile");
    }
    
    public static void feed_detail_likes_list_tap_profile_reset() {
        HardwareActions.pressBack();
        new ScreenLikesList();

        TestUtils.delayAndCaptureScreenshot("feed_detail_likes_list_tap_profile_reset");
    }
    
    public static void feed_detail_likes_list_scroll_load_more() {
        new ScreenLikesList().scrollDownForLoadMore();

        TestUtils.delayAndCaptureScreenshot("feed_detail_likes_list_scroll_load_more");
    }
}
