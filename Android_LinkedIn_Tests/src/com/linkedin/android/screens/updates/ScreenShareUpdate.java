package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Screen to share updates.
 * 
 * @author Dmitry.Somov
 * @created Aug 20, 2012 6:18:41 PM
 */
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
        Assert.assertTrue(
                "Wrong activity " + ACTIVITY_SHORT_CLASSNAME, 
                getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME));
        
        Assert.assertTrue("'LinkedIn' label is not present",
                getSolo().waitForText("LinkedIn"));
        
        Button shareButton = getSolo().getButton(SHARE_BUTTON_INDEX);
        EditText commentTextEditor = getSolo().getEditText(COMMENT_FIELD_INDEX);
        
        Assert.assertNotNull("'Share' button is not presented", shareButton);
        Assert.assertNotNull("'Comment Field' text editor is not presented", commentTextEditor);
        
        Assert.assertTrue("'Share' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(shareButton,
                LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT)); 
        
        HardwareActions.takeCurrentActivityScreenshot("'Share Update' screen");
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

    /**
     * Types random update text to 'Share an update' field.
     * 
     * @return update text that was entered.
     */
    public String typeRandomUpdateText() {
        String updateText = "Test update " + Math.random();
        Assert.assertNotNull("'Share an update' field is not present.", getSolo().getEditText(0));

        Logger.i("Typing random update text: '" + updateText + "'");
        getSolo().enterText(0, updateText);

        return updateText;
    }
}
