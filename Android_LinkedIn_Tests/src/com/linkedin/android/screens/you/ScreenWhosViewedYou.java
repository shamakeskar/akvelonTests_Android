package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'Who's viewed you' screen.
 * 
 * @author nikita.chehomov
 * @created Sep 7, 2012 1:35:11 PM
 */

public class ScreenWhosViewedYou extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.WVMPListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "WVMPListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenWhosViewedYou() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        Assert.assertNotNull("'Who's viewed you' text is not presented", getWhosViewedYouLabel());

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("Who's viewed you screen");
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
     * Gets 'Who's viewed you' label
     * 
     * @return 'Who's viewed you' label
     */
    public TextView getWhosViewedYouLabel() {
        HardwareActions.scrollUp();
        return getSolo().getText("Who's viewed you");
    }

    /**
     * Taps on connection.
     * 
     * @param name
     *            is name on which we need to tap.
     */
    public void tapOnConnection(String name) {
        TextView profileView = getSolo().getText(name);
        ViewUtils.tapOnView(profileView, name + " view");
    }
}
