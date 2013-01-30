package com.linkedin.android.screens.inbox;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.popups.Popup;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.DetailsScreensUtils;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Inbox message detail screen
 * 
 * @author vasily.gancharov
 * @created Aug 28, 2012 14:26:35 PM
 */
public class ScreenMessageDetail extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.messages.ViewMessageActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewMessageActivity";

    private static final int MESSAGE_AUTHOR_NAMETEXT_INDEX = 4;
    private static final int MESSAGE_AUTHOR_STATUSTEXT_INDEX = 5;
    private static final int MESSAGE_CREATEDATE_INDEX = 8;
    private static final int MESSAGE_TEXTVIEW_INDEX = 9;

    private static final int DELETE_BUTTON_INDEX = 1;
    private static final int REPLY_BUTTON_INDEX = 2;
    private static final int COMPOSE_BUTTON_INDEX = 3;
    // private static final int COLOR_LINK = -16777216;

    private static final String MARKUNREAD_VIEWNAME = "'Mark unread'";

    private static final ViewIdName ARCHIVE_BUTTON_ID_NAME = new ViewIdName("archive_button");
    private static final ViewIdName DELETE_BUTTON_ID_NAME = new ViewIdName("delete_button");
    private static final ViewIdName NAVIGATION_BAR_TITLE_ID_NAME = new ViewIdName(
            "navigation_bar_title");
    private static final ViewIdName PREVIOUS_BUTTON_ID_NAME = new ViewIdName("nav_inbox_previouse");
    private static final ViewIdName NEXT_BUTTON_ID_NAME = new ViewIdName("nav_inbox_next");
    private static final ViewIdName HEADER_ID_NAME = new ViewIdName("header");
    private static final ViewIdName MARK_LABEL_ID_NAME = new ViewIdName("mark_unread");
    private static final ViewIdName REPLY_ACTIONSHEET_BUTTON_ID_NAME = new ViewIdName(
            "reply_button");
    private static final String REPLY_LABEL = "Reply";
    private static final String REPLY_ALL_LABEL = "Reply All";
    private static final String FORWARD_LABEL = "Forward";
    private static final ViewIdName COMPOSE_ACTIONSHEET_BUTTON_ID_NAME = new ViewIdName(
            "compose_button");
    private static final String NEW_MESSAGE_LABEL = "New Message";
    private static final String NEW_INVITATION_LABEL = "New Invitation";
    private static final String COMPOSE_LABEL = "Compose";

    private static CharSequence changingText;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenMessageDetail() {
        super(ACTIVITY_CLASSNAME);
    }

    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        ImageButton backToInboxButton = getSolo().getImageButton(0);
        ImageButton deleteButton = getSolo().getImageButton(DELETE_BUTTON_INDEX);
        ImageButton replyButton = getSolo().getImageButton(REPLY_BUTTON_INDEX);
        ImageButton composeButton = getSolo().getImageButton(COMPOSE_BUTTON_INDEX);

        Assert.assertNotNull("'Back To Inbox' button is not present", backToInboxButton);
        Assert.assertNotNull("'Delete' button is not present", deleteButton);
        Assert.assertNotNull("'Reply' button is not present", replyButton);
        Assert.assertNotNull("'Compose' button is not present", composeButton);
    }

    /**
     * Gets Message Author Avatar image.
     * 
     * @return if exist image {@code ImageView}, else <b>null</b>
     */
    @SuppressWarnings("unused")
    private ImageView getMessageAuthorAvatarImage() {
        for (ImageView view : getSolo().getCurrentImageViews()) {
            Rect2DP viewRect = new Rect2DP(view);
            if ((viewRect.height == 50.0f && viewRect.width == 50.0f && viewRect.x == 15.0f && viewRect.y == 101.0f)
                    || (view.getId() == 2131230728)) {
                return view;
            }
        }
        return null;
    }

    /**
     * Verifies if there are any hyperlinks in message body and taps on first
     * found one.
     */
    public void tapOnFirstHyperlinkInMessageBody() {
        TextView messageTextView = getMessageTextView();
        Assert.assertNotNull("There is no message TextView on screen.", messageTextView);
        String messageText = messageTextView.getText().toString();
        String firstHyperlinkInMessage = RegexpUtils.getFirstFoundGroup(messageText,
                RegexpUtils.HYPERLINK_PATTERN);
        Assert.assertNotNull("There is no hyperlinks in message.", firstHyperlinkInMessage);
        TextViewUtils.clickOnTextViewSubstring(messageTextView, firstHyperlinkInMessage);
    }

    /**
     * Open Replay Screen
     * 
     * @return TODO create new 'Reply' screen
     */
    public void openReplayScreen() {
        getSolo().clickOnButton(REPLY_BUTTON_INDEX);

        // Get TextView by text
        TextView item = getSolo().getText("Reply");

        // Tap on TextView
        ViewUtils.tapOnView(item, "Reply");
    }

    /**
     * Open Forward Screen
     * 
     * @return TODO create new 'Forward' screen
     */
    public void openForwardScreen() {
        getSolo().clickOnButton(REPLY_BUTTON_INDEX);

        // Get TextView by text
        TextView item = getSolo().getText("Forward");

        // Tap on TextView
        ViewUtils.tapOnView(item, "Forward");
    }

    /**
     * Get Message Subject From Message Detail Screen
     * 
     * @return Message Subject
     */
    public String getMessageSubjectFromMessageDetailScreen() {
        TextView messageTitleTextView = getMessageTitleTextView();
        String text = messageTitleTextView.getText().toString();

        return text;
    }

    /**
     * Open New Message Screen
     * 
     * @return ScreenNewMessage
     */
    public ScreenNewMessage openNewMessageScreen() {
        getSolo().clickOnButton(COMPOSE_BUTTON_INDEX);

        // Get TextView by text
        TextView item = getSolo().getText("New Message");

        // Tap on TextView
        ViewUtils.tapOnView(item, "New Message");

        return new ScreenNewMessage();
    }

    /**
     * Open Invite Screen
     * 
     * @return TODO create new screen - 'Invite'
     */
    public void openInviteScreen() {
        getSolo().clickOnButton(COMPOSE_BUTTON_INDEX);

        // Get TextView by text
        TextView item = getSolo().getText("New Invitation");

        // Tap on TextView
        ViewUtils.tapOnView(item, "New Invitation");
    }

    /**
     * Open Delete Action Sheet From Message Detail Screen
     * 
     * @return ScreenProfile
     */
    public Popup openDeleteActionSheetFromMessageDetailScreen() {
        // Tap on ImageButton
        View delete = Id.getViewByViewIdName(DELETE_BUTTON_ID_NAME);
        ViewUtils.tapOnView(delete, "'Delete' button", true);

        Popup popup = new Popup("Delete Message", null);
        return popup;
    }

    /**
     * Open Sender Profile Screen
     * 
     * @return ScreenProfile
     */
    public ScreenProfile openSenderProfileScreen() {
        TextView messageAuthorNameTextView = getMessageTitleTextView();
        getSolo().clickOnView(messageAuthorNameTextView);

        return new ScreenProfile();
    }

    /**
     * Open Next Message
     * 
     * @return ScreenMessageDetail
     */
    public ScreenMessageDetail openNextMessage() {
        ImageView upArrowButton = getupArrowButton();
        getSolo().clickOnView(upArrowButton);

        return new ScreenMessageDetail();
    }

    /**
     * Open Previous Message
     * 
     * @return ScreenMessageDetail
     */
    public ScreenMessageDetail openPreviousMessage() {
        ImageView upArrowButton = getupArrowButton();
        getSolo().clickOnView(upArrowButton);

        return new ScreenMessageDetail();
    }

    /**
     * Verify Up and Down Arrow Buttons
     */
    public void verifyUpDownArrowButtons() {
        ImageView upArrowButton = getupArrowButton();
        Assert.assertNotNull("'Up Arrow' button is not present (or its coordinates are wrong)",
                upArrowButton);

        ImageView downArrowButton = getDownArrowButton();
        Assert.assertNotNull("'Down Arrow' button is not present (or its coordinates are wrong)",
                downArrowButton);
    }

    /**
     * Get Down Arrow Button
     * 
     * @return ImageView
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = (ImageView) Id.getViewByViewIdName(PREVIOUS_BUTTON_ID_NAME);
        return downArrowButton;
    }

    /**
     * Get Up Arrow Button
     * 
     * @return ImageView
     */
    public ImageView getupArrowButton() {
        ImageView upArrowButton = (ImageView) Id.getViewByViewIdName(NEXT_BUTTON_ID_NAME);
        return upArrowButton;
    }

    // /**
    // * Verify message text is not empty
    // */
    // private void verifyMessageText() {
    // String messageText = getMessageText();
    // Assert.assertTrue("'Message text' is empty",
    // !StringUtils.isNullOrWhitespace(messageText));
    // }
    //
    // /**
    // * Verify message created date text is not empty
    // */
    // private void verifyMessageCreatedDateText() {
    // TextView messageCreatedDateTextView = getMessageCreatedDateTextView();
    // String createdDateText = messageCreatedDateTextView.getText().toString();
    //
    // Assert.assertTrue("'Message created text' is empty",
    // !StringUtils.isNullOrWhitespace(createdDateText));
    // }
    //
    // /**
    // * Verify message title text is not empty
    // */
    // private void verifyMessageTitleText() {
    // String text = getMessageSubjectFromMessageDetailScreen();
    //
    // Assert.assertTrue("'Message title text' is empty",
    // !StringUtils.isNullOrWhitespace(text));
    // }
    //
    // /**
    // * Verify message author status text is not empty
    // */
    // private void verifyMessageAuthorStatusText() {
    // TextView messageAuthorStatusTextView = getMessageTitleTextView();
    // String text = messageAuthorStatusTextView.getText().toString();
    //
    // Assert.assertTrue("'Message Author Status text' is empty",
    // !StringUtils.isNullOrWhitespace(text));
    // }
    //
    // /**
    // * Verify message author name text is not empty
    // */
    // private void verifyMessageAuthorNameText() {
    // TextView messageAuthorNameTextView = getMessageTitleTextView();
    // String text = messageAuthorNameTextView.getText().toString();
    //
    // Assert.assertTrue("'Message Author Status text' is empty",
    // !StringUtils.isNullOrWhitespace(text));
    // }
    //
    // /**
    // * Verify '% of %' lable text
    // */
    // private void verifyOFLable() {
    // Assert.assertTrue("Title '.. of ..' is not present",
    // getSolo().getText(0).getText()
    // .toString().indexOf("of") > -1);
    // }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Message Details");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Gets TextView for current message text
     * 
     * @return Message TextView
     */
    public TextView getMessageTextView() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MESSAGE_TEXTVIEW_INDEX);

        Assert.assertNotNull("Message text is not present", textView);
        return textView;
    }

    /**
     * Gets TextView for current message created date
     * 
     * @return Message created date TextView
     */
    public TextView getMessageCreatedDateTextView() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MESSAGE_CREATEDATE_INDEX);

        Assert.assertNotNull("Message created date text is not present", textView);
        return textView;
    }

    /**
     * Gets TextView for current message Title
     * 
     * @return Message Title TextView
     */
    public TextView getMessageTitleTextView() {
        TextView textView = (TextView) Id.getViewByViewIdName(HEADER_ID_NAME);
        Assert.assertNotNull("Message Title text is not present", textView);
        return textView;
    }

    /**
     * Gets TextView for current message Author Status Text
     * 
     * @return Message Author Status Text TextView
     */
    public TextView getMessageAuthorStatusTextTextView() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MESSAGE_AUTHOR_STATUSTEXT_INDEX);

        Assert.assertNotNull("Message Author Status Text text is not present", textView);
        return textView;
    }

    /**
     * Gets TextView for current message Author Name
     * 
     * @return Message Author Name TextView
     */
    public TextView getMessageAuthorNameTextView() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MESSAGE_AUTHOR_NAMETEXT_INDEX);

        Assert.assertNotNull("Message Author Name text is not present", textView);
        return textView;
    }

    /**
     * Gets Text for current message
     * 
     * @return text of current message
     */
    public String getMessageText() {
        TextView messageText = getMessageTextView();
        return messageText.getText().toString();
    }

    /**
     * Taps on 'MarkUnRead' text button.
     */
    public void tapOnMarkUnread() {
        TextView textView = getMarkUnreadButton();

        Logger.i("Tap to 'MarkUnRead' button");
        ViewUtils.tapOnView(textView, MARKUNREAD_VIEWNAME);
    }

    /**
     * Verifies 'Mark unread' text button.
     */
    public void verifyMarkUnreadButton() {
        TextView textView = getMarkUnreadButton();

        Assert.assertNotNull("'Mark unread' text button is not presented", textView);
    }

    /**
     * Get 'Mark unread' TextView
     * 
     * @return 'Mark unread' TextView
     */
    public static TextView getMarkUnreadButton() {
        TextView textView = (TextView) Id.getViewByViewIdName(MARK_LABEL_ID_NAME);
        return textView;
    }

    @TestAction(value = "go_to_inbox_message_detail")
    public static void go_to_inbox_message_detail(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        ScreenExpose.expose_tap_inbox();
        ScreenInbox.inbox_tap_all_mail();
        ScreenAllMessages.inbox_mail_all_tap_message("go_to_inbox_message_detail");
    }

    @TestAction(value = "inbox_message_detail_tap_back")
    public static void inbox_message_detail_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_back");
    }

    @TestAction(value = "inbox_message_detail_tap_back_reset")
    public static void inbox_message_detail_tap_back_reset() {
        ScreenAllMessages.inbox_mail_all_tap_message("inbox_message_detail_tap_back_reset");
    }

    @TestAction(value = "inbox_message_detail")
    public static void inbox_message_detail() {
        ScreenAllMessages.inbox_mail_all_tap_message("inbox_message_detail");
    }

    @TestAction(value = "inbox_message_detail_tap_message_up")
    public static void inbox_message_detail_tap_message_up() {
        TextView changingTextView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
        Assert.assertNotNull("Navigation bar title is not present", changingTextView);
        changingText = changingTextView.getText();
        DetailsScreensUtils.tapUpButton();
        WaitActions.waitForTrueInFunction("New message is not loaded", new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                TextView titleView = (TextView) Id
                        .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if (titleView == null)
                    return false;
                return !changingText.equals(titleView.getText());
            }
        });
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_message_up");
    }

    @TestAction(value = "inbox_message_detail_tap_message_down")
    public static void inbox_message_detail_tap_message_down() {
        TextView changingTextView = (TextView) Id.getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
        Assert.assertNotNull("Navigation bar title is not present", changingTextView);
        changingText = changingTextView.getText();
        DetailsScreensUtils.tapDownButton();
        WaitActions.waitForTrueInFunction("New message is not loaded", new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                TextView titleView = (TextView) Id
                        .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                if (titleView == null)
                    return false;
                return !changingText.equals(titleView.getText());
            }
        });
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_message_down");
    }

    @TestAction(value = "inbox_message_detail_tap_profile")
    public static void inbox_message_detail_tap_profile() {
        new ScreenMessageDetail().openSenderProfileScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_profile");
    }

    @TestAction(value = "inbox_message_detail_tap_archive")
    public static void inbox_message_detail_tap_archive() {
        View arch = Id.getViewByViewIdName(ARCHIVE_BUTTON_ID_NAME);
        ViewUtils.tapOnView(arch, "'Archive' button", true);
        new ScreenAllMessages();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_archive");
    }

    @TestAction(value = "inbox_message_detail_toggle_read_state")
    public static void inbox_message_detail_toggle_read_state() {
        TextView markButton = getMarkUnreadButton();
        Assert.assertNotNull("Text mark/unmark is not present", markButton);
        changingText = markButton.getText();
        new ScreenMessageDetail().tapOnMarkUnread();
        WaitActions.waitForTrueInFunction("Text mark/unmark no changed", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                TextView markButton = getMarkUnreadButton();
                if (markButton == null)
                    return false;
                return !changingText.equals(markButton.getText());
            }
        });
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_toggle_read_state");
    }

    @TestAction(value = "inbox_message_detail_tap_delete")
    public static void inbox_message_detail_tap_delete() {
        new ScreenMessageDetail().openDeleteActionSheetFromMessageDetailScreen().tapOnButton(
                Popup.YES_BUTTON);
        new ScreenAllMessages();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_delete");
    }

    @TestAction(value = "inbox_message_detail_tap_respond_actionsheet")
    public static void inbox_message_detail_tap_respond_actionsheet() {
        View reply = Id.getViewByViewIdName(REPLY_ACTIONSHEET_BUTTON_ID_NAME);
        ViewUtils.tapOnView(reply, "'Reply Actionsheet' button", true);
        new Popup(REPLY_LABEL, REPLY_LABEL);
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_respond_actionsheet");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_reply")
    public static void inbox_message_detail_respond_actionsheet_tap_reply() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        int numLabel = 0;
        TextView reply = null;
        for (TextView textView : textViews) {
            if (textView.getText().equals(REPLY_LABEL)) {
                numLabel++;
                if (numLabel == 2) {
                    reply = textView;
                    break;
                }
            }
        }
        ViewUtils.tapOnView(reply, "'" + REPLY_LABEL + "' label");
        new ScreenReplyMessage();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_reply");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_reply_all")
    public static void inbox_message_detail_respond_actionsheet_tap_reply_all() {
        TextView reply = TextViewUtils.getTextViewByText(REPLY_ALL_LABEL);
        ViewUtils.tapOnView(reply, "'" + REPLY_ALL_LABEL + "' label");
        new ScreenReplyMessage();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_reply_all");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_forward")
    public static void inbox_message_detail_respond_actionsheet_tap_forward() {
        TextView forward = TextViewUtils.getTextViewByText(FORWARD_LABEL);
        ViewUtils.tapOnView(forward, "'" + FORWARD_LABEL + "' label");
        WaitActions.waitForTrueInFunction("'Forward screen' ia not present",
                new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        TextView textView = (TextView) Id
                                .getViewByViewIdName(NAVIGATION_BAR_TITLE_ID_NAME);
                        if (textView == null)
                            return false;
                        return FORWARD_LABEL.equals(textView.getText().toString());
                    }
                });
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_forward");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_cancel")
    public static void inbox_message_detail_respond_actionsheet_tap_cancel() {
        HardwareActions.pressBack();
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_cancel");
    }

    @TestAction(value = "inbox_message_detail_compose_actionsheet_tap_new_message")
    public static void inbox_message_detail_compose_actionsheet_tap_new_message() {
        TextView reply = TextViewUtils.getTextViewByText(NEW_MESSAGE_LABEL);
        ViewUtils.tapOnView(reply, "'" + NEW_MESSAGE_LABEL + "' label");
        new ScreenNewMessage();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_compose_actionsheet_tap_new_message");
    }

    @TestAction(value = "inbox_message_detail_tap_compose_actionsheet")
    public static void inbox_message_detail_tap_compose_actionsheet() {
        View reply = Id.getViewByViewIdName(COMPOSE_ACTIONSHEET_BUTTON_ID_NAME);
        ViewUtils.tapOnView(reply, "'Compose Actionsheet' button", true);
        new Popup(COMPOSE_LABEL, NEW_MESSAGE_LABEL);
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_compose_actionsheet");
    }

    @TestAction(value = "inbox_message_detail_compose_actionsheet_tap_new_invitation")
    public static void inbox_message_detail_compose_actionsheet_tap_new_invitation() {
        TextView reply = TextViewUtils.getTextViewByText(NEW_INVITATION_LABEL);
        ViewUtils.tapOnView(reply, "'" + NEW_INVITATION_LABEL + "' label");
        new ScreenNewInvitation();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_compose_actionsheet_tap_new_invitation");
    }

    @TestAction(value = "inbox_message_detail_compose_actionsheet_tap_cancel")
    public static void inbox_message_detail_compose_actionsheet_tap_cancel() {
        HardwareActions.pressBack();
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_compose_actionsheet_tap_cancel");
    }

    @TestAction(value = "inbox_message_detail_tap_expose")
    public static void inbox_message_detail_tap_expose() {
        new ScreenMessageDetail().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_expose");
    }

    @TestAction(value = "inbox_message_detail_tap_url")
    public static void inbox_message_detail_tap_url() {
        String textLink = null;
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        for (TextView textView : textViews) {
            textLink = RegexpUtils.getFirstFoundGroup(textView.getText().toString(),
                    RegexpUtils.HYPERLINK_PATTERN);
        }

        Solo solo = DataProvider.getInstance().getSolo();
        View view = null;
        try {
            view = solo.getText(textLink);
        } catch (Throwable e) {
            Assert.fail("Cannot find link '" + textLink + "' to tap.");
        }
        Assert.assertNotNull("Cannot find link '" + textLink + "' to tap.");
        solo.clickOnView(view);
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_url");
    }

    @TestAction(value = "inbox_message_detail_tap_profile_reset")
    public static void inbox_message_detail_tap_profile_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_profile_reset");
    }

    @TestAction(value = "inbox_message_detail_tap_archive_reset")
    public static void inbox_message_detail_tap_archive_reset() {
        ScreenAllMessages.inbox_mail_all_tap_message("inbox_message_detail_tap_archive_reset");
    }

    @TestAction(value = "inbox_message_detail_tap_delete_reset")
    public static void inbox_message_detail_tap_delete_reset() {
        ScreenAllMessages.inbox_mail_all_tap_message("inbox_message_detail_tap_delete_reset");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_reply_reset")
    public static void inbox_message_detail_respond_actionsheet_tap_reply_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_reply_reset");
    }

    @TestAction(value = "")
    public static void inbox_message_detail_respond_actionsheet_tap_reply_all_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_reply_all_reset");
    }

    @TestAction(value = "inbox_message_detail_respond_actionsheet_tap_forward_reset")
    public static void inbox_message_detail_respond_actionsheet_tap_forward_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_respond_actionsheet_tap_forward_reset");
    }

    @TestAction(value = "inbox_message_detail_compose_actionsheet_tap_new_message_reset")
    public static void inbox_message_detail_compose_actionsheet_tap_new_message_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_compose_actionsheet_tap_new_message_reset");
    }

    @TestAction(value = "inbox_message_detail_compose_actionsheet_tap_new_invitation_reset")
    public static void inbox_message_detail_compose_actionsheet_tap_new_invitation_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils
                .delayAndCaptureScreenshot("inbox_message_detail_compose_actionsheet_tap_new_invitation_reset");
    }

    @TestAction(value = "inbox_message_detail_tap_expose_reset")
    public static void inbox_message_detail_tap_expose_reset() {
        ScreenExpose.expose_tap_expose();
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_expose_reset");
    }

    @TestAction(value = "inbox_message_detail_tap_url_reset")
    public static void inbox_message_detail_tap_url_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_url_reset");
    }

    @TestAction(value = "inbox_message_detail_tap_url_precondition")
    public static void inbox_message_detail_tap_url_precondition(String senderEmail,
            String senderPassword, String recieverName, String message) {
        LoginActions.openUpdatesScreenOnStart(senderEmail, senderPassword);
        new ScreenUpdates().openExposeScreen().openInboxScreen().openPopupCompose()
                .tapOnNewMessageOnPopup();
        ScreenAddConnections screenAddConnections = new ScreenNewMessage()
                .openAddConnectionsScreen();
        screenAddConnections.chooseConnection(recieverName);
        screenAddConnections.tapOnDoneButton();
        ScreenNewMessage screenNewMessage = new ScreenNewMessage();
        TestUtils.typeTextInEditText0("Hello");
        TestUtils.typeTextInEditText1(message);
        screenNewMessage.sendMessage();
        new ScreenInbox();
        LoginActions.logout();
        TestUtils.delayAndCaptureScreenshot("inbox_message_detail_tap_url_precondition");
    }
}
