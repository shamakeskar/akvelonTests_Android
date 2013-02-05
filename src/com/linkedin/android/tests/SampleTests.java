package com.linkedin.android.tests;

import junit.framework.Assert;
import android.test.suitebuilder.annotation.Suppress;

import com.linkedin.android.fixtures.ServerRequestResponseUtils;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.screens.settings.ScreenSettings;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.Tag;
import com.linkedin.android.tests.utils.TestDiscover;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.WaitActions;

/**
 * Temporary class for debug tests.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 6:43:07 PM
 */
// @Suppress
public class SampleTests extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public SampleTests() {
        super(SampleTests.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    public void testSample() {
        startTest("12345678", "dsfasdfsg sdffs dfs");

        LoginActions.openUpdatesScreenOnStart();
        LoginActions.logout();

        passTest();
    }

    /**
     * Sample second test.
     */
    @Suppress
    // it annotation for ignore this test.
    public void testSecond() {
        startTest("01234567", "second");

        passTest();
    }

    /**
     * Change URL of server. Not forget enable fixtures for correct logout in
     * this test!!!
     */
    public void testChangeUrlOfServer() {
        disableLogoutAtEndForCurrentTest();
        startTest("00000000", "Change URL of Server");
        ScreenUpdates su = LoginActions.openUpdatesScreenOnStart();
        ScreenExpose se = su.openExposeScreen();
        ScreenYou sy = se.openYouScreen();
        ScreenSettings ss = sy.openSettingsScreen();
        ss.setServerURL(ServerRequestResponseUtils.SERVICE_CONNECTION_STRING.substring(7) + "data");
        WaitActions.waitSingleActivity(ScreenLogin.ACTIVITY_SHORT_CLASSNAME, "Login",
                DataProvider.WAIT_DELAY_LONG);
        passTest();
    }

    /**
     * Runs tag from 'tag.json' file on SD card.
     */
    public void testRunTag() {
        Tag tag = TestDiscover.getTagsFromFile(null)[0];
        if (tag == null) {
            Assert.fail("Object 'tag' in null.");
        }
        startTest(tag.getId(), tag.getName());

        TestUtils.executeTag(tag);
        passTest();
    }
}
