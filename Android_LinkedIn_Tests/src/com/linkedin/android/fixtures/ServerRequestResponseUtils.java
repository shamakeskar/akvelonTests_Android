package com.linkedin.android.fixtures;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.StreamUtils;
import com.linkedin.android.utils.StringDefaultValues;
import com.linkedin.android.utils.UrlUtils;

/**
 * The class contains methods to send requests to server and receive responses.
 * Used for run fixtures.
 * 
 * @author vasily.gancharov
 * @created Sep 5, 2012 19:25:37 PM
 */
public class ServerRequestResponseUtils {
    // CONSTANTS ------------------------------------------------------------
    // URL of test server (that running fixtures).
    public static final String SERVICE_CONNECTION_STRING = "http://10.0.2.2:8080/";
    // TODO remove comments below on release.
    // Please write there you IP's if needed, but commit in SVN only as comments!
//  public static final String SERVICE_CONNECTION_STRING = "http://192.168.3.127:8080/";//Alexey Makhalov
//  public static final String SERVICE_CONNECTION_STRING = "http://192.168.9.14:8080/";//Alexander Makarov
//  public static final String SERVICE_CONNECTION_STRING = "http://192.168.19.63:8080/";//Vladimir Belyakov
    
    // Response about successful operation from test server.
    public static final String OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE = "ok";
    
    // Timeout for http connection operation.
    private static final int HTTP_CONNECTION_TIMEOUT = 7000;

    /**
     * Sends request to default server (see SERVICE_CONNECTION_STRING constant
     * from {@code TestApplicationConstants}).
     * 
     * @param methodName
     *            server method name
     * @param params
     *            parameters to pass to method with following name
     *            {@code methodName}
     * @return response from default server (see SERVICE_CONNECTION_STRING
     *         constant from {@code TestApplicationConstants}) as {@code String}
     *         or EMPTY STRING if unexpected exception occurred
     */
    public static String sendRequestToDefaultServer(String methodName,
            List<BasicNameValuePair> params) {
        String serverUrl = UrlUtils.BuildAbsoluteUrl(
                SERVICE_CONNECTION_STRING, methodName);
        serverUrl = UrlUtils.addParamsInUtf8ToUrl(serverUrl, params);

        return sendRequestToServer(serverUrl);
    }

    /**
     * Sends {@code request} to server
     * 
     * @param request
     *            request (url with params) to send to server
     * @return response from server as {@code String} or EMPTY STRING if
     *         unexpected exception occurred
     */
    private static String sendRequestToServer(String request) {
        try {
            URL url = new URL(request);
            URLConnection connectionToServer = url.openConnection();
            connectionToServer.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
            InputStream input = connectionToServer.getInputStream();
            String result = StreamUtils.CopyStreamData(input);
            input.close();
            return result;
        } catch (Exception ex) {
            Logger.i("Unexpected exception occured during 'sendRequestToServer' method work: "
                    + ex.getMessage());
        }
        return StringDefaultValues.EMPTY_STRING;
    }

}
