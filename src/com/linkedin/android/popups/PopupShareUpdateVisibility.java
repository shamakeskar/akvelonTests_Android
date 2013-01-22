package com.linkedin.android.popups;

import junit.framework.Assert;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.screens.updates.ScreenShareUpdate;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;

public class PopupShareUpdateVisibility extends Popup {
    public static final String POPUP_TEXT = "Visibility";
    public static final String ANYONE_TEXT = "Anyone";
    public static final String CONNECTIONS_ONLY_TEXT = "Connections only";
    public PopupShareUpdateVisibility() {
        super(POPUP_TEXT, ANYONE_TEXT);        
    }
    public static void go_to_share_visibility() {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        screenUpdates.openShareUpdateScreen();
        share_visibility("go_to_share_visibility");
    }
    public static void share_visibility_tap_back() {
        HardwareActions.pressBack();
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("share_visibility_tap_back");
    }
    public static void share_visibility(String screenName) {
        Assert.assertNotNull(getSolo().getText(POPUP_TEXT));
        Logger.i("Tap on '" + POPUP_TEXT + "' button");
        getSolo().clickOnText(POPUP_TEXT);
        new PopupShareUpdateVisibility();
        TestUtils.delayAndCaptureScreenshot("share_visibility");
    }
    public static void share_visibility_tap_connections_only() {
        Assert.assertNotNull(getSolo().getText(CONNECTIONS_ONLY_TEXT));
        Logger.i("Tap on '" + CONNECTIONS_ONLY_TEXT + "' button on popup");
        getSolo().clickOnText(CONNECTIONS_ONLY_TEXT);
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("share_visibility_tap_connections_only");
    }
    public static void share_visibility_tap_anyone() {
        Assert.assertNotNull(getSolo().getText(ANYONE_TEXT));
        Logger.i("Tap on '" + ANYONE_TEXT + "' button on popup");
        getSolo().clickOnText(ANYONE_TEXT);
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("share_visibility_tap_anyone");
    }
    
    /**
     * Returns Solo object.
     * 
     * @return Solo object.
     */
    private static Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }
    public static void share_visibility_tap_back_reset() {
        share_visibility("share_visibility_tap_back_reset");
    }
    public static void share_visibility_tap_connections_only_reset() {
        share_visibility("share_visibility_tap_connections_only_reset");
    }    
}
