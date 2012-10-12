package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkedin.android.screens.more.ScreenCommentDiscussion;
import com.linkedin.android.screens.more.ScreenLikes;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.IntegerUtils;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.StringDefaultValues;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Base class for Discussion Detail screens.
 * 
 * @author vasily.gancharov
 * @created Sep 19, 2012 13:39:37 PM
 */
public abstract class BaseDiscussionDetailsScreen extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupPostActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupPostActivity";

    // Indexes relatively main screen
    protected static final int LIKE_BUTTON_INDEX = 0;
    protected static final int COMMENT_BUTTON_INDEX = 1;

    protected static final ViewIdName DISCUSSION_AUTHOR_SECTION_CHEVRON = new ViewIdName(
            "connections_chevron");

    private static final String PEOPLE_LIKE_THIS_COUNTER_LABEL = "people like this";

    private static final int NO_LIKES_NUMBER = 0;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseDiscussionDetailsScreen() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets 'Like' {@code ImageButton}
     * 
     * @return 'Like' {@code ImageButton}
     */
    public ImageButton getLikeButton() {
        return getSolo().getImageButton(LIKE_BUTTON_INDEX);
    }

    /**
     * Gets 'Comment' {@code ImageButton}
     * 
     * @return 'Comment' {@code ImageButton}
     */
    public ImageButton getCommentButton() {
        return getSolo().getImageButton(COMMENT_BUTTON_INDEX);
    }

    /**
     * Verifies 'Like' button
     */
    public void assertLikeButton() {
        ImageButton likeButton = getLikeButton();
        ViewAssertUtils.assertViewIsPlacedInLayout(
                "'Like' button is not present (or its coordinates are wrong)", likeButton,
                LayoutUtils.LOWER_LEFT_OF_2_BUTTONS_LAYOUT);
    }

    /**
     * Verifies 'Comment' button
     */
    public void assertCommentButton() {
        ImageButton commentButton = getCommentButton();
        ViewAssertUtils.assertViewIsPlacedInLayout(
                "'Comment' button is not present (or its coordinates are wrong)", commentButton,
                LayoutUtils.LOWER_RIGHT_OF_2_BUTTONS_LAYOUT);
    }

    /**
     * Taps on 'Like' button. Verifies that toast and 'people like this' row are
     * appeared.
     */
    public void likeUpdate() {
        int likesNumberBeforeTap = getLikesNumber();

        ImageButton likeButton = getLikeButton();
        ViewUtils.tapOnView(likeButton, "Like");
        verifyToast("Update Liked");

        int likesNumberAfterTap = getLikesNumber();
        TextView likesCounter = getLikesCounter();

        Assert.assertNotNull(
                "'people like this' label is not presented after tapping on 'Like' button",
                likesCounter);
        Assert.assertTrue("Wrong number of likes (expected: " + (++likesNumberBeforeTap)
                + ", present: " + likesNumberAfterTap + ")",
                likesNumberBeforeTap == likesNumberAfterTap);
    }

    /**
     * Taps on 'Unlike' button. Verifies that toast is appeared.
     */
    public void unlikeUpdate() {
        // TODO not checked tapOnUnlikeButton
        int likesNumberBeforeTap = getLikesNumber();

        ImageButton likeButton = getLikeButton();
        ViewUtils.tapOnView(likeButton, "Unlike");
        verifyToast("Update UnLiked");

        int likesNumberAfterTap = getLikesNumber();

        Assert.assertTrue("Wrong number of likes (expected: " + (--likesNumberBeforeTap)
                + ", present: " + likesNumberAfterTap + ")",
                likesNumberBeforeTap == likesNumberAfterTap);
    }

    /**
     * Taps on 'Comment' button.
     */
    public void tapOnCommentButton() {
        ImageButton commentButton = getCommentButton();
        ViewUtils.tapOnView(commentButton, "Comment");
    }

    /**
     * Gets number of people that like current discussion.
     * 
     * @return number of people that like current discussion or
     *         <b>NO_LIKES_NUMBER</b> if there is no/wrong "people like this"
     *         label.
     */
    public int getLikesNumber() {
        TextView peopleLikeThisCounter = getLikesCounter();
        if (null == peopleLikeThisCounter) {
            return NO_LIKES_NUMBER;
        }

        String peopleLikeThisCounterText = peopleLikeThisCounter.getText().toString();
        int likesNumber = getNumberFromLikesCounterText(peopleLikeThisCounterText);
        return likesNumber;
    }

    /**
     * Opens 'Comment' screen.
     * 
     * @return {@codeScreenCommentDiscussion} with just opened 'Comment' screen.
     */
    public ScreenCommentDiscussion openCommentScreen() {
        tapOnCommentButton();
        return new ScreenCommentDiscussion();
    }

    /**
     * Taps on image of connection, who create discussion.
     */
    public void tapOnDiscussionAuthorProfile() {
        View discussionAuthorSectionChevron = Id
                .getViewByViewIdName(DISCUSSION_AUTHOR_SECTION_CHEVRON);
        Assert.assertNotNull("There is no chevron in 'Discussion author' section",
                discussionAuthorSectionChevron);

        getSolo().clickOnView(discussionAuthorSectionChevron);
    }

    /**
     * Taps on 'People Likes This' label.
     */
    public void tapOnPeopleLikeThisLabel() {
        int count = 0;
        boolean condition = getSolo().searchText("people like this", 1, true, false);
        if (!condition) {
            count = 1;
            HardwareActions.scrollUp();
            condition = getSolo().searchText("person likes this", 1, true, false);
        }
        Assert.assertTrue("'People Like This' label is not present.", condition);
        TextView likeLabel;
        if (count == 0) {
            likeLabel = TextViewUtils.searchTextViewInActivity("people like this", false);
        } else {
            likeLabel = TextViewUtils.searchTextViewInActivity("person likes this", false);
        }
        ViewUtils.tapOnView(likeLabel, "'People Like This' label");
    }

    /**
     * Opens 'Likes' screen.
     * 
     * @return {@code ScreenLikes} with just opened 'Likes' screen.
     */
    public ScreenLikes openPeopleLikeThis() {
        tapOnPeopleLikeThisLabel();
        return new ScreenLikes();
    }

    /**
     * Gets "people like this" {@code TextView}.
     * 
     * @return "people like this" {@code TextView}.
     */
    public TextView getLikesCounter() {
        HardwareActions.scrollUp();
        TextView peopleLikeThisCounter = TextViewUtils.searchTextViewInActivity(
                PEOPLE_LIKE_THIS_COUNTER_LABEL, false);
        return peopleLikeThisCounter;
    }

    /**
     * Gets number of people that like current discussion.
     * 
     * @param likesCounterText
     *            "N people like this" {@code String}, where N - number of
     *            people that like current discussion.
     * @return number of people that like current discussion or
     *         <b>NO_LIKES_NUMBER</b> if there is no/wrong "people like this"
     *         label.
     */
    private int getNumberFromLikesCounterText(String likesCounterText) {
        if (null == likesCounterText) {
            return NO_LIKES_NUMBER;
        }

        likesCounterText = likesCounterText.replaceAll(PEOPLE_LIKE_THIS_COUNTER_LABEL,
                StringDefaultValues.EMPTY_STRING);
        likesCounterText = likesCounterText.trim();

        int likesNumber = IntegerUtils.getValueOfSafely(likesCounterText, NO_LIKES_NUMBER);
        return likesNumber;
    }

}
