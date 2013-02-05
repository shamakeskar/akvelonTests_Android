package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.updates.ScreenUpdateProfile;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for "Updated profile rollup" screen.
 * 
 * @author vasily.gancharov
 * @created Sep 21, 2012 17:37:31 PM
 */
public class ScreenUpdatedProfileRollup extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.home.v2.StreamDetailListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "StreamDetailListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenUpdatedProfileRollup() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();

        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "Updates Profile rollup list is not present", new Callable<Boolean>() {
                    public Boolean call() {
                        return TextViewUtils.searchTextViewInActivity(
                                " has updated these sections", false) != null;
                    }
                });
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

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_updates_profile_rollup_list")
    public static void go_to_updates_profile_rollup_list(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);

        updates_profile_rollup_list("go_to_updates_profile_rollup_list");
    }

    @TestAction(value = "updates_profile_rollup_list")
    public static void updates_profile_rollup_list() {
        updates_profile_rollup_list("updates_profile_rollup_list");
    }

    public static void updates_profile_rollup_list(String screenshotName) {
        new ScreenUpdates().openFirstUpdateProfileRollUpScreen();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "updates_profile_rollup_list_tap_back")
    public static void updates_profile_rollup_list_tap_back() {
        HardwareActions.pressBack();
        WaitActions.waitForScreenUpdate(); // Wait until screen is loaded.
        HardwareActions.scrollToTop();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_profile_rollup_list_tap_back");
    }

    @TestAction(value = "updates_profile_rollup_list_tap_update")
    public static void updates_profile_rollup_list_tap_update() {
        new ScreenUpdatedProfileRollup().openFirstUpdateProfile();

        TestUtils.delayAndCaptureScreenshot("updates_profile_rollup_list_tap_update");
    }

    @TestAction(value = "updates_profile_rollup_list_tap_update_reset")
    public static void updates_profile_rollup_list_tap_update_reset() {
        HardwareActions.pressBack();
        new ScreenUpdatedProfileRollup();

        TestUtils.delayAndCaptureScreenshot("updates_profile_rollup_list_tap_update_reset");
    }

    @TestAction(value = "updates_profile_rollup_list_tap_expose")
    public static void updates_profile_rollup_list_tap_expose() {
        new ScreenUpdatedProfileRollup().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("updates_profile_rollup_list_tap_expose");
    }

    @TestAction(value = "updates_profile_rollup_list_tap_expose_reset")
    public static void updates_profile_rollup_list_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenUpdatedProfileRollup();

        TestUtils.delayAndCaptureScreenshot("updates_profile_rollup_list_tap_expose_reset");
    }
}
