package com.linkedin.android.screens.updates;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenCalSplash;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenJobDetails;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.common.ScreenUpdatedProfileRollup;
import com.linkedin.android.screens.more.ScreenCompanies;
import com.linkedin.android.screens.more.ScreenDiscussionDetailsFromRecentUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.RegexpPatterns;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.StringDefaultValues;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewChecker;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Updates screen.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 10:55:40 AM
 */
public class ScreenUpdates extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.home.v2.NUSListActivityV2";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivityV2";
    public static final String HELPACTIVITY_CLASSNAME = "com.linkedin.android.help.HelpActivity";
    public static final String HELPACTIVITY_SHORT_CLASSNAME = "HelpActivity";

    public static final String LINKEDIN_TODAY_LABEL = "Top News";
    public static final String MENU_ITEM_REFRESH = "Refresh";
    public static final String NEWS_ARTICLE_SUMMARY_SHARED_BY_CURRENT_USER_WITHOUT_COMMENT = "You shared this";

    public static final int RECENT_UPDATES_LIST_VIEW_INDEX = 0;

    public static final int UPDATES_LIST_FIRST_ITEM_INDEX = 2;

    static final Rect SHARE_BUTTON_RECT = new Rect(398, 42, 82, 74);
    // ImageButton: like it button
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    private static final ViewIdName CALENDAR_VIEW = new ViewIdName("calendar_overlay_button");
    public static final ViewIdName NUS_LAYOUT = new ViewIdName("tt1_container");
    public static final ViewIdName NUS_IMAGE_LAYOUT = new ViewIdName("image");
    public static final ViewIdName NUS_HEADER_LAYOUT = new ViewIdName("header");
    public static final ViewIdName NUS_TIMESTAMP_LAYOUT = new ViewIdName("timestamp");
    public static final ViewIdName NUS_TEXT_LAYOUT = new ViewIdName("text");
    public static final ViewIdName NUS_FOOTER_LAYOUT = new ViewIdName("footer_layout");
    public static final ViewIdName NUS_FOOTER_IMG_LAYOUT = new ViewIdName("footer_img");
    public static final ViewIdName NUS_FOOTER_TEXT1_LAYOUT = new ViewIdName("footer_text_1");
    public static final ViewIdName NUS_FOOTER_TEXT2_LAYOUT = new ViewIdName("footer_test_2");
    public static final ViewIdName NUS_FOOTER_VIA2_LAYOUT = new ViewIdName("footer_via_2");
    public static final ViewIdName NUS_FOOTER_LOGO2_LAYOUT = new ViewIdName("footer_loge_2");
    public static final ViewIdName NUS_FOOTER_LIKES_LAYOUT = new ViewIdName("likes");
    public static final ViewIdName NUS_FOOTER_COMMENTS_LAYOUT = new ViewIdName("comments");
    public static final ViewIdName NUS_FOOTER_VIA1_LAYOUT = new ViewIdName("footer_via_1");
    public static final ViewIdName NUS_FOOTER_LOGO1_LAYOUT = new ViewIdName("footer_logo_1");
    public static final ViewIdName NUS_ROLLUP_HEADER_VIEW = new ViewIdName("sht3_text");
    private static final ViewIdName COMMENTS_AND_LIKES_LAYOUT = new ViewIdName(
            "likes_comments_container");
    // Default count of scrolls to scroll whole screen.
    private static final int DEFAULT_SCROLLS_COUNT = 150;

    private static final String COMMENTS_LABEL = " Comments";
    private static final String LIKES_LABEL = " Likes";

    public static final int COMMENTS_COUNT_FOR_SHOWN_AS_LIST = 11;
    public static final int LIKES_COUNT_FOR_SHOWN_AS_LIST = 1;
    public static final int COMMENTS_COUNT_FOR_SPINNER_APPEARING = 50;
    public static final int LIKES_COUNT_FOR_SPINNER_APPEARING = 50;
    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final int LINK_COLOR = 0xff999490;
    private static final String LABEL_HTTP = "http://";
    private static final String LABEL_DOMEN_COM = ".com";

    // Container for NUS with shared news and like/comment
    // private static final ViewIdName NUS_SIMPLE_RICH_ROW = new
    // ViewIdName("nus_simple_rich_row");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenUpdates() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activities.
        String curActivityName = getSolo().getCurrentActivity().getClass().getSimpleName();
        Assert.assertTrue(
                "Wrong activity (expected " + ACTIVITY_SHORT_CLASSNAME + " or "
                        + HELPACTIVITY_SHORT_CLASSNAME + ")",
                curActivityName.equals(ACTIVITY_SHORT_CLASSNAME)
                        || curActivityName.equals(HELPACTIVITY_SHORT_CLASSNAME));

        // Wait for more than 20 news are loaded.
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG,
                "'Updates' screen is not present (List with NUS is not loaded)",
                new Callable<Boolean>() {
                    public Boolean call() {
                        ListView listView = ListViewUtils.getFirstListView();
                        if (listView == null)
                            return false;
                        return (listView.getCount() > 20);
                    }
                });

        // Check that list views is not empty.
        Assert.assertTrue("List views is not present", !getSolo().getCurrentListViews().isEmpty());
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME,
                HELPACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Checks that current screen - Updates.
     * 
     * @return <b>true</b> if current screen - Updates.
     */
    public static boolean isOnUpdatesScreen() {
        return getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME);
    }

    /**
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        View button = getRightButtonInNavigationBar();
        Assert.assertNotNull("'Share button' is not present.", button);

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnView(button);
    }

    /**
     * Opens 'Share Update' screen by tapping on 'Share' button on nav bar.
     * 
     * @return {@code ShareUpdateScreen} with just opened 'Share Update' screen.
     */
    public ScreenShareUpdate openShareUpdateScreen() {
        tapOnShareButton();
        return new ScreenShareUpdate();
    }

    /**
     * Opens first News article Details screen.
     * 
     * @return {@code NewsArticleDetailsScreen} with just opened 'News Article
     *         Details' screen.
     */
    public ScreenNewsArticleDetails openFirstNewsArticleDetailsScreen() {
        Assert.assertNotNull(
                "There are no News articles (must contain tag 'also trending in') on current screen",
                getSolo().searchText("also trending in"));

        Logger.i("Tapping on first News article");
        getSolo().clickOnText("also trending in");

        return new ScreenNewsArticleDetails();
    }

    /**
     * Opens any Shared News article Details screen with link.
     * 
     * @return {@code ScreenSharedNewsDetails} with just opened 'Shared News
     *         Details' screen.
     */
    public ScreenUpdate openAnyUpdateWithLinkSharedByYourConnection() {
        ArrayList<TextView> exceptions = new ArrayList<TextView>();
        do {
            TextView link = TextViewUtils.searchTextViewInActivity(".com", exceptions, false);
            if (link == null) {
                if (getSolo().scrollDown())
                    continue;
                else
                    // we have reached bottom of page
                    return null;
            }
            int textColor = link.getCurrentTextColor();
            if (link.getText().toString().startsWith("http://") == false && link.isShown()
                    && textColor == 0xff999490) {
                ViewUtils.tapOnView(link, "Update with link");
                if (isSharedNewsDetailsScreen(3))
                    return new ScreenUpdate();
            }
            exceptions.add(link);
        } while (true);
    }

    /**
     * Tap on any update with link.
     */
    public void openAnyUpdateWithLink() {
        ArrayList<TextView> exceptions = new ArrayList<TextView>();
        int count_max_scroll = 30;
        do {
            TextView link = TextViewUtils.searchTextViewInActivity(LABEL_DOMEN_COM, exceptions,
                    false);
            if (link == null) {
                if (getSolo().scrollDown())
                    continue;
                else
                    Assert.fail("Update with link not found.");
            }
            int textColor = link.getCurrentTextColor();
            if (!link.getText().toString().startsWith(LABEL_HTTP) && link.isShown()
                    && textColor == LINK_COLOR) {
                ViewUtils.tapOnView(link, "Update with link");
                return;
            }
            exceptions.add(link);
        } while (count_max_scroll-- > 0);
    }

    /**
     * Opens {@code ScreenDiscussionDetailsFromRecentUpdates} of first found
     * group post.
     * 
     * @return just opened {@code ScreenDiscussionDetailsFromRecentUpdates} of
     *         first found group post.
     */
    public ScreenDiscussionDetailsFromRecentUpdates openDiscussionDetailsOfFirstGroupPost() {
        tapOnFirstGroupPost();
        return new ScreenDiscussionDetailsFromRecentUpdates();
    }

    /**
     * Taps on first found group post.
     */
    public void tapOnFirstGroupPost() {
        TextView nameOfGroupFromFirstGroupPost = searchFirstGroupPost();
        Assert.assertNotNull("There is no Group posts on current screen",
                nameOfGroupFromFirstGroupPost);

        getSolo().clickOnView(nameOfGroupFromFirstGroupPost);
    }

    /**
     * Looks for first group post.
     * 
     * @return first found group post or <b>null</b> if there is no group posts
     *         on current screen.
     */
    public TextView searchFirstGroupPost() {
        /*
         * final String groupLabelFromGroupPost = "Group:";
         * 
         * ArrayList<TextView> nonNameOfGroupTextViews = new
         * ArrayList<TextView>(); TextView nameOfGroupFromFirstGroupPost = null;
         * do { getSolo().searchText(groupLabelFromGroupPost);
         * nameOfGroupFromFirstGroupPost =
         * TextViewUtils.searchTextViewInActivity( groupLabelFromGroupPost,
         * nonNameOfGroupTextViews, false); if
         * (isNameOfGroupFromGroupPost(nameOfGroupFromFirstGroupPost)) { return
         * nameOfGroupFromFirstGroupPost; }
         * nonNameOfGroupTextViews.add(nameOfGroupFromFirstGroupPost); } while
         * (null != nameOfGroupFromFirstGroupPost);
         */
        int maxCountOfScrolls = 50;
        boolean isCanScroll = true;

        for (int i = 0; i < maxCountOfScrolls && isCanScroll; i++) {
            TextView postedThis = TextViewUtils.searchTextViewInActivity("posted this", false);
            if (postedThis != null) {
                return postedThis;
            }
            isCanScroll = ListViewUtils.scrollToNewItems();
        }
        return null;
    }

    /**
     * Checks if specified {@code textView} is name of group from group post.
     * 
     * @param textView
     *            name of group from group post.
     * @return <b>true</b> if specified {@code textView} is name of group from
     *         group post, <b>false</b> otherwise.
     */
    /*
     * private boolean isNameOfGroupFromGroupPost(TextView textView) { final int
     * nameOfGroupFromGroupPostIndex = 1;
     * 
     * if (null == textView) { return false; }
     * 
     * ViewParent textViewParent = textView.getParent(); if (!(textViewParent
     * instanceof RelativeLayout)) { return false; } RelativeLayout
     * textViewParentLayout = (RelativeLayout) textViewParent; TextView
     * groupName =
     * ViewGroupUtils.getChildViewByIndexSafely(textViewParentLayout,
     * nameOfGroupFromGroupPostIndex, TextView.class); if (textView !=
     * groupName) { return false; }
     * 
     * ViewParent textViewParent2Degree = textViewParentLayout.getParent(); if
     * (!(textViewParent2Degree instanceof RelativeLayout)) { return false; }
     * 
     * ViewParent textViewParent3Degree = textViewParent2Degree.getParent(); if
     * (!(textViewParent3Degree instanceof RelativeLayout)) { return false; }
     * 
     * ViewParent textViewParent4Degree = textViewParent3Degree.getParent(); if
     * (!(textViewParent4Degree instanceof LinearLayout)) { return false; }
     * 
     * View recentUpdatesList = (View) textViewParent4Degree.getParent(); return
     * recentUpdatesList instanceof ListView; }
     */

    /**
     * Used by openAnySharedNewsArticleDetailsScreen function
     * 
     * @param optionsCountInSharePopup
     *            number of send options in 'Share' popup. Actually 2 or 3
     * @return true if opened screen is ScreenSharedNewsDetails, and it contains
     *         3 send option in 'Share' popup, otherwise back to previous screen
     *         and return false
     */
    private boolean isSharedNewsDetailsScreen(int optionsCountInSharePopup) {
        int waiting_time = 8;
        // wait after tap on screen. We don't know what screen will be actually
        // open
        WaitActions.delay(waiting_time);
        if (getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ScreenUpdate.ACTIVITY_SHORT_CLASSNAME)) {
            ImageButton likeButton = (ImageButton) Id.getViewByViewIdName(LIKE_BUTTON);
            if (likeButton.isShown()) {
                ScreenUpdate newsDetails = new ScreenUpdate();
                newsDetails.tapOnForwardButton();
                int nr = newsDetails.getCountOptionsForForwardPopup().size();
                HardwareActions.pressBack();
                // Allow 3/2 when 2 ordered
                if (nr >= optionsCountInSharePopup)
                    return true;
            }
        }
        if (getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME) == false) {
            HardwareActions.pressBack();
            // wait for back to prev screen.
            WaitActions.delay(waiting_time);
        }
        return false;
    }

    /**
     * Opens Any shared News article Details screen.
     * 
     * @param optionsCountInSharePopup
     *            number of send options in 'Share' popup. Actually 2 or 3
     * @return {@code ScreenSharedNewsDetails} with just opened 'News Article
     *         Details' screen. return null if there is no shared news.
     */
    public ScreenUpdate openAnySharedNewsArticleDetailsScreen(int optionsCountInSharePopup) {
        do {
            getSolo().clickOnScreen(ScreenResolution.getScreenWidth() / 2,
                    ScreenResolution.getScreenHeight() / 2);
            if (isSharedNewsDetailsScreen(optionsCountInSharePopup))
                return new ScreenUpdate();

            getSolo().clickOnScreen(ScreenResolution.getScreenWidth() / 2,
                    ScreenResolution.getScreenHeight() * 9 / 10);
            if (isSharedNewsDetailsScreen(optionsCountInSharePopup))
                return new ScreenUpdate();
        } while (getSolo().scrollDown());
        return null;
    }

    /**
     * Finds current user shortened name ({@code TextView}) of first news
     * article shared by Your connection
     * 
     * @param currentUserShortenedName
     *            current user shortened name as {@code String}
     * @return current user shortened name ({@code TextView}) of first news
     *         article shared by Your connection or <b>null</b> if there is no
     *         such article on 'Updates' screen
     */
    public TextView findFirstNewsArticleSharedByYourConnection(String currentUserShortenedName) {
        ArrayList<TextView> nonNewsUpdatesSharedByCurrentUser = new ArrayList<TextView>();
        TextView updateSharedByCurrentUser;

        do {
            updateSharedByCurrentUser = TextViewUtils.searchTextViewInActivity(
                    currentUserShortenedName, nonNewsUpdatesSharedByCurrentUser, false);
            if (isArticleHasImageDisplayedAlongWithSummary(updateSharedByCurrentUser)) {
                return updateSharedByCurrentUser;
            }
            nonNewsUpdatesSharedByCurrentUser.add(updateSharedByCurrentUser);
        } while (null != updateSharedByCurrentUser);
        return null;
    }

    /**
     * Opens first news shared by connection. Additionally opens You screen to
     * get short name of current user.
     * 
     * @param connectionName
     *            is name of connection who shared news article *
     * @return {@code ScreenSharedNewsDetails} with just opened 'Shared News
     *         Article Details' screen.
     */
    public ScreenUpdate openFirstNewsArticleSharedByYourConnection() {

        ScreenExpose screenExpose = openExposeScreen();
        ScreenYou screenYou = screenExpose.openYouScreen();
        String currentUserShortenedName = screenYou.getCurrentUserShortenedName();
        screenExpose = screenYou.openExposeScreen();
        screenExpose.openUpdatesScreen();

        TextView firstNewsArticleSharedByYourConnection = findFirstNewsArticleSharedByYourConnection(currentUserShortenedName);
        Assert.assertNotNull("There are no News from your connections on current screen",
                firstNewsArticleSharedByYourConnection);

        Logger.i("Tapping on news shared by your connection");
        getSolo().clickOnView(firstNewsArticleSharedByYourConnection);

        return new ScreenUpdate();
    }

    /**
     * Taps on first Update.
     * 
     * @return <b>true</b> if tap, <b>false</b> if cannot find Update.
     */
    /*
     * public boolean tapOnFirstUpdate() { Logger.logElements();
     * 
     * 
     * NUS_SIMPLE_RICH_ROW
     * 
     * return false; }
     */

    /**
     * Opens first Update NUS.
     * 
     * @return {@code ScreenUpdate} object
     */
    /*
     * public ScreenUpdate openFirstUpdate() {
     * Assert.assertTrue("Cannot find Update NUS in list", tapOnFirstUpdate());
     * 
     * return new ScreenUpdate(); }
     */

    /**
     * Taps on first Viral Update.
     * 
     * @return <b>true</b> if tap, <b>false</b> if cannot find Viral Update.
     */
    public boolean tapOnFirstViralUpdate() {
        // Flag that it row contain label with company name.
        boolean isRowWithCompany;
        // String with content of ViralUpdate for log.
        String contentForLog;

        // Get list with NUS.
        ListView listView = getSolo().getCurrentListViews().get(0);

        // Scroll list to end or MAX_SCROLLS times.
        final int MAX_SCROLLS = 30;
        boolean isNeedScroll = true;
        for (int j = 0; isNeedScroll && j < MAX_SCROLLS; j++) {
            /*
             * int i = 0; for (View view : getSolo().getViews(listView)) { if
             * (view.getClass().getSimpleName().equals("TextView")){ if
             * (((TextView)view).getText().length() > 0){ Logger.d(i + ": " +
             * ((TextView)view).getText()); } i++; } }
             */

            // Initialize variables for search.
            isRowWithCompany = false;
            int counter = -1;// Counter in views list
            int lastPos = 0;// Last position of company name in TextViews list
            contentForLog = StringDefaultValues.EMPTY_STRING;
            // Check all views.
            for (View view : getSolo().getViews(listView)) {
                // If must be label.
                if (view.getClass().getSimpleName().equals("TextView")) {
                    String string = ((TextView) view).getText().toString();
                    counter++;
                    // If must be not empty string.
                    if (string.length() > 0) {
                        if (string.equals(StringData.test_own_company)) {
                            // If find string with company name.
                            contentForLog = string + "|";
                            isRowWithCompany = true;
                            lastPos = counter;
                        } else {
                            // If find below string with company name.
                            if (isRowWithCompany) {
                                int dif = counter - lastPos;
                                if (dif == 1) {
                                    // Next string after company name must be
                                    // news date.
                                    if (!RegexpUtils.isCanBeFound(string,
                                            RegexpPatterns.DATE_LIKE_10_HOURS_AGO)
                                            && !RegexpUtils.isCanBeFound(string,
                                                    RegexpPatterns.DATE_LIKE_OCTOBER_1)) {
                                        // If it not news date then current row
                                        // is
                                        // not ViralUpdate.
                                        contentForLog = StringDefaultValues.EMPTY_STRING;
                                        isRowWithCompany = false;
                                    }
                                } else if (2 < dif && dif < 10) {
                                    // In range (2,10) strings after company
                                    // name must be "Like" or "Comment".
                                    contentForLog += string.substring(0, (string.length() > 20 ? 20
                                            : string.length()))
                                            + "|";
                                    if (RegexpUtils.isCanBeFound(string,
                                            RegexpPatterns.LIKE_OR_COMMIT)) {
                                        // If "Like|Comment" found then it is
                                        // ViralUpdate.
                                        Logger.i("Tapping on ViralUpdate '" + contentForLog + "'");
                                        getSolo().clickOnView(view);
                                        return true;
                                    }
                                } else if (dif >= 10) {
                                    // If previous conditions not completed then
                                    // current row is not ViralUpdate.
                                    contentForLog = StringDefaultValues.EMPTY_STRING;
                                    isRowWithCompany = false;
                                }
                            }
                        }
                    }
                }
            }

            isNeedScroll = ListViewUtils.scrollToNewItems();
        }
        return false;
    }

    /**
     * Opens first Viral update.
     * 
     * @return {@code ScreenViralUpdate} object.
     */
    public ScreenViralUpdate openFirstViralUpdate() {
        Assert.assertTrue("Not found Viral Updates on Updates screen", tapOnFirstViralUpdate());

        return new ScreenViralUpdate();
    }

    /**
     * Opens LINKEDIN TODAY screen.
     * 
     * @return{@code LinkedInTodayScreen} with just opened 'LINKEDIN TODAY'
     *               screen.
     */
    public ScreenLinkedInToday openLinkedInTodayScreen() {
        TextViewUtils.searchAndScrollToVisibleText(LINKEDIN_TODAY_LABEL);

        TextView textView = getSolo().getText(LINKEDIN_TODAY_LABEL);
        ViewUtils.tapOnView(textView, "'Top News' banner");

        return new ScreenLinkedInToday();
    }

    /**
     * Opens SEARCH screen.
     * 
     * @return {@code SearchScreen} with just opened 'SEARCH' screen.
     */
    public ScreenSearch openSearchScreen() {
        View searchBar = getSearchBar();
        Assert.assertNotNull("Search bar is not present", searchBar);
        ViewUtils.tapOnView(searchBar, "search bar");
        return new ScreenSearch();
    }

    /**
     * Checks if there is article with specified text on Update screen
     * 
     * @param articleText
     *            target article text
     * @return <b>true</b> if such article exist, <b>false</b> otherwise
     */
    public boolean isUpdateScreenContainArticleWithSpecifiedText(String articleText) {
        final int minimumNumberOfMatches = 1;
        getSolo().waitForText(articleText, minimumNumberOfMatches, DataProvider.WAIT_DELAY_LONG,
                false, false);
        return getSolo().searchText(articleText);
    }

    /**
     * Checks does article with specified comment text or connection name has
     * image displayed along with summary
     * 
     * @param commentToArticle
     *            target article comment
     * @return <b>true</b> if image with article summary exist, <b>false</b>
     *         otherwise
     */
    public boolean isArticleHasImageDisplayedAlongWithSummary(String commentToArticle) {
        RelativeLayout commentedNewsArticleRelativeLayout = getCommentedNewsArticleRelativeLayout(commentToArticle);
        RelativeLayout visibleArticleSummaryWithImageRelativeLayout = getVisibleArticleSummaryWithImageRelativeLayout(
                commentedNewsArticleRelativeLayout, commentToArticle);

        return isImageWithSummaryPlacedInSpecifiedLayout(
                visibleArticleSummaryWithImageRelativeLayout, commentToArticle);
    }

    /**
     * Checks does article with specified comment or connection name
     * {@code TextView} has image displayed along with summary
     * 
     * @param commentToArticle
     *            target article comment as {@code TextView}
     * @return <b>true</b> if image with article summary exist, <b>false</b>
     *         otherwise
     */
    public boolean isArticleHasImageDisplayedAlongWithSummary(TextView commentToArticle) {

        Logger.i("Check there is image displayed along with summary for news article with comment: "
                + commentToArticle);

        if (commentToArticle == null)
            return false;

        RelativeLayout commentedNewsArticleRelativeLayout = getCommentedNewsArticleRelativeLayout(commentToArticle);
        String commentToArticleAsString = commentToArticle.getText().toString();
        RelativeLayout visibleArticleSummaryWithImageRelativeLayout = getVisibleArticleSummaryWithImageRelativeLayout(
                commentedNewsArticleRelativeLayout, commentToArticleAsString);

        return isImageWithSummaryPlacedInSpecifiedLayout(
                visibleArticleSummaryWithImageRelativeLayout, commentToArticleAsString);
    }

    /**
     * Gets commented news article relative layout
     * 
     * @param commentToArticle
     *            comment to target article
     * @return {@code RelativeLayout} news article layout
     */
    public RelativeLayout getCommentedNewsArticleRelativeLayout(String commentToArticle) {
        Logger.i("There is no article with following comment: " + commentToArticle, getSolo()
                .searchText(commentToArticle));

        TextView commentTextView = TextViewUtils.searchTextViewInLayout(commentToArticle, null,
                false);
        return getCommentedNewsArticleRelativeLayout(commentTextView);
    }

    /**
     * Gets commented news article relative layout
     * 
     * @param commentToArticle
     *            comment to target article
     * @return {@code RelativeLayout} news article layout
     */
    public RelativeLayout getCommentedNewsArticleRelativeLayout(TextView commentTextView) {
        if (null == commentTextView) {
            return null;
        }
        String commentToArticle = commentTextView.getText().toString();
        ViewParent commentTextViewParent = commentTextView.getParent();
        boolean isCommentTextViewParentRelativeLayout = commentTextViewParent instanceof RelativeLayout;
        Logger.i("Parent view of the comment: '" + commentToArticle + "' text view is null",
                isCommentTextViewParentRelativeLayout);
        if (!isCommentTextViewParentRelativeLayout) {
            return null;
        }

        RelativeLayout commentTextViewLayout = (RelativeLayout) commentTextViewParent;
        boolean iscommentTextViewLayoutVisible = commentTextViewLayout.getVisibility() == View.VISIBLE;
        Logger.i("RelativeLayout of the comment: '" + commentToArticle
                + "' text view is not visible", iscommentTextViewLayoutVisible);
        if (!iscommentTextViewLayoutVisible) {
            return null;
        }

        return commentTextViewLayout;
    }

    /**
     * Gets visible layout on which news article summary with image placed
     * 
     * @param commentedNewsArticleRelativeLayout
     *            commented news article layout
     * @param commentToArticle
     *            comment to target article
     * @return {@code RelativeLayout} layout on which news article summary with
     *         image placed
     */
    public RelativeLayout getVisibleArticleSummaryWithImageRelativeLayout(
            RelativeLayout commentedNewsArticleRelativeLayout, String commentToArticle) {
        final int imageDisplayedAlongWithSummaryLayoutIndex = 5;

        if (null == commentedNewsArticleRelativeLayout) {
            return null;
        }

        View articleSummaryWithImageView = commentedNewsArticleRelativeLayout
                .getChildAt(imageDisplayedAlongWithSummaryLayoutIndex);
        boolean isSummaryWithImagePlacedInRelativeLayout = articleSummaryWithImageView instanceof RelativeLayout;
        Logger.i(
                "There is no relative layout with news article summary and image for article with comment: "
                        + commentToArticle, isSummaryWithImagePlacedInRelativeLayout);

        if (!isSummaryWithImagePlacedInRelativeLayout) {
            return null;
        }

        RelativeLayout articleSummaryWithImageRelativeLayout = (RelativeLayout) articleSummaryWithImageView;
        boolean isLayoutVisible = articleSummaryWithImageRelativeLayout.getVisibility() == View.VISIBLE;
        Logger.i("Relative layout with news article summary and image for article with comment: '"
                + commentToArticle + "' is not visible", isLayoutVisible);

        if (!isLayoutVisible) {
            return null;
        }

        return articleSummaryWithImageRelativeLayout;
    }

    /**
     * Checks do image with summary placed on specified layout
     * 
     * @param layout
     *            layout on which article summary with image must be placed
     * @param commentToArticle
     *            comment to target article
     * @return <b>true</b> if image with summary placed on specified layout,
     *         <b>false</b> otherwise
     */
    public boolean isImageWithSummaryPlacedInSpecifiedLayout(RelativeLayout layout,
            String commentToArticle) {
        final int newsImageViewIndex = 0;
        final int newsArticleSummaryTextViewIndex = 1;

        if (null == layout) {
            return false;
        }

        View newsImageView = layout.getChildAt(newsImageViewIndex);
        View newsArticleSummaryTextView = layout.getChildAt(newsArticleSummaryTextViewIndex);

        boolean isNewsImageViewExist = (newsImageView instanceof ImageView)
                && newsImageView.isShown();
        boolean isNewsArticleSummaryTextViewExist = (newsArticleSummaryTextView instanceof TextView)
                && newsArticleSummaryTextView.isShown();

        Assert.assertTrue("There is no news article image for article with following comment: "
                + commentToArticle, isNewsImageViewExist);
        Assert.assertTrue("There is article summary text for article with following comment: "
                + commentToArticle, isNewsArticleSummaryTextViewExist);

        return isNewsImageViewExist && isNewsArticleSummaryTextViewExist;
    }

    /**
     * Gets {@code RelativeLayout} cell with new commented by specified comment.
     * 
     * @param comment
     *            is text of comment for search
     * @return {@code RelativeLayout} cell with new commented by specified
     *         comment, <b>null</b> if it is not exist.
     */
    public RelativeLayout getCommentedNewsCell(String comment) {
        // TODO: implement searching for comment for all page (implemented for
        // case: first cell with specified comment must be Commented News)
        RelativeLayout commentedNewsCell = getCommentedNewsArticleRelativeLayout(comment);

        if (commentedNewsCell != null) {
            return getVisibleArticleSummaryWithImageRelativeLayout(commentedNewsCell, comment);
        }

        return null;
    }

    /**
     * Gets parent {@code RelativeLayout} for main elements (news article label,
     * image, etc) of Updates list item with specified
     * {@code updatesListItemIndex}.
     * 
     * @param updatesListItemIndex
     *            Index of Updates list item to get the layout.
     * 
     * @return parent {@code RelativeLayout} for main elements (news article
     *         label, image, etc) of first Updates list item, or <b>null</b> if
     *         such layout doesn't exist.
     */
    public RelativeLayout getUpdatesListItemMainElementsParentLayout(int updatesListItemIndex) {
        ListView updatesList = getRecentUpdatesList();
        LinearLayout updatesListItem = ViewGroupUtils.getChildViewByIndexSafely(updatesList,
                updatesListItemIndex, LinearLayout.class);

        RelativeLayout sharedNewsArticleMainElementsParentLayout = getUpdatesListItemMainElementsParentLayout(updatesListItem);
        return sharedNewsArticleMainElementsParentLayout;
    }

    /**
     * Gets parent {@code RelativeLayout} for main elements (news article label,
     * image, etc) of Updates list item with specified {@code newsArticleLayout}
     * 
     * @param updatesListItemLayout
     *            {@code LinearLayout} of Updates list item
     * @return parent {@code RelativeLayout} for main elements (news article
     *         label, image, etc) of Updates list item with specified
     *         {@code newsArticleLayout} or <b>null</b> if there is no such
     *         {@code RelativeLayout}.
     */
    private RelativeLayout getUpdatesListItemMainElementsParentLayout(
            LinearLayout updatesListItemLayout) {
        final int firstRecentUpdatesListItemMainSectionIndex = 1;
        final int firstRecentUpdatesListItemMainSectionSecondDegreeIndex = 0;

        if (null == updatesListItemLayout) {
            return null;
        }

        RelativeLayout firstRecentUpdatesListItemMainSection = ViewGroupUtils
                .getChildViewByIndexSafely(updatesListItemLayout,
                        firstRecentUpdatesListItemMainSectionIndex, RelativeLayout.class);

        if (null == firstRecentUpdatesListItemMainSection) {
            return null;
        }

        RelativeLayout firstRecentUpdatesListItemMainSectionSecondDegree = ViewGroupUtils
                .getChildViewByIndexSafely(firstRecentUpdatesListItemMainSection,
                        firstRecentUpdatesListItemMainSectionSecondDegreeIndex,
                        RelativeLayout.class);

        return firstRecentUpdatesListItemMainSectionSecondDegree;
    }

    /**
     * Checks calendar is present in hero slot.
     * 
     * @return <b>true</b> if calendar is present in hero slot, else
     *         <b>false</b>.
     */
    @SuppressLint("DefaultLocale")
    public boolean isCalendarPresentInHeroSlot() {
        HardwareActions.scrollUp();

        List<View> viewsInCalendarLayout = LayoutUtils
                .getListOfViewsPlacedInLayout(LayoutUtils.CALENDAR_IN_HERO_SLOT_LAYOUT);

        for (View view : viewsInCalendarLayout) {
            if (view instanceof TextView) {
                String textOfView = ((TextView) view).getText().toString().toLowerCase();
                if ((textOfView.indexOf("no events") != -1)
                        || (textOfView.indexOf("january") != -1)
                        || (textOfView.indexOf("february") != -1)
                        || (textOfView.indexOf("march") != -1)
                        || (textOfView.indexOf("april") != -1) || (textOfView.indexOf("may") != -1)
                        || (textOfView.indexOf("june") != -1) || (textOfView.indexOf("july") != -1)
                        || (textOfView.indexOf("august") != -1)
                        || (textOfView.indexOf("september") != -1)
                        || (textOfView.indexOf("october") != -1)
                        || (textOfView.indexOf("november") != -1)
                        || (textOfView.indexOf("december") != -1)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Opens CALENDAR screen.
     * 
     * @return{@code CalendarScreen} with just opened 'CALENDAR' screen.
     */
    public ScreenCalendar openCalendarScreen() {
        // If we don't see 'CALENDAR' banner then scroll up.
        getSolo().scrollToTop();

        TextView calendarView = (TextView) Id.getViewByViewIdName(CALENDAR_VIEW);
        ViewUtils.tapOnView(calendarView, "Calendar", false);

        return new ScreenCalendar();
    }

    /**
     * Gets "Recent updates" {@code ListView}
     * 
     * @return "Recent updates" {@code ListView}
     */
    public ListView getRecentUpdatesList() {
        return ListViewUtils.getListViewFromCurrentScreenByIndex(RECENT_UPDATES_LIST_VIEW_INDEX);
    }

    /**
     * Verifies that "Recent updates" {@code ListView} is not empty
     * 
     * @return <b>true</b> if "Recent updates" {@code ListView} is not empty
     *         <b>false</b> otherwise
     */
    public boolean isRecentUpdatesListNotEmpty() {
        ListView recentUpdatesList = getRecentUpdatesList();
        return ListViewUtils.isListViewNotEmpty(recentUpdatesList);
    }

    /**
     * Taps on 'New Connection' roll up.
     */
    public void tapOnFirstNewConnectionsRollUp() {
        View rollupView = ViewUtils.scrollDownToViewById(NUS_ROLLUP_HEADER_VIEW,
                DEFAULT_SCROLLS_COUNT, new ViewChecker() {
                    @Override
                    public boolean check(View view) {
                        try {
                            return (((TextView) view).getText().toString()
                                    .indexOf("people have new connections") != -1);
                        } catch (ClassCastException e) {
                            return false;
                        } catch (NullPointerException e) {
                            return false;
                        }
                    }
                });

        ViewUtils.tapOnView(rollupView, "first connections roll up");
    }

    /**
     * Taps on 'Changed Jobs' roll up.
     */
    public void tapOnFirstNewJobsRollUp() {
        View rollupView = ViewUtils.scrollDownToViewById(NUS_ROLLUP_HEADER_VIEW,
                DEFAULT_SCROLLS_COUNT, new ViewChecker() {
                    @Override
                    public boolean check(View view) {
                        try {
                            return (((TextView) view).getText().toString()
                                    .indexOf("people changed jobs") != -1);
                        } catch (ClassCastException e) {
                            return false;
                        } catch (NullPointerException e) {
                            return false;
                        }
                    }
                });

        ViewUtils.tapOnView(rollupView, "first jobs roll up");
    }

    /**
     * Opens 'New Connections Roll Up' screen.
     * 
     * @return {@code ScreenNewConnectionsRollUp} with just opened 'New
     *         Connections Roll Up' screen.
     */
    public ScreenNewConnectionsRollUp openFirstNewConnectionsRollUp() {
        tapOnFirstNewConnectionsRollUp();
        // TODO describe/remove waitForScreenUpdate
        WaitActions.waitForScreenUpdate();
        return new ScreenNewConnectionsRollUp();
    }

    /**
     * Opens 'New Connections Roll Up' screen.
     * 
     * @return {@code ScreenNewConnectionsRollUp} with just opened 'New
     *         Connections Roll Up' screen.
     */
    public ScreenNewJobsRollUp openFirstNewJobsRollUp() {
        tapOnFirstNewJobsRollUp();

        return new ScreenNewJobsRollUp();
    }

    /**
     * Taps on 'Recommended job:' text.
     */
    public void tapOnFirstRecommendedJob() {
        Assert.assertNotNull("There is no text 'Recommended job:' on current screen", getSolo()
                .searchText("Recommended job:"));

        Logger.i("Tapping on Recommended job");
        getSolo().clickOnText("Recommended job:");
    }

    /**
     * Opens "Updated profile details" ("Profile roll up") screen of updated
     * profiles.
     * 
     * @return just opened {@code ScreenUpdatedProfileDetails} screen.
     */
    public ScreenUpdatedProfileRollup openFirstUpdateProfileRollUpScreen() {
        tapOnFirstUpdateProfileRollUp();
        return new ScreenUpdatedProfileRollup();
    }

    /**
     * Taps on Profile roll up.
     */
    public void tapOnFirstUpdateProfileRollUp() {
        final String profileRollUpText = "people have updated their profile";
        final int minimumNumberOfMatches = 1;

        Assert.assertNotNull("Update Profile rollup NUS is not presented",
                getSolo().searchText(profileRollUpText, minimumNumberOfMatches, true, true));

        Logger.i("Tapping on Update Profile roll up");
        getSolo().clickOnText(profileRollUpText);
    }

    /**
     * Opens first Job Details screen.
     * 
     * @return ScreenJobDetails.
     */
    public ScreenJobDetails openFirstJobDetailsScreen() {
        tapOnFirstRecommendedJob();
        return new ScreenJobDetails();
    }

    /**
     * Returns {@code LinearLayout} of news article shared by current user
     * without comment that has specified summary ({@code newsArticleHeader})
     * and image displayed along with summary.
     * 
     * @param newsArticleHeader
     *            required news article header.
     * @return {@code LinearLayout} of first news article shared by current user
     *         without comment or <b>null</b> if there is no such article.
     */
    public LinearLayout getFirstNewsArticleSharedByCurrentUserWithoutComment(
            String newsArticleHeader) {

        if (null == newsArticleHeader) {
            return null;
        }

        ArrayList<TextView> nonNewsArticleSummarySharedByCurrentUserWithoutCommentTextViews = new ArrayList<TextView>();
        TextView newsArticleSummarySharedByCurrentUserWithoutCommentTextViews = null;

        do {
            newsArticleSummarySharedByCurrentUserWithoutCommentTextViews = TextViewUtils
                    .searchTextViewInActivity(
                            NEWS_ARTICLE_SUMMARY_SHARED_BY_CURRENT_USER_WITHOUT_COMMENT, true);
            LinearLayout firstNewsArticleSharedByCurrentUserWithoutComment = getNewsArticleSharedByCurrentUserWithoutComment(
                    newsArticleSummarySharedByCurrentUserWithoutCommentTextViews, newsArticleHeader);
            if (null != firstNewsArticleSharedByCurrentUserWithoutComment) {
                return firstNewsArticleSharedByCurrentUserWithoutComment;
            }
            nonNewsArticleSummarySharedByCurrentUserWithoutCommentTextViews
                    .add(newsArticleSummarySharedByCurrentUserWithoutCommentTextViews);
        } while (null != newsArticleSummarySharedByCurrentUserWithoutCommentTextViews);
        return null;
    }

    /**
     * Returns {@code LinearLayout} of news article shared by current user
     * without comment that has specified summary ({@code newsArticleHeader})
     * and image displayed along with summary.
     * 
     * @param textView
     *            summary of news article shared by current user without
     *            comment.
     * @param newsArticleHeader
     *            required news article header.
     * 
     * @return @code LinearLayout} of news article shared by current user
     *         without comment or <b>null</b> if {@code textView} is not summary
     *         of the news article.
     */
    private LinearLayout getNewsArticleSharedByCurrentUserWithoutComment(TextView textView,
            String newsArticleHeader) {
        final int imageDisplayedAlongWithSummaryIndex = 0;
        final int newsArticleSummarySharedByCurrentUserWithoutCommentIndex = 1;
        final int newsArticleHeaderSharedByCurrentUserWithoutCommentIndex = 4;

        if (null == textView) {
            return null;
        }

        ViewParent textViewParent = textView.getParent();
        if (!(textViewParent instanceof RelativeLayout)) {
            return null;
        }

        RelativeLayout textViewParentLayout = (RelativeLayout) textViewParent;
        TextView newsArticleSummarySharedByCurrentUserWithoutComment = ViewGroupUtils
                .getChildViewByIndexSafely(textViewParentLayout,
                        newsArticleSummarySharedByCurrentUserWithoutCommentIndex, TextView.class);
        if (textView != newsArticleSummarySharedByCurrentUserWithoutComment) {
            return null;
        }

        // Verify if image displayed along with summary.
        ImageView imageDisplayedAlongWithSummary = ViewGroupUtils.getChildViewByIndexSafely(
                textViewParentLayout, imageDisplayedAlongWithSummaryIndex, ImageView.class);

        if (null == imageDisplayedAlongWithSummary) {
            return null;
        }

        ViewParent textViewParent2Degree = textViewParentLayout.getParent();
        if (!(textViewParent2Degree instanceof RelativeLayout)) {
            return null;
        }

        // Verify if 2 degree parent layout contains news article header
        RelativeLayout textViewParentLayout2Degree = (RelativeLayout) textViewParent2Degree;
        TextView newsArticleHeaderSharedByCurrentUserWithoutComment = ViewGroupUtils
                .getChildViewByIndexSafely(textViewParentLayout2Degree,
                        newsArticleHeaderSharedByCurrentUserWithoutCommentIndex, TextView.class);

        if (null == newsArticleHeaderSharedByCurrentUserWithoutComment) {
            return null;
        }

        // Verify if retrieved news article header equals to newsArticleHeader
        String retrievedNewsArticleHeader = newsArticleHeaderSharedByCurrentUserWithoutComment
                .getText().toString();

        if (!retrievedNewsArticleHeader.equals(newsArticleHeader)) {
            return null;
        }

        ViewParent textViewParent3Degree = textViewParent2Degree.getParent();
        if (!(textViewParent3Degree instanceof RelativeLayout)) {
            return null;
        }

        ViewParent textViewParent4Degree = textViewParent3Degree.getParent();
        if (!(textViewParent4Degree instanceof LinearLayout)) {
            return null;
        }

        ViewParent updatesList = textViewParent4Degree.getParent();
        if (!(updatesList instanceof ListView)) {
            return null;
        }
        return (LinearLayout) textViewParent4Degree;
    }

    /**
     * Returns total count {@code int} of updates items.
     */
    public int getUpdatesCount() {
        return getSolo().getCurrentListViews().get(0).getCount();
    }

    /**
     * Scroll down, wait for new updates are loaded.
     */
    public void scrollDownLoadMoreUpdates() {
        getSolo().scrollToBottom();

        // APP BUG: spinner doesn't present sometimes.
        // Check that you see spinner.
        Assert.assertTrue("Spinner is not present.",
                getSolo().waitForView(ProgressBar.class, 1, DataProvider.WAIT_DELAY_SHORT, false));

        // Wait when the spinner will disappear.
        long timeout = System.currentTimeMillis() + DataProvider.WAIT_DELAY_DEFAULT;
        while (getSolo().waitForView(ProgressBar.class, 1, DataProvider.DEFAULT_DELAY_TIME, false)) {
            Assert.assertTrue("Timeout. Spinner didn't disapear.",
                    timeout > System.currentTimeMillis());
        }
    }

    /**
     * Verifies NUS screen opened instead of Updates screen.
     */
    public static void verifyNUSSscreen() {
        WaitActions.waitForTrueInFunction("NUS screen is not present(title not shown)",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return ((View) Id.getViewByViewIdName(TITLE_VIEW)).isShown();
                    }
                });
    }

    /**
     * Opens first news specified by number of comments and likes.
     * 
     * @param countOfComments
     *            - minimal count of comments to news.
     * @param countOfLikes
     *            - minimal count of likes to news.
     * @return {@code ScreenUpdate} with just opened news specified by number of
     *         comments and likes.
     */
    public ScreenUpdate openNews(int countOfComments, int countOfLikes) {
        String errorString = "news with more than " + countOfComments + " comments and "
                + countOfLikes + " likes";

        RelativeLayout newsCell = getNewsCell(countOfComments, countOfLikes);
        Assert.assertNotNull("Can't find " + errorString, newsCell);

        ViewGroupUtils.tapFirstViewInLayout(newsCell, true, errorString, null);

        return new ScreenUpdate();
    }

    /**
     * Returns {@code RelativeLayout} of news specified by number of comments
     * and likes.
     * 
     * @param countOfComments
     *            - minimal count of comments to news.
     * @param countOfLikes
     *            - minimal count of likes to news.
     * @return {@code RelativeLayout} of news specified by number of comments
     *         and likes, or <b>null</b> if this news was not found.
     */
    public RelativeLayout getNewsCell(int countOfComments, int countOfLikes) {
        return getNewsCell(countOfComments, countOfLikes, DEFAULT_SCROLLS_COUNT);
    }

    /**
     * Returns {@code RelativeLayout} of news specified by number of comments
     * and likes.
     * 
     * @param countOfComments
     *            - minimal count of comments to news.
     * @param countOfLikes
     *            - minimal count of likes to news.
     * @param maxCountOfScrolls
     *            - max count of scrolls to find to news.
     * @return {@code RelativeLayout} of news specified by number of comments
     *         and likes, or <b>null</b> if this news was not found.
     */
    public RelativeLayout getNewsCell(int countOfComments, int countOfLikes, int maxCountOfScrolls) {

        for (int i = 0; i < maxCountOfScrolls; i++) {
            ArrayList<View> likesCommentsLayouts = Id
                    .getListOfViewByViewIdName(COMMENTS_AND_LIKES_LAYOUT);
            for (View layout : likesCommentsLayouts) {
                if (layout.isShown()) {
                    RelativeLayout relativeLayout = (RelativeLayout) layout;
                    int countOfCommentsInCurrentNews = 0;
                    int countOfLikesInCurrentNews = 0;
                    for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                        TextView childView = (TextView) relativeLayout.getChildAt(j);
                        String text = childView.getText().toString();
                        if (text.indexOf(COMMENTS_LABEL) != -1) {
                            countOfCommentsInCurrentNews = StringUtils.getNumberFromString(text);
                        }
                        if (text.indexOf(LIKES_LABEL) != -1) {
                            countOfLikesInCurrentNews = StringUtils.getNumberFromString(text);
                        }
                    }
                    if ((countOfCommentsInCurrentNews >= countOfComments)
                            && (countOfLikesInCurrentNews >= countOfLikes)) {
                        try {
                            return (RelativeLayout) relativeLayout.getParent().getParent();
                        } catch (ClassCastException e) {
                        }
                    }
                }
            }

            getSolo().drag(0, 0, (int) (ScreenResolution.getScreenHeight() * 0.75),
                    (int) (ScreenResolution.getScreenHeight() * 0.25), 10);
            // Wait for animation finished.
            WaitActions.waitForScreenUpdate();
        }

        return null;
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_updates")
    public static void go_to_updates(String email, String password) {
        LoginActions.logout();
        ScreenLogin loginScreen = new ScreenLogin();

        // Type credentials and tap 'Sign In'.
        loginScreen.typeEmail(email);
        loginScreen.typePassword(password);
        loginScreen.tapOnSignInButton();

        // Tap 'Do not sync'.
        loginScreen.handleSyncContactsDialog(PopupSyncContacts.DO_NOT_SYNC_TEXT);

        // Verify Calendar splash screen.
        new ScreenCalSplash();

        updates("go_to_updates");
    }

    @TestAction(value = "updates")
    public static void updates() {
        updates("updates");
    }

    public static void updates(String screenshotName) {
        ScreenCalSplash screenCalSplash = new ScreenCalSplash();

        screenCalSplash.tapAddCalendarButton();
        screenCalSplash.handleInButtonHint();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "updates_tap_expose")
    public static void updates_tap_expose() {
        new ScreenUpdates().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("updates_tap_expose");
    }

    @TestAction(value = "updates_tap_expose_reset")
    public static void updates_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_expose_reset");
    }

    @TestAction(value = "updates_tap_search")
    public static void updates_tap_search() {
        new ScreenUpdates().openSearchScreen();

        TestUtils.delayAndCaptureScreenshot("updates_tap_search");
    }

    @TestAction(value = "updates_tap_search_reset")
    public static void updates_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_search_reset");
    }

    @TestAction(value = "updates_tap_share")
    public static void updates_tap_share() {
        new ScreenUpdates().openShareUpdateScreen();

        TestUtils.delayAndCaptureScreenshot("updates_tap_share");
    }

    @TestAction(value = "updates_tap_share_reset")
    public static void updates_tap_share_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_share_reset");
    }

    @TestAction(value = "updates_tap_news")
    public static void updates_tap_news() {
        new ScreenUpdates().openLinkedInTodayScreen();

        TestUtils.delayAndCaptureScreenshot("updates_tap_news");
    }

    @TestAction(value = "updates_tap_news_reset")
    public static void updates_tap_news_reset() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_news_reset");
    }

    @TestAction(value = "updates_tap_cal")
    public static void updates_tap_cal() {
        new ScreenUpdates().openCalendarScreen();

        TestUtils.delayAndCaptureScreenshot("updates_tap_cal");
    }

    @TestAction(value = "updates_tap_cal_reset")
    public static void updates_tap_cal_reset() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_cal_reset");
    }

    @TestAction(value = "updates_pull_refresh")
    public static void updates_pull_refresh() {
        new ScreenUpdates().refreshScreen();

        TestUtils.delayAndCaptureScreenshot("updates_pull_refresh");
    }

    @TestAction(value = "updates_scroll_load_more")
    public static void updates_scroll_load_more() {
        ListView views = ListViewUtils.getFirstListView();
        Assert.assertNotNull("There is no ListView on screen or it's null", views);
        final int countOfUpdates = views.getCount();
        HardwareActions.scrollToBottomNTimes(1, 0);
        WaitActions.waitForProgressBarDisappear();
        WaitActions.waitForTrueInFunction("New updates are not loaded", new Callable<Boolean>() {
            public Boolean call() {
                ListView newViews = ListViewUtils.getFirstListView();
                if (newViews != null)
                    return (newViews.getCount() > countOfUpdates);
                else
                    return false;
            }
        });

        TestUtils.delayAndCaptureScreenshot("updates_scroll_load_more");
    }

    @TestAction(value = "updates_scroll_load_more_reset")
    public static void updates_scroll_load_more_reset() {
        HardwareActions.scrollToTop();

        TestUtils.delayAndCaptureScreenshot("updates_scroll_load_more_reset");
    }

    public static void updates_tap_update(String screenshotName) {
        RelativeLayout nusView = (RelativeLayout) ViewUtils.scrollToViewById(NUS_LAYOUT,
                ViewUtils.SCROLL_DOWN, DEFAULT_SCROLLS_COUNT);
        Assert.assertNotNull("No updates found on Updates screen", nusView);

        ViewGroupUtils.tapFirstViewInLayout(nusView, true, "first update", null);

        String[] activities = new String[] {ScreenFeedDetail.ACTIVITY_SHORT_CLASSNAME, ScreenCompanies.ACTIVITY_SHORT_CLASSNAME};
        WaitActions.waitMultiplyActivities(activities);
        if (getSolo().getCurrentActivity().getClass().getSimpleName().equals(activities[0])){
            new ScreenFeedDetail();
        } else {
            new ScreenCompanies();
        }
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "updates_tap_update")
    public static void updates_tap_update() {
        updates_tap_update("updates_tap_update");
    }

    @TestAction(value = "updates_tap_update_reset")
    public static void updates_tap_update_reset() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("updates_tap_update_reset");
    }
}
