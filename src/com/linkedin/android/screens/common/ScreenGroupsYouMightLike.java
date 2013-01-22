package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.more.ScreenGroupsDiscussionList;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Groups You Might Like screen.
 * 
 * @author nikita.chehomov
 * @created Aug 31, 2012 2:05:62 PM
 */
// Group join request sent.
public class ScreenGroupsYouMightLike extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupsYouMightLikeListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsYouMightLikeListActivity";

    static int INDEX_OF_LAYOUT_CHILD = 3;
    static int INDEX_OF_LOCK = 1;
    public static final String MENU_ITEM_REFRESH = "Refresh";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroupsYouMightLike() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue(
                "'You Might Like' label is not present",
                getSolo().waitForText("You Might Like", 1, DataProvider.WAIT_DELAY_LONG, false,
                        false));

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "GYML");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'OK' button on popup.
     */
    public void tapOnOkOnPopup() {
        Button okButton = getSolo().getButton("Ok");
        ViewUtils.tapOnView(okButton, "'Ok' button");
    }

    /**
     * Taps on 'Cancel' button on popup.
     */
    public void tapOnCancelOnPopup() {
        Button cancelButton = getSolo().getButton("Cancel");
        ViewUtils.tapOnView(cancelButton, "'Cancel' button");
    }

    /**
     * Taps on first visible 'Join' button.
     */
    public void tapOnFirstVisibleJoinButton() {
        Button joinButton = getSolo().getButton("Join");
        ViewUtils.tapOnView(joinButton, "'Join' button");
    }

    /**
     * Taps on first visible closed group.
     */
    public void tapOnVisibleClosedGroup() {

        RelativeLayout layout = null, layoutChild = null;
        ImageView lockImage = null;
        for (Button button : getSolo().getCurrentButtons()) {
            if (button.getParent() instanceof RelativeLayout) {
                layout = (RelativeLayout) button.getParent();
                if (layout.getChildAt(INDEX_OF_LAYOUT_CHILD) instanceof RelativeLayout) {
                    layoutChild = (RelativeLayout) layout.getChildAt(INDEX_OF_LAYOUT_CHILD);
                    if (layoutChild.getChildAt(INDEX_OF_LOCK).isShown())
                        lockImage = (ImageView) layoutChild.getChildAt(INDEX_OF_LOCK);
                }
            }
        }
        ViewUtils.tapOnView(lockImage, "'Lock' image (show)");
    }

    /**
     * Taps on first visible not closed group.
     */
    public void tapOnVisibleNotClosedGroup() {

        RelativeLayout layout = null, layoutChild = null;
        ImageView lockImage = null;
        for (Button button : getSolo().getCurrentButtons()) {
            if (button.getParent() instanceof RelativeLayout) {
                layout = (RelativeLayout) button.getParent();
                if (layout.getChildAt(INDEX_OF_LAYOUT_CHILD) instanceof RelativeLayout) {
                    layoutChild = (RelativeLayout) layout.getChildAt(INDEX_OF_LAYOUT_CHILD);
                    if (!layoutChild.getChildAt(INDEX_OF_LOCK).isShown())
                        lockImage = (ImageView) layoutChild.getChildAt(INDEX_OF_LOCK);
                }
            }
        }
        ViewUtils.tapOnView(lockImage, "'Lock' image (not show)");
    }

    // ACTIONS --------------------------------------------------------------
    public static void groups_gyml(String screenshotName) {
        ScreenGroups.groups_tap_gyml();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void groups_gyml() {
        groups_gyml("groups_gyml");
    }

    public static void go_to_groups_gyml() {
        ScreenGroups.go_to_groups();
        groups_gyml("go_to_groups_gyml");
    }

    public static void groups_gyml_tap_back() {
        HardwareActions.pressBack();
        new ScreenGroups();
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_back");
    }

    public static void groups_gyml_tap_expose() {
        new ScreenGroupsYouMightLike().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_expose");
    }

    public static void groups_gyml_tap_expose_reset() {
        tapOnINButton();
        new ScreenGroupsYouMightLike();
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_expose_reset");
    }

    public static void groups_gyml_tap_join() {
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_join");
        new ScreenGroupsYouMightLike().tapOnFirstVisibleJoinButton();
        new ScreenGroupsYouMightLike().verifyToast("Group join request sent.");
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_join");
    }

    public static void groups_gyml_tap_group() {

        new ScreenGroupsYouMightLike().tapOnVisibleNotClosedGroup();
        WaitActions.waitForScreenUpdate(); // wait until 'Groups Discussion
                                           // List' screen is loaded.
        ScreenGroupsDiscussionList screenGroupsDiscussionList = new ScreenGroupsDiscussionList();
        Button joinButton = getSolo().getButton("Join");
        ViewUtils.tapOnView(joinButton, "'Join' button");
        screenGroupsDiscussionList.verifyToast("Group join request sent.");
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_join");
    }

    public static void groups_gyml_tap_group_reset() {
        HardwareActions.pressBack();
        new ScreenGroupsYouMightLike();
        TestUtils.delayAndCaptureScreenshot("groups_gyml_tap_group_reset");
    }

    public static void groups_gyml_pull_refresh() {
        HardwareActions.pressMenu();

        HardwareActions.tapOnMenuOption(MENU_ITEM_REFRESH);

        new ScreenGroupsYouMightLike();
        TestUtils.delayAndCaptureScreenshot("updates_pull_refresh");
    }

    /**
     * Scroll down, wait for load more records.
     */
    public void scrollDownLoadMore() {
        getSolo().scrollToBottom();

        // APP BUG: spinner doesn't present sometimes.
        // Check that you see spinner.
        Assert.assertTrue("Spinner is not present.",
                getSolo().waitForView(ProgressBar.class, 1, DataProvider.WAIT_DELAY_SHORT, false));
        // Wait when the spinner will disappear.
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "Spinner didn't disapear.", new Callable<Boolean>() {
                    public Boolean call() {
                        return !(getSolo().waitForView(ProgressBar.class, 1,
                                DataProvider.WAIT_DELAY_DEFAULT, false));
                    }
                });
    }

    public static void groups_gyml_scroll_load_more() {
        new ScreenGroupsYouMightLike().scrollDownLoadMore();
        TestUtils.delayAndCaptureScreenshot("groups_gyml_scroll_load_more");
    }
}