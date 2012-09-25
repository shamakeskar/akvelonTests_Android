package com.linkedin.android.tests;

import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.widget.ListView;

import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;

/**
 * Temporary class for debug tests.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 6:43:07 PM
 */
// @Suppress
public class SampleTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public SampleTests() {
        super(SampleTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * First sample test.
     */
    public void testLogin() {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        
        Logger.d("asjsdfjksdfol");
        screenUpdates.openFirstViralUpdate();

    }

    /**
     * Sample second test.
     */
    @Suppress
    // it annotation for ignore this test.
    public void testSecond() {
        LoginActions.openUpdatesScreenOnStart();

        Logger.logAllOpenedActivities();
        Logger.d("solo2=" + getSolo());
        HardwareActions.takeCurrentActivityScreenshot(null);
    }
}
