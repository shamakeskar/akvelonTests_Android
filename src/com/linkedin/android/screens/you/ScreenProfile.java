package com.linkedin.android.screens.you;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseProfileScreen;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.inbox.ScreenInvitationDetails;
import com.linkedin.android.screens.inbox.ScreenInvitationsAll;
import com.linkedin.android.screens.more.ScreenCompanyDetails;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewGroupUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Profile screen.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 16:52:37 PM
 */
public class ScreenProfile extends BaseProfileScreen {

    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP PHOTO_RECT = new Rect2DP(17.3f, 106.7f, 90.0f, 90.0f);
    static final Rect2DP PHOTO_FRAME_RECT = new Rect2DP(12.0f, 92.7f, 100.0f, 110.0f);

    protected static final String PROFILE_LABEL = "Profile";
    protected static final String FORWARD_SEND_TO_CONNECTIONS_BUTTON = "Send to Conection";
    protected static final String FORWARD_EMAIL_BUTTON = "Email";

    private static final ViewIdName HEADER_LAYOUT = new ViewIdName("abs__action_bar_title");
    private static final ViewIdName PROFILE_INFO_LAYOUT = new ViewIdName("name_and_degree_layout");
    private static final ViewIdName PROFILE_LAYOUT = new ViewIdName("profile_layout");
    private static final ViewIdName RECENT_ACTIVITY_LAYOUT = new ViewIdName("recents_layout");
    private static final ViewIdName CONNECTIONS_LAYOUT = new ViewIdName("connections_layout");
    private static final ViewIdName GROUPS_LAYOUT = new ViewIdName("profile_groups_layout");
    private static final ViewIdName INCOMMON_LAYOUT = new ViewIdName("incommon_layout");
    private static final ViewIdName CONNECT_LAYOUT = new ViewIdName("connect_big");
    private static final ViewIdName PROFILE_PHOTO = new ViewIdName("picture");
    private static final ViewIdName IMAGE_PHOTO = new ViewIdName("image");

    private static final String RECOMMENDATIONS_LABEL = "Recommendations";
    private static final String WEBSITES_LABEL = "Websites";
    private static final String TWITTER_LABEL = "Twitter";
    private static final String ADDRESS_LABEL = "Address";
    private static final String ACCEPT_INVITE_BUTTON_LABEL = "Accept Invitation";
    private static final String INVITE_BUTTON_LABEL = "Connect";

    static final int CALL_BUTTON_INDEX = 0;
    static final int SEND_MESSAGE_BUTTON_INDEX = 1;
    static final int FORWARD_BUTTON_INDEX = 2;

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenProfile() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    /**
     * Verify 'Profile' label is present
     */
    protected void assertProfileLabelPresent() {
        ViewAssertUtils.assertLabel(PROFILE_LABEL);
    }

    /**
     * Taps on Send to Connection on Popup.
     */
    public void tapOnSendToConnectionOnPopup() {
        getSolo().searchText("Send to Connection");

        TextView sendToConnectionButton = getSolo().getText("Send to Connection");
        ViewUtils.tapOnView(sendToConnectionButton, "'Send to Connection' button");
    }

    /**
     * Tap on Message button and opens 'New message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New message' screen.
     */
    public ScreenNewMessage openNewMessage() {
        ViewUtils.tapOnView(getSolo().getImageButton(1), "Message button");
        return new ScreenNewMessage();
    }

    /**
     * Taps on 'In Common' section and opens 'In Common' screen.
     * 
     * @return {@code ScreenInCommon} with just opened 'In Common' screen.
     */
    public ScreenInCommon openInCommon() {
        ViewUtils.tapOnView(getSolo().getText(IN_COMMON_LABEL), "'In Common' section");
        return new ScreenInCommon();
    }

    /**
     * Taps on Email on Popup.
     */
    public void tapOnEmailOnPopup() {
        getSolo().searchText("Email");

        TextView emailButton = getSolo().getText("Email");
        ViewUtils.tapOnView(emailButton, "'Email' button");
    }

    /**
     * Gets 'Forward' image button
     * 
     * @return Forward image button
     */
    private ImageButton getForwardButton() {
        return getSolo().getImageButton(FORWARD_BUTTON_INDEX);
    }

    /**
     * Taps on 'Forward' button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getForwardButton();
        ViewUtils.tapOnView(forwardButton, "'Forward' button.");
    }

    /**
     * Gets 'Send message' image button
     * 
     * @return Send message image button
     */
    private ImageButton getMessageButton() {
        return getSolo().getImageButton(SEND_MESSAGE_BUTTON_INDEX);
    }

    /**
     * Taps on 'Send Message' button.
     */
    public void tapOnMessageButton() {
        ImageButton sendMessageButton = getMessageButton();
        ViewUtils.tapOnView(sendMessageButton, "'Send Message' button.");
    }

    /**
     * Verify that 'Profile' screen dismissed.
     */
    public void verifyThatProfileScreenDismissed() {
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Profile' screen is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return !(isCurrentActivityOpened());
                    }
                });
    }

    public static String getProfileName() {
        RelativeLayout profileInfoLayout = (RelativeLayout) Id
                .getViewByViewIdName(PROFILE_INFO_LAYOUT);
        Assert.assertNotNull("Profile name layout is not present", profileInfoLayout);
        TextView nameTextView = (TextView) profileInfoLayout.getChildAt(1);
        Assert.assertNotNull("Profile name is not present", nameTextView);
        String name = (String) nameTextView.getText();
        return name;
    }

    // ACTIONS --------------------------------------------------------------

    public static void profile(String screenshotName) {
        //ScreenSearch.search_tap_profile();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "profile")
    public static void profile() {
        profile("profile");
    }

    @TestAction(value = "go_to_profile")
    public static void go_to_profile(String email, String password) {
        ScreenSearch.go_to_search(email, password);
        new ScreenSearch().searchForContact(StringData.test_invitation_name);
        profile("go_to_profile");
    }

    @TestAction(value = "profile_tap_back")
    public static void profile_tap_back() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_back");
    }

    @TestAction(value = "profile_tap_expose")
    public static void profile_tap_expose() {
        new ScreenProfile().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("profile_tap_expose");
    }

    @TestAction(value = "profile_tap_expose_reset")
    public static void profile_tap_expose_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_expose_reset");
    }

    @TestAction(value = "profile_tap_search")
    public static void profile_tap_search() {
        HardwareActions.pressSearch();
        new ScreenSearch();
        TestUtils.delayAndCaptureScreenshot("profile_tap_search");
    }

    @TestAction(value = "profile_tap_search_reset")
    public static void profile_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_search_reset");
    }

    @TestAction(value = "profile_tap_fwd")
    public static void profile_tap_fwd() {
        new ScreenProfile().tapOnForwardButton();
        WaitActions.waitForTrueInFunction("'Forward' button's popup is not displayed",
                new Callable<Boolean>() {
                    public Boolean call() {
                        View sendToConnections = TextViewUtils
                                .getTextViewByText("Send to Connection");
                        View email = TextViewUtils.getTextViewByText("Send to Connection");
                        if (sendToConnections == null || email == null)
                            return false;
                        return (sendToConnections.isShown() && email.isShown());
                    }
                });
        TestUtils.delayAndCaptureScreenshot("profile_tap_fwd");
    }

    @TestAction(value = "profile_fwd_actionsheet_tap_cancel")
    public static void profile_fwd_actionsheet_tap_cancel() {
        Button cancel = getSolo().getButton(0);
        ViewUtils.tapOnView(cancel, "'Cancel' button");
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("profile_fwd_actionsheet_tap_cancel");
    }

    @TestAction(value = "profile_fwd_actionsheet_tap_send")
    public static void profile_fwd_actionsheet_tap_send() {
        TextView send = TextViewUtils.searchTextViewInActivity("Send to Connection", true);
        ViewUtils.tapOnView(send, "'Send to Connection'");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("profile_fwd_actionsheet_tap_send");
    }

    @TestAction(value = "profile_fwd_actionsheet_tap_send_reset")
    public static void profile_fwd_actionsheet_tap_send_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_fwd_actionsheet_tap_send_reset");
    }

    @TestAction(value = "profile_fwd_actionsheet_tap_email")
    public static void profile_fwd_actionsheet_tap_email() {
        TextView email = TextViewUtils.searchTextViewInActivity("Email", true);
        ViewUtils.tapOnView(email, "'Email'");
        WaitActions.waitForTrueInFunction("'Email' popup is not displayed",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return (TextViewUtils.getTextViewByText("Gmail") != null);
                    }
                });
        TestUtils.delayAndCaptureScreenshot("profile_fwd_actionsheet_tap_email");
    }

    @TestAction(value = "profile_fwd_actionsheet_tap_email_reset")
    public static void profile_fwd_actionsheet_tap_email_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_fwd_actionsheet_tap_email_reset");
    }

    @TestAction(value = "profile_tap_message")
    public static void profile_tap_message() {
        new ScreenProfile().tapOnMessageButton();
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("profile_tap_message");
    }

    @TestAction(value = "profile_tap_message_reset")
    public static void profile_tap_message_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_message_reset");
    }

    @TestAction(value = "profile_tap_photo")
    public static void profile_tap_photo() {
        ImageView photo = (ImageView) Id.getViewByViewIdName(PROFILE_PHOTO);
        ViewUtils.tapOnView(photo, "'Photo'");
        WaitActions.waitForTrueInFunction("Profile photo is not present", new Callable<Boolean>() {
            public Boolean call() {
                ImageView image = (ImageView) Id.getViewByViewIdName(IMAGE_PHOTO);
                return image != null;
            }
        });
        TestUtils.delayAndCaptureScreenshot("profile_tap_photo");
    }

    @TestAction(value = "profile_tap_photo_reset")
    public static void profile_tap_photo_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_photo_reset");
    }

    @TestAction(value = "profile_tap_activity")
    public static void profile_tap_activity() {
        RelativeLayout recentActivitiesLayout = (RelativeLayout) ViewUtils.scrollToViewById(
                RECENT_ACTIVITY_LAYOUT, ViewUtils.SCROLL_DOWN, 5);
        Assert.assertNotNull("'Recent Activity' section is not present", recentActivitiesLayout);
        ViewGroupUtils.tapFirstViewInLayout(recentActivitiesLayout, true,
                "'Recent Activities' section", null);
        new ScreenRecentActivity();
        TestUtils.delayAndCaptureScreenshot("profile_tap_activity");
    }

    @TestAction(value = "profile_tap_activity_reset")
    public static void profile_tap_activity_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_activity_reset");
    }

    @TestAction(value = "profile_tap_common")
    public static void profile_tap_common() {
        RelativeLayout incommonLayout = (RelativeLayout) ViewUtils.scrollToViewById(
                INCOMMON_LAYOUT, ViewUtils.SCROLL_DOWN, 5);
        Assert.assertNotNull("'Incommon' section is not present", incommonLayout);
        ViewUtils.tapOnView(incommonLayout, "'In Common' section");
        new ScreenInCommon();
        TestUtils.delayAndCaptureScreenshot("profile_tap_common");
    }

    @TestAction(value = "profile_tap_common_reset")
    public static void profile_tap_common_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_common_reset");
    }

    @TestAction(value = "profile_tap_connections")
    public static void profile_tap_connections() {
        RelativeLayout connectionsLayout = (RelativeLayout) ViewUtils.scrollToViewById(
                CONNECTIONS_LAYOUT, ViewUtils.SCROLL_DOWN, 5);
        Assert.assertNotNull("'Connections' section is not present", connectionsLayout);
        ViewGroupUtils.tapFirstViewInLayout(connectionsLayout, true, "'Connections' section", null);
        new ScreenYouConnections();
        TestUtils.delayAndCaptureScreenshot("profile_tap_connections");
    }

    @TestAction(value = "profile_tap_connections_reset")
    public static void profile_tap_connections_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_connections_reset");
    }

    @TestAction(value = "profile_tap_groups")
    public static void profile_tap_groups() {
        RelativeLayout groupsLayout = (RelativeLayout) ViewUtils.scrollToViewById(GROUPS_LAYOUT,
                ViewUtils.SCROLL_DOWN, 5);
        Assert.assertNotNull("'Groups' section is not present", groupsLayout);
        ViewGroupUtils.tapFirstViewInLayout(groupsLayout, true, "'Groups' section", null);
        WaitActions.waitForTrueInFunction("'Groups'screen is not present", new Callable<Boolean>() {
            public Boolean call() {
                TextView header = (TextView) Id.getViewByViewIdName(HEADER_LAYOUT);
                Assert.assertTrue("'Groups' section is not present", header != null);
                return ((String) header.getText()).equals("Groups");
            }
        });
        TestUtils.delayAndCaptureScreenshot("profile_tap_groups");
    }

    @TestAction(value = "profile_tap_groups_reset")
    public static void profile_tap_groups_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_groups_reset");
    }

    @TestAction(value = "profile_tap_website")
    public static void profile_tap_website() {
        int websiteIndex = 0;
        Assert.assertTrue("'Website' is not presented", getSolo().searchText(WEBSITES_LABEL));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                websiteIndex++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase(WEBSITES_LABEL))
                    break;
            }
        }
        TextView websiteView = getSolo().getText(websiteIndex);
        ViewUtils.tapOnView(websiteView, "'Website' view");
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("profile_tap_website");
    }

    @TestAction(value = "profile_tap_website_reset")
    public static void profile_tap_website_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_website_reset");
    }

    @TestAction(value = "profile_tap_profile")
    public static void profile_tap_profile() {
        final String nameBeforeTapping = getProfileName();
        int recommendationIndex = 0;
        Assert.assertTrue("'Recommendation' is not presented",
                getSolo().searchText(RECOMMENDATIONS_LABEL));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                recommendationIndex++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase(RECOMMENDATIONS_LABEL))
                    break;
            }
        }
        TextView recommendationView = getSolo().getText(recommendationIndex);
        ViewUtils.tapOnView(recommendationView, "'Website' view");
        WaitActions.waitForTrueInFunction("'Next'screen is not present", new Callable<Boolean>() {
            public Boolean call() {
                String nameAfterTapping = getProfileName();
                return nameBeforeTapping != nameAfterTapping;
            }
        });
        TestUtils.delayAndCaptureScreenshot("profile_tap_profile");
    }

    @TestAction(value = "profile_tap_profile_reset")
    public static void profile_tap_profile_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("profile_tap_profile_reset");
    }

    @TestAction(value = "profile_tap_twitter")
    public static void profile_tap_twitter() {
        boolean isText = TextViewUtils.searchAndScrollToVisibleText(TWITTER_LABEL);
        Assert.assertTrue("'Twitter' row is not present.", isText);
        Logger.i("Tapping on 'Twitter' row");
        getSolo().clickOnText(TWITTER_LABEL);
        new ScreenBrowser();
        TestUtils.delayAndCaptureScreenshot("profile_tap_twitter");
    }

    @TestAction(value = "profile_tap_twitter_reset")
    public static void profile_tap_twitter_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_twitter_reset");
    }

    @TestAction(value = "profile_tap_address")
    public static void profile_tap_address() {
        ScreenProfile profileScreen = new ScreenProfile();
        int addressIndex = 0;
        Assert.assertTrue("'Address' is not presented", getSolo().searchText(ADDRESS_LABEL));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                addressIndex++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase(ADDRESS_LABEL))
                    break;
            }
        }
        TextView recommendationView = getSolo().getText(addressIndex);
        ViewUtils.tapOnView(recommendationView, "'Address' view");
        profileScreen.verifyThatProfileScreenDismissed();
        TestUtils.delayAndCaptureScreenshot("profile_tap_address");
    }

    @TestAction(value = "profile_tap_address_reset")
    public static void profile_tap_address_reset() {
        HardwareActions.goBackOnPreviousActivity();
        getSolo().scrollToTop();
        TestUtils.delayAndCaptureScreenshot("profile_tap_address_reset");
    }

    @TestAction(value = "profile_tap_accept_invite_precondition")
    public static void profile_tap_accept_invite_precondition() {
        ViewUtils.goBackToExposeScreen();
        ScreenExpose.expose_tap_inbox();
        ScreenInbox.inbox_tap_all_invitations();
        ScreenInvitationsAll.inbox_invitations_all_tap_invitation();
        ScreenInvitationDetails.inbox_invitation_detail_tap_profile();
        TestUtils.delayAndCaptureScreenshot("profile_tap_accept_invite_precondition");
    }

    @TestAction(value = "profile_tap_accept_invite")
    public static void profile_tap_accept_invite() {
        Button button = getSolo().getButton(ACCEPT_INVITE_BUTTON_LABEL);
        Assert.assertNotNull("'" + ACCEPT_INVITE_BUTTON_LABEL + "'" + " button is not present.",
                button);
        ViewUtils.tapOnView(button, "'" + ACCEPT_INVITE_BUTTON_LABEL + "' button");
        WaitActions.waitForTrueInFunction("'Groups'screen is not present", new Callable<Boolean>() {
            public Boolean call() {
                TextView header = (TextView) Id.getViewByViewIdName(HEADER_LAYOUT);
                Assert.assertNotNull("'Accept Invite PYMK' screen is not present", header);
                return ((String) header.getText()).equals("Add Connections");
            }
        });
        TestUtils.delayAndCaptureScreenshot("profile_tap_accept_invite");
    }

    @TestAction(value = "profile_tap_invite_precondition")
    public static void profile_tap_invite_precondition() {
        ViewUtils.goBackToExposeScreen();
        ScreenExpose.expose_tap_groups_and_more();
        ScreenGroupsAndMore.groups_and_more_tap_pymk();
        ScreenPYMK.pymk_tap_profile();
        TestUtils.delayAndCaptureScreenshot("profile_tap_invite_precondition");
    }

    @TestAction(value = "profile_tap_invite")
    public static void profile_tap_invite() {
        View button = Id.getViewByViewIdName(CONNECT_LAYOUT);
        Assert.assertNotNull("'" + INVITE_BUTTON_LABEL + "'" + " button is not present.", button);
        ViewUtils.tapOnView(button, "'" + INVITE_BUTTON_LABEL + "' button");
        WaitActions.waitForTrueInFunction("'Groups'screen is not present", new Callable<Boolean>() {
            public Boolean call() {
                TextView header = (TextView) Id.getViewByViewIdName(HEADER_LAYOUT);
                Assert.assertNotNull("'Accept Invite PYMK' screen is not present", header);
                return ((String) header.getText()).equals("Add Connections");
            }
        });
        TestUtils.delayAndCaptureScreenshot("profile_tap_invite");
    }

    @TestAction(value = "profile_tap_exp_company")
    public static void profile_tap_exp_company() {
        getSolo().searchText("Experience", 1, true, true);
        LinearLayout common = (LinearLayout) Id.getViewByViewIdName(PROFILE_LAYOUT);
        int containerSize = common.getChildCount();
        View viewToTap = null;

        boolean isAfterExperience = false;
        for (int i = 0; i < containerSize; i++) {
            View currentChild = common.getChildAt(i);

            if ((currentChild instanceof TextView)
                    && ((TextView) currentChild).getText().equals("Experience")) {
                isAfterExperience = true;
            }

            if (isAfterExperience && (currentChild instanceof LinearLayout)) {
                if (((TextView) ((LinearLayout) currentChild).getChildAt(1)).getLinksClickable()) {
                    viewToTap = ((LinearLayout) currentChild).getChildAt(1);
                    break;
                }
            }
        }
        ViewUtils.tapOnView(viewToTap, "Company from Experience");
        new ScreenCompanyDetails();
        TestUtils.delayAndCaptureScreenshot("profile_tap_exp_company");
    }

    @TestAction(value = "profile_tap_exp_company_reset")
    public static void profile_tap_exp_company_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("profile_tap_exp_company_reset");
    }
}
