/**
 * 
 */
package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;

import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.WebViewHandler;

/**
 * Class for 'Jobs' Screen.
 * 
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenJobs extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.jsbridge.JobsActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "JobsActivity";

    public static final String CELL_CLASS_NAME = "media-body-v2";
    public static final ViewIdName NAV_LABEL = new ViewIdName("abs__action_bar_title");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenJobs() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Jobs' label is not present",
                getSolo().waitForText("Jobs", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();

        WebViewHandler webViewHandler = new WebViewHandler();
        Assert.assertTrue("'Jobs recommended for you' label is not present",
                webViewHandler.isTextPresent("Jobs recommended for you"));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Jobs");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Opens 'Expose' screen by tapping on 'IN' button.
     * 
     * @return {@code ScreenExpose} object.
     */
    public ScreenExpose openExposeScreen() {
        tapOnINButton();
        return new ScreenExpose(this);
    }

    // ACTIONS --------------------------------------------------------------
    public static void jobs_home(String screenshotName) {
        ScreenGroupsAndMore.groups_and_more_tap_jobs(screenshotName);
    }

    @TestAction(value = "jobs_home")
    public static void jobs_home() {
        jobs_home("jobs_home");
    }

    public static void go_to_jobs_home(String screenshotName) {
        // ScreenGroupsAndMore.go_to_groups_and_more();
        jobs_home(screenshotName);
    }

    @TestAction(value = "go_to_jobs_home")
    public static void go_to_jobs_home(String email, String password) {
        go_to_jobs_home("go_to_jobs_home");
    }

    @TestAction(value = "jobs_home_tap_expose")
    public static void jobs_home_tap_expose() {
        new ScreenJobs().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("jobs_home_tap_expose");
    }

    @TestAction(value = "jobs_home_tap_expose_reset")
    public static void jobs_home_tap_expose_reset() {
        tapOnINButton();
        new ScreenJobs();
        TestUtils.delayAndCaptureScreenshot("jobs_home_tap_expose_reset");
    }

    @TestAction(value = "jobs_home_tap_back")
    public static void jobs_home_tap_back() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("jobs_home_tap_back");
    }

    @TestAction(value = "jobs_home_tap_jmbii")
    public static void jobs_home_tap_jmbii(String jobName) {
        WebViewHandler webViewHandler = new WebViewHandler();

        Logger.i("Tapping on first job in list");
        webViewHandler.clickOnClass(CELL_CLASS_NAME, jobName);

        WaitActions.waitForTrueInFunction("'Job Details' screen is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView navLabel = (TextView) Id.getViewByViewIdName(NAV_LABEL);
                        Assert.assertNotNull("Navigation title is not present", navLabel);
                        return navLabel.getText() != "Job Details";
                    }
                });

        TestUtils.delayAndCaptureScreenshot("jobs_home_tap_jmbii");
    }
}
