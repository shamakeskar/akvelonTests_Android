package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import android.view.View;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for New Jobs Rollup screen.
 * 
 * @author evgeny.agapov
 * @created Jan 14, 2013 3:50:37 PM
 */
public class ScreenNewJobsRollUp extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.JobChangeRollupDetailsActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "JobChangeRollupDetailsActivity";

    private static final ViewIdName UPDATE_HEADER_VIEW = new ViewIdName("simple_action_row_header");
    private static final ViewIdName CONGRATULATION_BUTTON_VIEW = new ViewIdName(
            "simple_action_row_button");
    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "LinkedIn";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewJobsRollUp() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.linkedin.android.screens.base.BaseINScreen#verify()
     */
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        
        WaitActions.waitForTrueInFunction("'LinkedIn' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView titleView = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        return ((titleView != null) && (titleView.getText().equals(TITLE)));
                    }
                });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.linkedin.android.screens.base.BaseScreen#waitForMe()
     */
    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "New Jobs Details");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.linkedin.android.screens.base.BaseScreen#getActivityShortClassName()
     */
    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Opens profile screen by tapping on first update.
     * 
     * @return {@code ScreenProfile} of first's update owner.
     */
    public ScreenProfile openFirstUpdate() {
        TextView updateView = (TextView) Id.getViewByViewIdName(UPDATE_HEADER_VIEW);
        ViewUtils.tapOnView(updateView, "first update");

        return new ScreenProfile();
    }

    /**
     * Opens 'New Message' screen by tapping on Congratulate button.
     * 
     * @return {@code ScreenNewMessage} of just opened message for update's
     *         owner.
     */
    public ScreenNewMessage openNewMessageScreen() {
        View buttonCongratulateView = Id.getViewByViewIdName(CONGRATULATION_BUTTON_VIEW);
        ViewUtils.tapOnView(buttonCongratulateView, "'Congratulation' button");

        return new ScreenNewMessage();
    }

    public static void go_to_updates_new_jobs_rollup_list() {
        LoginActions.openUpdatesScreenOnStart();
        updates_new_jobs_rollup_list("go_to_updates_new_jobs_rollup_list");
    }

    public static void updates_new_jobs_rollup_list() {
        updates_new_jobs_rollup_list("updates_new_jobs_rollup_list");
    }

    public static void updates_new_jobs_rollup_list(String screenshotName) {
        new ScreenUpdates().openFirstNewJobsRollUp();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void updates_new_jobs_rollup_list_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_back");
    }

    public static void updates_new_jobs_rollup_list_tap_expose() {
        new ScreenNewJobsRollUp().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_expose");
    }

    public static void updates_new_jobs_rollup_list_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenNewJobsRollUp();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_expose_reset");
    }

    public static void updates_new_jobs_rollup_list_tap_profile() {
        new ScreenNewJobsRollUp().openFirstUpdate();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_profile");
    }

    public static void updates_new_jobs_rollup_list_tap_profile_reset() {
        HardwareActions.pressBack();
        new ScreenNewJobsRollUp();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_profile_reset");
    }

    public static void updates_new_jobs_rollup_list_tap_message() {
        new ScreenNewJobsRollUp().openNewMessageScreen();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_message");
    }

    public static void updates_new_jobs_rollup_list_tap_message_reset() {
        HardwareActions.goBackOnPreviousActivity();

        TestUtils.delayAndCaptureScreenshot("updates_new_jobs_rollup_list_tap_message_reset");
    }
}