package com.linkedin.android.screens.you;

import java.util.ArrayList;

import junit.framework.Assert;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseProfileScreen;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenSearch;
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
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for You screen.
 * 
 * @author evgeny.agapov
 * @created Aug 7, 2012 5:19:11 PM
 */
public class ScreenYou extends BaseProfileScreen {
    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP PHOTO_RECT = new Rect2DP(17.3f, 112.7f, 90.0f, 90.0f);
    static final Rect2DP PHOTO_FRAME_RECT = new Rect2DP(10.0f, 100.0f, 110.0f, 120.0f);
    static final Rect2DP PHOTO_CAMERA_RECT = new Rect2DP(68.7f, 163.4f, 38.7f, 39.3f);

    static final int SHARE_AND_SEND_BUTTONS_HEIGHT = 60;

    static final int AVATAR_PHOTO_CAMERA_INDEX = 8;
    static final int AVATAR_IMAGE_INDEX = 7;

    static final int SHARE_BUTTON_INDEX = 0;
    static final int SEND_BUTTON_INDEX = 1;

    static final String GROUP_BUTTON_TEXT = "Groups";
    static final String CONNECTIONS_BUTTON_TEXT = "Connections";
    static final String RECENT_ACTIVITY_BUTTON_TEXT = "Recent Activity";
    static final String WHOVIEWYOUPROFILE_BUTTON_TEXT = "Who's viewed your profile";
    static final String COMPANY_WEBSITE_BUTTON_TEXT = "Company Website";
    static final String LINKEDIN_SIGNIN_BUTTON_TEXT = "LinkedinSignIn";
    static final String SETTINGS_TEXT = "Settings";

    // TextView: current user name
    private static final ViewIdName NAME = new ViewIdName("name");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenYou() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        assertConnectionsLabelPresent();
        HardwareActions.scrollUp();
        assertRecentActivityLabelPresent();
        assertExperienceLabelPresent();

        assertPhotoFramePresent(PHOTO_FRAME_RECT);
        assertPhotoPresent(PHOTO_RECT);

        Assert.assertNotNull("Photo's camera is not present",
                getSolo().getCurrentImageViews().get(8));

        verifyINButton();

        verifySearchBar();

        verifyRightButtonInNavigationBar(SETTINGS_TEXT);
        
        ImageButton shareButton = getSolo().getImageButton(0);
        ImageButton sendButton = getSolo().getImageButton(1);
        
        Assert.assertNotNull("'Share' button is not present", shareButton);
        Assert.assertNotNull("'Send' button is not present", sendButton);

        Assert.assertTrue(
                "'Share' and 'Send' buttons are not present (or their coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(shareButton, LayoutUtils.LEFT_BUTTON_LAYOUT)
                        && LayoutUtils.isViewPlacedInLayout(sendButton,
                                LayoutUtils.RIGHT_BUTTON_LAYOUT)
                        && ViewUtils.isViewsPlacedInLineHorizontally(shareButton, sendButton));

        HardwareActions.takeCurrentActivityScreenshot("You screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "You");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
    
    /**
     * Checks that opened You screen.
     * 
     * @return <b>true</b> if current screen - You screen. 
     */
    public static boolean isOnYouScreen(){
    	if (!getSolo().getCurrentActivity().getClass().getSimpleName()
    			.equals(ACTIVITY_SHORT_CLASSNAME))
    		return false;
        if (!getSolo().waitForText(CONNECTIONS_LABEL,
                1, DataProvider.WAIT_DELAY_LONG, false, false))
        	return false;
        if (TextViewUtils.searchTextViewInActivity(RECENT_ACTIVITY_LABEL, true) == null)
        	return false;
        if (TextViewUtils.searchTextViewInActivity(EXPERIENCE_LABEL, true) == null)
        	return false;

    	return true;
    }

    /**
     * Asserts that You screen content correct name, headline and 'Who's viewed
     * your profile' with content.
     */
    public void assertAllDataForTestUser() {
        Assert.assertTrue("Test user name is not present",
                getSolo().searchText(StringData.test_name, 1, false, false));
        Assert.assertTrue("Test user headline is not present",
                getSolo().searchText(StringData.test_headline, 1, false, false));
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);
        TextView textWVYP = null;
        for (TextView textView : textViews) {
            if (textView.getText().equals("Who's viewed your profile")) {
                textWVYP = textView;
                break;
            }
        }
        if (textWVYP != null) {
            Rect2DP rectWVMP = new Rect2DP(textWVYP);
            // Expand rectWVMP to fit content.
            rectWVMP.width = ScreenResolution.getScreenWidthDP() - rectWVMP.x;
            rectWVMP.height += 100;
            // Find fit views.
            ArrayList<View> viewFit = rectWVMP.getViewsThatFitInThisRect(getSolo()
                    .getCurrentViews());
            Assert.assertTrue("Content of 'Who's viewed your profile' is not present",
                    viewFit.size() > 0);
            int countImages = 0;
            int countTexts = 0;
            // Check that it images and texts.
            for (View view : viewFit) {
                if (view.getClass().getSimpleName().equals("ImageView")) {
                    countImages++;
                } else if (view.getClass().getSimpleName().equals("TextView")) {
                    countTexts++;
                }
            }
            Assert.assertTrue("'Who's viewed your profile' not content images", countImages > 0);
            Assert.assertTrue("'Who's viewed your profile' not content texts", countTexts > 0);
        } else {
            Assert.fail("'Who's viewed your profile' label is not present");
        }
    }

    public ScreenSettings openSettingsScreen() {
        ImageView settingsButton = getRightButtonInNavigationBar();
        Assert.assertNotNull("'Settings' button is not present", settingsButton);
        Logger.i("Tapping on Settings button");
        getSolo().clickOnView(settingsButton);

        return new ScreenSettings();
    }

    /**
     * Open on 'Connections' text button.
     */
    public ScreenYouConnections openConnections() {
        tapToConnections();

        return new ScreenYouConnections();
    }

    /**
     * Gets current user name
     * 
     * @return current user name
     */
    public String getCurrentUserName() {
        View userNameView = Id.getViewByViewIdName(NAME);
        if (!(userNameView instanceof TextView)) {
            return StringDefaultValues.EMPTY_STRING;
        }

        TextView userNameTextView = (TextView) userNameView;
        return userNameTextView.getText().toString();
    }

    /**
     * Gets shortened current user name
     * 
     * @return shortened current user name (17 symbols)
     */
    public String getCurrentUserShortenedName() {
        String currentUserName = getCurrentUserName();
        // Reduce user name to 17 symbols
        String currentUserShortenedName = currentUserName.substring(0, 17);
        return currentUserShortenedName;
    }

    /**
     * Taps on 'AvatarPhotoCamera' image view.
     */
    public void tapAvatarPhotoCamera() {
        ImageView avatarPhotoCameraImageView = getSolo().getCurrentImageViews().get(
                AVATAR_PHOTO_CAMERA_INDEX);
        Assert.assertNotNull("'AvatarPhotoCamera' image view is not presented",
                avatarPhotoCameraImageView);

        Logger.i("Tapping on 'AvatarPhotoCamera' image view");
        getSolo().clickOnView(avatarPhotoCameraImageView);
    }

    /**
     * Taps on 'AvatarImage' image view.
     */
    public void tapAvatarImage() {
        ImageView avatarImageView = getSolo().getCurrentImageViews().get(AVATAR_IMAGE_INDEX);
        Assert.assertNotNull("'AvatarImage' image view is not presented", avatarImageView);

        Logger.i("Tapping on 'AvatarImage' image view");
        getSolo().clickOnView(avatarImageView);
    }

    /**
     * Taps on 'ShareButton' image button.
     */
    public void tapToShareButton() {
        ImageButton shareButton = getSolo().getImageButton(SHARE_BUTTON_INDEX);
        Assert.assertNotNull("'ShareButton' image button is not presented", shareButton);

        Logger.i("Tapping on 'ShareButton' image button");
        getSolo().clickOnView(shareButton);
    }

    /**
     * Taps on 'SendButton' image button.
     */
    public void tapToSendButton() {
        ImageButton sendButton = getSolo().getImageButton(SEND_BUTTON_INDEX);
        Assert.assertNotNull("'SendButton' image button is not presented", sendButton);

        Logger.i("Tapping on 'SendButton' image button");
        getSolo().clickOnView(sendButton);
    }

    /**
     * Taps on 'Who's viewed your profile' text button.
     */
    public void tapToWhoViewYouProfile() {
        TextView textWVYP = GetTextViewByText(WHOVIEWYOUPROFILE_BUTTON_TEXT);

        Assert.assertNotNull("'Who's viewed your profile' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    /**
     * Taps on 'Recent Activity' text button.
     */
    public void tapToRecentActivity() {
        TextView textWVYP = GetTextViewByText(RECENT_ACTIVITY_BUTTON_TEXT);

        Assert.assertNotNull("'Recent Activity' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    /**
     * Taps on 'Connections' text button.
     */
    public void tapToConnections() {
        TextView textWVYP = GetTextViewByText(CONNECTIONS_BUTTON_TEXT);

        Assert.assertNotNull("'Connections' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    /**
     * Taps on 'Groups' text button.
     */
    public void tapToGroups() {
        TextView textWVYP = GetTextViewByText(GROUP_BUTTON_TEXT);

        Assert.assertNotNull("'Groups' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    /**
     * Taps on 'Company Website' text button.
     */
    public void tapToCompanyWebsite() {
        getSolo().scrollDown();

        TextView textWVYP = GetTextViewByText(COMPANY_WEBSITE_BUTTON_TEXT);

        Assert.assertNotNull("'Company Website' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    /**
     * Taps on 'LinkedinSignIn' text button.
     */
    public void tapToLinkedinSignIn() {
        getSolo().scrollDown();

        TextView textWVYP = GetTextViewByText(LINKEDIN_SIGNIN_BUTTON_TEXT);

        Assert.assertNotNull("'LinkedinSignIn' is not present", textWVYP);

        getSolo().clickOnView(textWVYP);
    }

    private TextView GetTextViewByText(String text) {
        ArrayList<TextView> textViews = getSolo().getCurrentTextViews(null);

        for (TextView textView : textViews) {
            if (textView.getText().equals(text)) {
                return textView;
            }
        }

        return null;
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
     * Opens 'Who's viewed you' screen.
     * 
     * @return ScreenWhosViewedYou
     */
    public ScreenWhosViewedYou openWhosViewedYouScreen() {
        tapToWhoViewYouProfile();
        return new ScreenWhosViewedYou();
    }

    /**
     * Taps on 'Send to Connection' on popup.
     */
    public void tapOnSendToConnectionOnPopup() {
        TextView newMessageView = getSolo().getText("Send to Connection");
        ViewUtils.tapOnView(newMessageView, "'Send to Connection' text");
    }

    /**
     * Taps on 'forward' button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getSolo().getImageButton(1);
        ViewUtils.tapOnView(forwardButton, "'Forward' button");
    }

    /**
     * Opens 'NewMessage' screen.
     * 
     * @return ScreenNewMessage
     */
    public ScreenNewMessage openNewMessageScreen() {
        tapOnForwardButton();
        tapOnSendToConnectionOnPopup();
        return new ScreenNewMessage();
    }
}
