package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseEditProfileScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Edit Profile screen.
 * 
 * @author alexander.makarov
 * @created Jan 11, 2013 4:07:38 PM
 */
public class ScreenEditProfile extends BaseEditProfileScreen {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        Assert.assertTrue("Edit Profile screen is not present",
                getSolo().getText("Edit Profile") != null);
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_profile_edit")
    public static void go_to_profile_edit() {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        screenUpdates.openExposeScreen().openYouScreen();
        profile_edit("go_to_profile_edit");
    }

    public static void profile_edit(String screenshotName) {
        ScreenYou.handleEditYourProfileHint();
        ScreenYou.you_tap_profile_edit(screenshotName);
    }

    @TestAction(value = "profile_edit")
    public static void profile_edit() {
        profile_edit("profile_edit");
    }

    @TestAction(value = "profile_edit_tap_done")
    public static void profile_edit_tap_done() {
        TextView doneButton = getSolo().getText("Done");
        ViewUtils.tapOnView(doneButton, "'Done' button");
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("profile_edit_tap_done");

    }
}
