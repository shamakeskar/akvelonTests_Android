package com.linkedin.android.screens.settings;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.WaitActions;

public class ScreenSettingsAddConnections extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.connections.abi.FindConnectionsActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "FindConnectionsActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettingsAddConnections() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------

    @Override
    public void verify() {
        verifyCurrentActivity();
        Assert.assertTrue(
                "'Add Connections' screen is not present",
                getSolo().waitForText("Add Connections", 1, DataProvider.WAIT_DELAY_SHORT, false,
                        false));
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Add Connections");
    }
}
