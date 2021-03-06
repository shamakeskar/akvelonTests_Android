package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
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

        HardwareActions.takeCurrentActivityScreenshot("Recent Activity screen");
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
     * Taps on first visible news.
     */
    public void tapOnFirstVisibleNews() {
        TextView profileName = getSolo().getText(1);
        ViewUtils.tapOnView(profileName, "'Profile' view");
    }

}
