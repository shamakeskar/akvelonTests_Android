package com.linkedin.android.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.linkedin.android.tests.data.DataProvider;

/**
 * Class with WebView interactions.
 * 
 * @author alexey.makhalov
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewHandler {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    // Instance of WebView for interactions.
    private WebView webView;
    // ChromeClient of webView.
    private ChromeClient client;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Creates new instance of WebViewHandler.
     */
    public WebViewHandler() {
        // Try get webView from activity.
        ArrayList<View> listViews = DataProvider.getInstance().getSolo().getViews();
        Iterator<View> iterator = listViews.iterator();
        while (iterator.hasNext()) {
            View view = iterator.next();
            if (view instanceof WebView) {
                webView = (WebView) view;
            }
        }
        Assert.assertNotNull("Cannot find webView on current screen", webView);
        // Add ChromeClient to webView.
        client = ChromeClient.getChromeClient();
        webView.setWebChromeClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    // METHODS --------------------------------------------------------------
    /**
     * Clicking on link in web view
     * 
     * @param browser
     *            WebView with a HTML page loaded
     * @param link
     *            http link of the page to click on.
     */
    public void clickOnLink(String link) {
        // Javascript to click on anchor link in web view
        webView.loadUrl("javascript:links=document.getElementsByTagName('a');var linkPresent=false;"
                + "for(var i=0;i<links.length;i++){"
                + "if(links[i].innerHTML=='"
                + link
                + "'){ linkPresent=true;"
                + "var event = document.createEvent('MouseEvents');"
                + "event.initMouseEvent('click', true, window, 1, 0, 0, 0, 0, false, false, false, 1, document.body.parentNode);"
                + "links[i].dispatchEvent(event);"
                + "}"
                + "} if(linkPresent==false){console.log('ConsoleMessage'+'LinkNotPresent');}");

        // Checking if link is not present in web view
        // waitForConsoleMessage("LinkNotPresent");
    }

    /**
     * Clicking on html element by his class name and inner text
     * 
     * @param className
     *            class name of html tag
     * @param innerText
     *            partial text inside of tag
     */
    public void clickOnClass(String className, String innerText) {
        webView.loadUrl("javascript:elements=document.getElementsByClassName('"
                + className
                + "');var linkPresent=false;"
                + "for(var i=0;i<elements.length;i++){"
                + "if(elements[i].innerText.indexOf(\""
                + innerText
                + "\") !== -1) { linkPresent=true;"
                + "var event = document.createEvent('MouseEvents');"
                + "event.initMouseEvent('click', true, window, 1, 0, 0, 0, 0, false, false, false, 1, document.body.parentNode);"
                + "elements[i].dispatchEvent(event);" + "break; }"
                + "} if(linkPresent==false){console.log('ConsoleMessage'+'LinkNotPresent');}");
    }
    
    /**
     * Logs (as debug) innerText of all classes with specified name.
     * 
     * @param className
     */
    public void logAllClasses(String className) {
        webView.loadUrl("javascript:elements=document.getElementsByClassName('"
                + className
                + "');for(var i=0;i<elements.length;i++);console.log('ConsoleMessage'+i+':'+elements[i].innerText+'\n');");
        boolean isFound = WaitActions.isTrueInFunction(new Callable<Boolean>() {
            public Boolean call() {
                return client.isTextPresent();
            }
        });
        if (isFound) {
            Logger.d("innerText of classes with name '" + className + "':");
            Logger.d(client.getConsoleMessage());
        } else {
            Logger.d("Cannot found classes with names '" + className + "'");
        }
    }

    /**
     * Checking for text appears in web view in specified timeout.
     * 
     * @param text string to search
     * @param timeout timeout in ms
     * @return <b>true</b> if text appears in <b>timeout</b>.
     */
    public boolean isTextPresent(String text, int timeout) {
        client.cleanConsoleMessage();
        client.setFinalText(text);
        // Passing entire html doc to console log.
        webView.loadUrl("javascript:console.log('ConsoleMessage'+document.body.innerText);");
        return WaitActions.isTrueInFunction(new Callable<Boolean>() {
            public Boolean call() {
                return client.isTextPresent();
            }
        });
    }
    
    /**
     * Checking for text appears in web view in timeout = WAIT_DELAY_DEFAULT.
     * 
     * @param text string to search
     * @return <b>true</b> if text appears in <b>timeout</b>.
     */
    public boolean isTextPresent(String text) {
        return isTextPresent(text, DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Returns HTML code of current page.
     * 
     * @return whole HTML page
     */
    public String getHTML() {
        client.cleanConsoleMessage();
        webView.loadUrl("javascript:console.log('ConsoleMessage'+document.body.innerHTML);");
        WaitActions.waitForTrueInFunction("Cannot get 'document.body.innerHTML' of current page.",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return client.getConsoleMessage() != null;
                    }
                });
        return client.getConsoleMessage();
    }

    /**
     * Logging a whole HTML page.
     */
    public void logHTML() {
        // Get HTML.
        String page = getHTML();
        // Convert Unicode symbols to UTF-8.
        Matcher matcher = Pattern.compile("\\#((?i)[0-9a-f]{4})").matcher(page);
        while (matcher.find()) {
            int codepoint = Integer.valueOf(matcher.group(1), 16);
            page = page.replaceAll(matcher.group(0), String.valueOf((char) codepoint));
        }
        // Log HTML as debug.
        Logger.d("Html code for WebView '" + webView.getTitle() + "':");
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
     * Using for feedback from console (JS execution)
     * 
     * @author alexey.makhalov
     */
    static class ChromeClient extends WebChromeClient {
        // Prefix for find console messages in ConsoleMessage.
        private final String PREFIX = "ConsoleMessage";
        // Text for determine end of message.
        private String finalText;
        // Flag that finalText found in console messages.
        private boolean isTextPresent = false;
        // Instance of ConsoleMessage.
        private static ChromeClient client;
        // Last console message.
        private String consoleMessage;

        // Creating singleton object
        private ChromeClient() {
        }

        /**
         * Returns ChromeClient.
         * 
         * @return ChromeClient instance.
         */
        public static ChromeClient getChromeClient() {
            if (client == null)
                client = new ChromeClient();
            return client;
        }

        /**
         * Set text for determine end of message.
         * 
         * @param text
         *            string for determine end of message.
         */
        private void setFinalText(String text) {
            finalText = text;
            isTextPresent = false;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cmsg) {
            // Checking prefix.
            if (cmsg.message().startsWith(PREFIX)) {
                // This is console message - handle it.
                consoleMessage = cmsg.message().substring(PREFIX.length() - 1);
                if (finalText != null)
                    isTextPresent = consoleMessage.contains(finalText);
                return true;
            }
            return false;
        }

        /**
         * Returns <b>true</b> if text found.
         * 
         * @return <b>true</b> if text found.
         */
        public boolean isTextPresent() {
            return isTextPresent;
        }

        /**
         * Returns console message.
         * 
         * @return console message.
         */
        public String getConsoleMessage() {
            return consoleMessage;
        }

        /**
         * Cleans console message in ChromeClient.
         */
        public void cleanConsoleMessage() {
            consoleMessage = null;
            isTextPresent = false;
        }
    }
}
