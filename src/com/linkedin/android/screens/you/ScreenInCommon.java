package com.linkedin.android.screens.you;

import java.util.concurrent.Callable;

import android.view.ViewGroup;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'In Common' screen.
 * 
 * @author evgeny.agapov
 * @created Jan 14, 2013 7:35:11 PM
 */
public class ScreenInCommon extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.connections.ConnectionListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ConnectionListActivity";

    private static final ViewIdName CONNECTION_ROW_LAYOUT = new ViewIdName("connections_row");
    private static final String TITLE = "In Common";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * @param activityClassname
     */
    public ScreenInCommon() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction2("'In Common' screen is not present (list with profiles is not present)",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return ViewUtils.getViewByClassName("PinnedHeaderListView") != null;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, TITLE);
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @TestAction(value = "go_to_incommon")
    public static void go_to_incommon(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenSearch screenSearch = screenUpdates.openSearchScreen();

        screenSearch.tapOnFirstVisibleConnectionProfileScreen();

        incommon("go_to_incommon");
    }

    @TestAction(value = "incommon")
    public static void incommon() {
        incommon("incommon");
    }

    public static void incommon(String screenshotName) {
        new ScreenProfile().openInCommon();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "incommon_tap_expose")
    public static void incommon_tap_expose() {
        new ScreenInCommon().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("incommon_tap_expose");
    }

    @TestAction(value = "incommon_tap_expose_reset")
    public static void incommon_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenInCommon();

        TestUtils.delayAndCaptureScreenshot("incommon_tap_expose_reset");
    }

    @TestAction(value = "incommon_tap_back")
    public static void incommon_tap_back() {
        HardwareActions.pressBack();
        new ScreenProfile();

        TestUtils.delayAndCaptureScreenshot("incommon_tap_back");
    }

    @TestAction(value = "incommon_tap_profile")
    public static void incommon_tap_profile() {
        ViewGroup connectionRowLayout = (ViewGroup) Id.getViewByViewIdName(CONNECTION_ROW_LAYOUT);
        ViewGroupUtils.tapFirstViewInLayout(connectionRowLayout, true, "first connection", null);

        new ScreenProfile();
    }

    @TestAction(value = "incommon_tap_profile_reset")
    public static void incommon_tap_profile_reset() {
        HardwareActions.pressBack();
        new ScreenInCommon();

        TestUtils.delayAndCaptureScreenshot("incommon_tap_profile_reset");
    }
}
