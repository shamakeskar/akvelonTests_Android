/**
 * 
 */
package com.linkedin.android.screens.more;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
@SuppressWarnings("rawtypes")
public class ScreenAddCommentToDiscussion extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupPostCommentAddActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupPostCommentAddActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAddCommentToDiscussion() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activity.
        Assert.assertTrue("Wrong activity " + ACTIVITY_SHORT_CLASSNAME, getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));

        Assert.assertTrue("'Comment' screen is not present",
                getSolo().waitForText("Comment", 1, DataProvider.WAIT_DELAY_DEFAULT, false, false));
    }

    @Override
    public void waitForMe() {
        getSolo().waitForActivity(ACTIVITY_SHORT_CLASSNAME);
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
}
