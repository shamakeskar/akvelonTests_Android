package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for News article Details screen.
 * 
 * @author evgeny.agapov
 * @created Aug 10, 2012 2:17:23 PM
 */
public class ScreenNewsArticleDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.ViewArticleActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewArticleActivity";

    static int HEADER_LABEL_INDEX = 5;

    static float IMAGE_HEIGHT = 140.0f;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewsArticleDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Details' label is not present",
                getSolo().waitForText("Details", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        ImageButton forwardButton = getSolo().getImageButton(1);
        ImageButton sendButton = getSolo().getImageButton(0);

        Assert.assertNotNull("'Forward' button is not present", forwardButton);
        Assert.assertNotNull("'Send' button is not present", sendButton);

        Assert.assertTrue("'Send' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(sendButton,
                        LayoutUtils.LOWER_LEFT_OF_2_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_2_BUTTONS_LAYOUT));

        Assert.assertNotNull("Image after text of news is not present", getImage());

        HardwareActions.takeCurrentActivityScreenshot("News article Details screen");
    }

    /**
     * Verifies image after text of news.
     */
    public void verifyImage() {
        Assert.assertNotNull("Image after text of news is not present", getImage());
    }

    /**
     * Gets image after text of news.
     * 
     * @return if exist image after text of news {@code ImageView}, else
     *         <b>null</b>
     */
    private ImageView getImage() {
        for (ImageView view : getSolo().getCurrentImageViews()) {
            if (LayoutUtils.isViewPlacedInLayout(view, LayoutUtils.CENTER_BIG_IMAGE_LAYOUT)) {
                Rect2DP viewRect = new Rect2DP(view);
                if (Math.abs(viewRect.height - IMAGE_HEIGHT) < 1.0f) {
                    return view;
                }
            }
        }
        return null;
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
     * Taps on 'Message' button.
     */
    public void tapOnMessageButton() {
        ImageButton messageButton = getSolo().getImageButton(0);
        ViewUtils.tapOnView(messageButton, "'Message' button");
    }

    /**
     * Taps on 'Forward' button.
     */
    public void tapOnForwardButton() {
        Assert.assertNotNull("'Forward' button is not present.", getSolo().getImageButton(1));

        Logger.i("Tapping on 'Forward' button");
        getSolo().clickOnImageButton(1);
    }

    /**
     * Taps on article screen.
     */
    public void tapOnAtricleTitle() {
        View title = getSolo().getCurrentTextViews(null).get(HEADER_LABEL_INDEX);
        ViewUtils.tapOnView(title, "article title");
    }
    
    /**
     * Taps on article image.
     */
    public void tapOnArticleImage() {
        View image = getImage();
        ViewUtils.tapOnView(image, "article image");
    }
    
    /**
     * Opens 'Share News Article' screen.
     * 
     * @return {@code ShareNewsArticleScreen} with just opened 'Share News
     *         Article' screen.
     */
    public ScreenShareNewsArticle openShareNewsArticleScreen() {
        tapOnForwardButton();
        return new ScreenShareNewsArticle();
    }

    /**
     * Opens 'New Message' screen.
     * 
     * @return {@code NewMessageScreen} with just opened 'New Message' screen.
     */
    public ScreenNewMessage openNewMessageScreen() {
        tapOnMessageButton();
        return new ScreenNewMessage();
    }

    /**
     * Gets header of news on current Details screen.
     * 
     * @return header of news on current Details screen.
     */
    public String getArticleHeader() {
        return getSolo().getCurrentTextViews(null).get(HEADER_LABEL_INDEX).getText().toString();
    }
}
