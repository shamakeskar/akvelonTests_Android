package com.linkedin.android.screens.you;

import java.util.concurrent.Callable;

import android.widget.Button;
import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseProfileScreen;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Your Profile Photo.
 * 
 * @author nikita.chehomov
 * @created Jan 11, 2012
 */
public class ScreenYourProfilePhoto extends BaseProfileScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.profile.ViewImageActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewImageActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenYourProfilePhoto() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("'Profile Photo' is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        Button btn = getSolo().getButton(0);
                        if (btn == null)
                            return false;
                        return (btn.getText().toString().equalsIgnoreCase("Update photo"));
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Profile Photo");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @TestAction(value = "go_toprofile_photo")
    public static void go_to_profile_photo(String email, String password) {
         ScreenYou.go_to_you(email, password);
        profile_photo();
    }

    @TestAction(value = "profile_photo")
    public static void profile_photo() {
        ImageView picture = (ImageView) Id.getViewByViewIdName(new ViewIdName("picture"));
        ViewUtils.tapOnView(picture, "Your profile photo");
        new ScreenYourProfilePhoto();
        TestUtils.delayAndCaptureScreenshot("go_to_profile_photo");
    }

    @TestAction(value = "profile_photo_tap_back")
    public static void profile_photo_tap_back() {
        HardwareActions.pressBack();
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("profile_photo_tap_back");
    }

    @TestAction(value = "profile_photo_tap_edit")
    public static void profile_photo_tap_edit() {
        Button btn = getSolo().getButton(0);
        ViewUtils.tapOnView(btn, "Update photo");

        WaitActions.waitForTrueInFunction("'Update photo' popup is not displayed",
                new Callable<Boolean>() {
                    public Boolean call() {
                        if (getSolo().getText("Photo gallery") == null)
                            return false;
                        return (getSolo().getText("Photo gallery").isShown());
                    }
                });
        TestUtils.delayAndCaptureScreenshot("profile_photo_tap_edit");
    }

    @TestAction(value = "profile_photo_actionsheet_tap_cancel")
    public static void profile_photo_actionsheet_tap_cancel() {
        Button cancel = getSolo().getButton(0);
        ViewUtils.tapOnView(cancel, "'Cancel' button");
        new ScreenYou();
        TestUtils.delayAndCaptureScreenshot("profile_photo_actonsheet_tap_cancel");
    }
}
