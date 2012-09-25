package com.linkedin.android.screens.updates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenJobDetails;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.common.ScreenUpdatedProfileDetails;
import com.linkedin.android.screens.more.ScreenDiscussionDetailsFromRecentUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.StringDefaultValues;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Updates screen.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 10:55:40 AM
 */
public class ScreenUpdates extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.NUSListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NUSListActivity";
    public static final String HELPACTIVITY_CLASSNAME = "com.linkedin.android.help.HelpActivity";
    public static final String HELPACTIVITY_SHORT_CLASSNAME = "HelpActivity";

    public static final String LINKEDIN_TODAY_LABEL = "LINKEDIN TODAY";

    public static final String NEWS_ARTICLE_SUMMARY_SHARED_BY_CURRENT_USER_WITHOUT_COMMENT = "You shared this";

    public static final int RECENT_UPDATES_LIST_VIEW_INDEX = 0;

    static final Rect SHARE_BUTTON_RECT = new Rect(398, 42, 82, 74);
    // ImageButton: like it button
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");

    // Container for ViralUpdate
    private static final ViewIdName NUS_SIMPLE_RICH_ROW = new ViewIdName("nus_simple_rich_row");
    // Updates list
    private static final ViewIdName UPDATES_LIST = new ViewIdName("list");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenUpdates() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activity.
        Assert.assertTrue(
                "Wrong activity (expected " + ACTIVITY_SHORT_CLASSNAME + " or "
                        + HELPACTIVITY_SHORT_CLASSNAME,
                getSolo().getCurrentActivity().getClass().getSimpleName()
                        .equals(ACTIVITY_SHORT_CLASSNAME)
                        || getSolo().getCurrentActivity().getClass().getSimpleName()
                                .equals(HELPACTIVITY_SHORT_CLASSNAME));

        // Verify LINKEDIN TODAY banner present
        getLinkedInTodayBannerText();

        // Verify presence IN Button.
        verifyINButton();

        // Verify presence Share button.
        verifyRightButtonInNavigationBar("Share");

        // Verify presence search bar.
        verifySearchBar();

        // Check that list views is not empty.
        Assert.assertTrue("List views is not present", !getSolo().getCurrentListViews().isEmpty());

        HardwareActions.takeCurrentActivityScreenshot("Updates screen");
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
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        View button = getRightButtonInNavigationBar();
        Assert.assertNotNull("'Share button' is not present.", button);

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnView(button);
    }

    /**
     * Opens 'Share Update' screen.
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
    public ScreenSharedNewsDetails openAnyUpdateWithLinkSharedByYourConnection() {
        ArrayList<TextView> exceptions = new ArrayList<TextView>(); 
        do {
            TextView link = ViewUtils.searchTextViewInActivity(".com", exceptions, false);
            if (link == null) {
                if (getSolo().scrollDown())
                    continue;
                else
                    // we have reached bottom of page
                    return null;
            }
            int textColor = link.getCurrentTextColor();
            if (link.getText().toString().startsWith("http://") == false &&
                    link.isShown() &&
                    textColor == 0xff999490) {
                getSolo().clickOnView(link);
                if (isSharedNewsDetailsScreen(3))
                    return new ScreenSharedNewsDetails();
            }
            exceptions.add(link);
        } while (true);
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
        final String groupLabelFromGroupPost = "Group:";

        ArrayList<TextView> nonNameOfGroupTextViews = new ArrayList<TextView>();
        TextView nameOfGroupFromFirstGroupPost = null;
        do {
            getSolo().searchText(groupLabelFromGroupPost);
            nameOfGroupFromFirstGroupPost = ViewUtils.searchTextViewInActivity(
                    groupLabelFromGroupPost, nonNameOfGroupTextViews, false);
            if (isNameOfGroupFromGroupPost(nameOfGroupFromFirstGroupPost)) {
                return nameOfGroupFromFirstGroupPost;
            }
            nonNameOfGroupTextViews.add(nameOfGroupFromFirstGroupPost);
        } while (null != nameOfGroupFromFirstGroupPost);

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
    private boolean isNameOfGroupFromGroupPost(TextView textView) {
        final int nameOfGroupFromGroupPostIndex = 1;

        if (null == textView) {
            return false;
        }

        ViewParent textViewParent = textView.getParent();
        if (!(textViewParent instanceof RelativeLayout)) {
            return false;
        }
        RelativeLayout textViewParentLayout = (RelativeLayout) textViewParent;
        TextView groupName = ViewGroupUtils.getChildViewByIndexSafely(textViewParentLayout,
                nameOfGroupFromGroupPostIndex, TextView.class);
        if (textView != groupName) {
            return false;
        }

        ViewParent textViewParent2Degree = textViewParentLayout.getParent();
        if (!(textViewParent2Degree instanceof RelativeLayout)) {
            return false;
        }

        ViewParent textViewParent3Degree = textViewParent2Degree.getParent();
        if (!(textViewParent3Degree instanceof RelativeLayout)) {
            return false;
        }

        ViewParent textViewParent4Degree = textViewParent3Degree.getParent();
        if (!(textViewParent4Degree instanceof LinearLayout)) {
            return false;
        }

        View recentUpdatesList = (View) textViewParent4Degree.getParent();
        return recentUpdatesList instanceof ListView;
    }

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
        HardwareActions.delay(waiting_time);
        if (getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ScreenSharedNewsDetails.ACTIVITY_SHORT_CLASSNAME)) {
            ImageButton likeButton = (ImageButton) Id.getViewByName(LIKE_BUTTON);
            if (likeButton.isShown()) {
                ScreenSharedNewsDetails newsDetails = new ScreenSharedNewsDetails();
                int nr = newsDetails.tapOnForwardButton();
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
            HardwareActions.delay(waiting_time);
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
    public ScreenSharedNewsDetails openAnySharedNewsArticleDetailsScreen(
            int optionsCountInSharePopup) {
        do {
            getSolo().clickOnScreen(ScreenResolution.getScreenWidth() / 2,
                    ScreenResolution.getScreenHeight() / 2);
            if (isSharedNewsDetailsScreen(optionsCountInSharePopup))
                return new ScreenSharedNewsDetails();

            getSolo().clickOnScreen(ScreenResolution.getScreenWidth() / 2,
                    ScreenResolution.getScreenHeight() * 9 / 10);
            if (isSharedNewsDetailsScreen(optionsCountInSharePopup))
                return new ScreenSharedNewsDetails();
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
            updateSharedByCurrentUser = ViewUtils.searchTextViewInActivity(
                    currentUserShortenedName, nonNewsUpdatesSharedByCurrentUser, false);
            if (isArticleHasImageDisplayedAlongWithSummary(updateSharedByCurrentUser)) {
                return updateSharedByCurrentUser;
            }
            nonNewsUpdatesSharedByCurrentUser.add(updateSharedByCurrentUser);
        } while (null != updateSharedByCurrentUser);
        return null;
    }

    /**
     * Opens first news shared by connection.
     * 
     * @param connectionName
     *            is name of connection who shared news article *
     * @return {@code ScreenSharedNewsDetails} with just opened 'Shared News
     *         Article Details' screen.
     */
    public ScreenSharedNewsDetails openFirstNewsArticleSharedByYourConnection() {

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

        return new ScreenSharedNewsDetails();
    }

    /**
     * Opens first Viral Update (in first NUS_SIMPLE_RICH_ROW).
     * 
     * @return {@code ScreenViralUpdate} object.
     */
    public ScreenViralUpdate openFirstViralUpdate() {
        // RegExp for "Like" or "Comment".
        final Pattern pattern = Pattern.compile(".*Like.*|.*Comment.*");
        // Flag that it row contain label with company name.
        boolean isRowWithCompany;

        // Get list with NUS.
        ListView listView = getSolo().getCurrentListViews().get(0);
        
        // Scroll list to end or MAX_SCROLLS times.
        final int MAX_SCROLLS = 4;
        for (int j = 0; getSolo().scrollDownList(0) && j < MAX_SCROLLS; j++) {
            
            HardwareActions.delay(5);// Wait for ListView load more rows.
            
            Logger.d("Scrollllllllllllllllllllllllllllllll");
            
            /*int i = 0;
            for (View view : getSolo().getViews(listView)) {
                if (view.getClass().getSimpleName().equals("TextView")){
                    if (((TextView)view).getText().length() > 0){
                        Logger.d(i + ": " + ((TextView)view).getText());
                    }
                    i++;
                }
            }*/
            isRowWithCompany = false;
            for (View view : getSolo().getViews(listView)) {
                if (view.getClass().getSimpleName().equals("TextView")){
                    String string = ((TextView)view).getText().toString();
                    if (string.length() > 0){
                        if (string.equals(StringData.test_own_company)) {
                            isRowWithCompany = true;
                        } else if (isRowWithCompany){
                            Matcher matcher = pattern.matcher(string);
                            if (matcher.find()) {
                                // If "Like|Comment" found then it is ViralUpdate.
                                
                                Logger.d("String from ViralUpdate=" + string);
                                isRowWithCompany = false;
                            }
                        }
                    }
                }
            }
        }

        Logger.logElements(false, false);

        return /*new ScreenViralUpdate()*/null;
    }

    /**
     * Opens LINKEDIN TODAY screen.
     * 
     * @return{@code LinkedInTodayScreen} with just opened 'LINKEDIN TODAY'
     *               screen.
     */
    public ScreenLinkedInToday openLinkedInTodayScreen() {
        String bannerText = getLinkedInTodayBannerText();

        Logger.i("Tapping on 'LINKEDIN TODAY' banner");
        getSolo().clickOnText(bannerText);

        return new ScreenLinkedInToday();
    }

    /**
     * Opens SEARCH screen.
     * 
     * @return {@code SearchScreen} with just opened 'SEARCH' screen.
     */
    public ScreenSearch openSearchScreen() {
        EditText searchBar = getSearchBar();
        getSolo().clickOnView(searchBar);
        Logger.i("Tapping on 'Search bar'");
        ScreenSearch searchScreen = new ScreenSearch();
        return searchScreen;
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

        Logger.i("Check there is image displayed along with summary for news article with comment: "
                + commentToArticle);

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

        TextView commentTextView = ViewUtils.searchTextViewInLayout(commentToArticle, null, false);
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
                && newsImageView.getVisibility() == View.VISIBLE;
        boolean isNewsArticleSummaryTextViewExist = (newsArticleSummaryTextView instanceof TextView)
                && newsArticleSummaryTextView.getVisibility() == View.VISIBLE;

        Logger.i("There is no news article image for article with following comment: "
                + commentToArticle, isNewsImageViewExist);
        Logger.i("There is article summary text for article with following comment: "
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
     * Gets first {@code RelativeLayout} NUS cell on screen.
     * 
     * @return {@code RelativeLayout} NUS cell on screen, <b>null</b> if it is
     *         not exist.
     */
    public RelativeLayout getFirstCell() {
        TextView timeAgoTextView = null;
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            String textOfView = view.getText().toString();
            if ((textOfView.indexOf("hours ago") != -1) || (textOfView.indexOf("hour ago") != -1)
                    || (textOfView.indexOf("mins ago") != -1)
                    || (textOfView.indexOf("min ago") != -1)
                    || (textOfView.indexOf("secs ago") != -1)
                    || (textOfView.indexOf("sec ago") != -1)
                    || (textOfView.indexOf("days ago") != -1)
                    || (textOfView.indexOf("day ago") != -1)
                    || (textOfView.indexOf("months ago") != -1)
                    || (textOfView.indexOf("month ago") != -1)) {
                timeAgoTextView = view;
                break;
            }
        }

        if (timeAgoTextView.getParent() instanceof RelativeLayout) {
            return (RelativeLayout) (timeAgoTextView.getParent());
        }

        return null;
    }

    /**
     * Checks calendar is present in hero slot.
     * 
     * @return <b>true</b> if calendar is present in hero slot, else
     *         <b>false</b>.
     */
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
        HardwareActions.scrollUp();

        Assert.assertTrue("'CALENDAR' is disabled.", isCalendarPresentInHeroSlot());

        return ScreenCalendar.tapOnCalendarBigImage();
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
     * Gets LINKEDIN TODAY banner
     * 
     * @return LINKEDIN TODAY {@code String} banner
     */
    private String getLinkedInTodayBannerText() {
        final String NEWS_LABEL = "News";

        String bannerText = "";

        // If we don't see 'LINKEDIN TODAY' banner then scroll up.
        HardwareActions.scrollUp();

        boolean isBannerPresent = getSolo().waitForText(LINKEDIN_TODAY_LABEL, 1,
                DataProvider.WAIT_DELAY_DEFAULT, false, false);

        if (isBannerPresent) {
            bannerText = LINKEDIN_TODAY_LABEL;
            return bannerText;
        }

        // If 'LINKEDIN TODAY' banner is not present then search for the 'News'
        // banner.
        isBannerPresent = getSolo().getText(NEWS_LABEL).isShown();
        if (isBannerPresent) {
            bannerText = NEWS_LABEL;
        }

        Assert.assertTrue("'LINKEDIN TODAY' banner is not present.", isBannerPresent);

        return bannerText;
    }

    /**
     * Taps on 'New Connection' roll up.
     */
    public void tapOnNewConnectionsRollUp() {
        Logger.i("Searching new connections roll up...");
        Assert.assertTrue("Roll up not found",
                getSolo().searchText("people have new connections", 1, true, false));

        Logger.i("Tapping on new connections roll up");
        getSolo().clickOnText("people have new connections");
    }

    /**
     * Opens 'New Connections Roll Up' screen.
     * 
     * @return {@code ScreenNewConnectionsRollUp} with just opened 'New
     *         Connections Roll Up' screen.
     */
    public ScreenNewConnectionsRollUp openNewConnectionsRollUpScreen() {
        tapOnNewConnectionsRollUp();
        HardwareActions.waitForScreenUpdate();
        return new ScreenNewConnectionsRollUp();
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
    public ScreenUpdatedProfileDetails openProfileRollUpScreen() {
        tapOnProfileRollUp();

        ScreenUpdatedProfileDetails screenUpdatedProfileDetails = new ScreenUpdatedProfileDetails();
        return screenUpdatedProfileDetails;
    }

    /**
     * Taps on Profile roll up.
     */
    public void tapOnProfileRollUp() {
        final String profileRollUpText = "people have updated their profile";
        final int minimumNumberOfMatches = 1;

        Assert.assertNotNull("There is no text '" + profileRollUpText + "' on current screen",
                getSolo().searchText(profileRollUpText, minimumNumberOfMatches, true, true));

        Logger.i("Tapping on Profile roll up");
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
            newsArticleSummarySharedByCurrentUserWithoutCommentTextViews = ViewUtils
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

}