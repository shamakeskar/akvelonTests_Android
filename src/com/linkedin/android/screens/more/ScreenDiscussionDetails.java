package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseDiscussionDetailsScreen;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.DetailsScreensUtils;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Discussion Detail screen.
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 1:35:37 PM
 */
public class ScreenDiscussionDetails extends BaseDiscussionDetailsScreen {
    // CONSTANTS ------------------------------------------------------------

    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    protected static final int LIKE_BUTTON_INDEX = 0;
    protected static final int COMMENT_BUTTON_INDEX = 1;

    private static final ViewIdName FOOTER_LAYOUT = new ViewIdName("nav_footer");
    private static final ViewIdName UP_LAYOUT = new ViewIdName("nav_inbox_previous");
    private static final ViewIdName DOWN_LAYOUT = new ViewIdName("nav_inbox_next");
    private static final ViewIdName PROFILE_LAYOUT = new ViewIdName("profile_template_2");
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    private static final ViewIdName PROFILE_COMMENTER_LAYOUT = new ViewIdName("comments_row");

    private static final int PROFILE_AUTHOR_NAME_INDEX = 2;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();

        WaitActions.waitForTrueInFunction("Group discussion detail screen is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        View footer = (View) Id.getViewByViewIdName(FOOTER_LAYOUT);
                        return footer != null;
                    }
                });
    }

    /**
     * Taps on up arrow button.
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(upArrowButton, "up arrow button");
    }

    /**
     * Taps on down arrow button.
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(downArrowButton, "down arrow button");
    }

    public static String getDiscussionAuthor() {
        RelativeLayout profileLayout = (RelativeLayout) Id.getViewByViewIdName(PROFILE_LAYOUT);
        Assert.assertNotNull("'Profile layout' is not present", profileLayout);
        return ((TextView) profileLayout.getChildAt(PROFILE_AUTHOR_NAME_INDEX)).getText()
                .toString();
    }

    public static void verifyThatInvitationScreenChanged(final String userName) {
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Discussion Detail' screen is not changed", new Callable<Boolean>() {
                    public Boolean call() {
                        return (getDiscussionAuthor() != userName);
                    }
                });
    }

    /**
     * Taps on Like button.
     */
    public void tapOnLikeButton() {
        ImageButton likeButton = (ImageButton) Id.getViewByViewIdName(LIKE_BUTTON);
        Assert.assertTrue("'Like' button is not present.", likeButton.isShown());
        ViewUtils.tapOnView(likeButton, "'Like' button");
    }

    /**
     * Opens 'Likes List' screen by tapping '.. people liked this' button.
     * 
     * @return {@code ScreenLikesList} with just opened 'Likes List' screen.
     */
    public ScreenLikes openLikesList() {
        View likesButton = DetailsScreensUtils.getPeopleLikedLabel();
        Assert.assertNotNull("'.. people liked this' button is not present", likesButton);

        HardwareActions.scrollDownUntilElementVisible(likesButton, false, 10);
        ViewUtils.tapOnView(likesButton, "'.. people liked this' button");

        return new ScreenLikes();
    }

    /**
     * Opens 'Comments List' screen by tapping 'View all .. comments' button.
     * 
     * @return {@code ScreenCommentsList} with just opened 'Comments List'
     *         screen.
     */
    public ScreenDiscussionCommentList openCommentsList() {
        View allCommentsButton = DetailsScreensUtils.getViewAllCommentsButton();
        Assert.assertNotNull("'View all .. comments' button is not present", allCommentsButton);

        HardwareActions.scrollDownUntilElementVisible(allCommentsButton, false, 10);
        ViewUtils.tapOnView(allCommentsButton, "'View all .. comments' button");

        return new ScreenDiscussionCommentList();
    }

    // ACTIONS --------------------------------------------------------------
    public static void groups_discussion_detail(String screenshotName) {
        ScreenGroupsDiscussionList.groups_discussion_list_tap_discussion();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "groups_discussion_detail")
    public static void groups_discussion_detail() {
        groups_discussion_detail("groups_discussion_detail");
    }

    @TestAction(value = "go_to_groups_discussion_detail")
    public static void go_to_groups_discussion_detail(String email, String password) {

        ScreenGroupsDiscussionList.go_to_groups_discussion_list(email, password);
        groups_discussion_detail("go_to_groups_discussion_detail");

    }

    @TestAction(value = "groups_discussion_detail_tap_back")
    public static void groups_discussion_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_back");
    }

    @TestAction(value = "groups_discussion_detail_tap_expose")
    public static void groups_discussion_detail_tap_expose() {
        new ScreenDiscussionDetails().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_expose");
    }

    @TestAction(value = "groups_discussion_detail_tap_expose_reset")
    public static void groups_discussion_detail_tap_expose_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_expose_reset");
    }

    @TestAction(value = "groups_discussion_detail_tap_discussion_down")
    public static void groups_discussion_detail_tap_discussion_down() {
        String userName = getDiscussionAuthor();
        ImageView downButton = (ImageView) Id.getViewByViewIdName(DOWN_LAYOUT);
        Assert.assertTrue("'Down' button is not enabled", downButton.isEnabled() == true);
        Logger.i("Tapping on 'Down' button");
        getSolo().clickOnView(downButton);
        verifyThatInvitationScreenChanged(userName);
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_discussion_down");
    }

    @TestAction(value = "groups_discussion_detail_tap_discussion_up")
    public static void groups_discussion_detail_tap_discussion_up() {
        String userName = getDiscussionAuthor();
        ImageView upButton = (ImageView) Id.getViewByViewIdName(UP_LAYOUT);
        Assert.assertTrue("'Up' button is not enabled", upButton.isEnabled() == true);
        Logger.i("Tapping on 'Up' button");
        getSolo().clickOnView(upButton);
        verifyThatInvitationScreenChanged(userName);
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_discussion_up");
    }

    @TestAction(value = "groups_discussion_detail_tap_profile_author")
    public static void groups_discussion_detail_tap_profile_author() {
        RelativeLayout profileLayout = (RelativeLayout) Id.getViewByViewIdName(PROFILE_LAYOUT);
        Assert.assertNotNull("'Profile layout' is not present", profileLayout);
        TextView author = (TextView) profileLayout.getChildAt(PROFILE_AUTHOR_NAME_INDEX);
        ViewUtils.tapOnView(author, "Profile Author");
        new ScreenProfileOfNotConnectedUser();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_profile_author");
    }

    @TestAction(value = "groups_discussion_detail_tap_profile_author_reset")
    public static void groups_discussion_detail_tap_profile_author_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_profile_author_reset");
    }

    @TestAction(value = "groups_discussion_detail_tap_like_toggle")
    public static void groups_discussion_detail_tap_like_toggle() {
        final ScreenDiscussionDetails discussionDetail = new ScreenDiscussionDetails();
        int countOfLikes = DetailsScreensUtils.getLikesCount();
        discussionDetail.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        countOfLikes = DetailsScreensUtils.getLikesCount();
        discussionDetail.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_like_toggle");
    }

    @TestAction(value = "groups_discussion_detail_tap_comment")
    public static void groups_discussion_detail_tap_comment() {
        ImageButton commentButton = getSolo().getImageButton(COMMENT_BUTTON_INDEX);
        ViewUtils.tapOnView(commentButton, "'Comment' button");
        new ScreenAddCommentToDiscussion();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_comment");
    }

    @TestAction(value = "groups_discussion_detail_tap_comment_reset")
    public static void groups_discussion_detail_tap_comment_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_comment_reset");
    }

    @TestAction(value = "groups_discussion_detail_tap_comments_list")
    public static void groups_discussion_detail_tap_comments_list() {
        new ScreenDiscussionDetails().openCommentsList();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_comments_list");
    }

    @TestAction(value = "groups_discussion_detail_tap_comments_list_reset")
    public static void groups_discussion_detail_tap_comments_list_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_comments_list_reset");
    }

    @TestAction(value = "groups_discussion_detail_tap_likes_list")
    public static void groups_discussion_detail_tap_likes_list() {
        new ScreenDiscussionDetails().openLikesList();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_likes_list");
    }

    @TestAction(value = "groups_discussion_detail_tap_likes_list_reset")
    public static void groups_discussion_detail_tap_likes_list_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_likes_list_reset");
    }

    @TestAction(value = "groups_discussion_detail_tap_profile_commenter")
    public static void groups_discussion_detail_tap_profile_commenter() {
        RelativeLayout profileLayout = (RelativeLayout) ViewUtils.scrollToViewById(
                PROFILE_COMMENTER_LAYOUT, ViewUtils.SCROLL_DOWN, 5);
        Assert.assertNotNull("'Profile commenter' is not present", profileLayout);
        ViewGroupUtils.tapFirstViewInLayout(profileLayout, true, "first message", null);
        new ScreenProfileOfNotConnectedUser();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_profile_commenter");
    }

    @TestAction(value = "groups_discussion_detail_tap_profile_commenter_reset")
    public static void groups_discussion_detail_tap_profile_commenter_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenDiscussionDetails();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_detail_tap_profile_commenter_reset");
    }
}
