package com.linkedin.android.screens.settings;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

public class ScreenAbiLearnMore extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.common.MoreInfoActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "MoreInfoActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAbiLearnMore() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'How LinkedIn Uses Your Data' text is not present", getSolo()
                .waitForText("How LinkedIn Uses Your Data"));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Abi Learn More");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    // ACTIONS --------------------------------------------------------------
    public static void abi_learn_more(String screenshotName) {
        boolean isText = TextViewUtils.searchAndScrollToVisibleText("Learn More");
        Assert.assertTrue("'Learn More' label is not present.", isText);
        ViewUtils.tapOnLink("Learn More");
        new ScreenAbiLearnMore();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void abi_learn_more() {
        abi_learn_more("abi_learn_more");
    }

    public static void go_to_abi_learn_more() {
        ScreenSettings.go_to_settings();
        ScreenSettings.settings_tap_add_con();
        abi_learn_more("go_to_abi_learn_more");
    }
}
