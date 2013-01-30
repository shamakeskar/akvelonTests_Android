package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.android.popups.Popup;
import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Screen to share updates.
 * 
 * @author Dmitry.Somov
 * @created Aug 20, 2012 6:18:41 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenShareUpdate extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.UpdateStatusActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "UpdateStatusActivity";

    public static final int SHARE_BUTTON_INDEX = 0;
    public static final int COMMENT_FIELD_INDEX = 0;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenShareUpdate() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activity.
        Assert.assertTrue("Wrong activity " + ACTIVITY_SHORT_CLASSNAME, getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));

        Assert.assertTrue("'LinkedIn' label is not present", getSolo().waitForText("LinkedIn"));

        Button shareButton = getSolo().getButton(SHARE_BUTTON_INDEX);
        EditText commentTextEditor = getSolo().getEditText(COMMENT_FIELD_INDEX);

        Assert.assertNotNull("'Share' button is not presented", shareButton);
        Assert.assertNotNull("'Comment Field' text editor is not presented", commentTextEditor);

        HardwareActions.takeCurrentActivityScreenshot("'Share Update' screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Share Update");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        Button shareButton = getShareButton();

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnView(shareButton);
    }

    /**
     * Gets 'Share' button.
     * 
     * @return Share button
     */
    private static Button getShareButton() {
        Button shareButton = getSolo().getButton(SHARE_BUTTON_INDEX);
        Assert.assertNotNull("'Share' button is  not present.", shareButton);
        return shareButton;
    }

    @TestAction(value = "go_to_share")
    public static void go_to_share(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        screenUpdates.tapOnShareButton();
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("go_to_share");
    }

    public static void share_tap_share() {
        new ScreenShareUpdate().tapOnShareButton();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("share_tap_share");
    }

    public static void share_tap_share_precondition() {
        // ScreenShareUpdate screenShareUpdate = new ScreenShareUpdate();
        // Method typeRandomUpdateText is no longer used.
        // screenShareUpdate.typeRandomUpdateText();
        TestUtils.delayAndCaptureScreenshot("share_tap_share_precondition");
    }

    @TestAction(value = "share_tap_visibility")
    public static void share_tap_visibility() {
        Assert.assertNotNull(getSolo().getText("Visibility"));
        Logger.i("Tapping on button 'Visibility'");
        getSolo().clickOnText("Visibility");
        new Popup("Visibility", "Anyone");
        TestUtils.delayAndCaptureScreenshot("share_tap_visibility");
    }

    @TestAction(value = "share_tap_visibility_reset")
    public static void share_tap_visibility_reset() {
        HardwareActions.pressBack();
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("share_tap_visibility_reset");
    }

    public static void share_tap_share_reset() {
        new ScreenUpdates().tapOnShareButton();
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("share_tap_share_reset");
    }

    @TestAction(value = "share_tap_cancel")
    public static void share_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("share_tap_cancel");
    }

    @TestAction(value = "share")
    public static void share() {
        new ScreenShareUpdate();
    }
}
