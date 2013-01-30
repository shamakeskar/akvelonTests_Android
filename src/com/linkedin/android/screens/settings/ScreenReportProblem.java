/**
 * 
 */
package com.linkedin.android.screens.settings;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenReportProblem extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.debug.ReportProblemActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ReportProblemActivity";

    static final String REPORT_PROBLEM_LABEL = "Report problem";
    static final String SEND_REPORT_BUTTON = "Send Report";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenReportProblem() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'LinkedIn' label is not present",
                getSolo().waitForText("LinkedIn", 1, DataProvider.WAIT_DELAY_LONG, false, false));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Jobs");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    // ACTIONS --------------------------------------------------------------
    public static void report_problem(String screenshotName) {
        HardwareActions.scrollDown();
        TextView item = getSolo().getText(REPORT_PROBLEM_LABEL);
        ViewUtils.tapOnView(item, REPORT_PROBLEM_LABEL);
        new ScreenReportProblem();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "report_problem")
    public static void report_problem() {
        report_problem("report_problem");
    }

    @TestAction(value = "go_to_report_problem")
    public static void go_to_report_problem(String email, String password) {
        ScreenSettings.go_to_settings("user1@correo.linkedinlabs.com", "crazyman");
        report_problem("go_to_report_problem");
    }

    @TestAction(value = "report_problem_tap_back")
    public static void report_problem_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("report_problem_tap_back");
    }

    /*@TestAction(value = "report_problem_tap_send")
    public static void report_problem_tap_send() {
        String text = "This is a test message. Please disregard.";
        Assert.assertNotNull("Text field is not present.", getSolo().getEditText(0));

        Logger.i("Typing text: '" + text + "'");
        getSolo().enterText(0, TestUtils.verifyText(text));

        Button sendButton = getSolo().getButton(SEND_REPORT_BUTTON);
        ViewUtils.tapOnView(sendButton, SEND_REPORT_BUTTON + " button");

        new ScreenReportProblem().verifyToast("Successfully reported your problem to server!");
        TestUtils.delayAndCaptureScreenshot("report_problem_tap_send");
    }*/
}
