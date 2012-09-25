package com.linkedin.android.screens.you;

import android.widget.TextView;

import com.linkedin.android.screens.base.BaseProfileScreen;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Profile screen.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 16:52:37 PM
 */
public class ScreenProfile extends BaseProfileScreen {

    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP PHOTO_RECT = new Rect2DP(17.3f, 106.7f, 90.0f, 90.0f);
    static final Rect2DP PHOTO_FRAME_RECT = new Rect2DP(12.0f, 92.7f, 100.0f, 110.0f);

    protected static final String PROFILE_LABEL = "Profile";

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenProfile() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        assertConnectionsLabelPresent();
        assertProfileLabelPresent();

        assertPhotoFramePresent(PHOTO_FRAME_RECT);
        assertPhotoPresent(PHOTO_RECT);

        verifyINButton();

    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Verify 'Profile' label is present
     */
    protected void assertProfileLabelPresent() {
        ViewAssertUtils.assertLabel(PROFILE_LABEL);
    }

    /**
     * Taps on Send to Connection on Popup.
     */
    public void tapOnSendToConnectionOnPopup() {
        getSolo().searchText("Send to Connection");

        TextView sendToConnectionButton = getSolo().getText("Send to Connection");
        ViewUtils.tapOnView(sendToConnectionButton, "'Send to Connection' button");
    }

    /**
     * Taps on Email on Popup.
     */
    public void tapOnEmailOnPopup() {
        getSolo().searchText("Email");

        TextView emailButton = getSolo().getText("Email");
        ViewUtils.tapOnView(emailButton, "'Email' button");
    }

}
