package com.linkedin.android.screens.inbox;

import java.util.ArrayList;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.popups.Popup;
import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.RegexpUtils;
import com.linkedin.android.utils.StringUtils;
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

    static final int MESSAGE_AUTHOR_NAMETEXT_INDEX = 4;
    static final int MESSAGE_AUTHOR_STATUSTEXT_INDEX = 5;
    static final int MESSAGE_TITLE_INDEX = 6;
    static final int MARKUNREAD_TEXTVIEW_INDEX = 7;
    static final int MESSAGE_CREATEDATE_INDEX = 8;
    static final int MESSAGE_TEXTVIEW_INDEX = 9;

    static final int DELETE_BUTTON_INDEX = 1;
    static final int REPLY_BUTTON_INDEX = 2;
    static final int COMPOSE_BUTTON_INDEX = 3;

    static final String MARKUNREAD_VIEWNAME = "'Mark unread'";

    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenMessageDetail() {
        super(ACTIVITY_SHORT_CLASSNAME);
    }

    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        // Verify presence IN Button.
        verifyINButton();

        ImageButton backToInboxButton = getSolo().getImageButton(0);
        ImageButton deleteButton = getSolo().getImageButton(DELETE_BUTTON_INDEX);
        ImageButton replyButton = getSolo().getImageButton(REPLY_BUTTON_INDEX);
        ImageButton composeButton = getSolo().getImageButton(COMPOSE_BUTTON_INDEX);

        Assert.assertNotNull("'Back To Inbox' button is not present", backToInboxButton);
        Assert.assertNotNull("'Delete' button is not present", deleteButton);
        Assert.assertNotNull("'Reply' button is not present", replyButton);
        Assert.assertNotNull("'Compose' button is not present", composeButton);

        Assert.assertTrue("'Back To Inbox' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(backToInboxButton,
                        LayoutUtils.LOWER_LEFT_OF_4_BUTTONS_LAYOUT));

        Assert.assertTrue("'Delete' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(deleteButton,
                        LayoutUtils.LOWER_LEFT_CENTER_OF_4_BUTTONS_LAYOUT));

        Assert.assertTrue("'Reply' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(replyButton,
                        LayoutUtils.LOWER_RIGHT_CENTER_OF_4_BUTTONS_LAYOUT));

        Assert.assertTrue("'Compose' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(composeButton,
                        LayoutUtils.LOWER_RIGHT_OF_4_BUTTONS_LAYOUT));

        // Verify 'Mark unread' text button
        verifyMarkUnreadButton();

        // Verify '% of %' lable text
        verifyOFLable();

        // Verify message text is not empty
        verifyMessageText();

        // Verify message created date text is not empty
        verifyMessageCreatedDateText();

        // Verify message title text is not empty
        verifyMessageTitleText();

        // Verify message text author status is not empty
        verifyMessageAuthorStatusText();

        // Verify message author name text is not empty
        verifyMessageAuthorNameText();

        // Verify Up and Down Arrow Buttons
        verifyUpDownArrowButtons();

        // Verify Message Author Avatar image.
        Assert.assertNotNull("Message Author Avatar Image is not present",
                getMessageAuthorAvatarImage());

        HardwareActions.takeCurrentActivityScreenshot("Send message detail screen");
    }

    /**
     * Gets Message Author Avatar image.
     * 
     * @return if exist image {@code ImageView}, else <b>null</b>
     */
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
        getSolo().clickOnButton(DELETE_BUTTON_INDEX);

        Popup popup = new Popup("Delete Message", "Are you sure want to delete this message?");
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
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return downArrowButton;
    }

    /**
     * Get Up Arrow Button
     * 
     * @return ImageView
     */
    public ImageView getupArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return upArrowButton;
    }

    /**
     * Verify message text is not empty
     */
    private void verifyMessageText() {
        String messageText = getMessageText();
        Assert.assertTrue("'Message text' is empty", !StringUtils.isNullOrWhitespace(messageText));
    }

    /**
     * Verify message created date text is not empty
     */
    private void verifyMessageCreatedDateText() {
        TextView messageCreatedDateTextView = getMessageCreatedDateTextView();
        String createdDateText = messageCreatedDateTextView.getText().toString();

        Assert.assertTrue("'Message created text' is empty",
                !StringUtils.isNullOrWhitespace(createdDateText));
    }

    /**
     * Verify message title text is not empty
     */
    private void verifyMessageTitleText() {
        String text = getMessageSubjectFromMessageDetailScreen();

        Assert.assertTrue("'Message title text' is empty", !StringUtils.isNullOrWhitespace(text));
    }

    /**
     * Verify message author status text is not empty
     */
    private void verifyMessageAuthorStatusText() {
        TextView messageAuthorStatusTextView = getMessageTitleTextView();
        String text = messageAuthorStatusTextView.getText().toString();

        Assert.assertTrue("'Message Author Status text' is empty",
                !StringUtils.isNullOrWhitespace(text));
    }

    /**
     * Verify message author name text is not empty
     */
    private void verifyMessageAuthorNameText() {
        TextView messageAuthorNameTextView = getMessageTitleTextView();
        String text = messageAuthorNameTextView.getText().toString();

        Assert.assertTrue("'Message Author Status text' is empty",
                !StringUtils.isNullOrWhitespace(text));
    }

    /**
     * Verify '% of %' lable text
     */
    private void verifyOFLable() {
        Assert.assertTrue("Title '.. of ..' is not present", getSolo().getText(0).getText()
                .toString().indexOf("of") > -1);
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
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MESSAGE_TITLE_INDEX);

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
    public TextView getMarkUnreadButton() {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textView = textViews.get(MARKUNREAD_TEXTVIEW_INDEX);
        return textView;
    }
}
