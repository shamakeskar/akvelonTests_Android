package com.linkedin.android.screens.you;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'Who's viewed you' screen.
 * 
 * @author nikita.chehomov
 * @created Sep 7, 2012 1:35:11 PM
 */

public class ScreenWhosViewedYou extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.WVMPListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "WVMPListActivity";

    private static final ViewIdName CONNECTION_LAYOUT = new ViewIdName("connections_row");
    public static final ViewIdName ROLLUP_WVMP_ID_NAME = new ViewIdName("rollup_chevron");
    public static final ViewIdName PROFILE_ID_NAME = new ViewIdName("display_name");
    public static final ViewIdName NAVIGATION_BAR_TITLE_ID_NAME = new ViewIdName(
            "navigation_bar_title");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenWhosViewedYou() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "WVMP");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets 'Who's viewed you' label
     * 
     * @return 'Who's viewed you' label
     */
    public TextView getWhosViewedYouLabel() {
        HardwareActions.scrollUp();
        return getSolo().getText("Who's viewed you");
    }

    /**
     * Taps on connection.
     * 
     * @param name
     *            is name on which we need to tap.
     */
    public void tapOnConnection(String name) {
        TextView profileView = getSolo().getText(name);
        ViewUtils.tapOnView(profileView, name + " view");
    }

    // ACTIONS --------------------------------------------------------------
    public static void wvmp(String screenshotName) {
        TextView wvmp = TextViewUtils.searchTextViewInActivity("Who's viewed your profile", false);
        ViewUtils.tapOnView(wvmp, "'WVMP' label");
        new ScreenWhosViewedYou();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void wvmp() {
        wvmp("wvmp");
    }

    public static void go_to_wvmp() {
        ScreenExpose.go_to_expose();
        ScreenExpose.expose_tap_you();
        if (getSolo().waitForText("Edit your profile", 1, DataProvider.WAIT_DELAY_DEFAULT, false)) {
            Logger.i("Tapping on hint for 'Edit your profile'");
            getSolo().clickOnText("Edit your profile");
        }
        wvmp("go_to_wvmp");
    }

    public static void wvmp_expose() {
        new ScreenWhosViewedYou().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("wvmp_expose");
    }

    public static void wvmp_expose_reset() {
        tapOnINButton();
        new ScreenWhosViewedYou();
        TestUtils.delayAndCaptureScreenshot("wvmp_expose_reset");
    }

    public static void wvmp_back() {
        HardwareActions.pressBack();
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("wvmp_back");
    }

    public static void wvmp_tap_profile() {
        RelativeLayout connectionLayout = (RelativeLayout) Id
                .getViewByViewIdName(CONNECTION_LAYOUT);
        Assert.assertNotNull("There are no Connection in 'WVMP' screen", connectionLayout);

        ViewGroupUtils.tapFirstViewInLayout(connectionLayout, true, "first connection", null);
        TestUtils.delayAndCaptureScreenshot("wvmp_tap_profile");
    }

    public static void wvmp_tap_profile_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("wvmp_tap_profile_reset");
    }

    public static void go_to_wvmp_profiles_rollup() {
        ScreenWhosViewedYou.go_to_wvmp();
        wvmp_profiles_rollup("go_to_wvmp_profiles_rollup");
    }

    public static void wvmp_profiles_rollup(String screenshotName) {
        ArrayList<View> rollupList = Id.getListOfViewByViewIdName(ROLLUP_WVMP_ID_NAME);
        ImageView rollup = (ImageView) ViewUtils.getFirstShownViewInArray(rollupList);

        final TextView navBarTitle = (TextView) Id
                .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
        ViewUtils.tapOnView(rollup, "WVMP Rollup");
        WaitActions.waitForTrueInFunction("WVMP Rollup not opened", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                TextView newBarTitle = (TextView) Id
                        .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                return (!newBarTitle.equals(navBarTitle));
            }
        });
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void wvmp_profiles_rollup() {
        wvmp_profiles_rollup("wvmp_profiles_rollup");
    }

    public static void wvmp_profiles_rollup_tap_expose() {
        tapOnINButton();
        new ScreenExpose(null);
        TestUtils.delayAndCaptureScreenshot("wvmp_profiles_rollup");
    }

    public static void wvmp_profiles_rollup_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenWhosViewedYou();
        TestUtils.delayAndCaptureScreenshot("wvmp_profiles_rollup_tap_expose_reset");
    }

    public static void wvmp_profiles_rollup_tap_back() {
        HardwareActions.pressBack();
        new ScreenWhosViewedYou();
        TestUtils.delayAndCaptureScreenshot("wvmp_profiles_rollup_tap_back");
    }

    public static void wvmp_profiles_rollup_tap_profile() {
        TextView profile = (TextView) Id.getViewByViewIdName(PROFILE_ID_NAME);
        ViewUtils.tapOnView(profile, "First profile");
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("wvmp_profiles_rollup_tap_profile");
    }
}