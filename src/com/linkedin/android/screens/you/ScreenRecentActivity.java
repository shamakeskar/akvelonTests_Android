package com.linkedin.android.screens.you;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Recent Activity screen.
 * 
 * @author nikita.chehomov
 * @created Sep 13, 2012 7:42:37 PM
 */
public class ScreenRecentActivity extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.NUSListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivity";
    private static final ViewIdName NUS_LAYOUT = new ViewIdName("nus_simple_rich_row");
    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenRecentActivity() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Recent Activity' label not shown",
                getSolo().waitForText("Recent Activity"));

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Recent Activity");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on first visible news.
     */
    public void tapOnFirstVisibleNews() {
        TextView profileName = getSolo().getText(1);
        ViewUtils.tapOnView(profileName, "'Profile' view");
    }

    /**
     * Verifies NUS screen opened instead of Updates screen.
     */
    public static void verifyNUSScreen() {
        WaitActions.waitForTrueInFunction("NUS screen is not present(title not shown)",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return ((View) Id.getViewByViewIdName(TITLE_VIEW)).isShown();
                    }
                });
    }

    // ACTIONS --------------------------------------------------------------
    public static void recent_activity(String screenshotName) {
        TextView activity = TextViewUtils.searchTextViewInActivity("Recent Activity", false);
        ViewUtils.tapOnView(activity, "'Recent Activity'");
        new ScreenRecentActivity();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "recent_activity")
    public static void recent_activity() {
        recent_activity("recent_activity");
    }

    @TestAction(value = "go_to_recent_activity")
    public static void go_to_recent_activity(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        ScreenExpose.expose_tap_you();
        ScreenYou.handleEditYourProfileHint();
        recent_activity("go_to_recent_activity");
    }

    @TestAction(value = "recent_activity_expose")
    public static void recent_activity_expose() {
        new ScreenRecentActivity().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("recent_activity_expose");
    }

    @TestAction(value = "recent_activity_expose_reset")
    public static void recent_activity_expose_reset() {
        tapOnINButton();
        new ScreenRecentActivity();
        TestUtils.delayAndCaptureScreenshot("recent_activity_expose_reset");
    }

    @TestAction(value = "recent_activity_back")
    public static void recent_activity_back() {
        HardwareActions.pressBack();
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("recent_activity_back");
    }

    @TestAction(value = "recent_activity_tap_detail")
    public static void recent_activity_tap_detail() {
        RelativeLayout nusLayout = (RelativeLayout) Id.getViewByViewIdName(NUS_LAYOUT);
        Assert.assertNotNull("No updates found on Updates screen", nusLayout);

        ViewGroupUtils.tapFirstViewInLayout(nusLayout, true, "first update", null);

        verifyNUSScreen();
        TestUtils.delayAndCaptureScreenshot("recent_activity_tap_detail");
    }

    @TestAction(value = "recent_activity_tap_detail_reset")
    public static void recent_activity_tap_detail_reset() {
        HardwareActions.pressBack();
        new ScreenRecentActivity();
        TestUtils.delayAndCaptureScreenshot("recent_activity_tap_detail_reset");
    }
}
