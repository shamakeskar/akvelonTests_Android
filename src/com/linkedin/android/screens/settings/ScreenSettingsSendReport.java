package com.linkedin.android.screens.settings;

import junit.framework.Assert;
import android.widget.Button;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

@SuppressWarnings("rawtypes")
public class ScreenSettingsSendReport extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.debug.ReportProblemActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ReportProblemActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettingsSendReport() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        Assert.assertTrue("'LinkedIn' label is not presented", getSolo().waitForText("LinkedIn"));

        Assert.assertTrue("'Device log attached' label is not presented",
                getSolo().waitForText("Device log attached"));

        Button sendButton = getSolo().getButton(0);

        Assert.assertNotNull("'Send Report' button is not presented", sendButton);

        HardwareActions.takeCurrentActivityScreenshot("Send Report screen.");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Settings Send Report");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Send' button.
     */
    public void tapOnSendButton() {
        Button sendButton = getSolo().getButton(0);
        ViewUtils.tapOnView(sendButton, "'Send Report' button");
    }
}
