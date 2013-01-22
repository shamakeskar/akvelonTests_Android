package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreenSharedNewsDetails;
import com.linkedin.android.screens.more.ScreenCompanyDetails;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.RegexpPatterns;
import com.linkedin.android.tests.data.ViewIdName;
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
public class ScreenViralUpdate extends BaseScreenSharedNewsDetails {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_SHORT_CLASSNAME = "DetailsListActivity";
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home." + ACTIVITY_SHORT_CLASSNAME;
    // Company name view
    private static final ViewIdName HEADER = new ViewIdName("header");
    // Company headline view
    private static final ViewIdName HEADLINE = new ViewIdName("headline");
    // Post time
    private static final ViewIdName POST_TIME = new ViewIdName("footer_timestamp");

    // ViewIdName for buttons on tool bar.
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    private static final ViewIdName COMMENT_BUTTON = new ViewIdName("comment_button");
    private static final ViewIdName FORWARD_BUTTON = new ViewIdName("share_button");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenViralUpdate() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        Assert.assertTrue("Cannot wait until content of ViralUpdate is loading",
                getSolo().waitForText("Details", 1, DataProvider.WAIT_DELAY_DEFAULT));

        Assert.assertNotNull("Company image is not present", getSolo().getImage(0));
        Assert.assertNotNull("Header with company name is not present", Id.getViewByViewIdName(HEADER));
        Assert.assertNotNull("Company headline is not present", Id.getViewByViewIdName(HEADLINE));
        TextView textView = (TextView) Id.getViewByViewIdName(POST_TIME);
        Assert.assertNotNull("Post time is not present", textView);
        Assert.assertTrue("Post time is wrong", RegexpUtils.isCanBeFound(textView.getText()
                .toString(), RegexpPatterns.DATE_LIKE_10_HOURS_AGO) || 
                RegexpUtils.isCanBeFound(textView.getText()
                        .toString(), RegexpPatterns.DATE_LIKE_OCTOBER_1));
        
        ImageButton buttonLike = (ImageButton) Id.getViewByViewIdName(LIKE_BUTTON);
        ImageButton buttonComment = (ImageButton) Id.getViewByViewIdName(COMMENT_BUTTON);
        ImageButton buttonForward = (ImageButton) Id.getViewByViewIdName(FORWARD_BUTTON);
        Assert.assertNotNull("Like button is not present", buttonLike);
        Assert.assertNotNull("Comment button is not present", buttonComment);
        Assert.assertNotNull("Forward button is not present", buttonForward);
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "ViralUpdate");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Opens Company profile by tapping on title.
     * 
     * @return {@code ScreenCompanyDetails} object.
     */
    public ScreenCompanyDetails openCompanyProfile() {
        TextView company = Id.getViewByName(HEADER, TextView.class);
        Assert.assertNotNull("Title with company is not present", company);
        ViewUtils.tapOnView(company, "article title");

        return new ScreenCompanyDetails();
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
     * Opens 'Share News Article' screen by tapping on 'Forward' -> 'Share'.
     * 
     * @return {@code ScreenShareNewsArticle} with just opened 'Share News Article' screen.
     */
    public ScreenShareNewsArticle openShareScreen() {
        tapOnForwardButton();
        Assert.assertTrue("Share button is not present", getSolo().waitForText("Share", 1, DataProvider.WAIT_DELAY_DEFAULT));
        tapOnShareOnPopup();

        return new ScreenShareNewsArticle();
    }

}
