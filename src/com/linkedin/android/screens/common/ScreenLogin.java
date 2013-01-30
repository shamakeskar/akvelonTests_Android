package com.linkedin.android.screens.common;

import java.util.ArrayList;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.TextView;

import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
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

    // TextView's for status_message popup.
    public static final ViewIdName STATUS_MESSAGE = new ViewIdName("status_message");
    // TextView's for Sign up link.
    public static final ViewIdName SIGN_UP_LINK = new ViewIdName("login_signup_link");

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
        Assert.assertTrue("'Sign In' button not present", getSolo().searchText("Sign In"));
        Assert.assertTrue("'Sign up to join LinkedIn' text not present",
                getSolo().searchText("Sign up to join LinkedIn"));
        // HardwareActions.takeCurrentActivityScreenshot("Login");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Login",
                DataProvider.WAIT_DELAY_LONG /* DataProvider.WAIT_DELAY_DEFAULT */);
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
            Logger.i("Press back to close 'Welcome to LinkedIn' label");
            HardwareActions.pressBack();
        }
    }

    /**
     * Taps on "Add a profile photo" hint.
     */
    public void handleAddProfilePhotoHint() {
        getSolo().waitForText("Add a profile photo", 1, DataProvider.DEFAULT_DELAY_TIME, false);
        TextView status = Id.getViewByName(STATUS_MESSAGE, TextView.class);
        if (status != null) {
            ViewUtils.tapOnView(status, "'Add a profile photo' status message");

            Assert.assertTrue("'Cancel' button is not present",
                    getSolo().waitForText("Cancel", 1, DataProvider.DEFAULT_DELAY_TIME));
            Button cancelButton = getSolo().getButton("Cancel");
            ViewUtils.tapOnView(cancelButton, "'Cancel' button");

            // now we at You screen, return to update screen.
            ScreenYou youScreen = new ScreenYou();
            youScreen.openExposeScreen().tapOnUpdatesButton();
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
        for (Button button : buttons) {
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

    /**
     * Taps on 'SignUp' link.
     */
    public void tapSignUpLink() {
        Assert.assertNotNull("'SignUp' link is not found",
                getSolo().getText(" Sign up to join LinkedIn "));
        TextView signup = Id.getViewByName(SIGN_UP_LINK, TextView.class);
        if (signup != null) {
            // ViewUtils.tapOnView on signup doesn't work.
            // Only tapping on URL tagged part of text does work!
            TextViewUtils.tapOnLinkInTextView(signup, "Sign up");
        }
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_login")
    public static void go_to_login() {
        LoginActions.logout();
        new ScreenLogin();
        TestUtils.delayAndCaptureScreenshot("go_to_login");
    }

    @TestAction(value = "login")
    public static void login() {
        LoginActions.logout();
        new ScreenLogin();
        TestUtils.delayAndCaptureScreenshot("login");
    }

    /**
     * Fills in an email and password.
     * 
     * @param email
     *            - text for text field 'email'.
     * @param password
     *            - text for text field 'password'.
     */
    @TestAction(value = "login_tap_signin_precondition")
    public static void login_tap_signin_precondition(String email, String password) {
        ScreenLogin screenLogin = new ScreenLogin();
        screenLogin.typeEmail(email);
        screenLogin.typePassword(password);
        TestUtils.delayAndCaptureScreenshot("login_tap_signin_precondition");
    }

    /**
     * Tap 'Sign in' button and wait for logged on.
     */
    @TestAction(value = "login_tap_signin")
    public static void login_tap_signin() {
        ScreenLogin screenLogin = new ScreenLogin();
        screenLogin.tapOnSignInButton();

        if (getSolo().waitForText("Sync all", 1, DataProvider.WAIT_DELAY_DEFAULT)) {
            screenLogin.waitForSyncContactsDialog();
        } else {
            screenLogin.verifyToast("Oops, please check your email address and password.");
        }
        TestUtils.delayAndCaptureScreenshot("login_tap_signin");
    }

    @TestAction(value = "login_tap_signin_reset")
    public static void login_tap_signin_reset() {
        if (getSolo().waitForText("Sync all")) {
            Logger.i("Tapping on 'Sync all' text");
            getSolo().clickOnText("Sync all");
            new ScreenCalSplash();
            LoginActions.logout();
            new ScreenLogin();
        }
        TestUtils.delayAndCaptureScreenshot("login_tap_signin_reset");
    }

    @TestAction(value = "login_tap_signup")
    public static void login_tap_signup() {
        new ScreenLogin().tapSignUpLink();
        // Wait for open system browser.
        WaitActions.waitForScreenUpdate();
        Assert.assertFalse("Browser is not open", getSolo()
                .searchText(" Sign up to join LinkedIn "));
        TestUtils.delayAndCaptureScreenshot("login_tap_signup");
    }

    public static void login_tap_signup_reset() {
        HardwareActions.pressBack();
        TestUtils.delayAndCaptureScreenshot("login_tap_signup_reset");
    }

    @TestAction(value = "login_error_dialog_tap_ok")
    public static void login_error_dialog_tap_ok() {
        ScreenLogin screenLogin = new ScreenLogin();
        screenLogin.tapOnSignInButton();
        screenLogin.verifyToast("Oops, please check your email address and password.");
        TestUtils.delayAndCaptureScreenshot("login_error_dialog_tap_ok");
    }
}
