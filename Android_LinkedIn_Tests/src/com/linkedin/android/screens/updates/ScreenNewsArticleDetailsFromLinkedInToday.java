package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for News article Details screen (opened from LINKEDIN TODAY screen).
 * 
 * @author dmitry.somov
 * @created Aug 14, 2012 2:54:37 PM
 */
public class ScreenNewsArticleDetailsFromLinkedInToday extends ScreenNewsArticleDetails {
    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.ViewArticleActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewArticleActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewsArticleDetailsFromLinkedInToday() {
        super();
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
    
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);
        
        // Verify Up and Down Arrow Buttons
        verifyUpDownArrowButtons();

        // Verify that IN Button is present.
        verifyINButton();

        Assert.assertTrue("Title '.. of ..' is not present", getSolo().getText(0).getText()
                .toString().indexOf("of") > -1);
        
        ImageButton forwardButton = getSolo().getImageButton(1);
        ImageButton messageButton = getSolo().getImageButton(0);

        Assert.assertNotNull("'Forward' button is not present", forwardButton);
        Assert.assertNotNull("'Message' button is not present", messageButton);

        Assert.assertTrue("'Send' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(messageButton,
                        LayoutUtils.LOWER_LEFT_OF_2_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_2_BUTTONS_LAYOUT));

       // verifyImage();

        HardwareActions
                .takeCurrentActivityScreenshot("News Article Details screen (opened from LINKEDIN TODAY screen)");
    }

    /**
     * Verify Up and Down Arrow Buttons
     */
    public void verifyUpDownArrowButtons() {
        ImageView upArrowButton = getupArrowButton();
        Assert.assertNotNull("'Up Arrow' button is not present (or its coordinates are wrong)",
                upArrowButton);

        ImageView downArrowButton = getDownArrowButton();
        Assert.assertNotNull("'Down Arrow' button is not present (or its coordinates are wrong)",
                downArrowButton);
    }

    /**
     * Get Down Arrow Button
     * 
     * @return ImageView
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return downArrowButton;
    }

    /**
     * Get Up Arrow Button
     * 
     * @return ImageView
     */
    public ImageView getupArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return upArrowButton;
    }
    
    /**
     * Taps on up arrow button. (Change current article).
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(upArrowButton, "up arrow button");
    }
    
    /**
     * Taps on down arrow button. (Change current article).
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width, DOWN_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);

        ViewUtils.tapOnView(downArrowButton, "down arrow button");
    }
}