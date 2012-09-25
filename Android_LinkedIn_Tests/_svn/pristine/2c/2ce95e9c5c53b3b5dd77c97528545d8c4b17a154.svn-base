package com.linkedin.android.utils;

import java.util.ArrayList;

import junit.framework.Assert;
import android.view.View;

/**
 * Class for store location and size of view in inches.
 * 
 * @author alexander.makarov
 * @created Aug 10, 2012 3:30:52 PM
 */
public class Rect2DP {
    // CONSTANTS ------------------------------------------------------------
    public static final float DEFAULT_ACCURACY_OF_COMPARING = 1.0f;

    // PROPERTIES -----------------------------------------------------------
    public float x, y, width, height;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for {@code Rect2DP} object from 4 float values.
     * 
     * @param x
     *            is X coordinate of top left corner
     * @param y
     *            is Y coordinate of top left corner
     * @param width
     *            is width of rectangle
     * @param height
     *            is height of rectangle
     */
    public Rect2DP(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor for {@code Rect2DP} object from {@code View} object.
     * 
     * @param view
     *            {@code View} object for create rectangle
     */
    public Rect2DP(View view) {
        Assert.assertNotNull("Cannot create rectangle - view object is null", view);
        int[] xy = new int[2];
        view.getLocationInWindow(xy);
        int widthInt = view.getWidth();
        int heightInt = view.getHeight();
        this.x = xy[0] / ScreenResolution.getScreenDensity();
        this.y = xy[1] / ScreenResolution.getScreenDensity();
        this.width = widthInt / ScreenResolution.getScreenDensity();
        this.height = heightInt / ScreenResolution.getScreenDensity();
    }

    // METHODS --------------------------------------------------------------
    /**
     * Logs parameters this {@code Rect2DP}.
     */
    public void logParameters() {
        Logger.d("x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
    }

    /**
     * Checks width and height of current {@code Rect2DP} object are equal
     * {@code width} and {@code height} with prescribed accuracy {@code e}
     * 
     * @param width
     *            is width for comparing.
     * @param height
     *            is height for comparing.
     * @param e
     *            is accuracy of comparing.
     * @return <b>true</b> if width and height of current {@code Rect2DP} object
     *         are equal {@code width} and {@code height}, else <b>false</b>
     */
    public boolean isSizeEqual(float width, float height, float e) {
        return ((Math.abs(this.width - width) < e) && (Math.abs(this.height - height) < e));
    }

    /**
     * Checks properties {@code x}, {@code y}, {@code width} and {@code height}
     * of {@code rect} are equal properties of current {@code Rect2DP} object
     * with prescribed accuracy {@code e}
     * 
     * @param rect
     *            is {@code Rect2DP} object for comparing.
     * @param e
     *            is accuracy of comparing.
     * @return <b>true</b> if properties of compared objects are equal, else
     *         <b>false</b>
     */
    public boolean equals(Rect2DP rect, float e) {
        return ((rect != null) && (Math.abs(this.x - rect.x) < e)
                && (Math.abs(this.y - rect.y) < e) && (Math.abs(this.width - rect.width) < e) && (Math
                .abs(this.height - rect.height) < e));
    }

    /**
     * Checks that {@code View} is fit in this rectangle.
     * 
     * @param veiw {@code View} for check
     * @return <b>true</b> if view fit in this rectangle.
     */
    public boolean isViewFitInThisRect(View veiw) {
        Rect2DP rect2 = new Rect2DP(veiw);
        return (rect2.x >= x && rect2.y >= y && rect2.x + rect2.width <= x + width && rect2.y
                + rect2.height <= y + height);
    }

    /**
     * Returns {@code ArrayList} of the {@code View} that fit into this
     * {@code Rect2DP} object.
     * 
     * @param views
     *            is {@code ArrayList} of {@code View} for check
     * @return {@code ArrayList} of {@code View} that fit into this
     *         {@code Rect2DP} object
     */
    public ArrayList<View> getViewsThatFitInThisRect(ArrayList<View> views) {
        ArrayList<View> viewsFit = new ArrayList<View>();
        for (View view : views) {
            if (isViewFitInThisRect(view)) {
                viewsFit.add(view);
            }
        }
        return viewsFit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(height);
        result = prime * result + Float.floatToIntBits(width);
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Rect2DP rect = (Rect2DP) obj;
        return ((rect != null) && (Math.abs(this.x - rect.x) < DEFAULT_ACCURACY_OF_COMPARING)
                && (Math.abs(this.y - rect.y) < DEFAULT_ACCURACY_OF_COMPARING)
                && (Math.abs(this.width - rect.width) < DEFAULT_ACCURACY_OF_COMPARING) && (Math
                .abs(this.height - rect.height) < DEFAULT_ACCURACY_OF_COMPARING));
    }
}
