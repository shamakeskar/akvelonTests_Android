package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.updates.ScreenUpdateProfile;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for "Updated profile rollup" screen.
 * 
 * @author vasily.gancharov
 * @created Sep 21, 2012 17:37:31 PM
 */
public class ScreenUpdatedProfileRollup extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.NUSListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivity";

    private static final String DETAILS_LABEL = "Details";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenUpdatedProfileRollup() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForText(DETAILS_LABEL, "There is no '" + DETAILS_LABEL + "' label.");

        verifyINButton();
        assertUpdatedProfilesList();

        HardwareActions.takeCurrentActivityScreenshot("Update Profile rollup screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Update Profile Rollup");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Verifies 'Updated profiles' list.
     */
    private void assertUpdatedProfilesList() {
    	WaitActions.waitForScreenUpdate();
        ListView updatedProfilesList = getUpdatedProfilesList();
        Assert.assertNotNull("'Updated profiles' list is not present", updatedProfilesList);
        Assert.assertTrue("'Updated profiles' list width does not equal to screen width",
                ListViewUtils.isListViewWidthEqualToScreenWidth(updatedProfilesList));
        Assert.assertTrue("'Updated profiles' list is empty",
                ListViewUtils.isListViewNotEmpty(updatedProfilesList));
    }

    /**
     * Returns 'Updated profiles' {@code ListView}.
     * 
     * @return 'Updated profiles' {@code ListView}.
     */
    public ListView getUpdatedProfilesList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Returns {@code LinearLayout} of first item from "Updated profiles" list.
     * 
     * @return {@code LinearLayout} of first item from "Updated profiles" list.
     */
    public LinearLayout getFirstItemFromUpdatedProfilesList() {
        ListView list = getUpdatedProfilesList();
        View firstItem = list.getChildAt(0);
        Assert.assertTrue("First item is not instance of LinearLayout",
                firstItem instanceof LinearLayout);
        return (LinearLayout) firstItem;
    }

    /**
     * Taps on first item from "Updated profiles" list.
     */
    public void tapOnFirstItemFromUpdatedProfilesList() {
        LinearLayout firstItemFromUpdatedProfilesList = getFirstItemFromUpdatedProfilesList();
        ViewUtils.tapOnView(firstItemFromUpdatedProfilesList, "first user in list");
    }

    /**
     * Opens {@code ScreenSingleUpdatedProfileDetails} of first item from
     * updated profiles list.
     * 
     * @return {@code ScreenSingleUpdatedProfileDetails} of first item from
     *         updated profiles list.
     */
    public ScreenUpdateProfile openFirstUpdateProfile() {
        tapOnFirstItemFromUpdatedProfilesList();

        ScreenUpdateProfile screenSingleUpdatedProfileDetails = new ScreenUpdateProfile();
        return screenSingleUpdatedProfileDetails;
    }
}
