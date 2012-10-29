package com.linkedin.android.screens.common;

import java.util.ArrayList;

import junit.framework.Assert;
import android.widget.Button;

import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Login screen.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 7:42:22 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenLogin extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.authenticator.AuthenticatorActivity";

    public static final String ACTIVITY_SHORT_CLASSNAME = "AuthenticatorActivity";

    public static final String CALENDAR_SPLASH_ACTIVITY_SHORT_CLASSNAME = "CalendarOptInActivity";

    // Sizes of 'Sign In' button.
    public static final int SIGNIN_BUTTON_WIDTH = 418;
    public static final int SIGNIN_BUTTON_HEIGHT = 58;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenLogin() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected '" + ACTIVITY_SHORT_CLASSNAME + "', get '"
                        + getSolo().getCurrentActivity().getClass().getName() + "')",
                ACTIVITY_SHORT_CLASSNAME);
        Assert.assertTrue("'Email' field not present", getSolo().searchText("Email"));
        Assert.assertTrue("'Password' field not present", getSolo().searchText("Password"));
        Assert.assertTrue("'Sign In' button not present", getSolo().searchText("Sign In"));
        Assert.assertTrue("'Sign up to join LinkedIn' text not present",
                getSolo().searchText("Sign up to join LinkedIn"));
        // TODO complete implement
        HardwareActions.takeCurrentActivityScreenshot("Login");
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
     * Types in email field specified email.
     * 
     * @param email
     *            is string to type in email field
     */
    public void typeEmail(String email) {
        Assert.assertNotNull("Email field is not found", getSolo().getEditText(0));
        Logger.i("Enter '" + email + "' in user name field");
        getSolo().enterText(0, email);
    }

    /**
     * Types in password field specified password.
     * 
     * @param password
     *            is string to type in password field
     */
    public void typePassword(String password) {
        Assert.assertNotNull("Password field is not found", getSolo().getEditText(1));
        Logger.i("Enter '" + password + "' in user password field");
        getSolo().enterText(1, password);
    }

    /**
     * Taps on 'Sign In' button.
     */
    public void tapOnSignInButton() {
        Assert.assertNotNull("Password field is not found", getSolo().getButton("Sign In"));
        Logger.i("Tapping on 'Sign In' button");
        getSolo().clickOnButton("Sign In");
    }

    /**
     * Wait for SyncContactsDialog appears.
     */
    public void waitForSyncContactsDialog() {
        Assert.assertTrue("'Sync all' text is not present",
                getSolo().waitForText("Sync all", 1, DataProvider.WAIT_DELAY_DEFAULT));
        Assert.assertTrue("'Sync with existing contacts' text is not present", getSolo()
                .searchText("Sync with existing contacts"));
        Assert.assertTrue("'Do not sync LinkedIn contacts' text is not present", getSolo()
                .searchText("Do not sync LinkedIn contacts"));
    }

    /**
     * Default handler. Taps on 'Do not sync LinkedIn contacts' text on Sync
     * Contacts Dialog.
     */
    public void handleSyncContactsDialog() {
        handleSyncContactsDialog("Do not sync LinkedIn contacts");
    }

    /**
     * Taps on tapCase text on Sync Contacts Dialog.
     */
    public void handleSyncContactsDialog(String tapCase) {
        waitForSyncContactsDialog();
        Logger.i("Tapping on '" + tapCase + "' text");
        getSolo().clickOnText(tapCase);
    }

    /**
     * return true if CalendarSplash is shown.
     */
    public boolean isCalendarSplashPresents() {
        return getSolo().waitForActivity(CALENDAR_SPLASH_ACTIVITY_SHORT_CLASSNAME,
                DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Taps on 'Close' button in CalendarSplash.
     */
    public void handleCalendarSplash() {
        waitForCalendarSplashScreen();
        Logger.i("Tapping on 'Close' button in CalendarSplash");
        getSolo().clickOnImageButton(0);
    }

    /**
     * Taps on 'Add Calendar' button in CalendarSplash.
     */
    public void handleCalendarSplashAddCalendar() {
        waitForCalendarSplashScreen();
        Button addCalendarButton = getSolo().getButton("Add Calendar");
        ViewUtils.tapOnView(addCalendarButton, "'Add Calendar' button");
    }

    /**
     * Taps on "Welcome to LinkedIn" hint.
     */
    public void handleInButtonHint() {
        if (getSolo().waitForText("Welcome to LinkedIn", 1, DataProvider.WAIT_DELAY_DEFAULT, false)) {
            Logger.i("Tapping on hint for IN button");
            getSolo().clickOnScreen(50, 60);
        }
    }

    /**
     * Login in application with specified email and password.
     * 
     * @param email
     *            is string to type in email field
     * @param password
     *            is string to type in password field
     */
    public ScreenUpdates login(String email, String password) {
        // Type credentials and tap 'Sign In'.
        typeEmail(email);
        typePassword(password);
        tapOnSignInButton();
        
        PopupSyncContacts psc = new PopupSyncContacts();
        psc.tapOnDoNotSync();

        if (isCalendarSplashPresents()) {
            // Handle CalendarSplash.
            handleCalendarSplash();
            // Handle blue hint for IN button.
            handleInButtonHint();
        }
        // Return Updates screen.
        return new ScreenUpdates();
    }

    /**
     * Checks that 'Sign In' button showed.
     * 
     * @return <b>true</b> if 'Sign In' button showed
     */
    public static boolean isSignInButtonShowed() {
        ArrayList<Button> buttons = getSolo().getCurrentButtons();

        Logger.d("size=" + buttons.size());

        for (Button button : buttons) {
            Logger.d("name=" + button.getText() + ", s=" + button.isShown() + ", w="
                    + button.getWidth() + ", h=" + button.getHeight());
            if (button.isShown() && button.getText().equals("Sign In")) {
                if (button.getWidth() == SIGNIN_BUTTON_WIDTH
                        && button.getHeight() == SIGNIN_BUTTON_HEIGHT) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks that we are on Login screen.
     * 
     * @return <b>true</b> if we are on Login screen
     */
    public static boolean isOnLoginScreen() {
        return (getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME));
    }

    /**
     * Wait for Calendar splash screen appears.
     */
    public void waitForCalendarSplashScreen() {
        Assert.assertTrue(
                "Cannot wait Calendar Splach activity",
                getSolo().waitForActivity(CALENDAR_SPLASH_ACTIVITY_SHORT_CLASSNAME,
                        DataProvider.WAIT_DELAY_DEFAULT));

        Assert.assertTrue("'Add Your Calendar' text is not present",
                getSolo().waitForText("Add Your Calendar", 1, DataProvider.DEFAULT_DELAY_TIME));
        Assert.assertTrue("'Add Calendar' button is not present",
                getSolo().searchButton("Add Calendar"));
        Assert.assertTrue("'Add Calendar' text is not present", getSolo()
                .searchText("Add Calendar"));
        Assert.assertTrue("'Learn More' text is not present", getSolo().searchText("Learn More"));
    }

    /**
     * Verifies Learn More splash screen.
     */
    public void verifyLearnMoreSplashScreen() {
        Assert.assertTrue("'How LinkedIn uses your calendar data' text is not present", getSolo()
                .waitForText("How LinkedIn uses your calendar data"));

        Assert.assertTrue("'Learn More' text is not present", getSolo().searchText("Learn More"));
        Assert.assertNotNull(
                "'This feature accesses your phone's calendar...' text is not present",
                TextViewUtils.searchTextViewInLayout("This feature accesses your phone's calendar",
                        null, false));
    }
}
