package com.linkedin.android.screens.common;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;

/**
 * Class for Job Details screen.
 * 
 * @author nikita.chehomov
 * @created Sep 20, 2012 1:50:54 PM
 */
public class ScreenJobDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.jsbridge.JobsActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "JobsActivity";
    public static final String WEB_ACTIVITY_CLASSNAME = "com.linkedin.android.jsbridge.LiWebViewActivity";
    public static final String WEB_ACTIVITY_SHORT_CLASSNAME = "LiWebViewActivity";
    static final float COORDINATE_OF_SAVE_UNSAVE_JOB_XDP = 100 * 2 / 3;
    static final float COORDINATE_OF_SAVE_UNSAVE_JOB_YDP1 = 180;
    static final float COORDINATE_OF_SAVE_UNSAVE_JOB_YDP2 = 200;
    static final float COORDINATE_OF_SAVE_UNSAVE_JOB_YDP3 = 223;
    static final float COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_XDP = 50;
    static final float COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_YDP = ScreenResolution
            .getScreenHeightDP() - 50;
    static final int WAIT_FOR_SCREEN_LOAD_COMPLETELY = 25;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenJobDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // TODO: rewrite.
        WaitActions.delay(WAIT_FOR_SCREEN_LOAD_COMPLETELY);
        Assert.assertTrue("'Job Details' label is not present",
                getSolo().waitForText("Job Details", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();
        HardwareActions.takeCurrentActivityScreenshot("Job Details screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME,
                WEB_ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Save \ Unsave job' button, wait for button change color and
     * verify what it changed.It click on bottom of screen at first, because
     * this screen not work correctly and button became clickable only after
     * first click.
     */
    public void saveOrUnsaveJob() {
        Logger.i("Tapping on Save/Unsave button");
        getSolo().clickOnScreen(ScreenResolution.getScreenWidth() / 2,
                ScreenResolution.getScreenHeight() / 2);
        // Delay after first click for wait that screen is stable.
        WaitActions.delay(DataProvider.WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED);
        int colorOfFirstPoint = HardwareActions.saveCurrentScreenScreenshotInBitmap().getPixel(
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity()),
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP1 * ScreenResolution.getScreenDensity()));

        int colorOfSecondPoint = HardwareActions.saveCurrentScreenScreenshotInBitmap().getPixel(
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity()),
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP2 * ScreenResolution.getScreenDensity()));

        int colorOfThirdPoint = HardwareActions.saveCurrentScreenScreenshotInBitmap().getPixel(
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity()),
                (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP3 * ScreenResolution.getScreenDensity()));

        getSolo().clickOnScreen(
                COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity(),
                COORDINATE_OF_SAVE_UNSAVE_JOB_YDP1 * ScreenResolution.getScreenDensity());
        // Wait for button change color
        WaitActions.delay(DataProvider.WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED);

        int colorOfFirstChangePoint = HardwareActions
                .saveCurrentScreenScreenshotInBitmap()
                .getPixel(
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution
                                .getScreenDensity()),
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP1 * ScreenResolution
                                .getScreenDensity()));

        getSolo().clickOnScreen(
                COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity(),
                COORDINATE_OF_SAVE_UNSAVE_JOB_YDP2 * ScreenResolution.getScreenDensity());
        // Wait for button change color
        WaitActions.delay(DataProvider.WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED);

        int colorOfSecondChangePoint = HardwareActions
                .saveCurrentScreenScreenshotInBitmap()
                .getPixel(
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution
                                .getScreenDensity()),
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP2 * ScreenResolution
                                .getScreenDensity()));

        getSolo().clickOnScreen(
                COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution.getScreenDensity(),
                COORDINATE_OF_SAVE_UNSAVE_JOB_YDP3 * ScreenResolution.getScreenDensity());
        // Wait for button change color
        WaitActions.delay(DataProvider.WAIT_SAVE_UNSAVE_JOB_BUTTON_CHANGED);

        int colorOfThirdChangePoint = HardwareActions
                .saveCurrentScreenScreenshotInBitmap()
                .getPixel(
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_XDP * ScreenResolution
                                .getScreenDensity()),
                        (int) (COORDINATE_OF_SAVE_UNSAVE_JOB_YDP3 * ScreenResolution
                                .getScreenDensity()));

        Assert.assertTrue(
                "Save/Unsave button not changed",
                !((colorOfFirstPoint == colorOfFirstChangePoint)
                        && (colorOfSecondPoint == colorOfSecondChangePoint) && (colorOfThirdPoint == colorOfThirdChangePoint)));
    }

    /**
     * Scroll down to bottom of screen, wait that scroll is done and taps on
     * latest recommended job. Its need double click because Screen JobDetails
     * don't open if you click one time.
     */
    public void tapOnRecommendedJob() {
        for (int i = 0; i < 30; i++) {
            getSolo().drag(ScreenResolution.getScreenWidth() / 2,
                    ScreenResolution.getScreenWidth() / 2, ScreenResolution.getScreenHeight() - 50,
                    50, 3);
        }
        // Delay for wait that content is loaded.
        WaitActions.waitForScreenUpdate();
        Logger.i("Tapping on recommended job");
        getSolo().clickOnScreen(
                COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_XDP * ScreenResolution.getScreenDensity(),
                COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_YDP * ScreenResolution.getScreenDensity());
        // Delay after first click for wait that screen is stable.
        WaitActions.waitForScreenUpdate();
        getSolo().clickOnScreen(
                COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_XDP * ScreenResolution.getScreenDensity(),
                COORDINATE_OF_RECOMMENDED_JOB_IN_BOTTOM_YDP * ScreenResolution.getScreenDensity());
    };

    /**
     * Opens Job Details screen.
     * 
     * @return ScreenJobDetails.
     */
    public ScreenJobDetails openRecommendedJob() {
        tapOnRecommendedJob();
        return new ScreenJobDetails();
    }
}
