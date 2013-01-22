package com.linkedin.android.screens.updates;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
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
    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(224.7f, 25.4f, 42.7f, 55.4f);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 45, 57);

    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.ViewArticleActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewArticleActivity";

    private static final int MESSAGE_BUTTON_INDEX = 0;
    private static final int FORWARD_BUTTON_INDEX = 1;

    private static final int ARTICLE_NUMBER_TITLE_INDEX = 0;

    private static final ViewIdName UP_ARROW_BUTTON_ID_NAME = new ViewIdName("nav_inbox_previous");
    private static final ViewIdName DOWN_ARROW_BUTTON_ID_NAME = new ViewIdName("nav_inbox_next");
    private static final ViewIdName NAVIGATION_BAR_TITLE_ID_NAME = new ViewIdName("navigation_bar_title");
    private static CharSequence changingText;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewsArticleDetailsFromLinkedInToday() {
        super();
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "News Article Details from LinkedIn Today");
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
        ViewUtils.waitForToastDisappear();
        verifyArticleNumberTitle();
        verifyUpArrowButton();

        verifyDownArrowButton();
        
        ImageButton forwardButton = getSolo().getImageButton(FORWARD_BUTTON_INDEX);
        ImageButton messageButton = getSolo().getImageButton(MESSAGE_BUTTON_INDEX);

        Assert.assertNotNull("'Forward' button is not present", forwardButton);
        Assert.assertNotNull("'Message' button is not present", messageButton);

        Assert.assertTrue("'Send' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(messageButton,
                        LayoutUtils.LOWER_LEFT_OF_2_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_2_BUTTONS_LAYOUT));

        HardwareActions.takeCurrentActivityScreenshot("News Article Details screen");
    }
    
    /**
     * Gets Down Arrow Button.
     * 
     * @return Down Arrow Button {@code ImageView}.
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = Id.getViewByName(DOWN_ARROW_BUTTON_ID_NAME, ImageView.class);
        return downArrowButton;
    }

    /**
     * Gets Up Arrow Button.
     * 
     * @return Up Arrow Button {@code ImageView}.
     */
    public ImageView getUpArrowButton() {
        ImageView downArrowButton = Id.getViewByName(UP_ARROW_BUTTON_ID_NAME, ImageView.class);
        return downArrowButton;
    }

    /**
     * Taps on up arrow button. (Change current article).
     */
    public void tapOnUpArrowButton() {
        ViewUtils.waitForToastDisappear();
        ImageView upArrowButton = getUpArrowButton();

        ViewUtils.tapOnView(upArrowButton, "up arrow button");
    }

    /**
     * Taps on down arrow button. (Change current article).
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = getDownArrowButton();

        ViewUtils.tapOnView(downArrowButton, "down arrow button");
    }

    /**
     * Verify Down Arrow Button.
     */
    private void verifyDownArrowButton() {
        Assert.assertNotNull("'Down arrow' button is not presented", getDownArrowButton());
    }

    /**
     * Verify Up Arrow Button.
     */
    private void verifyUpArrowButton() {
        Assert.assertNotNull("'Up arrow' button is not presented", getUpArrowButton());
    }

    /**
     * Verifies that article number title {@code TextView} loaded properly.
     */
    private void verifyArticleNumberTitle() {
        final String articleNumberTitleSeparator = "of";
        
        WaitActions.waitForTrueInFunction("Title '.. of ..' is not present", new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws Exception {
                return getArticleNumberTitleText().contains(articleNumberTitleSeparator);
            }
        });
    }

    /**
     * Returns current article number title text.
     * 
     * @return current article number title text.
     */
    private String getArticleNumberTitleText() {
        TextView articleNumberTitle = getArticleNumberTitle();
        Assert.assertNotNull("There is no article number (...of...) title", articleNumberTitle);
        return articleNumberTitle.getText().toString();
    }

    /**
     * Returns current article number title {@code TextView}.
     * 
     * @return current article number title {@code TextView}.
     */
    private TextView getArticleNumberTitle() {
        return getSolo().getText(ARTICLE_NUMBER_TITLE_INDEX);
    }
    
    public static void go_to_news_detail() {
        LoginActions.openUpdatesScreenOnStart().openLinkedInTodayScreen().openFirstNewsArticleDetailsScreen();
        TestUtils.delayAndCaptureScreenshot("go_to_news_detail");
    }
    
    public static void news_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenLinkedInToday();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_back");
    }
    
    public static void news_detail() {
        new ScreenLinkedInToday().openFirstNewsArticleDetailsScreen();
        TestUtils.delayAndCaptureScreenshot("news_detail");
    }
    
    /**
     * Tap in back button and verify 'Screen News Article Details From LinkedInToday'.
     */
    public static void tapBackInNewsScreen(){
        HardwareActions.goBackOnPreviousActivity();
        new ScreenNewsArticleDetailsFromLinkedInToday();
    }
    
    public static void news_detail_tap_article_up() {
        TextView titleCurrentView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
        Assert.assertNotNull("Title in navigation bar is not present", titleCurrentView);
        changingText = titleCurrentView.getText();
        new ScreenNewsArticleDetailsFromLinkedInToday().tapOnUpArrowButton();
        WaitActions.waitForTrueInFunction("New news is not loaded", new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws Exception {
                TextView titleView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if(titleView == null) return false;
                return !changingText.equals(titleView.getText());
            }
        });
        new ScreenNewsArticleDetailsFromLinkedInToday();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_article_up");
    }

    public static void news_detail_tap_article_down() {
        TextView titleCurrentView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
        Assert.assertNotNull("Title in navigation bar is not present", titleCurrentView);
        changingText = titleCurrentView.getText();
        new ScreenNewsArticleDetailsFromLinkedInToday().tapOnDownArrowButton();
        WaitActions.waitForTrueInFunction("New news is not loaded", new Callable<Boolean>() {
            
            @Override
            public Boolean call() throws Exception {
                TextView titleView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if (titleView == null) return false;
                return !changingText.equals(titleView.getText());
            }
        });
        new ScreenNewsArticleDetailsFromLinkedInToday();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_article_down");
    }

    public static void news_detail_tap_view_article_header() {
        new ScreenNewsArticleDetailsFromLinkedInToday().tapOnAtricleTitle();
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_view_article_header");
    }

    public static void news_detail_tap_view_article_header_reset() {
        tapBackInNewsScreen();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_view_article_header_reset");
    }

    public static void news_detail_tap_view_article_image() {
        new ScreenNewsArticleDetailsFromLinkedInToday().tapOnArticleImage();
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_view_article_image");
    }

    public static void news_detail_tap_view_article_image_reset() {
        tapBackInNewsScreen();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_view_article_image_reset");
    }

    public static void news_detail_tap_message() {
        new ScreenNewsArticleDetailsFromLinkedInToday().tapOnMessageButton();
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_message");
    }

    public static void news_detail_tap_message_reset() {
        tapBackInNewsScreen();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_message_reset");
    }

    public static void news_detail_tap_share() {
        new ScreenNewsArticleDetailsFromLinkedInToday().openShareNewsArticleScreen();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_share(");
    }

    public static void news_detail_tap_share_reset() {
        tapBackInNewsScreen();
        TestUtils.delayAndCaptureScreenshot("detail_tap_share_reset");
    }

    public static void news_detail_tap_expose() {
        ScreenNewsArticleDetailsFromLinkedInToday.tapOnINButton();
        new ScreenExpose(null);
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_expose");
    }

    public static void news_detail_tap_expose_reset() {
        ScreenExpose.tapOnINButton();
        new ScreenNewsArticleDetailsFromLinkedInToday();
        TestUtils.delayAndCaptureScreenshot("news_detail_tap_expose_reset");
    }
}
