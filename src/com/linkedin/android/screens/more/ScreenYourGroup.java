package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for You Group screen.
 * 
 * @author Aleksey.Chichagov
 * @created Aug 28, 2012 3:20:37 PM
 */
public class ScreenYourGroup extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.groups.v2.GroupsPostsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsPostsListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenYourGroup() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("'Popular Posts' label is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return TextViewUtils.getTextViewByText("Popular Posts") != null;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Your Group");
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
     * Taps on first visible 'Latest' discussion.
     */
    public void tapOnFirstVisibleDiscussion() {
        int firstDiscussion = 0;
        Assert.assertTrue("'Latest' label is not presented", getSolo().searchText("Latest"));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                firstDiscussion++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase("Latest"))
                    break;
            }
        }
        TextView discussionView = getSolo().getText(firstDiscussion);
        ViewUtils.tapOnView(discussionView, "'Latest discussion' view");
    }

    /**
     * Taps on 'View All Popular' label.
     */
    public void tapOnViewAllPopularLabel() {
        TextView textView = getSolo().getText("View All Popular");
        ViewUtils.tapOnView(textView, "'View All Popular' label");
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
     * Opens 'Discussion Details' screen.
     * 
     * @return{@code ScreenDiscussionDetails} with just opened 'Discussion
     *               Detail' screen.
     */
    public ScreenDiscussionDetails openFirstVisibleDiscussion() {
        tapOnFirstVisibleDiscussion();
        return new ScreenDiscussionDetails();
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
     * Opens 'Discussion Details' with like.
     * 
     * @return {@code ScreenDiscussionDetails} with just opened 'Discussion
     *         Details' screen
     */
    public ScreenDiscussionDetails openDiscussionWithLike() {
        Assert.assertTrue("'Like' label is not presented",
                getSolo().searchText("Like", 1, true, false));
        TextView likeLabel = TextViewUtils.searchTextViewInActivity("Like", false);
        ViewUtils.tapOnView(likeLabel, "'Like' label");
        return new ScreenDiscussionDetails();
    }
}
