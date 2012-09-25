package com.linkedin.android.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * The class contains util methods to work with url.
 * 
 * @author vasily.gancharov
 * @created Sep 5, 2012 19:35:37 PM
 */
public class UrlUtils {

    // CONSTANTS ------------------------------------------------------------
    private static final String URL_ENCODING_DEFAULT_FORMAT = "utf-8";

    /**
     * Encodes {@code params} to UTF-8 and adds them to {@code url}
     * 
     * @param url
     *            url to add encoded {@code params}
     * @param params
     *            params to add to {@code url}
     * @return concatenation of {@code url} and {@code params} that encoded to
     *         UTF-8
     */
    public static String addParamsInUtf8ToUrl(String url, List<BasicNameValuePair> params) {
        return addParamsToUrl(url, params, URL_ENCODING_DEFAULT_FORMAT);
    }

    /**
     * Encodes {@code params} to {@code paramsEncoding} and adds them to
     * {@code url}
     * 
     * @param url
     *            url to add encoded {@code params}
     * @param params
     *            params to add to {@code url}
     * @param paramsEncoding
     *            name of encoding for {@code params}
     * @return concatenation of {@code url} and {@code params} that encoded to
     *         {@code paramsEncoding}
     */
    public static String addParamsToUrl(String url, List<BasicNameValuePair> params,
            String paramsEncoding) {
        if (null == url || null == params) {
            return url;
        }
        if (null == paramsEncoding) {
            paramsEncoding = URL_ENCODING_DEFAULT_FORMAT;
        }
        String query = URLEncodedUtils.format(params, paramsEncoding);
        url += "?" + query;
        return url;
    }

    /**
     * Builds absolute url from {@code baseUrl} and {@code relativeUrl}
     * 
     * @param baseUrl
     *            base url (absolute)
     * @param relativeUrl
     *            relative url to join to {@code baseUrl}
     * @return absolute url as {@code String} consisting of {@code baseUrl} and
     *         {@code relativeUrl} or EMPTY STRING if exception occurred during
     *         urls join
     */
    public static String BuildAbsoluteUrl(String baseUrl, String relativeUrl) {

        URL combinedUrl = null;
        try {
            URL baseUri = new URL(baseUrl);
            combinedUrl = new URL(baseUri, relativeUrl);
        } catch (MalformedURLException e) {
            return StringDefaultValues.EMPTY_STRING;
        }
        return combinedUrl.toString();
    }
}
