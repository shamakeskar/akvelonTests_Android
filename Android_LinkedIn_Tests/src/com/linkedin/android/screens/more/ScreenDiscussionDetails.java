package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseDiscussionDetailsScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Discussion Detail screen.
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 1:35:37 PM
 */
public class ScreenDiscussionDetails extends BaseDiscussionDetailsScreen {
    // CONSTANTS ------------------------------------------------------------

    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        
        Assert.assertTrue("Title '.. of ..' is not present", getSolo().getText(0).getText()
                .toString().indexOf("of") > -1);

        assertLikeButton();
        assertCommentButton();
        
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        Assert.assertNotNull("'Up arrow' button is not presented", upArrowButton);
        Assert.assertNotNull("'Down arrow' button is not presented", downArrowButton);

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("Discussion detail screen");
    }

    /**
     * Taps on up arrow button.
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(upArrowButton, "up arrow button");
    }

    /**
     * Taps on down arrow button.
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(downArrowButton, "down arrow button");
    }
    
}
