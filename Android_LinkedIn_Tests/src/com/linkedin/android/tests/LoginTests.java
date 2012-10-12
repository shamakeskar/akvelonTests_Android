package com.linkedin.android.tests;

import junit.framework.Assert;

import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.utils.Logger;

/**
 * Tests for Login screen.
 * 
 * @author nikita.chehomov
 * @created Aug 21, 2012 12:02:14 PM
 */
public class LoginTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public LoginTests() {
        super(LoginTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    /**
     * Test case 34632369: 'Log in - Fail.' Open Login screen. Type correct
     * email. Type wrong password. Tap on Sign In button. Verify that login
     * failed.
     */
    public void test34632369() {
        disableLogoutAtEndForCurrentTest();// No login at end in this test.
        startFixture("34632369");
        startTest("34632369", "Log in - Fail.");

        // Open Login screen.
        ScreenLogin screenLogin = new ScreenLogin();

        // Type correct email.
        screenLogin.typeEmail(StringData.test_email);

        // Type wrong password.
        screenLogin.typePassword("asiuufgoudf");

        // Tap on Sign In button.
        screenLogin.tapOnSignInButton();

        // Verify that log in failed.
        Assert.assertTrue(
                "Typing an incorrect password the message about wrong password is not present.",
                getSolo().waitForText("Oops, please check your email address and password"));
        Logger.i(DONE + "Verify that log in failed.");
        
        passTest();
    }
}