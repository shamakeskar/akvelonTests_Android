package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.more.ScreenCompanyDetails;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.RegexpPatterns;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

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
    // Company name view
    private static final ViewIdName HEADER = new ViewIdName("header");
    // Company headline view
    private static final ViewIdName HEADLINE = new ViewIdName("headline");
    // Post time
    private static final ViewIdName POST_TIME = new ViewIdName("footer_timestamp");

    // ImageButton: like it button
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    // TextView: like text '1 person likes this' for example
    public static final ViewIdName LIKE_TEXT = new ViewIdName("likes_text");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenViralUpdate() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activity.
        verifyCurrentActivity();

        WaitActions.delay(5);
        Logger.logElements();

        Logger.logElements("Button");
        Logger.logElements("ImageButton");

        Assert.assertTrue("Cannot wait until content of ViralUpdate is loading",
                getSolo().waitForText("Details", 1, DataProvider.WAIT_DELAY_DEFAULT));

        Assert.assertNotNull("Company image is not present", getSolo().getImage(0));
        Assert.assertNotNull("Header with company name is not present", Id.getViewByName(HEADER));
        Assert.assertNotNull("Company headline is not present", Id.getViewByName(HEADLINE));
        TextView textView = (TextView) Id.getViewByName(POST_TIME);
        Assert.assertNotNull("Post time is not present", textView);
        Assert.assertTrue("Post time is wrong", RegexpUtils.isCanBeFound(textView.getText()
                .toString(), RegexpPatterns.DATE_LIKE_10_HOURS_AGO));

        // Is it necessary ?
//        Assert.assertTrue("Count of followers is not present",
//                getSolo().searchText(RegexpPatterns.LIKE_10_FOLLOWERS.pattern(), 1, false, true));

        HardwareActions.takeCurrentActivityScreenshot("Viral Update");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "ViralUpdate");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    public ScreenCompanyDetails openCompanyProfile() {
        // TODO
        View company = getSolo().getCurrentTextViews(null).get(00000000);
        ViewUtils.tapOnView(company, "article title");

        return new ScreenCompanyDetails();
    }
    
    /**
     * Taps on Like button.
     */
    public void tapOnLikeButton() {
        ImageButton likeButton = (ImageButton) Id.getViewByName(LIKE_BUTTON);
        Assert.assertTrue("'Like' button is not present.", likeButton.isShown());

        ViewUtils.tapOnView(likeButton, "'Like' button");
    }

    /**
     * Taps on Comment button.
     */
    public void tapOnCommentButton() {
        Assert.assertNotNull("'Comment' button is not present.", getSolo().getImageButton(1));

        Logger.i("Tapping on 'Comment' button");
        getSolo().clickOnImageButton(1);
    }

    /**
     * Opens AddComment screen.
     */
    public ScreenAddComment openAddCommentScreen() {
        tapOnCommentButton();
        return new ScreenAddComment();
    }

    /**
     * Taps on Share on popup.
     */
    public void tapOnShareOnPopup() {
        Assert.assertNotNull("'Share' button is not present.",
                getSolo().searchText("Share"));

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnText("Share");
    }

    /**
     * Taps on Forward button and wait for popup appears
     * 
     * @return number of popup menu items except cancel button
     */
    public int tapOnForwardButton() {
        int nr = 0;
        Assert.assertNotNull("'Forward' button is not present.", getSolo().getImageButton(2));

        Logger.i("Tapping on 'Forward' button");
        getSolo().clickOnImageButton(2);

        if (getSolo().searchText("Share"))
            nr++;
        if (getSolo().searchText("Send to Connection"))
            nr++;
        if (getSolo().searchText("Reply Privately"))
            nr++;

        getSolo().searchText("Cancel");
        // TODO: May be better to return ArrayList of String
        // But it is not necessary now.
        return nr;
    }

    /**
     * Taps on Cancel button on popup.
     */
    public void tapOnCancelButtonOnPopup() {
        Assert.assertTrue("'Cancel' button is not present.", getSolo().searchText("Cancel"));

        Logger.i("Tapping on 'Cancel' button");
        getSolo().clickOnText("Cancel");
    }

    /**
     * Tap on 'Forward' -> 'Share' Opens 'Share News Article' screen.
     * 
     * @return {@code ScreenShareNewsArticle} with just opened 'Share News Article' screen.
     */
    public ScreenShareNewsArticle openShareScreen() {
        tapOnForwardButton();
        getSolo().waitForText("Share", 1, DataProvider.WAIT_DELAY_DEFAULT);
        tapOnShareOnPopup();

        return new ScreenShareNewsArticle();
    }

}
