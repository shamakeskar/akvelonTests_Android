package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Discussion Detail screen.
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 1:35:37 PM
 */
public class ScreenLikes extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupPostLikesListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupPostLikesListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenLikes() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue(
                "'Likes' labels is not present",
                getSolo().waitForText("Likes", 2, DataProvider.WAIT_DELAY_LONG, false,
                        false));

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("Likes screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    public void tapOnProfileWhoLikeDiscussion() {
        TextView profile = getSolo().getText(3);
        ViewUtils.tapOnView(profile, "Profile");
    }
}