package com.linkedin.android.screens.inbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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

import com.linkedin.android.popups.PopupCompose;
import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Inbox screen.
 * 
 * @author Dmitry.Somov
 * @created Aug 8, 2012 4:18:36 PM
 */
public class ScreenInbox extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.notifications.NotificationCenterListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NotificationCenterListActivity";

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
    private static final int ACCEPT_INVITATION_BUTTON_INDEX = 4;
    private static final int DECLINE_INVITATION_BUTTON_INDEX = 5;

    private static final String ACCEPT_INVITATION_BUTTON_LABEL = "Accept invitation";
    private static final String DECLINE_INVITATION_BUTTON_LABEL = "Decline invitation";

    private static final int FIRST_MESSAGE_IN_MESSAGES_LIST_INDEX = 3;
    // Delay for wait completely load Inbox webview.
    static final int WAIT_FOR_SCREEN_LOAD_COMPLETELY = 15;

    private static final ViewIdName MESSAGE_LAYOUT = new ViewIdName("message_row");
    private static final ViewIdName INVITATION_LAYOUT = new ViewIdName(
            "invite_reconnect_person_row");
    private static final ViewIdName ALL_MAIL_LAYOUT = new ViewIdName("nc_mail_layout");
    private static final ViewIdName ALL_INVITATION_LAYOUT = new ViewIdName("nc_invite_layout");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenInbox() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();

        // Wait for screen load completely
        WaitActions.delay(WAIT_FOR_SCREEN_LOAD_COMPLETELY);

        // Verify presence IN Button.
        verifyINButton();

        // Verify presence Compose button
        verifyComposeButton();

        Assert.assertTrue("'Inbox' label is not present on 'Inbox' screen",
                getSolo().searchText("Inbox", 1, false));
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
        ViewUtils.tapOnView(getComposeButton(), "'Compose' button");
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
     * Opens 'Compose' popup.
     * 
     * @return {@code PopupCompose}.
     */
    public PopupCompose openPopupCompose() {
        tapOnComposeButton();
        return new PopupCompose();
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
            senderNameTextView = TextViewUtils.searchTextViewInActivity(senderName,
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
     * Opens 'Invitation' screen.
     * 
     * @return{@code ScreenInvitationDetails} with just opened 'Invitation'
     *               screen.
     */
    public ScreenInvitationDetails openInvitation() {
        tapOnInvitation();
        return new ScreenInvitationDetails();
    }

    /**
     * Verify that 'Inbox' screen dismissed.
     */
    public void verifyThatInboxScreenDismissed() {
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Inbox' screen is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return !(isCurrentActivityOpened());
                    }
                });
    }

    // ACTIONS --------------------------------------------------------------
    public static void inbox(String screenshotName) {
        new ScreenExpose(null).openInboxScreen();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void inbox() {
        inbox("inbox");
    }

    public static void go_to_inbox() {
        ScreenExpose.go_to_expose();
        inbox("go_to_expose");
    }

    @TestAction(value = "inbox_tap_expose")
    public static void inbox_tap_expose() {
        new ScreenInbox().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_expose");
    }

    public static void inbox_tap_expose_reset() {
        tapOnINButton();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_expose_reset");
    }

    public static void inbox_tap_search() {
        new ScreenInbox().openSearchScreen();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_search");
    }

    public static void inbox_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_search_reset");
    }

    public static void inbox_tap_compose_actionsheet() {
        new ScreenInbox().openPopupCompose();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_compose_actionsheet");
    }

    public static void inbox_tap_compose_actionsheet_reset() {
        HardwareActions.pressBack();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_compose_actionsheet_reset");
    }

    public static void inbox_tap_message() {
        RelativeLayout messageLayout = (RelativeLayout) Id.getViewByViewIdName(MESSAGE_LAYOUT);
        Assert.assertNotNull("Message is not present on 'Inbox' screen", messageLayout);

        ViewGroupUtils.tapFirstViewInLayout(messageLayout, true, "first message", null);

        new ScreenMessageDetail();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_message");
    }

    public static void inbox_tap_message_reset() {
        HardwareActions.pressBack();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_message_reset");
    }

    public static void inbox_compose_actionsheet_tap_new_message() {
        new PopupCompose().tapOnNewMessageOnPopup();
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("inbox_compose_actionsheet_tap_new_message");
    }

    public static void inbox_compose_actionsheet_tap_new_message_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_compose_actionsheet_tap_new_message_reset");
    }

    public static void inbox_compose_actionsheet_tap_new_invitation() {
        new PopupCompose().tapOnNewInvitationOnPopup();
        new ScreenNewInvitation();
        TestUtils.delayAndCaptureScreenshot("inbox_compose_actionsheet_tap_new_invitation");
    }

    public static void inbox_compose_actionsheet_tap_new_invitation_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_compose_actionsheet_tap_new_invitation_reset");
    }

    public static void inbox_pull_refresh() {
        HardwareActions.pressMenu();
        HardwareActions.tapOnMenuOption("Refresh");
        new ScreenInbox();
    }

    public static void inbox_tap_invitation() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        Assert.assertNotNull("'Invitation' row is not present on 'Inbox' screen", invitationLayout);
        ViewGroupUtils.tapFirstViewInLayout(invitationLayout, true, "'Invitation' row", null);

        new ScreenInvitationDetails();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_invitations");
    }

    public static void inbox_tap_invitation_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_invitation_reset");
    }

    public static void inbox_tap_all_mail() {
        ViewUtils.waitForToastDisappear();
        RelativeLayout allMailLayout = (RelativeLayout) Id.getViewByViewIdName(ALL_MAIL_LAYOUT);
        if (allMailLayout == null) {
            ListViewUtils.scrollToNewItems();
        }
        allMailLayout = (RelativeLayout) Id.getViewByViewIdName(ALL_MAIL_LAYOUT);
        Assert.assertNotNull("'All Mail' button is not present on 'Inbox' screen", allMailLayout);
        ViewGroupUtils.tapFirstViewInLayout(allMailLayout, true, "'All Mail' button", null);

        new ScreenAllMessages();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_mail");
    }

    public static void inbox_tap_all_mail_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_mail_reset");
    }

    public static void inbox_tap_all_invitations() {
        ViewUtils.waitForToastDisappear();
        RelativeLayout allInvitationsLayout = (RelativeLayout) Id
                .getViewByViewIdName(ALL_INVITATION_LAYOUT);
        if (allInvitationsLayout == null) {
            ListViewUtils.scrollToNewItems();
        }
        allInvitationsLayout = (RelativeLayout) Id
                .getViewByViewIdName(ALL_INVITATION_LAYOUT);
        Assert.assertNotNull("'All Invitations' button is not present on 'Inbox' screen",
                allInvitationsLayout);
        ViewGroupUtils.tapFirstViewInLayout(allInvitationsLayout, true, "'All Invitations' button",
                null);

        new ScreenInvitationsAll();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_invitations");
    }

    public static void inbox_tap_all_invitations_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenInbox();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_all_invitations_reset");
    }

    public static void inbox_tap_notification() {
        ScreenInbox inboxScreen = new ScreenInbox();
        int firstNotificationIndex = 0;
        Assert.assertTrue("'Notifications' view not presented",
                getSolo().searchText("Notifications"));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                firstNotificationIndex++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase("Notifications"))
                    break;
            }
        }
        TextView notificationView = getSolo().getText(firstNotificationIndex);
        ViewUtils.tapOnView(notificationView, "'Group' view");
        inboxScreen.verifyThatInboxScreenDismissed();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_notification");
    }

    public static void inbox_tap_notification_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("inbox_tap_notification_reset");
    }

    public static void inbox_tap_invitation_accept() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        final String displayName = ((TextView) invitationLayout.getChildAt(1)).getText().toString();

        ImageView acceptInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, ACCEPT_INVITATION_BUTTON_INDEX, ImageView.class);
        new ScreenInbox().tapOnButton(acceptInvitationButton, ACCEPT_INVITATION_BUTTON_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation' row is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return (getSolo().searchText(displayName, 1, false, true) != true);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("inbox_tap_invitation_accept");
    }

    public static void inbox_tap_invitation_decline() {
        RelativeLayout invitationLayout = (RelativeLayout) Id
                .getViewByViewIdName(INVITATION_LAYOUT);
        final String displayName = ((TextView) invitationLayout.getChildAt(1)).getText().toString();

        ImageView declineInvitationButton = ViewGroupUtils.getChildViewByIndexSafely(
                invitationLayout, DECLINE_INVITATION_BUTTON_INDEX, ImageView.class);
        new ScreenInbox().tapOnButton(declineInvitationButton, DECLINE_INVITATION_BUTTON_LABEL);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Invitation' row is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return (getSolo().searchText(displayName, 1, false, true) != true);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("inbox_tap_invitation_decline");
    }
}
