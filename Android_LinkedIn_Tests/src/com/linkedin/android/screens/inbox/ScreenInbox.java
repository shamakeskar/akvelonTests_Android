package com.linkedin.android.screens.inbox;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.message.BasicNameValuePair;

import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Inbox screen.
 * 
 * @author Dmitry.Somov
 * @created Aug 8, 2012 4:18:36 PM
 */
public class ScreenInbox extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.messages.MessageListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "MessageListActivity";

    public static final int MESSAGES_LIST_VIEW_INDEX = 0;

    // "Message info" layout index relatively "Message" layout
    private static final int MESSAGE_INFO_LAYOUT_INDEX = 1;

    // View indexes relatively "Message info" layout
    private static final int MESSAGE_SENDER_NAME_INDEX = 3;
    private static final int MESSAGE_SUBJECT_INDEX = 4;

    // Invitation layout index relatively "Messages" ListView
    private static final int INVITATION_LAYOUT_INDEX = 1;

    // "Invitation info" layout index relatively "Invitation" layout
    private static final int INVITATION_INFO_LAYOUT_INDEX = 1;

    // Button indexes relatively "Invitation info" layout
    private static final int ACCEPT_INVITATION_BUTTON_INDEX = 5;
    private static final int DECLINE_INVITATION_BUTTON_INDEX = 6;

    private static final String ACCEPT_INVITATION_BUTTON_LABEL = "Accept invitation";
    private static final String DECLINE_INVITATION_BUTTON_LABEL = "Decline invitation";

    private static final int FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX = 3;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenInbox() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        // Verify presence IN Button.
        verifyINButton();

        // Verify presence Compose button
        verifyComposeButton();

        Assert.assertTrue("'Invitations' label is not present on Updates screen", getSolo()
                .searchText("Invitations", 1, false));

        Assert.assertTrue("'Messages' label is not present on Updates screens", getSolo()
                .searchText("Messages", 1, false));

        HardwareActions.takeCurrentActivityScreenshot("Inbox screen");
    }

    @Override
    public void waitForMe() {
        Assert.assertTrue("Cannot wait to launch activity '" + ACTIVITY_SHORT_CLASSNAME + "'",
                getSolo()
                        .waitForActivity(ACTIVITY_SHORT_CLASSNAME, DataProvider.WAIT_DELAY_DEFAULT));
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Returns 'Compose' button.
     * 
     * @return if exist 'Compose' button {@code View}, else <b>null</b>
     */
    public ImageView getComposeButton() {
        return getRightButtonInNavigationBar();
    }

    /**
     * Verifies 'Compose' button.
     */
    public void verifyComposeButton() {
        Assert.assertNotNull("'Compose' button is not presented", getComposeButton());
    }

    /**
     * Taps on 'Compose' button.
     */
    public void tapOnComposeButton() {
        ImageView composeButton = getComposeButton();
        Assert.assertNotNull("'Compose' button is not presented", composeButton);

        Logger.i("Tapping on 'Compose' button");
        getSolo().clickOnView(composeButton);
    }

    /**
     * Taps on first message from "Messages" list
     */
    public void tapOnFirstMessage() {
        Assert.assertTrue("'Messages' list is empty", isMessagesListNotEmpty());
        ListView messagesList = getMessagesList();

        HardwareActions.scrollUp();
        View firstMessage = messagesList.getChildAt(FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX);

        if (null == firstMessage) {
            return;
        }
        getSolo().clickOnView(firstMessage);
    }

    /**
     * Opens 'New Message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New Message' screen.
     */
    public ScreenNewMessage openNewMessageScreen() {
        tapOnComposeButton();
        tapOnNewMessageOnPopup();

        return new ScreenNewMessage();
    }

    /**
     * Opens first message from "Messages" list
     * 
     * @return {@code ScreenMessageDetail} object of just opened 'MESSAGE
     *         DETAIL' screen
     */
    public ScreenMessageDetail openFirstMessage() {
        tapOnFirstMessage();
        ScreenMessageDetail screenMessageDetail = new ScreenMessageDetail();
        return screenMessageDetail;
    }

    /**
     * Searches message by {@code senderName} in messages list (scroll down
     * screen 10 times).
     * 
     * @param senderName
     *            message sender name
     * @return message sender name as {@code TextView} or <b>null</b> if there
     *         is no such messages.
     */
    public TextView searchMessageBySenderName(String senderName) {
        final int numberOfTimesToScrollDown = 10;

        boolean isScreenCanBeScrolled = true;
        for (int i = 0; i < numberOfTimesToScrollDown; i++) {
            TextView senderNameTextView = searchMessageBySenderNameInActivity(senderName);
            if (null != senderNameTextView) {
                return senderNameTextView;
            }
            if (!isScreenCanBeScrolled) {
                WaitActions.delay(DataProvider.DEFAULT_DELAY_TIME);
            }
            isScreenCanBeScrolled = getSolo().scrollDown();
        }
        return null;
    }

    /**
     * Verifies if there is invitation on screen and taps on it.
     */
    public void tapOnInvitation() {
        LinearLayout invitationLayout = getInvitationLayout();
        Assert.assertNotNull("There is no invitation on screen", invitationLayout);
        Assert.assertTrue("There is no invitation on screen", isInvitationOnScreen());

        getSolo().clickOnView(invitationLayout);
    }

    /**
     * Verifies if there is invitation on screen.
     * 
     * @return <b>true</b> if there is invitation on screen, <b>false</b>
     *         otherwise.
     */
    public boolean isInvitationOnScreen() {
        ImageView acceptInvitationButton = getAcceptInvitationButton();
        ImageView declineInvitationButton = getDeclineInvitationButton();
        return null != acceptInvitationButton && null != declineInvitationButton;
    }

    /**
     * Taps on "Accept invitation" button from specified
     * {@code invitationLayout}
     */
    public void tapOnAcceptInvitationButton() {
        ImageView acceptInvitationButton = getAcceptInvitationButton();
        tapOnButton(acceptInvitationButton, ACCEPT_INVITATION_BUTTON_LABEL);
    }

    /**
     * Taps on "Decline invitation" button from specified
     * {@code invitationLayout}
     */
    public void tapOnDeclineInvitationButton() {
        ImageView declineInvitationButton = getDeclineInvitationButton();
        tapOnButton(declineInvitationButton, DECLINE_INVITATION_BUTTON_LABEL);
    }

    /**
     * Gets "Accept invitation" button ({@code ImageView}) from specified
     * {@code invitationLayout}
     * 
     * @return "Accept invitation" button ({@code ImageView}) from specified
     *         {@code invitationLayout} or <b>null</b> if there is no such
     *         button
     */
    public ImageView getAcceptInvitationButton() {
        return getButtonFromInvitationInfoLayout(ACCEPT_INVITATION_BUTTON_INDEX);
    }

    /**
     * Gets "Decline invitation" button ({@code ImageView}) from specified
     * {@code invitationLayout}
     * 
     * @return "Decline invitation" button ({@code ImageView}) from specified
     *         {@code invitationLayout} or <b>null</b> if there is no such
     *         button
     */
    public ImageView getDeclineInvitationButton() {
        return getButtonFromInvitationInfoLayout(DECLINE_INVITATION_BUTTON_INDEX);
    }

    /**
     * Gets "Messages" {@code ListView}
     * 
     * @return "Messages" {@code ListView}
     */
    public ListView getMessagesList() {
        return ListViewUtils.getListViewFromCurrentScreenByIndex(MESSAGES_LIST_VIEW_INDEX);
    }

    /**
     * Verifies that "Messages" {@code ListView} is not empty
     * 
     * @return <b>true</b> if "Messages" {@code ListView} is not empty
     *         <b>false</b> otherwise
     */
    public boolean isMessagesListNotEmpty() {
        ListView messagesList = getMessagesList();
        return null != messagesList
                && messagesList.getCount() > FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX;
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
     * Gets "Name" - "Subject" pairs of first three messages to compare (there
     * can be less than 3 pairs returned if there is not 3 pairs in "Messages"
     * list. At least one pair will be returned).
     * 
     * @return "Name" - "Subject" pairs of first three messages to compare.
     */
    public List<BasicNameValuePair> getInboxContentForCompare() {
        final int lastIndexOfListItemToCompare = FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX + 2;

        ListView messagesList = getMessagesList();
        Assert.assertNotNull("There is no 'Messages' list on screen", messagesList);
        Assert.assertTrue("'Messages' list is empty", isMessagesListNotEmpty());
        List<BasicNameValuePair> inboxContent = new ArrayList<BasicNameValuePair>();

        for (int i = FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX; i <= lastIndexOfListItemToCompare; i++) {
            LinearLayout messageLayout = ViewGroupUtils.getChildViewByIndexSafely(messagesList, i,
                    LinearLayout.class);
            if (null == messageLayout) {
                continue;
            }
            String senderName = getMessageSenderName(messageLayout);
            String subject = getMessageSubject(messageLayout);
            inboxContent.add(new BasicNameValuePair(senderName, subject));
        }
        return inboxContent;
    }

    /**
     * Searches message by {@code senderName} in list of currently loaded
     * messages (in current activity; does not scroll down to force 'Messages'
     * list update).
     * 
     * @param senderName
     *            message sender name
     * @return message sender name as {@code TextView} or <b>null</b> if there
     *         is no such messages.
     */
    private TextView searchMessageBySenderNameInActivity(String senderName) {
        ArrayList<TextView> nonSenderNameTextViews = new ArrayList<TextView>();
        TextView senderNameTextView;

        do {
            senderNameTextView = ViewUtils.searchTextViewInActivity(senderName,
                    nonSenderNameTextViews, true);
            if (isSenderName(senderNameTextView)) {
                return senderNameTextView;
            }
            nonSenderNameTextViews.add(senderNameTextView);
        } while (null != senderNameTextView);
        return null;
    }

    /**
     * Checks whether specified {@code textView} is sender name.
     * 
     * @param textView
     *            {@code TextView} to check if sender name
     * @return <b>true</b> if specified {@code textView} is sender name,
     *         <b>false</b> otherwise.
     */
    private boolean isSenderName(TextView textView) {
        if (null == textView) {
            return false;
        }

        ViewParent textViewParent = textView.getParent();
        if (!(textViewParent instanceof RelativeLayout)) {
            return false;
        }
        RelativeLayout textViewParentLayout = (RelativeLayout) textViewParent;
        TextView senderName = ViewGroupUtils.getChildViewByIndexSafely(textViewParentLayout,
                MESSAGE_SENDER_NAME_INDEX, TextView.class);
        if (textView != senderName) {
            return false;
        }
        ViewParent textViewParentLayoutParent = textViewParentLayout.getParent();
        if (!(textViewParentLayoutParent instanceof LinearLayout)) {
            return false;
        }
        LinearLayout messageListItemLayout = (LinearLayout) textViewParentLayoutParent;
        ViewParent messagesList = messageListItemLayout.getParent();
        return messagesList instanceof ListView;
    }

    /**
     * Gets sender name of message with specified {@code messageLayout}.
     * 
     * @param messageLayout
     *            message {@code LinearLayout}
     * @return sender name of message with specified {@code messageLayout} or
     *         <b>null</b> if there is no such subject.
     */
    private String getMessageSenderName(LinearLayout messageLayout) {
        return getTextViewValueFromMessageInfoLayout(messageLayout, MESSAGE_SENDER_NAME_INDEX);
    }

    /**
     * Gets subject of message with specified {@code messageLayout}.
     * 
     * @param messageLayout
     *            message {@code LinearLayout}
     * @return subject of message with specified {@code messageLayout} or
     *         <b>null</b> if there is no such subject.
     */
    private String getMessageSubject(LinearLayout messageLayout) {
        return getTextViewValueFromMessageInfoLayout(messageLayout, MESSAGE_SUBJECT_INDEX);
    }

    /**
     * Gets value of {@code TextView} with specified {@code textViewIndex} from
     * "Message info" layout. "Message info" layout placed on
     * {@code messageLayout}.
     * 
     * @param messageLayout
     *            message {@code LinearLayout}
     * @param textViewIndex
     *            index of {@code TextView} relatively "Message info" layout.
     * @return value of {@code TextView} with specified {@code textViewIndex}
     *         from "Message info" layout ("Message info" layout placed on
     *         {@code messageLayout}) or <b>null</b> if there is no such
     *         {@code TextView}.
     */
    private String getTextViewValueFromMessageInfoLayout(LinearLayout messageLayout,
            int textViewIndex) {
        RelativeLayout messageInfoLayout = getMessageInfoLayout(messageLayout);
        TextView textView = ViewGroupUtils.getChildViewByIndexSafely(messageInfoLayout,
                textViewIndex, TextView.class);
        String textViewValue = null == textView ? null : textView.getText().toString();
        return textViewValue;
    }

    /**
     * Gets {@code RelativeLayout} contained info about message from specified
     * {@code messageLayout}
     * 
     * @param messageLayout
     *            message {@code LinearLayout}
     * @return {@code RelativeLayout} contained info about message from
     *         specified {@code messageLayout} or <b>null</b> if there is no
     *         such layout
     */
    private RelativeLayout getMessageInfoLayout(LinearLayout messageLayout) {
        return ViewGroupUtils.getChildViewByIndexSafely(messageLayout, MESSAGE_INFO_LAYOUT_INDEX,
                RelativeLayout.class);
    }

    /**
     * Gets {@code LinearLayout} of Invitation
     * 
     * @return {@code LinearLayout} of Invitation or <b>null</b> if there is no
     *         such {@code LinearLayout}
     */
    private LinearLayout getInvitationLayout() {
        ListView messagesList = getMessagesList();
        return ViewGroupUtils.getChildViewByIndexSafely(messagesList, INVITATION_LAYOUT_INDEX,
                LinearLayout.class);
    }

    /**
     * Gets {@code RelativeLayout} contained info about invitation from
     * specified {@code invitationLayout}
     * 
     * @return {@code RelativeLayout} contained info about invitation from
     *         specified {@code invitationLayout} or <b>null</b> if there is no
     *         such layout
     */
    private RelativeLayout getInvitationInfoLayout() {
        LinearLayout invitationLayout = getInvitationLayout();
        RelativeLayout connectionInfoLayout = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, INVITATION_INFO_LAYOUT_INDEX, RelativeLayout.class);
        return connectionInfoLayout;
    }

    /**
     * Gets button from {@code invitationLayout} with specified
     * {@code buttonIndex}
     * 
     * @param buttonIndex
     *            index of required button
     * @return button from {@code invitationLayout} with specified
     *         {@code buttonIndex} or <b>null</b> if there is no such button
     */
    private ImageView getButtonFromInvitationInfoLayout(int buttonIndex) {
        RelativeLayout connectionInfoLayout = getInvitationInfoLayout();
        ImageView button = ViewGroupUtils.getChildViewByIndexSafely(connectionInfoLayout,
                buttonIndex, ImageView.class);
        return button;
    }

    /**
     * Taps on New Message on popup.
     */
    private void tapOnNewMessageOnPopup() {
        Assert.assertNotNull("'New Message' button is not present.",
                getSolo().searchText("New Message"));

        Logger.i("Tapping on 'New Message' button");
        getSolo().clickOnText("New Message");
    }

    /**
     * Taps on New Invitation on popup.
     */
    private void tapOnNewInvitationOnPopup() {
        Assert.assertNotNull("'New Invitation' button is not present.",
                getSolo().searchText("New Invitation"));

        Logger.i("Tapping on 'New Invitation' button");
        getSolo().clickOnText("New Invitation");
    }

    /**
     * Opens 'Invitation' screen.
     * 
     * @return{@code ScreenInvitationDetails} with just opened 'Invitation'
     *               screen.
     */
    public ScreenInvitationDetails openInvitation() {
        tapOnInvitation();
        return new ScreenInvitationDetails();
    }
}
