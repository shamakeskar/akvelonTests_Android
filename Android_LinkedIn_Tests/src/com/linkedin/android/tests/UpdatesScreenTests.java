package com.linkedin.android.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.test.suitebuilder.annotation.Suppress;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.popups.Popup;
import com.linkedin.android.screens.ScreenLogin;
import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.common.ScreenUpdatedProfileDetails;
import com.linkedin.android.screens.more.ScreenCommentDiscussion;
import com.linkedin.android.screens.more.ScreenDiscussionDetailsFromRecentUpdates;
import com.linkedin.android.screens.updates.ScreenAddComment;
import com.linkedin.android.screens.updates.ScreenLinkedInToday;
import com.linkedin.android.screens.updates.ScreenNewConnectionDetails;
import com.linkedin.android.screens.updates.ScreenNewConnectionsRollUp;
import com.linkedin.android.screens.updates.ScreenNewsArticleDetails;
import com.linkedin.android.screens.updates.ScreenNewsArticleDetailsFromLinkedInToday;
import com.linkedin.android.screens.updates.ScreenShareNewsArticle;
import com.linkedin.android.screens.updates.ScreenShareUpdate;
import com.linkedin.android.screens.updates.ScreenSharedNewsDetails;
import com.linkedin.android.screens.updates.ScreenSingleUpdatedProfileDetails;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.utils.ContactInfoUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Tests for Updates screen.
 * 
 * @author dmitry.somov
 * @created Aug 10, 2012 6:29:03 PM
 */
public class UpdatesScreenTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public UpdatesScreenTests() {
        super(UpdatesScreenTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 33848187: 'Recent updates. Load more.'
     * 
     * Open Updates screen. Fast scroll down. Verify that spinner is present and
     * new data is shown. Fast scroll down. Verify that spinner is present and
     * new data is shown.
     */
    public void test33848187() {
        startFixture("33848187");
        Logger.i(START_TEST + "33848187: 'Recent updates. Load more.'");

        // Open Updates screen.
        LoginActions.openUpdatesScreenOnStart();

        // Scroll up if we don't see 'LINKEDIN TODAY' banner.
        HardwareActions.scrollUp();

        // Save default count of list views.
        int listViewsCount = getSolo().getCurrentListViews().get(0).getCount();

        // Scroll down.
        Logger.i("Fast scroll screen down");
        getSolo().scrollToBottom();

        // Check that you see spinner.
        Assert.assertTrue("Spinner is not present.",
                getSolo().waitForView(ProgressBar.class, 1, DataProvider.DEFAULT_DELAY_TIME, false));
        Logger.i(DONE + "Check that spinner is present.");

        // Wait when the spinner will disappear.
        WaitActions.waitForProgressBarDisappear();

        // Verify that more data is loaded.
        Assert.assertTrue("After scroll more data is not loaded.", listViewsCount < getSolo()
                .getCurrentListViews().get(0).getCount());
        Logger.i(DONE + "Verify new data is shown after scroll down.");

        // Update count of list views.
        listViewsCount = getSolo().getCurrentListViews().get(0).getCount();

        // Scroll down.
        Logger.i("Fast scroll screen down.");
        getSolo().scrollToBottom();

        // Check that you see spinner.
        Assert.assertTrue("Spinner is not present.",
                getSolo().waitForView(ProgressBar.class, 1, DataProvider.DEFAULT_DELAY_TIME, false));
        Logger.i(DONE + "Check that spinner is present.");

        // Wait when the spinner will disappear.
        WaitActions.waitForProgressBarDisappear();

        // Verify that more data is loaded.
        Assert.assertTrue("After scroll more data is not loaded.", listViewsCount < getSolo()
                .getCurrentListViews().get(0).getCount());
        Logger.i(DONE + "Verify new data is shown after scroll down.");

        Logger.i(PASS + "33848187");
    }

    /**
     * Test case 34066559: 'Add comment to news article'
     * 
     * Open Updates screen. Select news article from list. Click 'Forward'
     * button. Type some comment. Click 'Share'.
     * 
     * Verify: - 'Sending update' toast is shown. - 'Update sent' toast is
     * shown. - the article is shared (shown in updates with the comments). -
     * the article has image displayed along with summary.
     */
    public void test34066559() {
        startFixture("34066559");
        Logger.i(START_TEST + "34066559: 'Add comment to news article from Recent updates.'");

        // Login and open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open first news article from 'Recent updates' screen.
        ScreenNewsArticleDetails newsArticleDetailsScreen = screenUpdates
                .openFirstNewsArticleDetailsScreen();

        // Click on Forward button.
        ScreenShareNewsArticle shareNewsArticleScreen = newsArticleDetailsScreen
                .openShareNewsArticleScreen();

        // Generate random string of max Comment field value length.
        String randomComment = StringUtils
                .generateStringRandomly(ScreenShareNewsArticle.ADD_COMMENT_TEXT_FIELD_DEFAULT_VALUE_LENGTH);

        // Type generated string to Comment field.
        shareNewsArticleScreen.typeInComment(randomComment);

        // Click Share button.
        newsArticleDetailsScreen = shareNewsArticleScreen.shareComment();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Open Expose screen
        ScreenExpose screenExpose = newsArticleDetailsScreen.openExposeScreen();
        // Go to Updates screen.
        screenUpdates = screenExpose.openUpdatesScreen();

        // Scroll to the top of Update screen.
        HardwareActions.scrollUp();

        // Check if update screen contains article with specified text.
        boolean isUpdateScreenContainArticleWithSpecifiedText = screenUpdates
                .isUpdateScreenContainArticleWithSpecifiedText(randomComment);
        Assert.assertTrue("Test comment to news article was not shared.",
                isUpdateScreenContainArticleWithSpecifiedText);
        Logger.i(DONE
                + "Verify that the article is shared and is shown in Updates screen with the comment.");

        // Verify that image displayed along with summary for article with
        // comment.
        boolean isArticleHasImageDisplayedAlongWithSummary = screenUpdates
                .isArticleHasImageDisplayedAlongWithSummary(randomComment);
        Assert.assertTrue(
                "There is no image displayed along with summary for article with comment: "
                        + randomComment, isArticleHasImageDisplayedAlongWithSummary);
        Logger.i(DONE + "Verify that image displayed along with summary for article with comment.");

        Logger.i(PASS + "34066559");
    }

    /**
     * Test case 34066475: 'Share the News article from NUS'
     * 
     * Open Updates screen. Open first News Article Details screen. Tap on
     * Forward button. Verify 'Share News Article' screen. Tap on Share button.
     * Open Updates screen. Verify that the article is shown in Updates screen.
     * Verify that the article has image displayed along with summary.
     */
    public void test34066475() {
        startFixture("34066475");
        Logger.i(START_TEST + "34066475: 'Share the News article from NUS'");

        // Open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Tap on first news article to open article Details Screen.
        ScreenNewsArticleDetails detailsScreen = updatesScreen.openFirstNewsArticleDetailsScreen();

        // Save header of current Details screen.
        String articleHeader = detailsScreen.getArticleHeader();

        // Tap on Forward button. Verify 'Share News Article' screen.
        ScreenShareNewsArticle shareScreen = detailsScreen.openShareNewsArticleScreen();

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user is returned to 'News Article Details' screen.
        detailsScreen = new ScreenNewsArticleDetails();

        // Open Expose screen
        ScreenExpose screenExpose = detailsScreen.openExposeScreen();
        // Open Updates screen.
        updatesScreen = screenExpose.openUpdatesScreen();

        // Open Expose screen
        screenExpose = updatesScreen.openExposeScreen();
        // Reopen Updates screen (refresh does not work with fixtures).
        updatesScreen = screenExpose.openUpdatesScreen();

        // Get first cell layout.
        RelativeLayout firstCellLayout = updatesScreen.getFirstCell();

        // Verify that the article with specified header is shown in Updates
        // screen.
        Assert.assertNotNull("The article with header '" + articleHeader + "'"
                + " is not present on Updates screen.",
                ViewUtils.searchTextViewInLayout(articleHeader, firstCellLayout, false));
        Logger.i(DONE + "Verify that the article is shared and is shown in Updates screen.");

        // Get layout of article summary with image.
        RelativeLayout visibleArticleSummaryWithImageRelativeLayout = updatesScreen
                .getVisibleArticleSummaryWithImageRelativeLayout(firstCellLayout, articleHeader);

        // Verify article with specified header has image displayed along with
        // summary.
        Assert.assertTrue(
                "Article with specified header has no image displayed along with summary",
                updatesScreen.isImageWithSummaryPlacedInSpecifiedLayout(
                        visibleArticleSummaryWithImageRelativeLayout, articleHeader));
        Logger.i(DONE + "Verify that the article has image displayed along with summary.");

        Logger.i(PASS + "34066475");
    }

    /**
     * Test case 34066627: 'Share the news article from the LinkedIn Today
     * (original news category).'
     * 
     * Open Updates screen. Open LINKEDIN TODAY screen. Open first News Article
     * Details screen. Tap on Forward button. Verify 'Share News Article'
     * screen. Tap on Share button. Open Updates screen. Verify that the article
     * is shown in Updates screen. Verify that the article has image displayed
     * along with summary.
     */
    public void test34066627() {
        startFixture("34066627");
        Logger.i(START_TEST
                + "34066627: 'Share the news article from the LinkedIn Today (original news category).'");

        // Open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open LINKEDIN TODAY screen.
        ScreenLinkedInToday linkedInTodayScreen = updatesScreen.openLinkedInTodayScreen();

        // Open first News Article Details screen.
        ScreenNewsArticleDetailsFromLinkedInToday articleScreen;
        articleScreen = linkedInTodayScreen.openFirstNewsArticleDetailsScreen();

        // Save header of current Details screen.
        String articleHeader = articleScreen.getArticleHeader();

        // Tap on Forward button. Verify 'Share News Article' screen.
        ScreenShareNewsArticle shareScreen = articleScreen.openShareNewsArticleScreen();

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user is returned to 'News Article Details' screen.
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Open Expose screen
        ScreenExpose screenExpose = articleScreen.openExposeScreen();
        // Open Updates screen.
        updatesScreen = screenExpose.openUpdatesScreen();

        // Open Expose screen
        screenExpose = updatesScreen.openExposeScreen();
        // Reopen Updates screen (refresh does not work with fixtures).
        updatesScreen = screenExpose.openUpdatesScreen();

        // Get first cell layout.
        RelativeLayout firstCellLayout = updatesScreen.getFirstCell();

        // Verify that the article with specified header is shown in Updates
        // screen.
        Assert.assertNotNull("The article with header '" + articleHeader + "'"
                + " is not present on Updates screen.",
                ViewUtils.searchTextViewInLayout(articleHeader, firstCellLayout, false));
        Logger.i(DONE + "Verify that the article is shared and is shown in Updates screen.");

        // Get layout of article summary with image.
        RelativeLayout visibleArticleSummaryWithImageRelativeLayout = updatesScreen
                .getVisibleArticleSummaryWithImageRelativeLayout(firstCellLayout, articleHeader);

        // Verify article with specified header has image displayed along with
        // summary.
        Assert.assertTrue(
                "Article with specified header has no image displayed along with summary",
                updatesScreen.isImageWithSummaryPlacedInSpecifiedLayout(
                        visibleArticleSummaryWithImageRelativeLayout, articleHeader));
        Logger.i(DONE + "Verify that the article has image displayed along with summary.");

        Logger.i(PASS + "34066627");
    }

    /**
     * Test case 34632415: 'Log in - success - Calendar disabled'
     * 
     * Log in with a correct username & correct password. Verify Log in is
     * succeed. Tap on "Sync all" when prompted about Contact Sync. Tap on
     * "Learn More" when reaching the Calendar Splash Page. Tap on the Back
     * button from the Learn More page. Tap on the Cross sign to dismiss the
     * Calendar Splash page. Tap on the Blue bubble to dismiss it. Verify once
     * the Calendar Splash Page and the bubble are dismissed the user reach the
     * NUS page. Verify Updates screen loads properly. Verify the Hero Slot
     * doesn't display the Calendar.
     */
    public void test34632415() {
        startFixture("34632415");

        Logger.i(START_TEST + "34632415: 'Log in - success - Calendar disabled'");

        // Verify Login screen.
        ScreenLogin loginScreen = new ScreenLogin();

        // Type credentials and tap 'Sign In'.
        loginScreen.typeEmail(StringData.test_email);
        loginScreen.typePassword(StringData.test_password);
        loginScreen.tapOnSignInButton();

        // Tap 'Sync all'.
        Assert.assertTrue("'Sync all' text is not present", getSolo().waitForText("Sync all"));
        Logger.i("Tapping on 'Sync all' text");
        getSolo().clickOnText("Sync all");

        // Verify Calendar splash screen.
        loginScreen.waitForCalendarSplashScreen();

        // Tap 'Learn More' text. Verify Learn More splash screen.
        if (getSolo().searchText("Learn More")) {
            Logger.i("Tapping on 'Learn More' text");
            getSolo().clickOnText("Learn More");
        }
        loginScreen.verifyLearnMoreSplashScreen();

        // Tap 'Back'. Verify Calendar splash screen.
        HardwareActions.pressBack();
        loginScreen.waitForCalendarSplashScreen();

        // Handle CalendarSplash.
        loginScreen.handleCalendarSplash();

        // Handle blue hint for IN button.
        loginScreen.handleInButtonHint();

        // Verify Updates screen.
        ScreenUpdates screenUpdates = new ScreenUpdates();
        Logger.i(DONE
                + " Verify once the Calendar Splash Page and the bubble are dismissed the user reach the NUS page.");
        Logger.i(DONE + "Verify Updates screen loads properly.");

        // Check the Hero Slot doesn't display the Calendar
        Assert.assertTrue("Calendar is present in Hero Slot",
                !screenUpdates.isCalendarPresentInHeroSlot());
        Logger.i(DONE + "Verify the Hero Slot doesn't display the Calendar.");

        Logger.i(PASS + "34632415");
    }

    /**
     * Test case 34632561: 'NUS - Sharing.'
     * 
     * Open Updates screen. Tap on 'Share' icon. Verify 'Share Update' screen.
     * Type random update text to the 'Share an update' field. Tap on Share
     * button. Verify 'Update sent' toast is shown. Verify that user returned to
     * Updates screen.
     */
    public void test34632561() {
        startFixture("34632561");
        Logger.i(START_TEST + "34632561: 'NUS - Sharing.'");

        // Open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Tap on 'Share' icon. Verify 'Share Update' screen.
        ScreenShareUpdate shareScreen = screenUpdates.openShareUpdateScreen();

        // Type random update text to the 'Share an update' field.
        @SuppressWarnings("unused")
        String updateText = shareScreen.typeRandomUpdateText();

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user returned to Updates screen.
        screenUpdates = new ScreenUpdates();
        Logger.i(DONE
                + "Verify that the compose screen disappears and the user goes back to the NUS screen.");

        Logger.i(PASS + "34632561");
    }

    /**
     * Test case 34632599: 'NUS - Sharing - Cancel.'
     * 
     * Open Updates screen. Tap on 'Share' icon. Verify 'Share Update' screen.
     * Type random update text to the 'Share an update' field. Tap on Back
     * button. Verify 'Cancel Message' popup. Tap on 'Yes' button on popup.
     * Verify that user returned to Updates screen.
     */
    public void test34632599() {
        startFixture("34632599");

        Logger.i(START_TEST + "34632599: 'NUS - Sharing - Cancel.'");

        // Open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Tap on 'Share' icon. Verify 'Share Update' screen.
        ScreenShareUpdate shareScreen = screenUpdates.openShareUpdateScreen();

        // Type random update text to the 'Share an update' field.
        @SuppressWarnings("unused")
        String updateText = shareScreen.typeRandomUpdateText();

        // Tap on Back button.
        HardwareActions.pressBack();

        // Verify 'Cancel Message' popup.
        Popup popup = new Popup("Cancel", "Are you sure you want to discard this message?");

        // Tap on 'Yes' button on popup.
        popup.tapOnButton("Yes");

        // Verify that user returned to Updates screen.
        screenUpdates = new ScreenUpdates();
        Logger.i(DONE
                + "Verify that the compose screen disappears and the user goes back to the NUS screen.");

        Logger.i(PASS + "34632599");
    }

    /**
     * Test case 34066339: 'News from NUS.'
     * 
     * Open Updates screen. On the NUS Stream tap on news article. Verify that
     * News Web view is displayed.
     */
    public void test34066339() {
        startFixture("34066339");

        Logger.i(START_TEST + "34066339: 'News from NUS.'");

        // Open Updates screen.
        ScreenUpdates updates = LoginActions.openUpdatesScreenOnStart();

        // Tap on first news article to open article Details Screen
        ScreenNewsArticleDetails detail = updates.openFirstNewsArticleDetailsScreen();

        // Verify article Details Screen
        detail.verify();
        Logger.i(DONE + "Verify News Web view is displayed.");

        Logger.i(PASS + "34066339");
    }

    /**
     * Test case 35973635: 'NUS - Update.' Steps: 1- From the Updates
     * (originally: NUS) page, tap on a simple NUS type (news NUS). 2- Tap on
     * the Profile. 3- Hit back. 4- Tap on the like button. 5- Tap on Comment
     * button. 6- Comment on it and tap on send. 7- Tap on Share. 8- Tap on
     * Share from the options. 9 -Comment on the article and tap on share. 10 -
     * Tap on Share. From the Share options Tap on Send to Connection. 11 -
     * Select connection and tap on send. 12 - Tap on the share
     * 
     * Expected Result: 1- The Feed Details Page shows up. 2- Profile page shows
     * up. 3- Feed Detail shows up. 4- Update liked toast is shown and (n)
     * person liked this is shown on the feed detail. 5- Comment compose screen
     * shows up. 6- Posting Comment and comment posted toast is shown. 7- Share
     * options show up. 8- Comment compose show up. 9-Feed Detail Page shows up.
     * 10 - Message Compose screen shows up. 11 -Send message and Message sent
     * toast is shown. 12 - Reply Compose screen is shown. 13 - Send message and
     * Message sent toast is shown. 14 - Feed Detail is shown.
     */
    public void test35973635() {
        startFixture("35973635");
        Logger.i(START_TEST + "35973635: 'NUS -  Update.'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenSharedNewsDetails newsDetails = screenUpdates
                .openAnySharedNewsArticleDetailsScreen(3);
        Assert.assertNotNull("Can not find any shared news", newsDetails);
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 2
        newsDetails.tapOnConnectionProfile();
        Logger.i(DONE + "Profile page shows up");

        // STEP 3
        HardwareActions.pressBack();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "Feed Detail shows up");

        // STEP 4
        newsDetails.tapOnLikeButton();
        newsDetails.verifyToast("Update"); // Update Liked & Update UnLiked
        Logger.i(DONE + "Update liked toast is shown");
        TextView likeText = (TextView) Id.getViewByName(ScreenSharedNewsDetails.LIKE_TEXT);
        Assert.assertNotNull("(n) person liked this is shown", likeText);
        Logger.i(DONE + "(n) person liked this is shown on the feed detail");

        // STEP 5
        ScreenAddComment addComment = newsDetails.openAddCommentScreen();
        Logger.i(DONE + "Comment compose screen shows up");

        // STEP 6
        addComment.typeUpdateText(null);
        addComment.tapOnSendButton();
        addComment.verifyToast("Comment sent");
        Logger.i(DONE + "Posting Comment and comment posted toast is shown");

        // STEP 7 & 8
        newsDetails = new ScreenSharedNewsDetails();
        ScreenShareNewsArticle shareNews = newsDetails.openShareNewsArticleScreen();
        Logger.i(DONE + "Share options and comment compose show up.");

        // STEP 9
        shareNews.typeRandomComment();
        shareNews.tapOnShareButton();
        addComment.verifyToast("Update sent");
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 10
        ScreenNewMessage newMessage = newsDetails.openSendToConnectionScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 11
        ScreenAddConnections connectionsScreen = newMessage.openAddConnectionsScreen();
        connectionsScreen.chooseConnection(null);
        connectionsScreen.tapOnDoneButton();

        // Verify that user returned to the 'New Message' screen.
        newMessage = new ScreenNewMessage();

        newMessage.tapOnSendButton();
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35973635");
    }

    /**
     * Test case 35974273: 'NUS- Group Post.'
     * 
     * Steps: From the Updates page, tap on Group Post. Tap on Profile. Tap
     * Back. Tap on the like button. Tap on Comment button. Comment on it and
     * tap on send. Hit Back.
     * 
     * Expected results: Feed Details Page is shown. Profile Page is shown.
     * Update liked toast is shown and (n) person liked this is shown on the
     * feed detail. Comment compose screen shows up. Posting Comment and comment
     * posted toast is shown. NUS stream is shown.
     */
    public void test35974273() {
        // TODO uncomment when fixtures will be recorded for the test
        // startFixture("35974273");
        Logger.i(START_TEST + "35974273: 'NUS- Group Post.'");

        // Login and open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open first found Group Post.
        ScreenDiscussionDetailsFromRecentUpdates screenDiscussionDetailsFromRecentUpdates = updatesScreen
                .openDiscussionDetailsOfFirstGroupPost();
        Logger.i(DONE + "The Group Post details page is shown");

        // Tap on discussion author profile
        screenDiscussionDetailsFromRecentUpdates.tapOnDiscussionAuthorProfile();
        @SuppressWarnings("unused")
        ScreenProfileOfConnectedUser screenProfileOfConnectedUser = new ScreenProfileOfConnectedUser();
        Logger.i(DONE + "The Group Post author profile page is shown");

        // Back to Group Post screen
        HardwareActions.pressBack();
        screenDiscussionDetailsFromRecentUpdates = new ScreenDiscussionDetailsFromRecentUpdates();

        // TODO verify toasts
        // Tap on like button. Verify update liked toast is shown and
        // "N people like this" is shown.
        screenDiscussionDetailsFromRecentUpdates.likeUpdate();
        Logger.i(DONE + "Update liked toast is shown and 'N people like this' is shown");

        // Open Comment screen
        ScreenCommentDiscussion screenCommentDiscussion = screenDiscussionDetailsFromRecentUpdates
                .openCommentScreen();
        Logger.i(DONE + "Comment screen is shown");

        // Type comment
        screenCommentDiscussion.typeRandomDetailTextOfDiscussion();

        // Tap on Send button. Verify "Posting comment" and "Comment sent"
        // toasts shown.
        screenCommentDiscussion.tapOnSendButton();
        Logger.i(DONE + "'Posting comment' and 'Comment sent' toasts were shown");
        screenDiscussionDetailsFromRecentUpdates = new ScreenDiscussionDetailsFromRecentUpdates();

        // Back to Updates screen
        HardwareActions.pressBack();
        updatesScreen = new ScreenUpdates();
        Logger.i(DONE + "Updates screen is shown");

        Logger.i(PASS + "35974273");
    }

    /**
     * Test case 35973757: 'NUS - News.' Steps: 1- From the Updates (originally:
     * NUS) page, tap on news. 2- Tap on the Profile. 3- Hit back. 4- Tap on the
     * like button. 5- Tap on Comment button. 6- Comment on it and tap on send.
     * 7- Tap on Share. 8- Tap on Share from the options. 9 -Comment on the
     * article and tap on share. 10 - Tap on Share. From the Share options Tap
     * on Send to Connection. 11 - Select connection and tap on send. 12 - Tap
     * on Share. From the Share options Tap on Reply Privately. 13 - Enter some
     * text and Tap on Send. 14 - Tap on Share. From the Share options 15 - Tap
     * on Cancel.
     * 
     * Expected Result: 1- The Feed Details Page shows up. 2- Profile page shows
     * up. 3- Feed Detail shows up. 4- Update liked toast is shown and (n)
     * person liked this is shown on the feed detail. 5- Comment compose screen
     * shows up. 6- Posting Comment and comment posted toast is shown. 7- Share
     * options show up. 8- Comment compose show up. 9-Feed Detail Page shows up.
     * 10 - Message Compose screen shows up. 11 -Send message and Message sent
     * toast is shown. 12 - Reply Compose screen is shown. 13 - Send message and
     * Message sent toast is shown. 14 - Feed Detail is shown.
     */
    public void test35973757() {
        startFixture("35973757");
        Logger.i(START_TEST + "35973635: 'NUS -  News.'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenSharedNewsDetails newsDetails = screenUpdates
                .openAnySharedNewsArticleDetailsScreen(3);
        Assert.assertNotNull("Can not find any shared news", newsDetails);
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 2
        newsDetails.tapOnConnectionProfile();
        Logger.i(DONE + "Profile page shows up");

        // STEP 3
        HardwareActions.pressBack();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "Feed Detail shows up");

        // STEP 4
        newsDetails.tapOnLikeButton();
        newsDetails.verifyToast("Update"); // Update Liked & Update UnLiked
        Logger.i(DONE + "Update liked toast is shown");
        TextView likeText = (TextView) Id.getViewByName(ScreenSharedNewsDetails.LIKE_TEXT);
        Assert.assertNotNull("(n) person liked this is shown", likeText);
        Logger.i(DONE + "(n) person liked this is shown on the feed detail");

        // STEP 5
        ScreenAddComment addComment = newsDetails.openAddCommentScreen();
        Logger.i(DONE + "Comment compose screen shows up");

        // STEP 6
        addComment.typeUpdateText(null);
        addComment.tapOnSendButton();
        addComment.verifyToast("Comment sent");
        Logger.i(DONE + "Posting Comment and comment posted toast is shown");

        // STEP 7 & 8
        newsDetails = new ScreenSharedNewsDetails();
        ScreenShareNewsArticle shareNews = newsDetails.openShareNewsArticleScreen();
        Logger.i(DONE + "Share options and comment compose show up.");

        // STEP 9
        shareNews.typeRandomComment();
        shareNews.tapOnShareButton();
        addComment.verifyToast("Update sent");
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 10
        ScreenNewMessage newMessage = newsDetails.openSendToConnectionScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 11
        ScreenAddConnections connectionsScreen = newMessage.openAddConnectionsScreen();
        connectionsScreen.chooseConnection(null);
        connectionsScreen.tapOnDoneButton();

        // Verify that user returned to the 'New Message' screen.
        newMessage = new ScreenNewMessage();

        newMessage.tapOnSendButton();
        newMessage.verifyToast("Message sent");
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 12
        ScreenReplyMessage replyMessage = newsDetails.openReplyPrivatelyScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 13
        replyMessage.tapOnSendButton();
        replyMessage.verifyToast("Message sent");
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 14
        newsDetails.tapOnForwardButton();
        getSolo().waitForText("Cancel", 1, DataProvider.WAIT_DELAY_DEFAULT);
        newsDetails.tapOnCancelButtonOnPopup();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35973757");
    }

    /**
     * Test case 35974167: 'NUS - Simple Update' Steps: 1- From the Updates
     * (originally: NUS) page, tap on a Simple update shared by your connection,
     * with no link. 2- Tap on the Profile. 3- Hit back. 4- Tap on the like
     * button. 5- Tap on Comment button. 6- Comment on it and tap on send. 7-
     * Tap on Share. 8- From the Share options Tap on Send to Connection. 9 -
     * Select connection and tap on send. 10 - Tap on Share. From the Share
     * options Tap on Reply Privately. 11 - Enter some text and Tap on Send. 12
     * - Tap on Share. From the Share options. Tap on Cancel.
     * 
     * Expected Result: 1- The Feed Details Page shows up. 2- Profile page shows
     * up. 3- Feed Detail shows up. 4- Update liked toast is shown and (n)
     * person liked this is shown on the feed detail. 5- Comment compose screen
     * shows up. 6- Posting Comment and comment posted toast is shown. 7- Share
     * options show up. 8- Message Comment compose show up. 9-Send message and
     * Message sent toast is shown. 10 - Reply Compose screen is shown. 11 -
     * Send message and Message sent toast is shown. 12 - Feed Detail is shown.
     */
    public void test35974167() {
        startFixture("35974167");
        Logger.i(START_TEST + "35973635: 'NUS - Simple Update'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenSharedNewsDetails newsDetails = screenUpdates
                .openAnySharedNewsArticleDetailsScreen(2);
        Assert.assertNotNull("Can not find any shared news", newsDetails);
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 2
        newsDetails.tapOnConnectionProfile();
        Logger.i(DONE + "Profile page shows up");

        // STEP 3
        HardwareActions.pressBack();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "Feed Detail shows up");

        // STEP 4
        newsDetails.tapOnLikeButton();
        newsDetails.verifyToast("Update"); // Update Liked & Update UnLiked
        Logger.i(DONE + "Update liked toast is shown");
        TextView likeText = (TextView) Id.getViewByName(ScreenSharedNewsDetails.LIKE_TEXT);
        Assert.assertNotNull("(n) person liked this is shown", likeText);
        Logger.i(DONE + "(n) person liked this is shown on the feed detail");

        // STEP 5
        ScreenAddComment addComment = newsDetails.openAddCommentScreen();
        Logger.i(DONE + "Comment compose screen shows up");

        // STEP 6
        addComment.typeUpdateText(null);
        addComment.tapOnSendButton();
        addComment.verifyToast("Comment sent");
        Logger.i(DONE + "Posting Comment and comment posted toast is shown");

        // STEP 7 & 8
        newsDetails = new ScreenSharedNewsDetails();
        ScreenNewMessage newMessage = newsDetails.openSendToConnectionScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 9
        ScreenAddConnections connectionsScreen = newMessage.openAddConnectionsScreen();
        connectionsScreen.chooseConnection(null);
        connectionsScreen.tapOnDoneButton();

        // Verify that user returned to the 'New Message' screen.
        newMessage = new ScreenNewMessage();

        newMessage.tapOnSendButton();
        newMessage.verifyToast("Message sent");
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 10
        ScreenReplyMessage replyMessage = newsDetails.openReplyPrivatelyScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 11
        replyMessage.tapOnSendButton();
        replyMessage.verifyToast("Message sent");
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 12
        newsDetails.tapOnForwardButton();
        getSolo().waitForText("Cancel", 1, DataProvider.WAIT_DELAY_DEFAULT);
        newsDetails.tapOnCancelButtonOnPopup();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35974167");
    }

    /**
     * Test case 35975187: 'NUS - Reshare' Steps: 1- From the NUS page, tap on a
     * update with link shared by your connection. 2- Tap on the Profile. 3- Hit
     * back. 4- Tap on the like button. 5- Tap on Comment button. 6- Comment on
     * it and tap on send. 7- Tap on Share . 8- Tap on Share from the options. 9
     * -Comment on the article and tap on share 10 - Tap on Share. From the
     * Share options Tap on Send to Connection. 11 - Select connection and tap
     * on send. 12 - Tap on Share. From the Share options Tap on Reply
     * Privately. 13 - Enter some text and Tap on Send. 14 - Tap on Share. From
     * the Share options Tap on Cancel. 15- Tap on the link. 16- Tap on refresh.
     * 17- Tap on Share button. 18 - Tap on Share from the options. 19 - Tap on
     * Share. From the Share options Tap on Send to Connection. 20 - Select
     * connection and tap on send. 21- Tap on Share. From the Share options Tap
     * on open in a browser. 22- Hit Back. 23- Hit back again.
     * 
     * Expected results: 1- The Feed Details Page shows up. 2- Profile page
     * shows up. 3- Feed Detail shows up. 4- Update liked toast is shown and (n)
     * person liked this is shown on the feed detail. 5- Comment compose screen
     * shows up. 6- Posting Comment and comment posted toast is shown. 7- Share
     * options show up. 8- Comment compose show up. 9-Feed Detail Page shows up.
     * 10 - Message Compose screen shows up. 11 -Send message and Message sent
     * toast is shown. 12 - Reply Compose screen is shown. 13 - Send message and
     * Message sent toast is shown. 14 - Feed Detail is shown. 15 - Opens in a
     * web view browser. 16 - It should take the user to the same page but just
     * refreshes the page. 17 - Share options show up. 18 - Comment compose show
     * up. 19 -Feed Detail Page shows up. 20 - Message Compose screen shows up.
     * 21 - Send message and Message sent toast is shown. 22 - Previous page is
     * shown. 23 - Feed Detail shows up
     */
    @Suppress
    public void test35975187() {
        startFixture("35975187");
        Logger.i(START_TEST + "35975187: 'NUS - Reshare'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenSharedNewsDetails newsDetails = screenUpdates
                .openAnyUpdateWithLinkSharedByYourConnection();
        Assert.assertNotNull("Can not find any shared news", newsDetails);
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 2
        newsDetails.tapOnConnectionProfile();
        Logger.i(DONE + "Profile page shows up");

        // STEP 3
        HardwareActions.pressBack();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "Feed Detail shows up");

        // STEP 4
        newsDetails.tapOnLikeButton();
        newsDetails.verifyToast("Update"); // Update Liked & Update UnLiked
        Logger.i(DONE + "Update liked toast is shown");
        TextView likeText = (TextView) Id.getViewByName(ScreenSharedNewsDetails.LIKE_TEXT);
        Assert.assertNotNull("(n) person liked this is shown", likeText);
        Logger.i(DONE + "(n) person liked this is shown on the feed detail");

        // STEP 5
        ScreenAddComment addComment = newsDetails.openAddCommentScreen();
        Logger.i(DONE + "Comment compose screen shows up");

        // STEP 6
        addComment.typeUpdateText(null);
        addComment.tapOnSendButton();
        addComment.verifyToast("Comment sent");
        Logger.i(DONE + "Posting Comment and comment posted toast is shown");

        // STEP 7 & 8
        newsDetails = new ScreenSharedNewsDetails();
        ScreenShareNewsArticle shareNews = newsDetails.openShareNewsArticleScreen();
        Logger.i(DONE + "Share options and comment compose show up.");

        // STEP 9
        shareNews.typeRandomComment();
        shareNews.tapOnShareButton();
        addComment.verifyToast("Update sent");
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 10
        ScreenNewMessage newMessage = newsDetails.openSendToConnectionScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 11
        ScreenAddConnections connectionsScreen = newMessage.openAddConnectionsScreen();
        connectionsScreen.chooseConnection(null);
        connectionsScreen.tapOnDoneButton();

        // Verify that user returned to the 'New Message' screen.
        newMessage = new ScreenNewMessage();

        newMessage.tapOnSendButton();
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 12
        ScreenReplyMessage replyMessage = newsDetails.openReplyPrivatelyScreen();
        Logger.i(DONE + "Message Compose screen shows up");

        // STEP 13
        replyMessage.tapOnSendButton();
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();

        // STEP 14
        newsDetails.tapOnForwardButton();
        getSolo().waitForText("Cancel", 1, DataProvider.WAIT_DELAY_DEFAULT);
        newsDetails.tapOnCancelButtonOnPopup();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 15
        TextView link = newsDetails.getTextViewWithGrayColorLink();
        Assert.assertNotNull("There are no link on current screen", link);
        getSolo().clickOnView(link);
        ScreenBrowser screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "Opens in a web view browser.");

        // STEP 16
        screenBrowser.tapOnRefreshButton();
        Logger.i(DONE + "Page refreshed.");

        // STEP 17
        ScreenShareNewsArticle shareNews2 = screenBrowser.openShareNewsArticleScreen();
        Logger.i(DONE + "Comment compose show up");

        // STEP 18
        shareNews2.typeRandomComment();
        shareNews2.tapOnShareButton();
        shareNews2.verifyToast("Update sent");
        screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 19
        ScreenNewMessage message = screenBrowser.openNewMessageScreen();
        Logger.i(DONE + "Comment compose show up");

        // STEP 20
        ScreenAddConnections connectionsScreen2 = message.openAddConnectionsScreen();
        connectionsScreen2.chooseConnection(null);
        connectionsScreen2.tapOnDoneButton();
        message = new ScreenNewMessage();
        message.tapOnSendButton();
        screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "Send message and Message sent toast is shown");

        // STEP 21
        screenBrowser.tapOnForwardButton();
        screenBrowser.tapOnOpenInBrowserOnPopup();
        // Wait for another application (Browser)
        HardwareActions.delay(30);
        boolean browserIsShown = false;
        // while another application (Browser) is on top
        while (getSolo().getCurrentActivity().getCurrentFocus().isShown() == false) {
            try {
                Instrumentation i = new Instrumentation();
                i.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                // We send Back key press signal to linked in application.
                break;
            } catch (SecurityException e) {
                // Send Back key press signal to another application!
                // We should catch java.lang.SecurityException:
                // Injecting to another application requires INJECT_EVENTS permission
                // It's GOOD catch!
                if (!browserIsShown) {
                    browserIsShown = true;
                    Logger.i(DONE + "Browser is shown");
                }
            }
            // User should press Back button on the phone!
            // because we can not operate with another application
            Logger.i("PLEASE PRESS 'Back' BUTTON. This is not automative part of test.");
            HardwareActions.delay(10);
            
        }
        assertTrue("Browser is not shown", browserIsShown);
        
        // STEP 22
        // We should check for double back pressing
        if (getSolo().getCurrentActivity().getClass()
                .getSimpleName().equals(ScreenBrowser.ACTIVITY_SHORT_CLASSNAME)) {
            screenBrowser = new ScreenBrowser();
            HardwareActions.pressBack();
        }
        Logger.i(DONE + "Previous page is shown");

        // STEP 23
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35975187");
    }

    /**
     * Test case 35974233: 'NUS - Profile Roll up'. Steps: 1- From the NUS page,
     * tap on Profile roll up. 2- Tap on the connections updated profile 3- Tap
     * on Profile. 4- Hit Back. 5 -Tap on the like button. 6- - Tap on Comment
     * button. 7 - Comment on it and tap on send. 8 - Tap on Share . 9 - From
     * the Share options Tap on Send to Connection. 10 - Select connection and
     * tap on send. 11 - Tap on Share. From the Share options Tap on Reply
     * Privately. 12 - Enter some text and Tap on Send. 13 - Tap on Share. From
     * the Share options Tap on Cancel. 14- Hit Back 15- Hit Back again
     * 
     * Expected results: 1- Details Page is shown. All your connection's Profile
     * updates are shown. 2- Details Page is shown. 3- Profile Page is shown. 4-
     * Previous screen - Details page is shown. 5- Update liked toast is shown
     * and (n) person liked this is shown on the feed detail. 6- Comment compose
     * screen shows up. 7- Posting Comment and comment posted toast is shown. 8-
     * Share options show up. 9 - Message Compose screen shows up. 10 -Send
     * message and Message sent toast is shown. 11 - Reply Compose screen is
     * shown. 12 - Send message and Message sent toast is shown. 13- Detail page
     * is shown. 14 - Previous page is shown. 15- NUS Page is shown.
     */
    public void test35974233() {
        startFixture("35974233");
        Logger.i(START_TEST + "35974233: 'NUS - Profile Roll up'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        HardwareActions.waitForScreenUpdate();
        ScreenUpdatedProfileDetails rollUp = screenUpdates.openProfileRollUpScreen();
        Logger.i(DONE + "The Profile Roll Up Page shows up");

        // STEP 2
        ScreenSingleUpdatedProfileDetails rollUpDetails = rollUp.openFirstUpdatedProfileDetails();
        Logger.i(DONE + "The Update Profile Details Page shows up");

        // STEP 3
        ScreenProfileOfConnectedUser profile = rollUpDetails.openConnectionProfile();
        Logger.i(DONE + "The Your Connection Profile Page shows up");

        // STEP 4
        HardwareActions.pressBack();
        rollUpDetails = new ScreenSingleUpdatedProfileDetails();
        Logger.i(DONE + "The Update Profile Details Page shows up");

        // STEP 5
        rollUpDetails.tapOnLikeButton();
        rollUpDetails.verifyToast("Update Liked");
        rollUpDetails.verifyPeopleLikedLabel();
        Logger.i(DONE + "The Likes Toasts shows up and '(n) person liked this' shows up");

        // STEP 6
        ScreenAddComment addComment = rollUpDetails.openAddCommentScreen();
        Logger.i(DONE + "The Add Comment Page shows up");

        // STEP 7
        String comment = "testing comment";
        addComment.typeUpdateText(comment);
        // Verify send comment and tap on send button.
        addComment.verifyTwoToastsStart("Posting comment", "Comment sent");
        addComment.tapOnSendButton();
        addComment.verifyTwoToastsEnd();
        // Verify that comment was posted.
        HardwareActions.waitForScreenUpdate();
        rollUpDetails = new ScreenSingleUpdatedProfileDetails();
        Assert.assertTrue("The comment wasn`t posted", getSolo().searchText(comment));
        Logger.i(DONE + "The Add Comment Page shows up and verify send of comment");

        // STEP 8
        Assert.assertTrue("Share options are not show", rollUpDetails.tapOnForwardButton() >= 2);
        Logger.i(DONE + "The Share Options Page shows up");

        // STEP 9
        rollUpDetails.tapOnSendToConnectionOnPopup();
        ScreenNewMessage sendToConnection = new ScreenNewMessage();
        Logger.i(DONE + "The  Message Compose Page shows up");

        // STEP 10
        ScreenAddConnections addConnections = sendToConnection.openAddConnectionsScreen();
        addConnections.chooseConnection(null);
        addConnections.tapOnDoneButton();
        sendToConnection = new ScreenNewMessage();
        sendToConnection.verifyTwoToastsStart("Sending message", "Message sent");
        sendToConnection.tapOnSendButton();
        sendToConnection.verifyTwoToastsEnd();
        Logger.i(DONE + "The Message Sent Toast shows up");

        // STEP 11
        rollUpDetails = new ScreenSingleUpdatedProfileDetails();
        ScreenReplyMessage replayMessage = rollUpDetails.openReplyPrivatelyScreen();
        Logger.i(DONE + "The  Replay Compose Page shows up");

        // STEP 12
        replayMessage.typeMessage("c");
        replayMessage.verifyTwoToastsStart("Sending message", "Message sent");
        replayMessage.tapOnSendButton();
        replayMessage.verifyTwoToastsEnd();
        Logger.i(DONE + "The Replay Message Sent Toast shows up");

        // STEP 13
        rollUpDetails = new ScreenSingleUpdatedProfileDetails();
        rollUpDetails.tapOnForwardButton();
        rollUpDetails.tapOnCancelButtonOnPopup();
        rollUpDetails = new ScreenSingleUpdatedProfileDetails();
        Logger.i(DONE + "The Update Profile Details Page shows up");

        // STEP 14
        HardwareActions.pressBack();
        rollUp = new ScreenUpdatedProfileDetails();
        Logger.i(DONE + "The Profile Roll Up Page shows up");

        // STEP 15
        HardwareActions.pressBack();
        HardwareActions.scrollUp();
        screenUpdates = new ScreenUpdates();
        Logger.i(DONE + "The NUS Page shows up");

        Logger.i(PASS + "35974233");
    }
}
