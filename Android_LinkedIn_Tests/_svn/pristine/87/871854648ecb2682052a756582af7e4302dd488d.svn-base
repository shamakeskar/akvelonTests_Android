package com.linkedin.android.screens.updates;


import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreenSharedNewsDetails;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Shared News details screen.
 * 
 * @author nikita.chehomov
 * @created Aug 15, 2012 4:52:45 PM
 */
public class ScreenSharedNewsDetails extends BaseScreenSharedNewsDetails {
    
    // CONSTANTS ------------------------------------------------------------
    
    static int HEADER_LABEL_INDEX = 5;
    
    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    
    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        super.verify();
        
        HardwareActions.takeCurrentActivityScreenshot("Shared News Details Screen");
    }
    
    /**
     * Taps on Share on popup.
     */
    public void tapOnShareOnPopup() {
        Assert.assertNotNull("'Share' button is not present.", getSolo().getText("Share"));

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnText("Share");
    }
    
    /**
     * Opens 'Share News Article' screen.
     * 
     * @return {@code ShareNewsArticleScreen} with just opened 'Share News
     *         Article' screen.
     */
    public ScreenShareNewsArticle openShareNewsArticleScreen() {
        tapOnForwardButton();

        getSolo().waitForText("Share", 1, DataProvider.WAIT_DELAY_DEFAULT);
        tapOnShareOnPopup();

        return new ScreenShareNewsArticle();
    }

    /**
     * Gets header of news on current Details screen.
     * 
     * @return header of news on current Details screen.
     */
    public String getArticleHeader() {
        return getSolo().getCurrentTextViews(null).get(HEADER_LABEL_INDEX).getText().toString();
    }
    
    /**
     * Gets {@code RelativeLayout} of shared news placed in body of article;
     * 
     * @return {@code RelativeLayout} of shared news placed in body of article;
     */
    public RelativeLayout getSharedNewsRelativeLayout() {
        final Rect2DP SHARED_NEWS_IMAGE_LAYOUT = new Rect2DP(10, 0, 55, LayoutUtils.screenHeight);

        List<View> listViews = LayoutUtils.getListOfViewsPlacedInLayout(SHARED_NEWS_IMAGE_LAYOUT);

        for (View sharedNewsImage : listViews) {
            if (sharedNewsImage instanceof ImageView) {
                return (RelativeLayout) sharedNewsImage.getParent();
            }
        }
        return null;
    }
    
    /**
     * Gets text of shared news header.
     * @return text of shared news header if it was found, else <b>null</b>.
     */
    public String getSharedNewsHeader() {
        RelativeLayout sharedNewsRelativeLayout = getSharedNewsRelativeLayout();
        if (sharedNewsRelativeLayout != null) {
            return getSolo().getCurrentTextViews(sharedNewsRelativeLayout).get(0).getText().toString();
        }
        
        return null;
    }

    /**
     * find TextView with special link.
     * 
     * @return {@code TextView} with this link.
     */
    public TextView getTextViewWithGrayColorLink() {
        ArrayList<TextView> exceptions = new ArrayList<TextView>(); 
        do {
            TextView link = ViewUtils.searchTextViewInActivity(".com", exceptions, false);
            if (link == null) {
                    return null;
            }
            int textColor = link.getCurrentTextColor();
            if (link.isShown() && textColor == 0xff999490) {
                    return link;
            }
            exceptions.add(link);
        } while (true);
    }

}
