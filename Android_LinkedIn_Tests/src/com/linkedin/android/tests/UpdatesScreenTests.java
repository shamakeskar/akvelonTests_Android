package com.linkedin.android.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
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
     * Test case 34066659: 'Share the news article from the LinkedIn Today
     * (original news category) with comments.'
     * 
     * Open Updates screen. Open LINKEDIN TODAY screen. Open first News Article
     * Details screen. Tap on Forward button. Verify 'Share News Article'
     * screen. Type random comment. Tap on Share button. Open Updates screen.
     * Verify that the article is shown in Updates screen with the comment.
     * Verify that the article has image displayed along with summary.
     */
    public void test34066659() {
        startFixture("34066659");
        Logger.i(START_TEST
                + "34066659: 'Share the news article from the LinkedIn Today (original news category) with comments.'");

        ScreenUpdates screenUpdates = LoginActions
                .openUpdatesScreenOnStart(StringData.test_email_large_connections,
                        StringData.test_password_large_connections);

        // Open LINKEDIN TODAY screen.
        ScreenLinkedInToday linkedInTodayScreen = screenUpdates.openLinkedInTodayScreen();

        // Open first News Article Details screen.
        ScreenNewsArticleDetailsFromLinkedInToday articleScreen;
        articleScreen = linkedInTodayScreen.openFirstNewsArticleDetailsScreen();

        // Tap on Forward button. Verify 'Share News Article' screen.
        ScreenShareNewsArticle shareScreen = articleScreen.openShareNewsArticleScreen();

        // Type random comment.
        String comment = shareScreen.typeRandomComment();

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user is returned to 'News Article Details' screen.
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Open Expose screen.
        ScreenExpose screenExpose = articleScreen.openExposeScreen();
        // Open Updates screen.
        ScreenUpdates updatesScreen = screenExpose.openUpdatesScreen();

        // Open Expose screen.
        screenExpose = updatesScreen.openExposeScreen();
        // Reopen Updates screen (refresh does not work with fixtures).
        updatesScreen = screenExpose.openUpdatesScreen();

        // Verify that the article is shown in Updates screen with the comment.
        Assert.assertTrue("The article with a comment '" + comment + "'"
                + " is not present on Updates screen.",
                getSolo().waitForText(comment, 1, DataProvider.WAIT_DELAY_LONG, false, false));
        Logger.i(DONE
                + "Verify that the article is shared and is shown in Updates screen with the comment.");

        // Verify that the article has image displayed along with summary.
        Assert.assertTrue(
                "There is no image displayed along with summary for article with comment: "
                        + comment,
                screenUpdates.isArticleHasImageDisplayedAlongWithSummary(comment));
        Logger.i(DONE + "Verify that image displayed along with summary for article with comment.");

        Logger.i(PASS + "34066659");
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
     * Test case 34066763: 'Reshare any article from the NUS stream with
     * comments'
     * 
     * Steps: Launch the application and log in with valid credentials. In the
     * NUS Stream, tap on the news article shared by your connection. Tap on the
     * forward button. Comment on the article and Tap on the Share button.
     * 
     * Expected Result: Sending update and Update sent toast is shown. Verify
     * that the article is shared and is shown in updates with the comments.
     * Verify that the article has image displayed along with summary.
     */
    public void test34066763() {
        startFixture("34066763");
        Logger.i(START_TEST + "34066763: 'Reshare any article from the NUS stream with comments'");

        // Login and open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open "Shared news details" screen of first news article shared by
        // current user
        ScreenSharedNewsDetails screenSharedNewsDetails = updatesScreen
                .openFirstNewsArticleSharedByYourConnection();

        // Tap on Forward button. Verify 'Share News Article' screen.
        ScreenShareNewsArticle shareScreen = screenSharedNewsDetails.openShareNewsArticleScreen();

        // Generated comment.
        String comment = "sDGaVWTAvq";

        // Type comment.
        shareScreen.typeInComment(comment);

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Sending update' and 'Update sent' toasts are shown.");

        // Verify that user is returned to "Shared news details" screen.
        screenSharedNewsDetails = new ScreenSharedNewsDetails();

        // Open Expose screen
        ScreenExpose screenExpose = screenSharedNewsDetails.openExposeScreen();
        // Open Updates screen.
        updatesScreen = screenExpose.openUpdatesScreen();

        // Verify that the article is shown in Updates screen with the comment.
        HardwareActions.scrollUp();
        Assert.assertTrue("The article with a comment '" + comment + "'"
                + " is not present on Updates screen.",
                getSolo().waitForText(comment, 1, DataProvider.WAIT_DELAY_LONG, false, true));
        Logger.i(DONE
                + "Verify that the article is shared and is shown in Updates screen with the comment.");

        // Verify that the article has image displayed along with summary.
        boolean isArticleHasImageDisplayedAlongWithSummary = updatesScreen
                .isArticleHasImageDisplayedAlongWithSummary(comment);
        Assert.assertTrue(
                "There is no image displayed along with summary for article with comment: "
                        + comment, isArticleHasImageDisplayedAlongWithSummary);
        Logger.i(DONE + "Verify that image displayed along with summary for article with comment.");

        Logger.i(PASS + "34066763");
    }

    /**
     * Test case 34066709: 'Reshare any article from the NUS stream'
     * 
     * Steps: Launch the application and log in with valid credentials. In the
     * NUS Stream, tap on the news article shared by your connection. Tap on the
     * forward button. Tap on the Share button.
     * 
     * Expected Result: Sending update and Update sent toast is shown. Verify
     * that the article is shared and is shown in updates. Verify that the
     * article has image displayed along with summary.
     */
    public void test34066709() {
        startFixture("34066709");
        Logger.i(START_TEST + "34066709: 'Reshare any article from the NUS stream'");

        // Login and open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open "Shared news details" screen of first news article shared by
        // current user
        ScreenSharedNewsDetails screenSharedNewsDetails = updatesScreen
                .openFirstNewsArticleSharedByYourConnection();

        // Save header of current Details screen.
        String articleHeader = screenSharedNewsDetails.getSharedNewsHeader();

        // Tap on Forward button. Verify 'Share News Article' screen.
        // ShareNewsArticleScreen
        ScreenShareNewsArticle shareScreen = screenSharedNewsDetails.openShareNewsArticleScreen();

        // Tap on Share button.
        shareScreen.verifyTwoToastsStart("Sending update", "Update sent");
        shareScreen.tapOnShareButton();
        shareScreen.verifyTwoToastsEnd();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user is returned to 'News Article Details' screen.
        screenSharedNewsDetails = new ScreenSharedNewsDetails();

        // Open Expose screen
        ScreenExpose screenExpose = screenSharedNewsDetails.openExposeScreen();
        // Open Updates screen.
        updatesScreen = screenExpose.openUpdatesScreen();

        // Open Expose screen
        screenExpose = screenSharedNewsDetails.openExposeScreen();
        // Reopen Updates screen.
        updatesScreen = screenExpose.openUpdatesScreen();

        // Verify that the article with specified header is shown in Updates
        // screen.
        HardwareActions.scrollUp();

        final int sharedNewsArticleAppearanceWaitTimeSec = 3;
        HardwareActions.delay(sharedNewsArticleAppearanceWaitTimeSec);
        LinearLayout firstNewsArticleSharedByCurrentUserWithoutComment = updatesScreen
                .getFirstNewsArticleSharedByCurrentUserWithoutComment(articleHeader);
        // Verify article with specified header has image displayed along with
        // summary.
        Assert.assertNotNull("The article with header '" + articleHeader + "'"
                + " and image displayed along with summary is not present on Updates screen.",
                firstNewsArticleSharedByCurrentUserWithoutComment);
        Logger.i(DONE
                + "Verify that the article is shared and is shown in Updates screen with image displayed along with summary.");

        Logger.i(PASS + "34066709");
    }

    /**
     * Test case 34632483: 'NUS - Expose'
     * 
     * Open Updates screen. Tap on LinkedIn logo. Tap on 'Updates' box (return
     * to the NUS). Verify screen.
     */
    public void test34632483() {
        startFixture("34632483");

        Logger.i(START_TEST + "34632483: 'NUS - Expose'");

        // Open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open Expose screen
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        // Refresh Updates screen and verify.
        screenExpose.openUpdatesScreen();
        Logger.i(DONE + "Data loads properly when reaching Expose and going back to the NUS page");

        Logger.i(PASS + "34632483");
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
     * Test case 34632447: 'Log in - success - Calendar enabled.'
     * 
     * Log in with a correct username & correct password. Verify Log in is
     * succeed. Tap on "Sync all" when prompted about Contact Sync. Tap on
     * "Learn More" when reaching the Calendar Splash Page. Tap on the Back
     * button from the Learn More page. Tap on "Add Calendar" to enable Calendar
     * feature. Verify once the Calendar Splash Page and the bubble are
     * dismissed the user reach the NUS page. Verify Updates screen loads
     * properly. Verify the Hero Slot displays the Calendar feature.
     */
    public void test34632447() {
        startFixture("34632447");

        Logger.i(START_TEST + "34632447: 'Log in - success - Calendar enabled.'");

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

        // Tap on 'Add Calendar' button.
        Button addCalendarButton = getSolo().getButton("Add Calendar");
        ViewUtils.tapOnView(addCalendarButton, "'Add Calendar' button");

        // Handle blue hint for IN button.
        loginScreen.handleInButtonHint();

        // Verify Updates screen.
        ScreenUpdates screenUpdates = new ScreenUpdates();
        Logger.i(DONE
                + "Verify once the Calendar Splash Page and the bubble are dismissed the user reach the NUS page.");
        Logger.i(DONE + "Verify Updates screen loads properly.");

        // Check the Hero Slot displays the Calendar feature.
        Assert.assertTrue("Calendar is not present in Hero Slot",
                screenUpdates.isCalendarPresentInHeroSlot());
        Logger.i(DONE + "Verify the Hero Slot displays the Calendar feature.");

        Logger.i(PASS + "34632447");
    }

    /**
     * Test case 34632519: 'NUS - Search / Send message Connection / Send invite
     * / go back NUS.'
     * 
     * Login as test user. Open Updates screen. Tap on the Search box. Verify
     * the Connections are loaded. Tap on any Connection name to reach the
     * profile page. Verify the Profile Page load properly (data accuracy) . Tap
     * on the Send Message icon, Enter content and hit Send. Verify the toasts
     * "Sending message..." and "Message sent" show up. Hit back. Verify the
     * Search page loads properly. Search for a name to find a 2nd degree
     * contact. Verify the data accuracy of the Search results. Tap on any 2nd
     * degree contact. Verify the Profile Page loads. Once the Profile page
     * loads, tap on the Invite button. Verify the toasts "Sending invite..."
     * and "Invite sent" show up. Hit back. Verify the Search page loads
     * properly. Hit Back. Verify the NUS page loads properly.
     * 
     */
    public void test34632519() {
        startFixture("34632519");
        Logger.i(START_TEST
                + "34632519: 'NUS - Search / Send message Connection / Send invite / go back NUS'");

        // Create list of all connections of current user (test_user).
        List<String> listOfConnections = new ArrayList<String>();
        listOfConnections.add("Makarov Alexander");
        listOfConnections.add("Mauricio Andreas");
        listOfConnections.add("Jane Andriichuk");
        listOfConnections.add("Tom Baller");
        listOfConnections.add("John12-12-2010-17-22 Doe");
        listOfConnections.add("user108 fortest");
        listOfConnections.add("Gheorghi Gorelko");
        listOfConnections.add("Alex Makarov");
        listOfConnections.add("Iryna Pavlenko");
        listOfConnections.add("Alex Pronin");
        listOfConnections.add("Alex Rudyk");
        listOfConnections.add("akv testing");
        listOfConnections.add("\u7687\u5BA4\u6771\u4EAC\u5E02\u6771\u4EAC\u90FD\u6771\u4EAC\u90FD");
        listOfConnections
                .add("\u0412\u0430\u043B\u0435\u043D\u0442\u0438\u043D\u0430 \u041B\u043E\u0433\u0443\u043D\u043E\u0432\u0430");

        // Save name of 2nd degree connection of current user (test_user).
        String nameOfSecondDegreeConnection = "evgeny agapov";

        // Open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open Search screen.
        ScreenSearch searchScreen = screenUpdates.openSearchScreen();

        // Check all connections are loaded.
        for (String connectionName : listOfConnections) {
            Assert.assertTrue("Connection with name '" + connectionName
                    + "' not presented on Search screen.", getSolo().searchText(connectionName));
        }
        Logger.i(DONE + "Verify the Connections are loaded.");

        // Scroll to top of screen.
        HardwareActions.scrollUp();

        // Tap on first connection.
        searchScreen.tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfConnectedUser profileScreen = new ScreenProfileOfConnectedUser();
        Assert.assertTrue("Connection  name is not '" + listOfConnections.get(0) + "'.", getSolo()
                .searchText(listOfConnections.get(0)));
        Logger.i(DONE + "Verify the Profile Page load properly.");

        // Tap on the Send Message icon.
        ScreenNewMessage newMessageScreen = profileScreen.openNewMessageScreen();

        // Enter content.
        newMessageScreen.typeSubject(null);
        newMessageScreen.typeMessage(null);

        // Tap Send.
        newMessageScreen.tapOnSendButton();
        Logger.i(DONE + "Verify 'Message sent' toast is shown.");

        // Verify Profile screen.
        profileScreen = new ScreenProfileOfConnectedUser();

        // Hit back.
        HardwareActions.pressBack();

        // Verify Search screen.
        searchScreen = new ScreenSearch();
        Logger.i(DONE + "Verify the Search page loads properly.");

        // Search for a name to find a 2nd degree contact
        searchScreen.searchForContact(nameOfSecondDegreeConnection);

        TextView connectionNameTextView = getSolo().getText(nameOfSecondDegreeConnection);
        int connectionDegree = ContactInfoUtils.getContactDegree(searchScreen
                .getContactDegree(connectionNameTextView));

        Assert.assertTrue("Connection '" + nameOfSecondDegreeConnection + "' degree is not '"
                + ContactInfoUtils.CONTACT_DEGREE_2 + "'.",
                connectionDegree == ContactInfoUtils.CONTACT_DEGREE_2);
        Logger.i(DONE + "Verify the data accuracy of the Search results.");

        // Tap on first connection.
        searchScreen.tapOnFirstVisibleConnectionProfileScreen();
        ScreenProfileOfNotConnectedUser secondDegreeConnectionProfileScreen = new ScreenProfileOfNotConnectedUser();
        Assert.assertTrue("Connection  name is not '" + nameOfSecondDegreeConnection + "'.",
                getSolo().searchText(nameOfSecondDegreeConnection));
        Logger.i(DONE + "Verify the Profile Page load properly.");

        // Tap on the Invite button.
        secondDegreeConnectionProfileScreen.tapOnInviteToConnectButton();
        Logger.i(DONE + "Verify 'Invitation sent' toast is shown.");

        // Hit back.
        HardwareActions.pressBack();

        // Verify Search screen.
        searchScreen = new ScreenSearch();
        Logger.i(DONE + "Verify the Search page loads properly.");

        // Hit back.
        HardwareActions.pressBack();

        // Verify Updates screen.
        screenUpdates = new ScreenUpdates();
        Logger.i(DONE + "Verify the NUS page loads properly.");

        Logger.i(PASS + "34632519");
    }

    /**
     * Test case 34632709: 'NUS - LinkedIn Today.'
     * 
     * Login as test user. Open Updates screen. Open LINKEDIN TODAY screen. Tap
     * on news big image. Verify 'News Article Details' screen. Tap on 'Message'
     * button. Verify 'New Message' screen. Tap on 'Add Connections' button.
     * Verify 'Add Connections' screen. Choose connection. Tap on 'Done' button.
     * Verify that user returned to the 'New Message' screen. Tap on 'Send'
     * button. Verify that user returned to the 'News Article Details' screen.
     * Tap on 'Forward' button. Verify 'Share News Article' screen. Type random
     * comment. Tap on 'Share' button. Verify that user returned to the 'News
     * Article Details' screen. Tap on article title. Hit back. Tap on article
     * image. Hit back. Go to the LINKEDIN TODAY screen. Scroll down quickly to
     * the bottom of the articles list. Verify that all the rows load properly,
     * images is downloaded. Scroll to top. Verify that all the rows load
     * properly, images is downloaded. Open first news article. Tap on the Up
     * and Down arrow multiple times to see other articles. Hit back (you are
     * back on the LINKEDIN TODAY screen).
     */
    public void test34632709() {
        startFixture("34632709");
        Logger.i(START_TEST + "34632709: 'NUS - LinkedIn Today.'");

        // Open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open LINKEDIN TODAY screen.
        ScreenLinkedInToday linkedInTodayScreen = screenUpdates.openLinkedInTodayScreen();
        Logger.i(DONE + "Verify that you on LINKEDIN TODAY screen.");

        // Tap on news big image.
        linkedInTodayScreen.tapOnNewsBigImage();

        // Verify 'News Article Details' screen.
        ScreenNewsArticleDetailsFromLinkedInToday articleScreen;
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();
        Logger.i(DONE + "Verify that you on 'News Article Details' screen.");

        // Tap on 'Message' button. Verify 'New Message' screen.
        ScreenNewMessage newMessageScreen = articleScreen.openNewMessageScreen();
        Logger.i(DONE + "Verify the compose message screen shows up.");

        // Tap on 'Add Connections' button. Verify 'Add Connections' screen.
        ScreenAddConnections connectionsScreen = newMessageScreen.openAddConnectionsScreen();

        // Choose connection.
        connectionsScreen.chooseConnection(null);

        // Tap on 'Done' button.
        connectionsScreen.tapOnDoneButton();

        // Verify that user returned to the 'New Message' screen.
        newMessageScreen = new ScreenNewMessage();

        newMessageScreen.tapOnSendButton();
        Logger.i(DONE + " Verify 'Message sent' toast is shown.");

        // Verify that user returned to the 'News Article Details' screen.
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Tap on 'Forward' button. Verify 'Share News Article' screen.
        ScreenShareNewsArticle shareScreen = articleScreen.openShareNewsArticleScreen();
        Logger.i(DONE + "Verify the Share compose screen shows up.");

        // Type random comment.
        shareScreen.typeRandomComment();

        // Tap on 'Share' button.
        shareScreen.tapOnShareButton();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Verify that user returned to the 'News Article Details' screen.
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Tap on article title. Verify Browser screen.
        articleScreen.tapOnAtricleTitle();
        @SuppressWarnings("unused")
        ScreenBrowser browserScreen = new ScreenBrowser();
        Logger.i(DONE + "Verify the browser is launched and reaches the article URL.");

        // Tap on 'Back' button. Verify that user returned to the 'News Article
        // Details' screen.
        HardwareActions.pressBack();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();
        Logger.i(DONE + "Verify that 'News Article Details' screen reloads properly.");

        // Tap on article image. Verify Browser screen.
        articleScreen.tapOnArticleImage();
        browserScreen = new ScreenBrowser();
        Logger.i(DONE + "Verify the browser is launched and reaches the article URL.");

        // Tap on 'Back' button. Verify that user returned to the 'News Article
        // Details' screen.
        HardwareActions.pressBack();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();
        Logger.i(DONE + "Verify that 'News Article Details' screen reloads properly.");

        // Tap on 'Back' button. Verify that user returned to the LINKEDIN TODAY
        // screen.
        HardwareActions.pressBack();
        linkedInTodayScreen = new ScreenLinkedInToday();
        Logger.i(DONE + "Verify that LINKEDIN TODAY screen reloads properly.");

        // Scroll down quickly to the bottom of the articles list.
        Logger.i("Scroll down quickly to the bottom of the articles list.");
        getSolo().scrollToBottom();
        HardwareActions.waitForScreenUpdate(); // To new content loaded.

        // Verify that all the rows load properly, images is downloaded.
        linkedInTodayScreen.verifyNewsList();
        Logger.i(DONE + "Verify that all the rows load properly, images is downloaded.");

        // Scroll up quickly to the top of the articles list.
        Logger.i("Scroll up quickly to the top of the articles list.");
        getSolo().scrollToTop();
        HardwareActions.waitForScreenUpdate(); // To new content loaded.

        // Verify that all the rows load properly, images is downloaded.
        linkedInTodayScreen.verifyNewsList();
        Logger.i(DONE + "Verify that all the rows load properly, images is downloaded.");

        // Open first news article.
        articleScreen = linkedInTodayScreen.openFirstNewsArticleDetailsScreen();
        Logger.i(DONE + "Verify that you on 'News Article Details' screen.");

        // Tap on 'Down Arrow' button. Verify 'News Article Details' screen.
        articleScreen.tapOnDownArrowButton();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Tap on 'Down Arrow' button. Verify 'News Article Details' screen.
        articleScreen.tapOnDownArrowButton();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Tap on 'Up Arrow' button. Verify 'News Article Details' screen.
        articleScreen.tapOnUpArrowButton();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();

        // Tap on 'Up Arrow' button. Verify 'News Article Details' screen.
        articleScreen.tapOnUpArrowButton();
        articleScreen = new ScreenNewsArticleDetailsFromLinkedInToday();
        Logger.i(DONE + "Verify that each 'News Article Details' screen loads properly.");

        // Tap on 'Back' button. Verify that user returned to the LINKEDIN TODAY
        // screen.
        HardwareActions.pressBack();
        linkedInTodayScreen = new ScreenLinkedInToday();
        Logger.i(DONE + "Verify that LINKEDIN TODAY screen reloads properly.");

        Logger.i(PASS + "34632709");
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
        newMessage.verifyToast("Message sent");
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
        screenDiscussionDetailsFromRecentUpdates.tapOnLikeButton();
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
     * Test case 35974115: 'NUS - Viral Update' Steps: 1- From the Updates page,
     * tap on a Viral Update. 2- Tap on the Profile. 3- Hit back. 4- Tap on the
     * like button. 5- Tap on Comment button. 6- Comment on it and tap on send.
     * 7- Tap on Share. 8- Tap on Share from the options. 9 - Comment on the
     * article and tap on share
     * 
     * Expected Result: 1- The Feed Details Page shows up. 2- Profile page shows
     * up. 3- Feed Detail shows up. 4- Update liked toast is shown and (n)
     * person liked this is shown on the feed detail. 5- Comment compose screen
     * shows up. 6- Posting Comment and comment posted toast is shown. 7- Share
     * options show up. 8- Comment compose show up. 9 - Feed Detail is shown.
     */
    @SuppressWarnings("null")
    public void test35974115() {
        //TODO startFixture("35974115");
        Logger.i(START_TEST + "35974115: 'NUS - Viral Update'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        screenUpdates.openFirstNewsArticleDetailsScreen();
        ScreenSharedNewsDetails newsDetails = null;
        // Assert.assertNotNull("Can not find any shared news", newsDetails);
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

        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35974115");
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
    public void test35975187() {
        // startFixture("35975187");
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
        shareNews = screenBrowser.openShareNewsArticleScreen();
        Logger.i(DONE + "Comment compose show up");

        // STEP 18
        shareNews.typeRandomComment();
        shareNews.tapOnShareButton();
        shareNews.verifyToast("Update sent");
        screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "The Feed Details Page shows up");

        // STEP 19
        ScreenNewMessage message = screenBrowser.openNewMessageScreen();
        Logger.i(DONE + "Comment compose show up");

        // STEP 20
        connectionsScreen = message.openAddConnectionsScreen();
        connectionsScreen.chooseConnection(null);
        connectionsScreen.tapOnDoneButton();
        message = screenBrowser.openNewMessageScreen();
        message.tapOnSendButton();
        screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "Send message and Message sent toast is shown");

        // STEP 21
        screenBrowser.tapOnForwardButton();
        screenBrowser.tapOnOpenInBrowserOnPopup();
        // TODO: add browser checking
        HardwareActions.delay(30);
        Logger.i("Activity: " + getSolo().getCurrentActivity().getClass().getCanonicalName());
        Logger.i(DONE + "Browser is shown");

        // STEP 22
        HardwareActions.pressBack();
        screenBrowser = new ScreenBrowser();
        Logger.i(DONE + "Previous page is shown");

        // STEP 23
        HardwareActions.pressBack();
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35975187");
    }

    /**
     * Test case 35974205: 'NUS - New Connection Roll up'. Steps: 1 - From the
     * NUS page, tap on Connection roll up. 2 - Tap on one of the roll up in the
     * details page. 3 - Tap on your connections Profile. 4 - Hit back. 5 - Tap
     * on Your Connection's connection. 6 - Hit back twice.. 7 - Hit Back one
     * more time.
     * 
     * Expected results: 1. Details Page is shown.All your connection's new
     * connections are shown. 2.Your Connection's new connections are displayed.
     * 3. Profile Page is displayed and he is your 1st degree connection. 4.
     * Previous page is shown. 5. Profile page is shown and he is yours 2nd
     * degree connection. 6. Previous pages is shown. 7- NUS page is shown.
     */
    public void test35974205() {
        startFixture("35974205");
        Logger.i(START_TEST + "35974205: 'NUS - New Connection Roll up'");

        // STEP 1
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        ScreenNewConnectionsRollUp rollUp = screenUpdates.openNewConnectionsRollUpScreen();
        Logger.i(DONE + "The New Connections Roll Up Page shows up");

        // STEP 2
        ScreenNewConnectionDetails rollUpDetails = rollUp.openNewConnectionDetails();
        Logger.i(DONE + "The New Connections Details Page shows up");

        // STEP 3
        ScreenProfileOfConnectedUser profile1 = rollUpDetails.openYourConectionProfile();
        Assert.assertTrue("Degree not found or not eqvivalent 1", profile1.getNumberOfDegree() == 1);
        Logger.i(DONE + "The Your Connection Profile Page shows up and verify degree");

        // STEP 4
        HardwareActions.pressBack();
        rollUpDetails = new ScreenNewConnectionDetails();
        Logger.i(DONE + "The New Connections Details Page shows up");

        // STEP 5
        ScreenProfileOfNotConnectedUser p = rollUpDetails.openConnectionsConnectionProfile();
        Assert.assertTrue("Degree not found or not eqvivalent 2", p.getNumberOfDegree() == 2);
        Logger.i(DONE + "The Connection`s Connection Profile Page shows up and verify degree");

        // STEP 6
        HardwareActions.pressBack();
        rollUpDetails = new ScreenNewConnectionDetails();
        HardwareActions.pressBack();
        rollUp = new ScreenNewConnectionsRollUp();
        Logger.i(DONE + "The New Connections Roll Up Page shows up");

        // STEP 7
        HardwareActions.pressBack();
        HardwareActions.scrollUp();
        screenUpdates = new ScreenUpdates();
        Logger.i(DONE + "The NUS Page shows up");

        Logger.i(PASS + "35974205");
    }
}
