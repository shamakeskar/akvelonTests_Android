/**
 * 
 */
package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenGroups;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenGroupsDiscussionList extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupsDiscussionListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsDiscussionListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroupsDiscussionList() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Group Discussion List' screen is not present",
                getSolo()
                        .waitForText("Most Popular", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Jobs");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'New Discussion' button.
     */
    public void tapOnNewDiscussionButton() {
        tapOnRightButtonInNavigationBar("New discussion");
    }

    /**
     * Opens 'News Discussion' screen.
     * 
     * @return{@code ScreenNewDiscussion} with just opened 'New Discussion'
     *               screen
     */
    public ScreenNewDiscussion openNewDiscussionScreen() {
        tapOnNewDiscussionButton();
        return new ScreenNewDiscussion();
    }

    /**
     * Taps on first visible 'Most popular' discussion.
     */
    public void tapOnFirstVisibleMostPopularDiscussion() {
        int firstPopularDiscussion = 0;
        Assert.assertTrue("'Most Popular' label is not presented",
                getSolo().searchText("Most Popular"));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                firstPopularDiscussion++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase("Most Popular"))
                    break;
            }
        }
        TextView discussionView = getSolo().getText(firstPopularDiscussion);
        ViewUtils.tapOnView(discussionView, "'Popular discussion' view");
    }

    /**
     * Opens 'Discussion Details' screen.
     * 
     * @return{@code ScreenDiscussionDetails} with just opened 'Discussion
     *               Details' screen.
     */
    public ScreenDiscussionDetails openFirstVisibleMostPopularDiscussion() {
        tapOnFirstVisibleMostPopularDiscussion();
        return new ScreenDiscussionDetails();
    }

    /**
     * Taps on 'View All Popular' label.
     */
    public void tapOnViewAllPopularLabel() {
        TextView textView = getSolo().getText("View All Popular");
        ViewUtils.tapOnView(textView, "'View All Popular' label");
    }

    /**
     * Opens 'All Popular' screen.
     * 
     * @return{@code ScreenAllPopularDiscussion} with just opened 'All Popular'
     *               screen.
     */
    public ScreenAllPopularDiscussion openAllPopularDiscussionScreen() {
        tapOnViewAllPopularLabel();
        return new ScreenAllPopularDiscussion();
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

    // ACTIONS --------------------------------------------------------------
    public static void groups_discussion_list(String screenshotName) {
        ScreenGroups.groups_tap_group();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void groups_discussion_list() {
        groups_discussion_list("groups_discussion_list");
    }

    public static void go_to_groups_discussion_list() {
        ScreenGroups.go_to_groups();
        groups_discussion_list("go_to_groups_discussion_list");
    }

    public static void groups_discussion_list_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_back");
    }

    public static void groups_discussion_list_tap_expose() {
        new ScreenGroupsDiscussionList().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_expose");
    }

    public static void groups_discussion_list_tap_expose_reset() {
        tapOnINButton();
        new ScreenGroupsDiscussionList();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_expose_reset");
    }

    public static void groups_discussion_list_tap_post_new_discussion() {
        new ScreenGroupsDiscussionList().openNewDiscussionScreen();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_post_new_discussion");
    }

    public static void groups_discussion_list_tap_post_new_discussion_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_post_new_discussion_reset");
    }

    public static void groups_discussion_list_tap_discussion() {
        new ScreenGroupsDiscussionList().openFirstVisibleMostPopularDiscussion();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_discussion");
    }

    public static void groups_discussion_list_tap_discussion_reset() {
        HardwareActions.pressBack();
        new ScreenAllPopularDiscussion();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_discussion_reset");
    }

    public static void groups_discussion_list_tap_view_all_popular() {
        new ScreenGroupsDiscussionList().openAllPopularDiscussionScreen();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_view_all_popular");
    }

    public static void groups_discussion_list_tap_view_all_popular_reset() {
        HardwareActions.pressBack();
        new ScreenAllPopularDiscussion();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_tap_view_all_popular_reset");
    }

    public static void groups_discussion_list_scroll_load_more() {
        new ScreenGroupsDiscussionList().scrollDownLoadMore();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_scroll_load_more");
    }
}