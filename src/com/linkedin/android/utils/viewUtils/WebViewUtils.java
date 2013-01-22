package com.linkedin.android.utils.viewUtils;

import java.util.ArrayList;

import junit.framework.Assert;

import org.openqa.selenium.android.AndroidWebDriver;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;

import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;

/**
 * Class with utils for get and check UI elements from WebView.
 * 
 * @author alexander.makarov
 * @created Jan 11, 2013 7:43:39 PM
 */
public class WebViewUtils {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns first {@code WebView} on current screen.
     * 
     * @return first {@code WebView} on current screen.
     */
    public static WebView getFisrtWebViewOnScreen() {
        WebView webView = null;
        ArrayList<View> views = DataProvider.getInstance().getSolo().getCurrentViews();
        for (View view : views) {
            if (view instanceof WebView) {
                webView = (WebView) view;
                break;
            }
        }
        return webView;
    }

    /**
     * Returns {@code AndroidWebDriver} for specified WebView on current screen.
     * 
     * @param webView
     * @return
     */
    public static AndroidWebDriver getAndroidWebDriver(final WebView webView) {
        Activity activity = DataProvider.getInstance().getActivity();
        AndroidWebDriver webDriver = null;
        try {
            webDriver = new AndroidWebDriver(activity);
            String url = webView.getOriginalUrl();
            Logger.i("WebView opened with url '" + url + "'");
            webDriver.get(url);
            webDriver.setAcceptSslCerts(true);
        } catch (Exception e) {
            webDriver.quit();
            Assert.fail("Cannot create AndroidWebDriver.");
        }
        return webDriver;
        /*
         * AsyncWebDriverActionRunner tmp = new
         * AsyncWebDriverActionRunner(activity, webView);
         * activity.runOnUiThread(tmp); synchronized (tmp) { try { tmp.wait(); }
         * catch (InterruptedException e) {
         * Logger.d("Cannot wait AsyncWebDriverActionRunner");
         * e.printStackTrace(); } } return tmp.getAndroidWebDriver();
         */
    }

    /**
     * Returns {@code AndroidWebDriver} for first WebView on current screen.
     * 
     * @return {@code AndroidWebDriver} object.
     */
    public static AndroidWebDriver getAndroidWebDriver() {
        WebView webView = getFisrtWebViewOnScreen();
        Assert.assertNotNull("This screen not contain WebView", webView);
        return getAndroidWebDriver(webView);
    }

    /**
     * Logs (as debug) html code of specified {@code AndroidWebDriver}. Before
     * use it check that you simulator have at least 128 MB "VM heap" size!
     * 
     * @param driver
     *            {@code AndroidWebDriver} for log html code.
     */
    public static void logPageHtmlCode(AndroidWebDriver driver) {
        Logger.d("Html code for WebView '" + driver.getTitle() + "':");
        String page = driver.getPageSource();
        int symbolsCounter = 0;
        int endSymbol = 0;
        while (symbolsCounter < page.length()) {
            endSymbol = (symbolsCounter + 4000 <= page.length()) ? symbolsCounter + 4000 : page
                    .length();
            Logger.d(page.substring(symbolsCounter, endSymbol));
            symbolsCounter = endSymbol;
        }
    }

    /**
     * Class for run WebDriver actions in UI thread.
     * 
     * @author alexander.makarov
     * @created Jan 14, 2013 3:44:08 PM
     */
    @SuppressWarnings("unused")
    @Deprecated
    private class AsyncWebDriverActionRunner implements Runnable {
        private Activity activity;
        private WebView webView;
        private AndroidWebDriver webDriver;

        public AsyncWebDriverActionRunner(Activity activity, WebView webView) {
            this.activity = activity;
            this.webView = webView;
        }

        @Override
        public void run() {
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // DesiredCapabilities caps = new DesiredCapabilities();
                        // caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
                        // true);
                        webDriver = new AndroidWebDriver(activity);
                        webDriver.setAcceptSslCerts(true);
                        webDriver.get(webView.getOriginalUrl());
                    }
                });
            } catch (Exception e) {
                webDriver.quit();
                Logger.e("Cannot create AndroidWebDriver for this WebView.", e);
                Assert.fail("Cannot create AndroidWebDriver for this WebView.");
            }
            synchronized (this) {
                this.notify();
            }
        }

        public AndroidWebDriver getAndroidWebDriver() {
            Logger.d("webDriver=" + webDriver);
            return webDriver;
        }
    }
}
