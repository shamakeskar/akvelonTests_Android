/**
 * 
 */
package com.linkedin.android.screens.more;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;

/**
 * Class for 'Companies' Screen.
 * 
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenCompanies extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.jsbridge.CompaniesActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "CompaniesActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCompanies() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Companies' label is not present",
                getSolo().waitForText("Companies", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Companies");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @TestAction(value = "go_to_companies")
    public static void go_to_companies(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        new ScreenExpose(null).openGroupsAndMoreScreen();
        companies("go_to_companies");
    }
    
    @TestAction(value = "companies_tap_back")
    public static void companies_tap_back() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("companies_tap_back");
    }

    public static void companies(String screenName) {
        new ScreenGroupsAndMore().openCompaniesScreen();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }

    @TestAction(value = "companies_tap_expose")
    public static void companies_tap_expose() {
        new ScreenCompanies().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("companies_tap_expose");
    }

    @TestAction(value = "companies_tap_expose_reset")
    public static void companies_tap_expose_reset() {
        ScreenExpose.tapOnINButton();
        new ScreenCompanies();
        TestUtils.delayAndCaptureScreenshot("companies_tap_expose_reset");
    }

    @TestAction(value = "companies_tap_back_reset")
    public static void companies_tap_back_reset() {
        companies("companies_tap_back_reset");
    }
}
