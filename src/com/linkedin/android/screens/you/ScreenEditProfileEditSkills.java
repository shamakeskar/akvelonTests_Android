package com.linkedin.android.screens.you;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseEditProfileScreen;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Edit Profile screen (Edit skills WebView subscreen).
 * 
 * @author alexey.makhalov
 */
public class ScreenEditProfileEditSkills extends BaseEditProfileScreen {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    public void verify() {
        verifyCurrentActivity();
        Assert.assertTrue("Edit Profile (Edit Skills) screen is not present",
                TextViewUtils.getTextViewByText("Skills") != null);
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "profile_edit_skil_edit_tap_back")
    public static void profile_edit_skil_edit_tap_back() {
        TextView button = TextViewUtils.getTextViewByText("Back");
        ViewUtils.tapOnView(button, "'Back' button");
        new ScreenEditProfile();
        TestUtils.delayAndCaptureScreenshot("profile_edit_skil_edit_tap_back");
    }
}
