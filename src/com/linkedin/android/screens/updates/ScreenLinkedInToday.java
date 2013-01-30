package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for LINKEDIN TODAY screen.
 * 
 * @author dmitry.somov
 * @created Aug 14, 2012 10:58:17 AM
 */
public class ScreenLinkedInToday extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.NewsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NewsListActivity";

    static final int NEWS_ARTICLE_IMAGE_INDEX = 1;
    static final int NEWS_ARTICLE_IMAGE_RELATIVE_LAYOUT_INDEX = 1;

    static final Rect2DP NEWS_BIG_IMAGE = new Rect2DP(0.0f, 75.3f, 320.0f, 200.0f);
    static final Rect2DP CATEGORIES_BUTTON_LAYOUT = new Rect2DP(275.0f, 26.0f, 46.0f, 48.0f);
    static final Rect2DP CATEGORIES_BUTTON_RECT = new Rect2DP(276.0f, 27.0f, 44.0f, 46.0f);

    public static final String MENU_ITEM_REFRESH = "Refresh";

    // TextView: top news article label
    private static final ViewIdName TOP_NEWS_ARTICLE_LABEL = new ViewIdName("title");
    // ListView: news
    private static final ViewIdName NEWS_LIST = new ViewIdName(Id.ANDROID_PACKAGE_NAME, "list");
    public static final ViewIdName MANAGE_BUTTON = new ViewIdName("nav_settings");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenLinkedInToday() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        // Scroll to the top of activity to ensure News big image is on screen
        HardwareActions.scrollUp();

        Assert.assertTrue("'LINKEDIN TODAY' screen is not present",
                getSolo().waitForText("News", 3, DataProvider.WAIT_DELAY_DEFAULT, false, false));

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("LINKEDIN TODAY screen");
    };

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "LinkedIn Today");
    }

    /**
     * Opens first News Article Details screen.
     * 
     * @return {@codeNewsArticleDetailsScreenFromLinkedInToday} with just opened
     *         'News Article Details' screen.
     */
    public ScreenNewsArticleDetailsFromLinkedInToday openFirstNewsArticleDetailsScreen() {
        ViewUtils.waitForToastDisappear();
        TextView article = getSolo().getText(4);
        Assert.assertNotNull("News Articles is not present on LINKEDIN TODAY screen", article);
        Logger.i("Tapping on first News article");
        getSolo().clickOnView(article);

        return new ScreenNewsArticleDetailsFromLinkedInToday();
    }

    /**
     * Taps on news big image.
     */
    public void tapOnNewsBigImage() {
        ImageView newsBigImage = getNewsBigImage();
        ViewUtils.tapOnView(newsBigImage, "news big image");
    }

    /**
     * Verifies 'News' list and images of news articles from it.
     */
    public void verifyNewsList() {

        ListView newsList = getNewsList();
        Assert.assertNotNull("'News' list is not present", newsList);
        Assert.assertTrue("'News' list width does not equal to screen width",
                ListViewUtils.isListViewWidthEqualToScreenWidth(newsList));
        Assert.assertTrue("'News' list is empty", ListViewUtils.isListViewNotEmpty(newsList));
        Assert.assertTrue("Not all of the news images were loaded properly",
                isNewsListImagesLoaded());
    }

    /**
     * Gets top news article label as {@code TextView}
     * 
     * @return top news article label as {@code TextView}
     */
    public TextView getTopNewsArticleLabel() {
        return (TextView) Id.getViewByViewIdName(TOP_NEWS_ARTICLE_LABEL);
    }

    /**
     * Gets 'News' {@code ListView}
     * 
     * @return 'News' {@code ListView}
     */
    public ListView getNewsList() {
        return (ListView) Id.getViewByViewIdName(NEWS_LIST);
    }

    /**
     * Gets news big image
     * 
     * @return {@code ImageView} of news big image
     */
    public ImageView getNewsBigImage() {
        return LayoutUtils.getImageViewByItsLayoutAndSize(LayoutUtils.NEWS_BIG_IMAGE_LAYOUT,
                NEWS_BIG_IMAGE.width, NEWS_BIG_IMAGE.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
    }

    /**
     * Gets 'Categories' button ({@code ImageView})
     * 
     * @return 'Categories' button ({@code ImageView})
     */
    public ImageView getCategoriesButton() {
        return LayoutUtils.getImageViewByItsLayoutAndSize(CATEGORIES_BUTTON_LAYOUT,
                CATEGORIES_BUTTON_RECT.width, CATEGORIES_BUTTON_RECT.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
    }

    /**
     * Verifies images of all visible news articles from 'News' list were loaded
     * 
     * @return <b>true</b> if images of all visible news articles from 'News'
     *         list were loaded <b>false</b> otherwise
     * 
     */
    private boolean isNewsListImagesLoaded() {

        final int newsListExpectedLoadTimeSec = 5;

        ListView newsList = getNewsList();
        if (null == newsList) {
            return false;
        }

        // wait while 'News' list loading
        WaitActions.delay(newsListExpectedLoadTimeSec);

        int newsArticlesCount = newsList.getChildCount();
        for (int i = 0; i < newsArticlesCount; i++) {
            View newsListChild = newsList.getChildAt(i);
            if (!(newsListChild instanceof LinearLayout)
                    || newsListChild.getVisibility() != View.VISIBLE) {
                continue;
            }
            LinearLayout newsArticleLayout = (LinearLayout) newsList.getChildAt(i);
            if (isSeparatorLayoutBetweenNewsArticles(newsArticleLayout)) {
                continue;
            }
            if (!isNewsArticleImageLoaded(newsArticleLayout)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets image layout as {@code RelativeLayout} of news article with
     * specified {@code newsArticleLayout}
     * 
     * @param newsArticleLayout
     *            {@code LinearLayout} of news article
     * @return {@code RelativeLayout} image layout of news article with
     *         specified {@code newsArticleLayout} or <b>null</b> if such image
     *         layout does not exist.
     */
    private RelativeLayout getNewsArticleImageLayout(LinearLayout newsArticleLayout) {
        if (!ViewGroupUtils.isViewGroupContainChildWithIndex(newsArticleLayout,
                NEWS_ARTICLE_IMAGE_RELATIVE_LAYOUT_INDEX)) {
            return null;
        }
        View newsArticleImageLayoutView = newsArticleLayout
                .getChildAt(NEWS_ARTICLE_IMAGE_RELATIVE_LAYOUT_INDEX);
        if (!(newsArticleImageLayoutView instanceof RelativeLayout)) {
            return null;
        }
        RelativeLayout newsArticleImageLayout = (RelativeLayout) newsArticleImageLayoutView;
        return newsArticleImageLayout;
    }

    /**
     * Checks if specified {@code newsArticleLayout} is separator between news
     * articles
     * 
     * @param newsArticleLayout
     *            {@code LinearLayout} to verify if separator between news
     *            articles
     * @return <b>true</b> if specified {@code newsArticleLayout} is separator
     *         between news articles <b>false</b> otherwise.
     */
    private boolean isSeparatorLayoutBetweenNewsArticles(LinearLayout newsArticleLayout) {
        return null != newsArticleLayout && newsArticleLayout.getChildCount() == 0;
    }

    /**
     * Verifies image of news article with specified {@code RelativeLayout} was
     * loaded
     * 
     * @param newsArticleLayout
     *            {@code RelativeLayout} of target news article
     * @return <b>true</b> if image of news article with specified
     *         {@code RelativeLayout} was loaded <b>false</b> otherwise.
     */
    private boolean isNewsArticleImageLoaded(LinearLayout newsArticleLayout) {
        RelativeLayout newsArticleImageLayout = getNewsArticleImageLayout(newsArticleLayout);

        if (!ViewGroupUtils.isViewGroupContainChildWithIndex(newsArticleImageLayout,
                NEWS_ARTICLE_IMAGE_INDEX)) {
            return false;
        }
        View newsArticleImageView = newsArticleImageLayout.getChildAt(NEWS_ARTICLE_IMAGE_INDEX);
        return newsArticleImageView instanceof ImageView;
    }

    @TestAction(value = "go_to_news")
    public static void go_to_news(String email, String password) {
        LoginActions.openUpdatesScreenOnStart(email, password);

        news("go_to_news");
    }

    @TestAction(value = "news")
    public static void news() {
        news("news");
    }

    public static void news(String screenshotName) {
        // Sometimes screen below than LinkedIn Today title and it's need to
        // scroll up for click on it.
        getSolo().scrollToTop();

        TextView title = (TextView) Id.getViewByViewIdName(TOP_NEWS_ARTICLE_LABEL);
        ViewUtils.tapOnView(title, "LINKEDIN TODAY");

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "news_tap_back")
    public static void news_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("news_tap_back");
    }

    @TestAction(value = "news_tap_article")
    public static void news_tap_article() {
        View topNewsTitle = (View) Id.getViewByViewIdName(TOP_NEWS_ARTICLE_LABEL);
        Assert.assertNotNull("No updates found on News screen", topNewsTitle);

        ViewUtils.tapOnView(topNewsTitle, "first update");

        ScreenUpdates.verifyNUSSscreen();

        TestUtils.delayAndCaptureScreenshot("news_tap_article");
    }

    @TestAction(value = "news_tap_article_reset")
    public static void news_tap_article_reset() {
        HardwareActions.pressBack();
        new ScreenLinkedInToday();

        TestUtils.delayAndCaptureScreenshot("news_tap_article_reset");
    }

    @TestAction(value = "news_tap_expose")
    public static void news_tap_expose() {
        new ScreenLinkedInToday().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("news_tap_expose");
    }

    @TestAction(value = "news_tap_expose_reset")
    public static void news_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenLinkedInToday();

        TestUtils.delayAndCaptureScreenshot("news_tap_expose_reset");
    }

    @TestAction(value = "news_pull_refresh")
    public static void news_pull_refresh() {
        new ScreenLinkedInToday().refreshScreen();
        TestUtils.delayAndCaptureScreenshot("news_pull_refresh");
    }

    @TestAction(value = "news_tap_manage")
    public static void news_tap_manage() {
        View manageButton = (View) Id.getViewByViewIdName(MANAGE_BUTTON);
        Assert.assertNotNull("'Manage' button (top right corner) is not present");

        ViewUtils.tapOnView(manageButton, "'Manage' button");

        new ScreenCategories();

        TestUtils.delayAndCaptureScreenshot("news_manage");
    }

    @TestAction(value = "news_manage_reset")
    public static void news_manage_reset() {
        HardwareActions.pressBack();
        new ScreenLinkedInToday();

        TestUtils.delayAndCaptureScreenshot("news_manage_reset");
    }
}
