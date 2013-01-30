package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Categories screen
 * 
 * @author evgeny.agapov
 * @created Jan 10, 2013 3:19:17 PM
 */
public class ScreenCategories extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.TopicsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "TopicsListActivity";

    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "Categories";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCategories() {
        super(ACTIVITY_CLASSNAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkedin.android.screens.base.BaseINScreen#verify()
     */
    @Override
    public void verify() {
        WaitActions.waitForTrueInFunction("'Categories' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView title = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        Assert.assertNotNull("'Categories' title is not present", title);
                        return title.getText().equals(TITLE);
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Categories");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @TestAction(value = "go_to_news_manage")
    public static void go_to_news_manage(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);
        new ScreenUpdates().openLinkedInTodayScreen();

        news_manage("go_to_news_manage");
    }

    @TestAction(value = "news_manage")
    public static void news_manage() {
        news_manage("news_manage");
    }

    public static void news_manage(String screenshotName) {
        View manageButton = (View) Id.getViewByViewIdName(ScreenLinkedInToday.MANAGE_BUTTON);
        Assert.assertNotNull("'Manage' button (top right corner) is not present");

        ViewUtils.tapOnView(manageButton, "'Manage' button");

        new ScreenCategories();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "news_manage_tap_back")
    public static void news_manage_tap_back() {
        HardwareActions.pressBack();
        new ScreenLinkedInToday();

        TestUtils.delayAndCaptureScreenshot("news_manage_tap_back");
    }

    @TestAction(value = "news_manage_tap_expose")
    public static void news_manage_tap_expose() {
        new ScreenCategories().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("news_manage_tap_expose");
    }

    @TestAction(value = "news_manage_tap_expose_reset")
    public static void news_manage_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenCategories();

        TestUtils.delayAndCaptureScreenshot("news_manage_tap_expose_reset");
    }
}
