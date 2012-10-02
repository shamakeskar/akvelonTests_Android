package com.linkedin.android.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.test.suitebuilder.annotation.Suppress;
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
import com.linkedin.android.screens.common.ScreenJobDetails;
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
import com.linkedin.android.screens.updates.ScreenViralUpdate;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.utils.ContactInfoUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
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

        // Type test comment.
        String comment = "Test comment 0.42590884519218986";
        shareScreen.typeInComment(comment);

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
        startTest("34066559", "Add comment to news article from Recent updates.");

        // Login and open Updates screen.
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();

        // Open first news article from 'Recent updates' screen.
        ScreenNewsArticleDetails newsArticleDetailsScreen = screenUpdates
                .openFirstNewsArticleDetailsScreen();

        // Click on Forward button.
        ScreenShareNewsArticle shareNewsArticleScreen = newsArticleDetailsScreen
                .openShareNewsArticleScreen();

        // Test comment.
        String testComment = "Test comment 0.42590884519218986";

        // Type generated string to Comment field.
        shareNewsArticleScreen.typeInComment(testComment);

        // Click Share button.
        newsArticleDetailsScreen = shareNewsArticleScreen.shareComment();
        Logger.i(DONE + "Verify 'Update sent' toast is shown.");

        // Open Expose screen
        ScreenExpose screenExpose = newsArticleDetailsScreen.openExposeScreen();
        // Go to Updates screen.
        screenUpdates = screenExpose.openUpdatesScreen();

        // Scroll to the top of Update screen.
        HardwareActions.scrollUp();

        // Wait while shared item appears on Updates screen.
        final int updatesScreenUpdateWaitTime = 3;
        WaitActions.delay(updatesScreenUpdateWaitTime);
        
        // Check if update screen contains article with specified text.
        boolean isUpdateScreenContainArticleWithSpecifiedText = screenUpdates
                .isUpdateScreenContainArticleWithSpecifiedText(testComment);
        Assert.assertTrue("Test comment to news article was not shared.",
                isUpdateScreenContainArticleWithSpecifiedText);
        Logger.i(DONE
                + "Verify that the article is shared and is shown in Updates screen with the comment.");

        // Verify that image displayed along with summary for article with
        // comment.
        boolean isArticleHasImageDisplayedAlongWithSummary = screenUpdates
                .isArticleHasImageDisplayedAlongWithSummary(testComment);
        Assert.assertTrue(
                "There is no image displayed along with summary for article with comment: "
                        + testComment, isArticleHasImageDisplayedAlongWithSummary);
        Logger.i(DONE + "Verify that image displayed along with summary for article with comment.");

        passTest();
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

        final int sharedNewsArticleExpectedIndex = ScreenUpdates.UPDATES_LIST_FIRST_ITEM_INDEX;
        // Get first cell layout.
        RelativeLayout firstCellLayout = updatesScreen
                .getUpdatesListItemMainElementsParentLayout(sharedNewsArticleExpectedIndex);

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
        startTest("34066627",
                "Share the news article from the LinkedIn Today (original news category).");

        // Open Updates screen.
        ScreenUpdates updatesScreen = LoginActions.openUpdatesScreenOnStart();

        // Open LINKEDIN TODAY screen.
        ScreenLinkedInToday linkedInTodayScreen = updatesScreen.openLinkedInTodayScreen();

        // Open first News Article Details screen.
        ScreenNewsArticleDetailsFromLinkedInToday articleScreen;
        articleScreen = linkedInTodayScreen.openFirstNewsArticleDetailsScreen();

        // Save header of current Details screen.
        String articleHeader = articleScreen.getArticleHeader();
        final int shortenedArticleHeaderLength = 25;
        String shortenedArticleHeader = articleHeader.substring(shortenedArticleHeaderLength);

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

        final int sharedNewsArticleExpectedIndex = ScreenUpdates.UPDATES_LIST_FIRST_ITEM_INDEX + 1;
        // Get parent layout for main elements of shared news article.
        RelativeLayout sharedNewsArticleLayout = updatesScreen
                .getUpdatesListItemMainElementsParentLayout(sharedNewsArticleExpectedIndex);

        // Verify that the article with specified header is shown in Updates
        // screen.
        Assert.assertNotNull("The article with header '" + articleHeader + "'"
                + " is not present on Updates screen.", ViewUtils.searchTextViewInLayout(
                shortenedArticleHeader, sharedNewsArticleLayout, false));
        Logger.i(DONE + "Verify that the article is shared and is shown in Updates screen.");

        // Get layout of article summary with image.
        RelativeLayout visibleArticleSummaryWithImageRelativeLayout = updatesScreen
                .getVisibleArticleSummaryWithImageRelativeLayout(sharedNewsArticleLayout,
                        articleHeader);

        // Verify article with specified header has image displayed along with
        // summary.
        Assert.assertTrue(
                "Article with specified header has no image displayed along with summary",
                updatesScreen.isImageWithSummaryPlacedInSpecifiedLayout(
                        visibleArticleSummaryWithImageRelativeLayout, articleHeader));
        Logger.i(DONE + "Verify that the article has image displayed along with summary.");

        passTest();
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
        WaitActions.delay(sharedNewsArticleAppearanceWaitTimeSec);
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
        ScreenYou screenYou = new ScreenYou();
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

        newMessage.sendMessage();
        Logger.i(DONE + " Send message and Message sent toast is shown");
        newsDetails = new ScreenSharedNewsDetails();
        Logger.i(DONE + "The Feed Details Page shows up");

        Logger.i(PASS + "35973635");
    }
}
