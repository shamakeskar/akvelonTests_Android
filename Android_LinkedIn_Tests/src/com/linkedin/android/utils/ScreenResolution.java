package com.linkedin.android.utils;

import android.util.DisplayMetrics;

import com.linkedin.android.tests.data.DataProvider;

/**
 * Class for return screen resolution.
 * 
 * @author alexander.makarov
 * @created Aug 7, 2012 6:55:05 PM
 */
public final class ScreenResolution {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    private static int screenWidth = Integer.MIN_VALUE;
    private static int screenHeight = Integer.MIN_VALUE;
    private static float screenDensity = Float.MIN_VALUE;

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns screen width.
     * 
     * @return screen width
     */
    public static int getScreenWidth() {
        if (screenWidth == Integer.MIN_VALUE) {
            DisplayMetrics metrics = new DisplayMetrics();
            DataProvider.getInstance().getSolo().getCurrentActivity().getWindowManager()
                    .getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
        }
        return screenWidth;
    }

    /**
     * Returns screen width to density-depending compare.
     * 
     * @return screen width to density-depending compare.
     */
    public static float getScreenWidthDP() {
        return getScreenWidth() / getScreenDensity();
    }

    /**
     * Returns screen height.
     * 
     * @return screen height
     */
    public static int getScreenHeight() {
        if (screenHeight == Integer.MIN_VALUE) {
            DisplayMetrics metrics = new DisplayMetrics();
            DataProvider.getInstance().getSolo().getCurrentActivity().getWindowManager()
                    .getDefaultDisplay().getMetrics(metrics);
            screenHeight = metrics.heightPixels;
        }
        return screenHeight;
    }

    /**
     * Returns screen height to density-depending compare.
     * 
     * @return screen height to density-depending compare.
     */
    public static float getScreenHeightDP() {
        return getScreenHeight() / getScreenDensity();
    }

    /**
     * Returns screen density.
     * 
     * @return screen density
     */
    public static float getScreenDensity() {
        if (screenDensity == Float.MIN_VALUE) {
            DisplayMetrics metrics = new DisplayMetrics();
            DataProvider.getInstance().getSolo().getCurrentActivity().getWindowManager()
                    .getDefaultDisplay().getMetrics(metrics);
            screenDensity = metrics.density;
        }
        return screenDensity;
    }

    /**
     * Writes in log line with physical parameters of device.
     */
    public static void logScreenParamaters() {
        Logger.d(getScreenWidth() + "x" + getScreenHeight() + "(in DP: " + getScreenWidthDP() + "x"
                + getScreenHeightDP() + "), density = " + getScreenDensity());
    }
}
