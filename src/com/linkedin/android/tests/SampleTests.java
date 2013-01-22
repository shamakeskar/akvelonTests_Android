package com.linkedin.android.tests;

import java.util.ArrayList;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidWebDriver;

import android.test.suitebuilder.annotation.Suppress;
import android.view.View;
import android.webkit.WebView;

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
import com.linkedin.android.utils.Logger;
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

        Logger.d("Start delay");
        //LoginActions.openUpdatesScreenOnStart();
        WaitActions.delay(25);
        Logger.d("End delay");
        
        WebView web = null;
        ArrayList<View> views = getSolo().getCurrentViews();
        for (View view : views) {
            if (view instanceof WebView) {
                web = (WebView) view;
                break;
            }
        }
        Logger.d("web=" + web.toString());
        Logger.d("web.getOriginalUrl()=" + web.getOriginalUrl());
        
        WebDriver driver = null;
        try {
            driver = new AndroidWebDriver(getActivity());
            driver.get(web.getOriginalUrl());
            
            Logger.d("check 0");
            
            Logger.d(driver.getPageSource());
            Logger.d("title=" + driver.getTitle());

            Logger.d("check 1");
            
            WebElement link = driver.findElement(By.partialLinkText("English"));
            
            Logger.d("link=" + link.toString());
            
            link.click();
            
            Logger.d("check 2");
            
            Logger.d(driver.getPageSource());
            Logger.d("title=" + driver.getTitle());
            
        } catch (Exception e) {
            Logger.e("Fail", e);
        } finally {
            driver.quit();
        }

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
