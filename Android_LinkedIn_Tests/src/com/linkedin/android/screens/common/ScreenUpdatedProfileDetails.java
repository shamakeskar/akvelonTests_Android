package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.updates.ScreenSingleUpdatedProfileDetails;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;

/**
 * Class for "Updated profile details" ("Profile roll up") screen.
 * 
 * @author vasily.gancharov
 * @created Sep 21, 2012 17:37:31 PM
 */
public class ScreenUpdatedProfileDetails extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.NUSListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivity";

    private static final String DETAILS_LABEL = "Details";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenUpdatedProfileDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {

        WaitActions.waitForText(DETAILS_LABEL, "There is no '" + DETAILS_LABEL + "' label.");

        verifyINButton();

        // TODO replace with wait action.
        // Wait while 'Updated profiles' list loads.
        final int listLoadDelaySec = 3;
        HardwareActions.delay(listLoadDelaySec);
        
        assertUpdatedProfilesList();
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
     * Gets 'Updated profiles' {@code ListView}.
     * 
     * @return 'Updated profiles' {@code ListView}.
     */
    public ListView getUpdatedProfilesList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Opens {@code ScreenSingleUpdatedProfileDetails} of first item from
     * updated profiles list.
     * 
     * @return {@code ScreenSingleUpdatedProfileDetails} of first item from
     *         updated profiles list.
     */
    public ScreenSingleUpdatedProfileDetails openFirstUpdatedProfileDetails() {
        tapOnFirstItemFromUpdatedProfilesList();

        ScreenSingleUpdatedProfileDetails screenSingleUpdatedProfileDetails = new ScreenSingleUpdatedProfileDetails();
        return screenSingleUpdatedProfileDetails;
    }

    /**
     * Taps on first item from "Updated profiles" list.
     */
    public void tapOnFirstItemFromUpdatedProfilesList() {
        LinearLayout firstItemFromUpdatedProfilesList = getFirstItemFromUpdatedProfilesList();
  
        getSolo().clickOnView(firstItemFromUpdatedProfilesList);
    }

    /**
     * Gets {@code LinearLayout} of first item from "Updated profiles" list.
     * 
     * @return {@code LinearLayout} of first item from "Updated profiles" list.
     */
    public LinearLayout getFirstItemFromUpdatedProfilesList() {
        final int firstItemIndex = 0;

        ListView updatedProfilesList = getUpdatedProfilesList();
        return ViewGroupUtils.getChildViewByIndexSafely(updatedProfilesList, firstItemIndex,
                LinearLayout.class);
    }

    /**
     * Verifies 'Updated profiles' list.
     */
    private void assertUpdatedProfilesList() {
        ListView updatedProfilesList = getUpdatedProfilesList();
        Assert.assertNotNull("'Updated profiles' list is not present", updatedProfilesList);
        Assert.assertTrue("'Updated profiles' list width does not equal to screen width",
                ListViewUtils.isListViewWidthEqualToScreenWidth(updatedProfilesList));
        Assert.assertTrue("'Updated profiles' list is empty",
                ListViewUtils.isListViewNotEmpty(updatedProfilesList));
    }

}
