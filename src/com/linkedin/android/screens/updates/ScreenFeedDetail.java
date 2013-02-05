package com.linkedin.android.screens.updates;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.popups.Popup;
import com.linkedin.android.popups.PopupForward;
import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.base.BaseScreenSharedNewsDetails;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.more.ScreenCompanyDetails;
import com.linkedin.android.screens.more.ScreenGroupsDiscussionList;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.DetailsScreensUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewChecker;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Feed Detail actions.
 * 
 * @author alexander.makarov
 * @created Jan 14, 2013 6:20:42 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenFeedDetail extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.home.v2.StreamDetailActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "StreamDetailActivity";

    private static final String LIKE_LIST_LABEL = "people like this";
    private static final ViewIdName NAVIGATION_BAR_TITLE_ID_NAME = new ViewIdName(
            "navigation_bar_title");
    private static final ViewIdName COMMEN_ROW_ID_NAME = new ViewIdName("comments_row");
    private static final ViewIdName TWITER_AND_HASH_TAG_ID_NAME = new ViewIdName("text1");
    private static final ViewIdName ROLLUP_HEADER_ID_NAME = new ViewIdName("rollup_header");
    private static final ViewIdName HEADER_ID_NAME = new ViewIdName("header");
    private static final ViewIdName GROUP_ID_NAME = new ViewIdName("dnut1_text_1");
    private static final ViewIdName ALERT_MESSAGE_ID_NAME = new ViewIdName("message");
    private static final ViewIdName STREAM_DETAIL = new ViewIdName("stream_detail_root");

    private static final String LIKES_LABEL = "Likes";
    private static final String HASHTAG_LABEL = "#hash";
    protected static final Object TWITER_LABEL = "Twitter";
    private static final String TWIT_LABEL = "@twit";
    private static final String TITLE_ALERT = "Members Only Group";
    private static final String TEXT_ALERT = "join this group?";
    private static final int LINK_COLOR = -14914655;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenFeedDetail() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        WaitActions.waitForTrueInFunction("Stream detail root is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return Id.getViewByViewIdName(STREAM_DETAIL) != null;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "StreamDetail");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Opens first Update with footer.
     * 
     * @return {@code ScreenUpdates} object.
     */
    public static ScreenUpdates openUpdateWithFooter() {
        int maxCountOfScrolls = 50;
        boolean isCanScroll = true;

        for (int i = 0; i < maxCountOfScrolls && isCanScroll; i++) {
            ArrayList<View> nusViews = Id.getListOfViewByViewIdName(ScreenUpdates.NUS_LAYOUT);
            Assert.assertTrue("No updates found on Updates screen", nusViews.size() != 0);
            for (View nusView : nusViews) {
                int nusChildsCount = ((RelativeLayout) nusView).getChildCount();
                for (int j = 0; j < nusChildsCount; j++) {
                    View view = ((RelativeLayout) nusView).getChildAt(j);
                    if (view instanceof RelativeLayout) {
                        view.measure(ScreenResolution.getScreenWidth(),
                                ScreenResolution.getScreenHeight());
                        if (view.getMeasuredWidth() > 0) {
                            ViewUtils.tapOnView(nusView, "first Update with footer");
                            return new ScreenUpdates();
                        }
                    }
                }
            }
            isCanScroll = ListViewUtils.scrollToNewItems();
        }
        Assert.fail("Cannot find Update with footer.");
        return null;
    }

    @TestAction(value = "go_to_feed_detail")
    public static void go_to_feed_detail(String email, String password) {
        LoginActions.openUpdatesScreenOnStart();
        openUpdateWithFooter();
        TestUtils.delayAndCaptureScreenshot("go_to_feed_detail");
    }

    @TestAction(value = "feed_detail_tap_back")
    public static void feed_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_back");
    }

    @TestAction(value = "feed_detail_tap_back_reset")
    public static void feed_detail_tap_back_reset() {
        openUpdateWithFooter();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_back_reset");
    }

    @TestAction(value = "feed_detail_tap_expose")
    public static void feed_detail_tap_expose() {
        new ScreenUpdate().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_expose");
    }

    @TestAction(value = "feed_detail_tap_expose_reset")
    public static void feed_detail_tap_expose_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_expose_reset");
    }

    @TestAction(value = "feed_detail_tap_profile_author_precondition")
    public static void feed_detail_tap_profile_author_precondition(final String textInFeed) {
        View connectionProfileSectionChevron = Id
                .getViewByViewIdName(BaseScreenSharedNewsDetails.CONNECTION_PROFILE_SECTION_CHEVRON);

        if (!getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ScreenFeedDetail.ACTIVITY_SHORT_CLASSNAME)
                || connectionProfileSectionChevron == null) {
            HardwareActions.goBackOnPreviousActivity();
            new ScreenUpdates();

            View rollupView = ViewUtils.scrollDownToViewById(
                    BaseScreenSharedNewsDetails.CONNECTION_PROFILE_SECTION_CHEVRON,
                    DataProvider.DEFAULT_SCROLLS_COUNT, new ViewChecker() {
                        public boolean check(View view) {
                            return ViewGroupUtils.findChildThatContainText(view, textInFeed, false) != null;
                        }
                    });
            Assert.assertNotNull("Cannot find feed with text '" + textInFeed + "'", rollupView);
            ViewGroupUtils.tapFirstViewInLayout((ViewGroup) rollupView, true,
                    "first update with text '" + textInFeed + "'", null);
            new ScreenFeedDetail();
        }
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_author_precondition");
    }

    @TestAction(value = "feed_detail_tap_profile_author")
    public static void feed_detail_tap_profile_author() {
        DetailsScreensUtils.openProfileAuthor();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_author");
    }

    @TestAction(value = "feed_detail_tap_profile_author_reset")
    public static void feed_detail_tap_profile_author_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_author_reset");
    }

    @TestAction(value = "feed_detail_tap_company")
    public static void feed_detail_tap_company() {
        ScreenUpdate.tapOnConnectionProfile();
        new ScreenCompanyDetails();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_company");
    }

    @TestAction(value = "feed_detail_tap_profile_new_connection")
    public static void feed_detail_tap_profile_new_connection() {
        TextView titleView = (TextView) Id.getViewByViewIdName(ROLLUP_HEADER_ID_NAME);
        Assert.assertNotNull("Rollup Header is not present", titleView);
        String oldText = titleView.getText().toString();
        ViewUtils.tapOnView(titleView, "RollUp");
        new ScreenNewConnectionDetails();
        titleView = (TextView) Id.getViewByViewIdName(HEADER_ID_NAME);
        Assert.assertNotNull("Rollup Header is not present", titleView);
        Assert.assertTrue("Screen opens the wrong people",
                oldText.indexOf(titleView.getText().toString()) != -1);
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_new_connection");
    }

    @TestAction(value = "feed_detail_tap_update")
    public static void feed_detail_tap_update() {
        TextView titleView;
        int maxCountScroll = 5;
        do {
            titleView = (TextView) Id.getViewByViewIdName(HEADER_ID_NAME);
            if (titleView != null)
                break;
            getSolo().scrollDown();

            // Wait for end animations scroll.
            WaitActions.waitForScreenUpdate();
        } while (maxCountScroll-- > 0);
        Assert.assertNotNull("Update is not present", titleView);
        String oldText = titleView.getText().toString();
        ViewUtils.tapOnView(titleView, "RollUp");
        new ScreenNewConnectionDetails();
        titleView = (TextView) Id.getViewByViewIdName(HEADER_ID_NAME);
        Assert.assertNotNull("Header is not present", titleView);
        Assert.assertTrue("Not reach the relevant profile page", titleView.getText().toString()
                .equals(oldText));
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_update");
    }

    @TestAction(value = "feed_detail_tap_url")
    public static void feed_detail_tap_url() {
        ArrayList<TextView> list = getSolo().getCurrentTextViews(null);
        TextView view = null;
        Assert.assertTrue("Cannot find links (LinkTextViews) on this screen", list.size() > 0);
        for (TextView textView : list) {
            if (textView.getTextColors().getDefaultColor() == LINK_COLOR
                    && textView.getText().length() > 0) {
                view = textView;
            }
        }
        Assert.assertNotNull("Cannot find links on this screen", view);
        ViewUtils.tapOnView(view, view.getText().toString());
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_url");
    }

    @TestAction(value = "feed_detail_tap_url_reset")
    public static void feed_detail_tap_url_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_url_reset");
    }

    @TestAction(value = "feed_detail_tap_like_toggle")
    public static void feed_detail_tap_like_toggle() {
        final ScreenUpdate screenUpdate = new ScreenUpdate();
        int countOfLikes = DetailsScreensUtils.getLikesCount();
        screenUpdate.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        countOfLikes = DetailsScreensUtils.getLikesCount();
        screenUpdate.tapOnLikeButton();
        DetailsScreensUtils.verifyCountOfLikesChanged(countOfLikes);
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_like_toggle");
    }

    @TestAction(value = "feed_detail_tap_actionsheet")
    public static void feed_detail_tap_actionsheet() {
        new ScreenUpdate().tapOnForwardButton();
        new PopupForward(PopupForward.SEND_TO_CONNECTION_TEXT, PopupForward.SHARE_TEXT,
                PopupForward.REPLY_PRIVATELY_TEXT);
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_actionsheet");
    }

    @TestAction(value = "feed_detail_tap_company_reset")
    public static void feed_detail_tap_company_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_company_reset");
    }

    @TestAction(value = "feed_detail_tap_comment")
    public static void feed_detail_tap_comment() {
        new ScreenUpdate().tapOnCommentButton();
        new ScreenAddComment();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_comment");
    }

    @TestAction(value = "feed_detail_tap_likes_list")
    public static void feed_detail_tap_likes_list() {
        getSolo().searchText(LIKE_LIST_LABEL);
        ViewUtils.tapOnView(TextViewUtils.getTextViewByPartialText(LIKE_LIST_LABEL), "Like list");
        WaitActions.waitForTrueInFunction("Screen 'Like list' is not present",
                new Callable<Boolean>() {

                    @Override
                    public Boolean call() {
                        TextView titleView = (TextView) Id
                                .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                        if (titleView == null)
                            return false;
                        return titleView.getText().toString().equals(LIKES_LABEL);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_likes_list");
    }

    /**
     * Returns {@code Solo} object
     * 
     * @return {@code Solo} object
     */
    protected static Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }

    @TestAction(value = "feed_detail_tap_likes_list_reset")
    public static void feed_detail_tap_likes_list_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_likes_list_reset");
    }

    @TestAction(value = "feed_detail_tap_comment_reset")
    public static void feed_detail_tap_comment_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_comment_reset");
    }

    @TestAction(value = "feed_detail_tap_profile_commenter_reset")
    public static void feed_detail_tap_profile_commenter_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_commenter_reset");
    }

    @TestAction(value = "feed_detail_tap_profile_commenter")
    public static void feed_detail_tap_profile_commenter() {
        int maxScroll = 5;
        while (getSolo().scrollDown() && maxScroll-- > 0)
            ;
        View view = Id.getViewByViewIdName(COMMEN_ROW_ID_NAME);
        ViewUtils.tapOnView(view, "First commenter");
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_commenter");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_send_to_connection")
    public static void feed_detail_actionsheet_tap_send_to_connection() {
        new PopupForward(PopupForward.SEND_TO_CONNECTION_TEXT)
                .tapOnOption(PopupForward.SEND_TO_CONNECTION_TEXT);
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_send_to_connection");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_send_to_connection_reset")
    public static void feed_detail_actionsheet_tap_send_to_connection_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_send_to_connection_reset");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_reply_privately")
    public static void feed_detail_actionsheet_tap_reply_privately() {
        new PopupForward(PopupForward.REPLY_PRIVATELY_TEXT)
                .tapOnOption(PopupForward.REPLY_PRIVATELY_TEXT);
        new ScreenReplyMessage();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_reply_privately");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_reply_privately_reset")
    public static void feed_detail_actionsheet_tap_reply_privately_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_reply_privately_reset");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_share")
    public static void feed_detail_actionsheet_tap_share() {
        new PopupForward(PopupForward.SHARE_TEXT).tapOnOption(PopupForward.SHARE_TEXT);
        new ScreenShareUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_share");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_share_reset")
    public static void feed_detail_actionsheet_tap_share_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_share_reset");
    }

    @TestAction(value = "feed_detail_actionsheet_tap_cancel")
    public static void feed_detail_actionsheet_tap_cancel() {
        HardwareActions.pressBack();
        Assert.assertFalse("Actionsheet is not dissapear",
                getSolo().searchText(PopupForward.SHARE_TEXT, true));
        TestUtils.delayAndCaptureScreenshot("feed_detail_actionsheet_tap_cancel");
    }

    @TestAction(value = "feed_detail_tap_hashtag")
    public static void feed_detail_tap_hashtag() {
        TextView textView = (TextView) Id.getViewByViewIdName(TWITER_AND_HASH_TAG_ID_NAME);
        Assert.assertNotNull("Section 'Twiter and HashTag is not present'", textView);
        TextViewUtils.tapOnLinkInTextView(textView, HASHTAG_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG, "Title '" + TWITER_LABEL
                + "' is not present", new Callable<Boolean>() {

            @Override
            public Boolean call() {
                TextView titleView = (TextView) Id
                        .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if (titleView == null)
                    return false;
                return titleView.getText().toString().equals(TWITER_LABEL);
            }
        });
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_hashtag");
    }

    @TestAction(value = "feed_detail_tap_twiiter_handle")
    public static void feed_detail_tap_twiiter_handle() {
        TextView textView = (TextView) Id.getViewByViewIdName(TWITER_AND_HASH_TAG_ID_NAME);
        Assert.assertNotNull("Section 'Twiter and HashTag is not present'", textView);
        TextViewUtils.tapOnLinkInTextView(textView, TWIT_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG, "Title '" + TWIT_LABEL
                + "' is not present", new Callable<Boolean>() {

            @Override
            public Boolean call() {
                TextView titleView = (TextView) Id
                        .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if (titleView == null)
                    return false;
                if (titleView.getText().toString().indexOf(TWIT_LABEL) != -1)
                    return true;
                return false;
            }
        });
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_twiiter_handle");
    }

    @TestAction(value = "feed_detail_tap_hashtag_reset")
    public static void feed_detail_tap_hashtag_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_hashtag_reset");
    }

    @TestAction(value = "feed_detail_tap_twiiter_handle_reset")
    public static void feed_detail_tap_twiiter_handle_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdate();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_twiiter_handle_reset");
    }

    @TestAction(value = "feed_detail_rollup_list_tap_update_reset")
    public static void feed_detail_rollup_list_tap_update_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenNewConnectionsRollUp();
        TestUtils.delayAndCaptureScreenshot("feed_detail_rollup_list_tap_update_reset");
    }

    @TestAction(value = "feed_detail_tap_profile_new_connection_reset")
    public static void feed_detail_tap_profile_new_connection_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenNewConnectionsRollUp();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_profile_new_connection_reset");
    }

    @TestAction(value = "feed_detail_tap_group")
    public static void feed_detail_tap_group() {
        TextView textView = (TextView) Id.getViewByViewIdName(GROUP_ID_NAME);
        ViewUtils.tapOnView(textView, "Group");
        new ScreenGroupsDiscussionList();
        TestUtils.delayAndCaptureScreenshot("feed_detail_tap_group");
    }

    @TestAction(value = "feed_detail_join_closed_group_dialog_tap_cancel_precondition")
    public static void feed_detail_join_closed_group_dialog_tap_cancel_precondition() {
        TextView textView = (TextView) Id.getViewByViewIdName(GROUP_ID_NAME);
        ViewUtils.tapOnView(textView, "Group");
        new Popup(TITLE_ALERT, TEXT_ALERT);
        TestUtils
                .delayAndCaptureScreenshot("feed_detail_join_closed_group_dialog_tap_cancel_precondition");
    }

    @TestAction(value = "feed_detail_join_closed_group_dialog_tap_cancel")
    public static void feed_detail_join_closed_group_dialog_tap_cancel() {
        new Popup(TITLE_ALERT, TEXT_ALERT).tapOnButton(Popup.CANCEL_BUTTON);
        WaitActions.waitForTrueInFunction("Alert is not disappears", new Callable<Boolean>() {

            @Override
            public Boolean call() {
                return !getSolo().searchText(TITLE_ALERT, true);
            }
        });
        TestUtils.delayAndCaptureScreenshot("feed_detail_join_closed_group_dialog_tap_cancel");
    }

    @TestAction(value = "feed_detail_join_closed_group_dialog_tap_join")
    public static void feed_detail_join_closed_group_dialog_tap_join() {
        TextView textView = (TextView) Id.getViewByViewIdName(ALERT_MESSAGE_ID_NAME);
        Assert.assertNotNull("Alert message is not present", textView);
        String groupName = textView.getText().toString();

        int endName = groupName.indexOf('"', 1);
        groupName = groupName.substring(1, endName);
        new Popup(TITLE_ALERT, TEXT_ALERT).tapOnButton(Popup.OK_BUTTON);
        ScreenUpdate.tapOnINButton();
        new ScreenExpose(null).openGroupsAndMoreScreen().openGroupsScreen();
        WaitActions.waitForText(groupName, "Group is not present");
        TestUtils.delayAndCaptureScreenshot("feed_detail_join_closed_group_dialog_tap_join");
    }
}
