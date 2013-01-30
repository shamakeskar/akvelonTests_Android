package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for New Connection Roll Up screen.
 * 
 * @author aleksey.chichagov
 * @created Sep 19, 2012 2:50:37 PM
 */
public class ScreenNewConnectionsRollUp extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.NUSListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewConnectionsRollUp() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("Wrong activity (expected " + ACTIVITY_SHORT_CLASSNAME, getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));

        Assert.assertTrue("'Details' label not shown", getSolo().waitForText("Details"));

        Assert.assertNotNull("Connections list is empty", getConnectionRollUp());

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("New Connection Roll Up screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "New Connections Rollup");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets Connection Roll Up.
     * 
     * @return {@code TextView} connection roll up.
     */
    private TextView getConnectionRollUp() {
        HardwareActions.scrollUp();
        if (getSolo().searchText("new connections.", 1, true, false)) {
            return TextViewUtils.searchTextViewInActivity("new connections.", false);
        }

        HardwareActions.scrollUp();
        if (getSolo().searchText("is now connected", 1, true, false)) {
            return TextViewUtils.searchTextViewInActivity("is now connected", false);
        }
        return null;
    }

    /**
     * Taps on first roll up.
     */
    public void tapOnFirstRollUp() {
        TextView rollUp = getConnectionRollUp();
        WaitActions.waitForScreenUpdate();
        ViewUtils.tapOnView(rollUp, "'Connection Roll Up' view");
    }

    /**
     * Taps on second roll up.
     */
    public void tapOnSecondRollUp() {
        TextView textView = getSolo().getText(4);
        WaitActions.waitForScreenUpdate();
        ViewUtils.tapOnView(textView, "'Connection Roll Up' view");
    }

    /**
     * Opens 'New Connection Details' screen.
     * 
     * @return {@code ScreenNewConnectionDetails} with just opened 'New
     *         Connection Details' screen.
     */
    public ScreenNewConnectionDetails openNewConnectionDetails() {
        tapOnFirstRollUp();
        return new ScreenNewConnectionDetails();
    }

    @TestAction(value = "go_to_updates_connection_rollup_list")
    public static void go_to_updates_connection_rollup_list(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);

        updates_connection_rollup_list("go_to_updates_connection_rollup_list");
    }

    @TestAction(value = "updates_connection_rollup_list")
    public static void updates_connection_rollup_list() {
        updates_connection_rollup_list("updates_connection_rollup_list");
    }

    public static void updates_connection_rollup_list(String screenshotName) {
        new ScreenUpdates().tapOnFirstNewConnectionsRollUp();
        new ScreenNewConnectionsRollUp();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "updates_connection_rollup_list_tap_back")
    public static void updates_connection_rollup_list_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_back");
    }

    @TestAction(value = "updates_connection_rollup_list_tap_update")
    public static void updates_connection_rollup_list_tap_update() {
        new ScreenNewConnectionsRollUp().tapOnFirstRollUp();
        new ScreenNewConnectionDetails();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_back");
    }

    @TestAction(value = "updates_connection_rollup_list_tap_update_reset")
    public static void updates_connection_rollup_list_tap_update_reset() {
        HardwareActions.pressBack();
        new ScreenNewConnectionsRollUp();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_update_reset");
    }

    @TestAction(value = "updates_connection_rollup_list_tap_expose")
    public static void updates_connection_rollup_list_tap_expose() {
        new ScreenNewConnectionsRollUp().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_expose");
    }

    @TestAction(value = "updates_connection_rollup_list_tap_expose_reset")
    public static void updates_connection_rollup_list_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenNewConnectionsRollUp();

        TestUtils.delayAndCaptureScreenshot("updates_connection_rollup_list_tap_expose_reset");
    }
}
