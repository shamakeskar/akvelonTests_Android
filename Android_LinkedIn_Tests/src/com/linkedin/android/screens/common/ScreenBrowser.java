package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.updates.ScreenShareNewsArticle;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Browser screen.
 * 
 * @author nikita.chehomov
 * @created Aug 23, 2012 2:18:45 PM
 */
public class ScreenBrowser extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.common.WebViewActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "WebViewActivity";
    private static final ViewIdName WEB_PROGRESS_BAR = new ViewIdName("webProgress");
    private static final ViewIdName WEB_PAGE_TITLE = new ViewIdName("navigation_bar_title");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenBrowser() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();

        // Wait full loading web view.
        WaitActions.waitForProgressBarDisappear(0, DataProvider.WAIT_DELAY_DEFAULT);

        ImageButton previousPageButton = getSolo().getImageButton(0);
        ImageButton nextPageButton = getSolo().getImageButton(1);
        ImageButton refreshButton = getSolo().getImageButton(2);
        ImageButton forwardButton = getSolo().getImageButton(3);

        Assert.assertNotNull("'Previous Page' button is not present", previousPageButton);
        Assert.assertNotNull("'Next Page' button is not present", nextPageButton);
        Assert.assertNotNull("'Refresh' button is not present", refreshButton);
        Assert.assertNotNull("'Forward' button is not present", forwardButton);

        Assert.assertTrue("'Previous Page' button is not presented (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(previousPageButton,
                        LayoutUtils.LOWER_LEFT_OF_4_BUTTONS_LAYOUT));
        Assert.assertTrue("'Next Page' button is not presented (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(nextPageButton,
                        LayoutUtils.LOWER_LEFT_CENTER_OF_4_BUTTONS_LAYOUT));
        Assert.assertTrue("'Refresh' button is not presented (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(refreshButton,
                        LayoutUtils.LOWER_RIGHT_CENTER_OF_4_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not presented (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_4_BUTTONS_LAYOUT));

        HardwareActions.takeCurrentActivityScreenshot("Browser");
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
     * Wait while page content is loaded
     */
    public void waitForPageContentLoaded() {
        ProgressBar progress = (ProgressBar) Id.getViewByViewIdName(WEB_PROGRESS_BAR);
        if (progress.isShown() == false)
            return;
        // TODO set timeout - may be infinite loop
        while (progress.isShown())
            ;
    }

    /**
     * Get page title
     * 
     * @return String contains page title
     */
    public String getPageTitle() {
        TextView title = (TextView) Id.getViewByViewIdName(WEB_PAGE_TITLE);
        return title.getText().toString();
    }

    /**
     * Taps on Forward button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getSolo().getImageButton(3);
        ViewUtils.tapOnView(forwardButton, "'Forward' button");
    }

    /**
     * Taps on Next Page button.
     */
    public void tapOnNextPageButton() {
        ImageButton nextPageButton = getSolo().getImageButton(1);
        ViewUtils.tapOnView(nextPageButton, "'Next Page' button");
    }

    /**
     * Taps on Previous Page button.
     */
    public void tapOnPreviousPageButton() {
        ImageButton previousPageButton = getSolo().getImageButton(0);
        ViewUtils.tapOnView(previousPageButton, "'Previous Page' button");
    }

    /**
     * Taps on Refresh button.
     */
    public void tapOnRefreshButton() {
        ImageButton refreshButton = getSolo().getImageButton(2);
        ViewUtils.tapOnView(refreshButton, "'Refresh' button");
    }

    /**
     * Taps on Share on Popup.
     */
    public void tapOnShareOnPopup() {
        getSolo().searchText("Share");

        TextView shareButton = getSolo().getText("Share");
        ViewUtils.tapOnView(shareButton, "'Share' button");
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
     * Taps on Open in Browser on Popup.
     */
    public void tapOnOpenInBrowserOnPopup() {
        getSolo().searchText("Open in Browser");

        TextView openInBrowserButton = getSolo().getText("Open in Browser");
        ViewUtils.tapOnView(openInBrowserButton, "'Open in Browser' button");
    }

    /**
     * Taps on Copy Link on Popup.
     */
    public void tapOnCopyLinkOnPopup() {
        getSolo().searchText("Copy Link");

        TextView copyLinkButton = getSolo().getText("Copy Link");
        ViewUtils.tapOnView(copyLinkButton, "'Copy Link' button");
        verifyToast("Copied");
    }

    /**
     * Opens 'Share News Article' screen.
     * 
     * @return ShareNewsArticleScreen
     */
    public ScreenShareNewsArticle openShareNewsArticleScreen() {
        tapOnForwardButton();
        tapOnShareOnPopup();
        return new ScreenShareNewsArticle();
    }

    /**
     * Opens 'New Message' screen.
     * 
     * @return NewMessageScreen
     */
    public ScreenNewMessage openNewMessageScreen() {
        tapOnForwardButton();
        tapOnSendToConnectionOnPopup();
        return new ScreenNewMessage();
    }
}
