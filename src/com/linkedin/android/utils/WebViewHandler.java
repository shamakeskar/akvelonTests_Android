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
import android.webkit.JsResult;
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
    // Prefix for find single line message in ConsoleMessage.
    private final static String MESSAGE_PREFIX = "ConsoleMessage";
    // Prefix for find partial message in ConsoleMessage.
    private final static String MESSAGE_PART_PREFIX = "ConsolePart";
    // Tag to find the end of multiline console message in ConsoleMessage.
    private final static String END_OF_MESSAGE = "ConsoleEnd";

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
     * Wait for the end of message tag.
     */
    private void waitForConsole() {
        waitForConsole(DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Wait for the end of message tag in specified timeout.
     * 
     * @param timeout timeout in ms
     */
    private void waitForConsole(int timeout) {
        waitForConsole(null, DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Wait for the end of message tag.
     * 
     * @param errorMessage message in case of timeout
     */
    private void waitForConsole(String errorMessage) {
        waitForConsole(errorMessage, DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Wait for the end of message tag in specified timeout.
     * 
     * @param errorMessage message in case of timeout
     * @param timeout timeout in ms
     */
    private void waitForConsole(String errorMessage, int timeout) {
        boolean success = WaitActions.isTrueInFunction(timeout, new Callable<Boolean>() {
            public Boolean call() {
                return client.isMessageReceived();
            }
        });
        if (!success) {
            Logger.d(client.getRawConsole());
            Assert.fail(errorMessage == null ? "No answer from console. Make sure '" + END_OF_MESSAGE
                    + "' tag is present in JS" : errorMessage);
        }
    }

    /**
     * Clicking on link in web view
     * 
     * @param browser
     *            WebView with a HTML page loaded
     * @param link
     *            http link of the page to click on.
     */
    public void clickOnLink(String link) {
        client.cleanConsoleMessage();
        webView.loadUrl("javascript:links=document.getElementsByTagName('a');var found=false;"
                + "for(var i=0;i<links.length;i++){"
                + "if(links[i].innerHTML=='"
                + link
                + "'){ found=true;"
                + "var event = document.createEvent('MouseEvents');"
                + "event.initMouseEvent('click', true, window, 1, 0, 0, 0, 0, false, false, false, 1, document.body.parentNode);"
                + "links[i].dispatchEvent(event);"
                + "}"
                + "} if(found){console.log('" + MESSAGE_PREFIX + "' + 'class is found');}"
                + "else{console.log('" + END_OF_MESSAGE + "');}");
        waitForConsole();
        if (!client.getConsoleMessage().contains("class is found")) {
            Assert.fail("Cannot tap on link " + link);
        }
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
        client.cleanConsoleMessage();
        webView.loadUrl("javascript:elements=document.getElementsByClassName('"
                + className
                + "');var found=false;"
                + "for(var i=0;i<elements.length;i++){"
                + "if(elements[i].innerText.indexOf(\""
                + innerText
                + "\") !== -1) { found=true;"
                + "elements[i].scrollIntoView(true);"
                + "var event = document.createEvent('MouseEvents');"
                + "event.initMouseEvent('click', true, window, 1, 0, 0, 0, 0, false, false, false, 1, document.body.parentNode);"
                + "elements[i].dispatchEvent(event);" + "break; }"
                + "} if(found){console.log('" + MESSAGE_PREFIX + "' + 'class is found');}"
                + "else{console.log('" + END_OF_MESSAGE + "');}");
        waitForConsole();
        if (!client.getConsoleMessage().contains("class is found")) {
            Assert.fail("Cannot tap on web element with class '" + className 
                    + "' and text '" + innerText + "'");
        }
    }

    /**
     * Logs (as debug) innerText of all classes with specified name.
     * 
     * @param className
     */
    public void logAllClasses(String className) {
        client.cleanConsoleMessage();
        webView.loadUrl("javascript:elements=document.getElementsByClassName('"
                + className
                + "');for(var i=0;i<elements.length;i++) { "
                + "console.log('" + MESSAGE_PART_PREFIX + "'+i+':'+elements[i].innerText+' '+elements[i].offsetTop);}"
                + "console.log('" + END_OF_MESSAGE + "');");
        waitForConsole();
        Logger.d(client.getConsoleMessage());
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
     * Checking for text appears in web view in specified timeout.
     * 
     * @param text string to search
     * @param timeout timeout in ms
     * @return <b>true</b> if text appears in <b>timeout</b>.
     */
    public boolean isTextPresent(String text, int timeout) {
        client.cleanConsoleMessage();
        // Passing entire html doc to console log.
        webView.loadUrl("javascript:console.log('" + MESSAGE_PREFIX + "'+document.body.innerText);");
        waitForConsole("Cannot get innerText for webview content", timeout);
        return client.getConsoleMessage().contains(text);
    }

    /**
     * Returns HTML code of current page.
     * 
     * @return whole HTML page
     */
    public String getHTML() {
        client.cleanConsoleMessage();
        webView.loadUrl("javascript:console.log('" + MESSAGE_PREFIX + "'+document.body.innerHTML);");
        waitForConsole("Cannot get innerHTML for webview content");
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
        // Instance of ConsoleMessage.
        private static ChromeClient client;
        // Whole console message.
        private String consoleMessage = "";
        // Flag that indicates whole message is received.
        private boolean isMessageReceived = false;
        // Raw console data.
        private String consoleRaw = "";

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

        @Override
        public boolean onConsoleMessage(ConsoleMessage cmsg) {
            // Checking prefix.
            if (cmsg.message().startsWith(MESSAGE_PREFIX)) {
                // We get whole message. Stop receiving.
                consoleMessage = cmsg.message().substring(MESSAGE_PREFIX.length());
                isMessageReceived = true;
            }            
            if (cmsg.message().startsWith(MESSAGE_PART_PREFIX)) {
                // We get part of a message. Wait for more or the end tag.
                consoleMessage += cmsg.message().substring(MESSAGE_PART_PREFIX.length()) + "\n";
            } else if (cmsg.message().startsWith(END_OF_MESSAGE)) {
                // Stop receiving.
                isMessageReceived = true;
            } else {
                consoleRaw += cmsg.message() + "\n";
            }
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Logger.d(message);
            result.confirm();
            return true;
        }

        /**
         * Returns <b>true</b> if text found.
         * 
         * @return <b>true</b> if text found.
         */
        public boolean isMessageReceived() {
            return isMessageReceived;
        }

        /**
         * Returns console message.
         * @return console message.
         */
        public String getConsoleMessage() {
            return consoleMessage;
        }
        
        /**
         * Returns raw console data.
         * @return raw console data.
         */
        public String getRawConsole() {
            return consoleRaw;
        }


        /**
         * Cleans console message in ChromeClient.
         */
        public void cleanConsoleMessage() {
            consoleMessage = "";
            consoleRaw = "";
            isMessageReceived = false;
        }
    }
}
