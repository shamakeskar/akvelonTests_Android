package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Screen for Viral Update.
 * 
 * @author alexander.makarov
 * @created Sep 24, 2012 2:17:06 PM
 */
public class ScreenViralUpdate extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.DetailsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "DetailsListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenViralUpdate() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        
        Logger.d("verify");
        
        // Verify current activity.
        verifyCurrentActivity();

        Logger.logElements();
        /*Assert.assertTrue("'LinkedIn' label is not present", getSolo().waitForText("LinkedIn"));

        Button shareButton = getSolo().getButton(SHARE_BUTTON_INDEX);
        EditText commentTextEditor = getSolo().getEditText(COMMENT_FIELD_INDEX);

        Assert.assertNotNull("'Share' button is not presented", shareButton);
        Assert.assertNotNull("'Comment Field' text editor is not presented", commentTextEditor);

        Assert.assertTrue("'Share' button is not present (or its coordinates are wrong)",
                LayoutUtils
                        .isViewPlacedInLayout(shareButton, LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT));

        HardwareActions.takeCurrentActivityScreenshot("'Share Update' screen");*/
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "ViralUpdate");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
}
