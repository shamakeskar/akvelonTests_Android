package com.linkedin.android.screens.you;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseProfileScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Profile Photo.
 * 
 * @author nikita.chehomov
 * @created Jan 11, 2012
 */
public class ScreenProfilePhoto extends BaseProfileScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.profile.ViewImageActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewImageActivity";
    public static final int INDEX_OF_FIRST_CONNECTION = 12;
    public static final int VALUE_OF_IMAGE_VIEW_FOR_ONE_PROFILE = 2;
    public static final int INDEX_OF_PROFILE_PHOTO = 2;

    public static final Rect2DP PROFILE_PHOTO = new Rect2DP(0, 25, 360, 615);

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenProfilePhoto() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        WaitActions.waitForTrueInFunction("Photo is not loaded", new Callable<Boolean>() {
            public Boolean call() {
                ImageView photo = getSolo().getImage(0);
                Rect2DP photo2DP = new Rect2DP(photo);
                return photo2DP.equals(PROFILE_PHOTO, 1);
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

    public static void profile_photo_from_profile() {
        ImageView photo = getSolo().getImage(INDEX_OF_PROFILE_PHOTO);
        ViewUtils.tapOnView(photo, "Profile Photo");
        TestUtils.delayAndCaptureScreenshot("go_to_profile_photo_from_profile");
    }

    public static void go_to_profile_photo_from_profile() {
        ScreenYouConnections.go_to_connections();
        ArrayList<ImageView> views = getSolo().getCurrentImageViews();
        // Open connection than tap on photo if it's not open than press back
        // and open next connection. If photo is opened than break.
        for (int i = INDEX_OF_FIRST_CONNECTION; i < views.size(); i += VALUE_OF_IMAGE_VIEW_FOR_ONE_PROFILE) {
            ImageView image = getSolo().getImage(i);
            ViewUtils.tapOnView(image, "Your connection");
            new ScreenProfileOfConnectedUser();
            ImageView photo = getSolo().getImage(INDEX_OF_PROFILE_PHOTO);
            ViewUtils.tapOnView(photo, "Profile Photo");
            if (getSolo()
                    .waitForActivity(ACTIVITY_SHORT_CLASSNAME, DataProvider.WAIT_DELAY_DEFAULT))
                break;
            HardwareActions.pressBack();
            new ScreenYouConnections();
        }

        HardwareActions.pressBack();
        new ScreenProfileOfConnectedUser();
        profile_photo_from_profile();
    }

    public static void profile_photo_from_profile_tap_back() {
        HardwareActions.pressBack();
        new ScreenProfileOfConnectedUser();
        TestUtils.delayAndCaptureScreenshot("profile_photo_from_profile_tap_back");
    }
}