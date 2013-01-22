package com.linkedin.android.screens.updates;

import java.util.ArrayList;

import junit.framework.Assert;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.utils.DetailsScreensUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Feed Detail actions.
 * 
 * @author alexander.makarov
 * @created Jan 14, 2013 6:20:42 PM
 */
public class ScreenFeedDetail {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Opens first Update with footer.
     * 
     * @return {@code ScreenUpdates} object.
     */
    public static ScreenUpdates openUpdateWithFooter() {
        int maxCountOfScrolls = 50;
        boolean isCanScroll = true;

        for (int i = 0; i < maxCountOfScrolls && isCanScroll; i++) {
            ArrayList<View> nusViews = Id.getListOfViewByViewIdName(ScreenUpdates.NUS_LAYOUT);
            Assert.assertTrue("No updates found on Updates screen", nusViews.size() != 0);
            for (View nusView : nusViews) {
                int nusChildsCount = ((RelativeLayout) nusView).getChildCount();
                for (int j = 0; j < nusChildsCount; j++) {
                    View view = ((RelativeLayout) nusView).getChildAt(j);
                    if (view instanceof RelativeLayout) {
                        view.measure(ScreenResolution.getScreenWidth(),
                                ScreenResolution.getScreenHeight());
                        if (view.getMeasuredWidth() > 0) {
                            ViewUtils.tapOnView(nusView, "first Update with footer");
                            return new ScreenUpdates();
                        }
                    }
                }
            }
            isCanScroll = ListViewUtils.scrollToNewItems();
        }
        Assert.fail("Cannot find Update with footer.");
        return null;
    }

    @TestAction(value = "go_to_feed_detail")
    public static void go_to_feed_detail() {
        LoginActions.openUpdatesScreenOnStart();
        openUpdateWithFooter();
        TestUtils.delayAndCaptureScreenshot("go_to_feed_detail");
    }

    @TestAction(value = "feed_detail_tap_back")
    public static void feed_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_back");
    }

    @TestAction(value = "feed_detail_tap_back_reset")
    public static void feed_detail_tap_back_reset() {
        openUpdateWithFooter();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_back_reset");
    }

    @TestAction(value = "feed_detail_tap_expose")
    public static void feed_detail_tap_expose() {
        new ScreenUpdate().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_expose");
    }

    @TestAction(value = "feed_detail_tap_expose_reset")
    public static void feed_detail_tap_expose_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_expose_reset");
    }

    @TestAction(value = "feed_detail_tap_profile_author")
    public static void feed_detail_tap_profile_author() {
        new ScreenUpdate().openConnectionProfile();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_author");
    }

    @TestAction(value = "feed_detail_tap_profile_author_reset")
    public static void feed_detail_tap_profile_author_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_author_reset");
    }

    @TestAction(value = "feed_detail_tap_company")
    public static void feed_detail_tap_company() {
        // From a Company NUS type, tap on the top row to reach the Company
        // profile page
    }

    @TestAction(value = "feed_detail_tap_profile_new_connection")
    public static void feed_detail_tap_profile_new_connection() {
        // From New Connections roll up detail view, tap on any profile
    }

    @TestAction(value = "updates_connection_rollup_list_tap_update")
    public static void updates_connection_rollup_list_tap_update() {
        // From Connections Roll up detail view, tap on a roll up
    }

    @TestAction(value = "feed_detail_tap_url")
    public static void feed_detail_tap_url() {
        ArrayList<TextView> list = TextViewUtils.getLinkTextViews();
        Assert.assertTrue("Cannot find links (LinkTextViews) on this screen", list.size() > 0);
        String link = null;
        for (int i = 0; i < list.size(); i++) {
            TextView view = list.get(i);
            link = RegexpUtils.getFirstFoundGroup(view.getText().toString(),
                    RegexpUtils.HYPERLINK_PATTERN);
            if (link != null)
                break;
        }
        Assert.assertNotNull("Cannot find links on this screen", link);
        ViewUtils.tapOnLink(link);
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_url");
    }

    @TestAction(value = "feed_detail_tap_url_reset")
    public static void feed_detail_tap_url_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_url_reset");
    }

    @TestAction(value = "feed_detail_tap_like_toggle")
    public static void feed_detail_tap_like_toggle() {
        final ScreenUpdate screenUpdate = new ScreenUpdate();
        final int countOfLikes = DetailsScreensUtils.getLikesCount();
        screenUpdate.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        screenUpdate.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_like_toggle");
    }

    @TestAction(value = "feed_detail_tap_actionsheet")
    public static void feed_detail_tap_actionsheet() {
        new ScreenUpdate().tapOnForwardButton();

        // TODO
        WaitActions.delay(2);
        Logger.logElements();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_actionsheet");
    }
}
