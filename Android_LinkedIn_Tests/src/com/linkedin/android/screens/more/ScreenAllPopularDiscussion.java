package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for All Popular screen.
 * 
 * @author Aleksey.Chichagov
 * @created Sep 7, 2012 3:46:37 PM
 */
public class ScreenAllPopularDiscussion extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupsDiscussionListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsDiscussionListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAllPopularDiscussion() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Most Popular' label is not present",
                getSolo()
                        .waitForText("Most Popular", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();
        verifyRightButtonInNavigationBar("New discussion");

        HardwareActions.takeCurrentActivityScreenshot("Your group screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
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
}
